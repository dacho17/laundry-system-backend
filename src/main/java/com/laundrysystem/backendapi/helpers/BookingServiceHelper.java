package com.laundrysystem.backendapi.helpers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.laundrysystem.backendapi.dtos.ActiveBookingsDto;
import com.laundrysystem.backendapi.dtos.ActivityDto;
import com.laundrysystem.backendapi.dtos.BookingDto;
import com.laundrysystem.backendapi.dtos.PurchaseBooking;
import com.laundrysystem.backendapi.dtos.PurchaseDto;
import com.laundrysystem.backendapi.dtos.ReservedBookingDto;
import com.laundrysystem.backendapi.dtos.TimeslotAvailabilityDto;
import com.laundrysystem.backendapi.entities.Booking;
import com.laundrysystem.backendapi.entities.LaundryAsset;
import com.laundrysystem.backendapi.entities.LaundryAssetUse;
import com.laundrysystem.backendapi.entities.Purchase;
import com.laundrysystem.backendapi.entities.User;
import com.laundrysystem.backendapi.enums.TimeslotAvailabilityStatus;
import com.laundrysystem.backendapi.exceptions.ForbiddenActionException;
import com.laundrysystem.backendapi.mappers.BookingMapper;
import com.laundrysystem.backendapi.mappers.LaundryAssetUseMapper;
import com.laundrysystem.backendapi.utils.Formatting;


public class BookingServiceHelper {
	private static final long USAGE_TIME_OFFSET_IN_MS = 300000;
	private static final long BOOKING_SLOT_LENGTH_MS = 3600000;
	private static final long MINUTES_TO_MS_MULTIPLIER = 60000;
	
	private static final Logger logger = LoggerFactory.getLogger(BookingServiceHelper.class);
	
	public static LaundryAsset getTargetLaundryAsset(int assetId, List<LaundryAsset> laundryAssets) throws ForbiddenActionException {
		Optional<LaundryAsset> targetAssetCandidate = laundryAssets.stream().filter((asset) -> asset.getId() == assetId).findFirst();
		if (targetAssetCandidate.isEmpty()) {
			logger.error(String.format("Selected asset does not exist (assetId=%d). Availability can not be fetched.", assetId));
			throw new ForbiddenActionException();
		}
		LaundryAsset targetAsset = targetAssetCandidate.get();
		
		return targetAsset;
	}
	
	public static List<TimeslotAvailabilityDto> getEarliestAvailabilities(List<LaundryAsset> laundryAssets, User user) {
		List<TimeslotAvailabilityDto> earliestAvailabilities = new ArrayList<>();
		Timestamp curTimestamp = new Timestamp(System.currentTimeMillis());
		ActivityDto earliestAvailability;
		for (LaundryAsset asset: laundryAssets) {
			logger.info(String.format("Fetching earliest availability for asset (assetId=%d).", asset.getId()));
			List<Booking> earliestBookings = getEarliestBooking(asset);
			
			if (earliestBookings == null || earliestBookings.size() == 0) {
				// asset can be used immediately, there is no future bookings on the asset
				earliestAvailability = new PurchaseDto(
					Formatting.timestampToDateStr(curTimestamp),
					curTimestamp,
					asset.getName(),
					asset.getServicePrice(),
					asset.getCurrency(),
					asset.getId()
				);
				earliestAvailabilities.add(new TimeslotAvailabilityDto(
					earliestAvailability,
					TimeslotAvailabilityStatus.FREE_TO_USE,
					asset.getIsOperational(),
					null,
					null
				));
				
				logger.info(String.format("The earliest availability for asset (assetId=%d) is timeslot=%s.", asset.getId(), earliestAvailability.getChosenTimeslot()));
				continue;
			}
			
			Booking earliestBooking = earliestBookings.get(0);
			
			// check if this is a currently running slot by the user
			if (earliestBooking.getUser().getId() == user.getId()) {
				System.out.println(String.format("\n\nExamining asset assetId=%d as possbily currently running\n\n", asset.getId()));
				Optional<LaundryAssetUse> curUseCand = earliestBooking.getLaundryAssetUses().stream().findFirst();
				Optional<LaundryAssetUse> prevUseCand = getAssetUseFromPreviousTimeslot(earliestBooking);
				if (curUseCand.isPresent() && curUseCand.get().getEndTime().getTime() > Formatting.getCurTimestamp().getTime()) {
					System.out.println(String.format("\n\nExamining asset assetId=%d and its use %s\n\n", asset.getId(), curUseCand.get().toString()));
					earliestAvailability = new PurchaseDto(
						Formatting.timestampToDateStr(earliestBooking.getPurchase().getCreatedDate()),
						earliestBooking.getTimeslot(),
						asset.getName(),
						asset.getServicePrice(),
						asset.getCurrency(),
						asset.getId()
					);
					earliestAvailabilities.add(new TimeslotAvailabilityDto(
						earliestAvailability,
						TimeslotAvailabilityStatus.RUNNING_BY_USER,
						asset.getIsOperational(),
						curUseCand.get().getEndTime(),
						null
					));
					logger.info(String.format("The earliest availability for asset (assetId=%d) is timeslot=%s.", asset.getId(), earliestAvailability.getChosenTimeslot()));
					continue;
				}
			}

			long runningTimeInMiliseconds = asset.getRunningTime() * MINUTES_TO_MS_MULTIPLIER;
			boolean canAssetBeUsedImmediately = 
				curTimestamp.getTime() + runningTimeInMiliseconds < earliestBooking.getTimeslot().getTime();
			if (canAssetBeUsedImmediately) {
				// asset can be used immediately, there is no overlapping bookings
				earliestAvailability = new PurchaseDto(
					Formatting.timestampToDateStr(curTimestamp),
					curTimestamp,
					asset.getName(),
					asset.getServicePrice(),
					asset.getCurrency(),
					asset.getId()
				);
				earliestAvailabilities.add(new TimeslotAvailabilityDto(
					earliestAvailability,
					TimeslotAvailabilityStatus.FREE_TO_USE,
					asset.getIsOperational(),
					null, null
				));
				logger.info(String.format("The earliest availability for asset (assetId=%d) is timeslot=%s.", asset.getId(), earliestAvailability.getChosenTimeslot()));
				continue;
			}
			
			System.out.println(String.format("\n\nExamining asset assetId=%d and its earliest booking %s\n\n", asset.getId(), earliestBooking.toString()));
			// there is overlapping bookings. check if the booking is user's and there is enough time for the asset to run
			if (earliestBooking.getUser().getId() == user.getId()) {
				// calculating for how long does the user have this asset booked
				Timestamp finalBookingTimeslotStart = null;
				List<Booking> usersBookings = earliestBookings.stream().filter(booking -> booking.getUser().getId() == user.getId()).toList();
				for (int i = 0; i < usersBookings.size(); i++) {
					Booking book = usersBookings.get(i);
					if (finalBookingTimeslotStart == null 
							|| book.getTimeslot().getTime() == finalBookingTimeslotStart.getTime() + BOOKING_SLOT_LENGTH_MS) {
						finalBookingTimeslotStart = book.getTimeslot();
					}
				}
				
				if ((curTimestamp.getTime() + runningTimeInMiliseconds) < earliestBooking.getTimeslot().getTime() + BOOKING_SLOT_LENGTH_MS) {
					earliestAvailability = new PurchaseDto(
						Formatting.timestampToDateStr(curTimestamp),
						curTimestamp,
						asset.getName(),
						asset.getServicePrice(),
						asset.getCurrency(),
						asset.getId()
					);

					earliestAvailabilities.add(new TimeslotAvailabilityDto(
						earliestAvailability,
						TimeslotAvailabilityStatus.FREE_TO_USE_BOOKED,
						asset.getIsOperational(),
						null,
						new Timestamp (finalBookingTimeslotStart.getTime() + BOOKING_SLOT_LENGTH_MS)
					));
					logger.info(String.format("The earliest availability for asset (assetId=%d) is timeslot=%s.", asset.getId(), earliestAvailability.getChosenTimeslot()));
					continue;
				// the earliest booking slot is user's, and the slot is either:
					// 1. the only existing future one, or 2. the following booking slot is also user's
				} else if (earliestBookings.size() == 1 || (earliestBookings.size() > 1 && earliestBookings.get(1).getUser().getId() == user.getId())) {
					earliestAvailability = new PurchaseDto(
							Formatting.timestampToDateStr(curTimestamp),
							curTimestamp,
							asset.getName(),
							asset.getServicePrice(),
							asset.getCurrency(),
							asset.getId()
						);

					earliestAvailabilities.add(new TimeslotAvailabilityDto(
						earliestAvailability,
						TimeslotAvailabilityStatus.FREE_TO_USE_BOOKED,
						asset.getIsOperational(),
						null, new Timestamp (finalBookingTimeslotStart.getTime() + BOOKING_SLOT_LENGTH_MS)
					));
					logger.info(String.format("The earliest availability for asset (assetId=%d) is timeslot=%s.", asset.getId(), earliestAvailability.getChosenTimeslot()));
					continue;
				}
			}
			
			// calculate for when can the booking be made
			long earliestAvailableTimeslotAt = getEarliestAvaialability(asset).getTime();
			Optional<Booking> bookingCand = earliestBookings.stream()
				.filter(booking -> booking.getUser().getId() == user.getId() && curTimestamp.getTime() + runningTimeInMiliseconds < booking.getTimeslot().getTime() + BOOKING_SLOT_LENGTH_MS).findFirst();

			if (bookingCand.isPresent()) {
				long earliestUsersBooking = bookingCand.get().getTimeslot().getTime();
				if (earliestUsersBooking < earliestAvailableTimeslotAt) {
					earliestAvailability = new BookingDto(
							Formatting.timestampToDateStr(curTimestamp),
							new Timestamp(earliestUsersBooking),
							asset.getName(),
							asset.getId()
						);
					earliestAvailabilities.add(new TimeslotAvailabilityDto(
						earliestAvailability,
						TimeslotAvailabilityStatus.BOOKED_BY_USER,
						asset.getIsOperational(),
						null,
						null
					));
					continue;
				}
			}

			
			earliestAvailability = new BookingDto(
				Formatting.timestampToDateStr(curTimestamp),
				new Timestamp(earliestAvailableTimeslotAt),
				asset.getName(),
				asset.getId()
			);

			earliestAvailabilities.add(new TimeslotAvailabilityDto(earliestAvailability, TimeslotAvailabilityStatus.AVAILABLE_FROM, asset.getIsOperational(), null, null));
			logger.info(String.format("The earliest availability for asset (assetId=%d) is timeslot=%s.", asset.getId(), earliestAvailability.getChosenTimeslot()));
		}
		
		return earliestAvailabilities;
	}

	public static List<Booking> getEarliestBooking(LaundryAsset laundryAsset) {
		Timestamp curTimestamp = new Timestamp(System.currentTimeMillis());
		List<Booking> assetBookings = laundryAsset.getBookings();
		if (assetBookings.size() == 0) {
			return null;
		}
//		else if (assetBookings.size() == 1) {
//			return assetBookings.get(0);
//		}
		
		logger.info(String.format("There are %d elements in the list!", assetBookings.size()));
		List<Booking> earliestBookingCandidates = assetBookings.stream()
				.filter((booking) -> (booking.getTimeslot().getTime() + BOOKING_SLOT_LENGTH_MS) > curTimestamp.getTime())
				.sorted((first, second) -> first.getTimeslot().compareTo(second.getTimeslot())).toList();

		if (earliestBookingCandidates.size() == 0) {
			logger.info(String.format("No future bookings have been found on the asset with assetId=%d", laundryAsset.getId()));
			return null;
		}
		
		return earliestBookingCandidates;
	}
	
	public static Timestamp getEarliestAvaialability(LaundryAsset laundryAsset) {
		Timestamp curTimestamp = new Timestamp(System.currentTimeMillis());
		List<Booking> assetBookings = laundryAsset.getBookings();
		if (assetBookings.size() == 0) {
			return null;
		}
		
		List<Booking> sortedBookings = assetBookings.stream()
			.filter((booking) -> (booking.getTimeslot().getTime() + BOOKING_SLOT_LENGTH_MS) > curTimestamp.getTime())
			.sorted((first, second) -> first.getTimeslot().compareTo(second.getTimeslot())).toList();
		
		Iterator<Booking> bookIter = sortedBookings.iterator();
		
		long earliestAvaialabilityTimeslot = 0;
		long prevTimeslotStart = 0;
		Booking curElement = getNextIterElement(bookIter);
		while (curElement != null) {
			System.out.println(String.format("Current element slot is %s", curElement.getTimeslot()));
			if (prevTimeslotStart == 0) {
				prevTimeslotStart = curElement.getTimeslot().getTime();
				curElement = getNextIterElement(bookIter);
				continue;
			}
			
			if (prevTimeslotStart + BOOKING_SLOT_LENGTH_MS == curElement.getTimeslot().getTime()) {
				prevTimeslotStart = curElement.getTimeslot().getTime();
				curElement = getNextIterElement(bookIter);
				continue;
			}
			
			break;
		}
		
		earliestAvaialabilityTimeslot = prevTimeslotStart + BOOKING_SLOT_LENGTH_MS;
		return new Timestamp(earliestAvaialabilityTimeslot);
	}
	
	public static List<Booking> getBookingSlots(User user, LaundryAsset laundryAsset, Timestamp timeslot) {
		List<Booking> necessaryBookingSlots = new ArrayList<>();
		Booking newBookingSlot = new Booking(
			new Timestamp(System.currentTimeMillis()),
			timeslot,
			user,
			laundryAsset
		);
		necessaryBookingSlots.add(newBookingSlot);
		
		long curTs = Formatting.getCurTimestamp().getTime();
		if (newBookingSlot.getTimeslot().getTime() + BOOKING_SLOT_LENGTH_MS < curTs + laundryAsset.getRunningTime() * MINUTES_TO_MS_MULTIPLIER) {
			Booking additionalBookingSlot = new Booking(
				new Timestamp(System.currentTimeMillis()),
				new Timestamp(timeslot.getTime() + BOOKING_SLOT_LENGTH_MS),
				user,
				laundryAsset
			);
			necessaryBookingSlots.add(additionalBookingSlot);
		}
		
		return necessaryBookingSlots;
	}

	public static List<BookingDto> getUserFutureBookings(User user) {
		logger.info(String.format("Fetching future bookings for the user with userId=%d", user.getId()));
		Timestamp currTimestamp = new Timestamp(System.currentTimeMillis());
		List<BookingDto> futureBookings = user.getBookings().stream()
			.filter((booking) -> currTimestamp.getTime() < (booking.getTimeslot().getTime() + BOOKING_SLOT_LENGTH_MS) && booking.getPurchase() == null)
			.sorted((bookingOne, bookingTwo) -> bookingOne.getTimeslot().compareTo(bookingTwo.getTimeslot()))
			.map((booking) -> BookingMapper.toDTO(booking)).toList();
		
		logger.info(String.format("%d future bookings found for the user with userId=%d", futureBookings.size(), user.getId()));
		return futureBookings;
	}
	
	public static ActiveBookingsDto getUserActiveBookings(User user) {
		logger.info(String.format("Fetching active bookings for user with userId=%d", user.getId()));
		
		List<ReservedBookingDto> purchasedBookings = new ArrayList<>();
//		List<BookingDto> expiredBookings = new ArrayList<>();
		Timestamp currTimestamp = new Timestamp(System.currentTimeMillis());
		List<Booking> futureBookings = new ArrayList<>();
		Set<Integer> assetsInUse = new HashSet<>();
		user.getBookings().stream()
			.filter((booking) -> (currTimestamp.getTime() <= booking.getTimeslot().getTime() + BOOKING_SLOT_LENGTH_MS))	// && (currTimestamp.getTime() <= (booking.getTimeslot().getTime() + BOOKING_SLOT_LENGTH_MS)
			.forEach((booking) -> {
				// get a connected use if exists and add it to the list. This will be shown as purchased
				Optional<LaundryAssetUse> laundryAssetUse = booking.getLaundryAssetUses().stream()
					.filter(use -> use.getStartTime().getTime() <= currTimestamp.getTime() && currTimestamp.getTime() <= use.getEndTime().getTime())
					.findFirst();
				if (laundryAssetUse.isPresent()) {
					assetsInUse.add(Integer.valueOf(laundryAssetUse.get().getLaundryAsset().getId()));
					ReservedBookingDto reservedBooking = LaundryAssetUseMapper.MAPtoReservedBooking(laundryAssetUse.get());
					purchasedBookings.add(reservedBooking);
				} else {
					futureBookings.add(booking);
				}
			});
		System.out.println("Purchased bookings:\n");
		System.out.println(purchasedBookings);
		System.out.println("Future bookings:\n");
		System.out.println(futureBookings);
		// consider only those assets which are currently being unused
		List<Booking> unusedAssetFutureBookings = futureBookings.stream()
				.filter(book -> !assetsInUse.contains(Integer.valueOf(book.getLaundryAsset().getId()))).toList();
		System.out.println("Unused future bookings:\n");
		System.out.println(unusedAssetFutureBookings);
		
		// merge subsequent timeslots to create a mega timeslot. This will be shown as toPurchase
		List<ReservedBookingDto> bookingsToPurchase = makeBookingBlocks(unusedAssetFutureBookings);
		
		logger.info(String.format("Returning active bookings for user [userId=%d]."
				+ "[%d bookingsToBePurchased, %d purchasedBooking]",	// , %d expiredBookings
				user.getId(), bookingsToPurchase.size(), purchasedBookings.size()));	// , expiredBookings.size()
		
		return new ActiveBookingsDto(bookingsToPurchase, purchasedBookings); // , expiredBookings
	}

	public static List<BookingDto> getAssetBookingsOnDate(LaundryAsset laundryAsset, Timestamp date, int userId) {
		String strDate = Formatting.getDayMonthYearFromTimestamp(date);
		Timestamp curTs = Formatting.getCurTimestamp();

		logger.info(String.format("Filtering availability of laundryAsset with id=%d for date=%s.", laundryAsset.getId(), strDate));
		List<BookingDto> bookingsOnDate = laundryAsset.getBookings().stream()
			.filter((booking) -> ((date.getTime() <= booking.getTimeslot().getTime())
				&& Formatting.getDayMonthYearFromTimestamp(booking.getTimeslot()).equals(strDate)))
			.sorted((bookingOne, bookingTwo) -> bookingOne.getTimeslot().compareTo(bookingTwo.getTimeslot()))
			.map((booking) -> BookingMapper.toDTO(booking)).toList();

		if (bookingsOnDate.size() > 0) {
			Booking firstFutureBooking = getEarliestBooking(laundryAsset).get(0);
			if (firstFutureBooking.getTimeslot().getTime() > curTs.getTime()) {
				long endRunningTime = curTs.getTime() + laundryAsset.getRunningTime() * MINUTES_TO_MS_MULTIPLIER;
				if (firstFutureBooking.getTimeslot().getTime() <= endRunningTime && firstFutureBooking.getUser().getId() != userId) {
					List<BookingDto> bookingsOnDateExpanded = new ArrayList<>();
					bookingsOnDateExpanded.add(new BookingDto(
						Formatting.timestampToDateStr(curTs),
						Formatting.curTimestampToCurTimeslot(),
						laundryAsset.getName(),
						laundryAsset.getId())
					);
					bookingsOnDate.forEach(booking -> bookingsOnDateExpanded.add(booking));
					logger.info(String.format("The bookings have been expanded with the initial blocked booking. Additional %d bookings have been found on the laundry asset with id=%d for date=%s.", bookingsOnDate.size(), laundryAsset.getId(), strDate));
					return bookingsOnDateExpanded;
				}
			}
		}
		

		logger.info(String.format("%d bookings have been found on the laundry asset with id=%d for date=%s.", bookingsOnDate.size(), laundryAsset.getId(), strDate));
		return bookingsOnDate;
	}
	
	// check if the current timeslot is already booked and do not book it again, same for the following
	// 1. if fits into current timeslot book the one
	// 2. else book both the current and the following timeslots
	// or: book the current time slot and the following if it is required
	public static List<PurchaseBooking> getSlotsToBook(User user, LaundryAsset laundryAsset) {
		List<PurchaseBooking> slotsToBook = new ArrayList<>();
		Timestamp curTimestamp = new Timestamp(System.currentTimeMillis());
		Timestamp currentTimeSlot = Formatting.curTimestampToCurTimeslot();
		PurchaseBooking curBookingSlot = prepareBookingsForStoring(currentTimeSlot, user, laundryAsset);
		slotsToBook.add(curBookingSlot);

		Timestamp nextTimeslot = new Timestamp(currentTimeSlot.getTime() + BOOKING_SLOT_LENGTH_MS);
		if ((curTimestamp.getTime() + laundryAsset.getRunningTime() * MINUTES_TO_MS_MULTIPLIER)
				> nextTimeslot.getTime()) {
			PurchaseBooking nextBookingSlot = prepareBookingsForStoring(nextTimeslot, user, laundryAsset);
			slotsToBook.add(nextBookingSlot);
		}
		
		return slotsToBook;
	}
	
	public static boolean isAssetAvailableAt(LaundryAsset laundryAsset, Timestamp timeslot, int userId) {
//		long curTs = Formatting.getCurTimestamp().getTime();

		// for current timeslot the current timestamp must be sent and check against it made!
		Timestamp tempTimestamp = timeslot;
		if (Formatting.curTimestampToCurTimeslot().getTime() == timeslot.getTime()) {
			tempTimestamp = Formatting.getCurTimestamp();
		}
		Timestamp usedTimestamp = tempTimestamp;

		long endRunningTime = usedTimestamp.getTime() + laundryAsset.getRunningTime() * MINUTES_TO_MS_MULTIPLIER;
		Optional<Booking> overlappingBookingCandidate = laundryAsset.getBookings().stream()
			.filter((booking)
					-> booking.getTimeslot().getTime() == timeslot.getTime()
					|| ((booking.getTimeslot().getTime() <= endRunningTime)
							&& (usedTimestamp.getTime() < booking.getTimeslot().getTime())
							&& (booking.getUser().getId() != userId))).findAny();
		
		if (!overlappingBookingCandidate.isEmpty()) {
			logger.info(String.format("Selected asset [assetId=%d] is not avaialble at the requested time [time=%s].",
				laundryAsset.getId(), usedTimestamp.toString()));
			return false;
		}

		return true;
	}
	
	public static boolean isAllowedToPurchase(LaundryAsset laundryAsset, User user) {
		List<Booking> earliestBookings = getEarliestBooking(laundryAsset);
		
		if (earliestBookings == null) {
			return true;
		}
		
		Timestamp curTimestamp = new Timestamp(System.currentTimeMillis());
		long runningTimeInMS = laundryAsset.getRunningTime() * MINUTES_TO_MS_MULTIPLIER;
		
		// check if there is a laundry asset use
		boolean isAssetCurrentlyRunning = laundryAsset.getLaundryAssetUses().stream()
			.anyMatch(use -> use.getStartTime().getTime() <= curTimestamp.getTime() && curTimestamp.getTime() <= use.getEndTime().getTime());
		System.out.println(String.format("Asset is currently running %b", isAssetCurrentlyRunning));
		if (isAssetCurrentlyRunning) {
			return false;
		}
		
		// check if there is a booking that overlaps with the running time
		long earliestBookingStartTime = earliestBookings.get(0).getTimeslot().getTime();
		boolean isOverlappingWithBooking =
			(curTimestamp.getTime() < earliestBookingStartTime && earliestBookingStartTime < curTimestamp.getTime() + runningTimeInMS)
			|| (curTimestamp.getTime() > earliestBookingStartTime);
		System.out.println(String.format("Asset is overlapping with the earliest booking %b", isOverlappingWithBooking));

		// there is no booking to interfere with the running time of the asset
		if (!isOverlappingWithBooking) {
			return true;
		}
		
		// if the earliest booking is interfering, check if it belongs to the user. If it does it can be used by them
		if (isOverlappingWithBooking && earliestBookings.get(0).getUser().getUsername() == user.getUsername()) {
			// check if the run falls withing the current timeslot
			if ((curTimestamp.getTime() + runningTimeInMS) < earliestBookings.get(0).getTimeslot().getTime() + BOOKING_SLOT_LENGTH_MS) {
				return true;				
			}
			// for the machine to run, additional slot is needed. the additional slot is booked and it is not immediately following
			if (earliestBookings.size() == 1 || earliestBookings.get(1).getTimeslot().getTime() > earliestBookings.get(0).getTimeslot().getTime() + BOOKING_SLOT_LENGTH_MS) {
				return true;
			}
			// for the machine to run, additional slot is needed. the additional slot is booked and it is user's
			if (earliestBookings.size() > 1 && earliestBookings.get(1).getUser().getUsername().equals(user.getUsername())) {
				return true;
			}
		}
		
		return false;
	}
	
	private static <T> T getNextIterElement(Iterator<T> iter) {
		try {
			return iter.next();
		} catch(Exception exc) {
			return null;
		}
	}
	
	private static List<ReservedBookingDto> makeBookingBlocks(List<Booking> bookings) {
		Map<Integer, List<Booking>> groupedBookings = new HashMap<>();
		
		bookings.forEach(booking -> {
			Integer assetId = Integer.valueOf(booking.getLaundryAsset().getId());
			if (!groupedBookings.containsKey(assetId)) {
				groupedBookings.put(assetId, new ArrayList<>());
			}
			
			List<Booking> curList = groupedBookings.get(assetId);
			curList.add(booking);
		});
		
		List<ReservedBookingDto> immediateBookings = new ArrayList<>();
		// in groupedBookings map I now have bookings groupped per laundryAsset
		// for each key create an immediate megaslot. By sorting the bookings, checking if the first is immediate,
		// and expanding the timeslot for each subsequent booking
		groupedBookings.forEach((assetId, assetBookings) -> {
			List<Booking> sortedBookings = assetBookings.stream()
				.sorted((bkOne, bkTwo) -> bkOne.getTimeslot().compareTo(bkTwo.getTimeslot()))
				.toList();
			
			long curTs = Formatting.getCurTimestamp().getTime();
			Booking earliestBooking = sortedBookings.get(0);
			Timestamp earliestTimeslot = earliestBooking.getTimeslot();
			if (earliestTimeslot.getTime() <= curTs) {
				LaundryAsset laundryAsset = earliestBooking.getLaundryAsset();
				ReservedBookingDto reservedBookingDto = new ReservedBookingDto(
					assetId,
					laundryAsset.getName(),
					earliestTimeslot,
					new Timestamp(earliestTimeslot.getTime() + BOOKING_SLOT_LENGTH_MS ),
					laundryAsset.getServicePrice(),
					laundryAsset.getCurrency()
				);
				
				for (int i = 1; i < sortedBookings.size(); i++) {
					Booking candidateSequentialBooking = sortedBookings.get(i);
					if (candidateSequentialBooking.getTimeslot().getTime() == reservedBookingDto.getToTimeslot().getTime()) {
						Timestamp newToTimeslot = new Timestamp(reservedBookingDto.getToTimeslot().getTime() + BOOKING_SLOT_LENGTH_MS);
						reservedBookingDto.setToTimeslot(newToTimeslot);
					}
				}
				
				// check if the asset has enough time to run before the expiry of the timeslot
				long endTimeOfSlot = reservedBookingDto.getToTimeslot().getTime();
				long endTimeOfAssetOperation = curTs + laundryAsset.getRunningTime() * MINUTES_TO_MS_MULTIPLIER;
				if (endTimeOfAssetOperation < endTimeOfSlot) {
					immediateBookings.add(reservedBookingDto);					
				}
			}
		});
		
		return immediateBookings;
	}

	private static Optional<LaundryAssetUse> getAssetUseFromPreviousTimeslot(Booking followingTimeslot) {
		Timestamp timeslot = new Timestamp(followingTimeslot.getTimeslot().getTime() - BOOKING_SLOT_LENGTH_MS);
		User user = followingTimeslot.getUser();
		Optional<Booking> prevBookingCand = user.getBookings().stream().filter(book -> book.getTimeslot().getTime() == timeslot.getTime()).findFirst();

		if (!prevBookingCand.isPresent()) {
			return null;			
		}

		Optional<LaundryAssetUse> assetUse = prevBookingCand.get().getLaundryAssetUses().stream().findFirst();
		if (assetUse.isPresent() && assetUse.get().getEndTime().getTime() >= followingTimeslot.getTimeslot().getTime()) {
			return assetUse;
		}

		return null;
	}

	private static PurchaseBooking prepareBookingsForStoring(Timestamp timeslot, User user, LaundryAsset laundryAsset) {
		PurchaseBooking bookingSlot = new PurchaseBooking(new Booking(
				Formatting.getCurTimestamp(),
				timeslot,
				user,
				laundryAsset
			),false
		);

		// check if the user already has a booking before attempting to create it again 
		Optional<Booking> timeslotBooking = user.getBookings().stream()
			.filter(book -> book.getTimeslot().getTime() == timeslot.getTime() && book.getLaundryAsset().getId() == laundryAsset.getId()).findAny();
		if (timeslotBooking.isPresent()) {
			bookingSlot.setIsAlreadyCreated(true);
		}

		return bookingSlot;
	}
}
