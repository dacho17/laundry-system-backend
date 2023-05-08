package com.laundrysystem.backendapi.services;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.laundrysystem.backendapi.dtos.ActiveBookingsDto;
import com.laundrysystem.backendapi.dtos.ActivityDto;
import com.laundrysystem.backendapi.dtos.BookingDto;
import com.laundrysystem.backendapi.dtos.BookingRequestDto;
import com.laundrysystem.backendapi.dtos.LaundryAssetDto;
import com.laundrysystem.backendapi.dtos.PurchaseDto;
import com.laundrysystem.backendapi.dtos.TimeslotAvailabilityDto;
import com.laundrysystem.backendapi.entities.Booking;
import com.laundrysystem.backendapi.entities.LaundryAsset;
import com.laundrysystem.backendapi.entities.LaundryAssetUse;
import com.laundrysystem.backendapi.entities.PaymentCard;
import com.laundrysystem.backendapi.entities.Purchase;
import com.laundrysystem.backendapi.entities.User;
import com.laundrysystem.backendapi.exceptions.DbException;
import com.laundrysystem.backendapi.exceptions.EntryNotFoundException;
import com.laundrysystem.backendapi.exceptions.ForbiddenActionException;
import com.laundrysystem.backendapi.helpers.ActivityHelper;
import com.laundrysystem.backendapi.helpers.BookingServiceHelper;
import com.laundrysystem.backendapi.helpers.UserDataHelper;
import com.laundrysystem.backendapi.mappers.BookingMapper;
import com.laundrysystem.backendapi.mappers.LaundryAssetMapper;
import com.laundrysystem.backendapi.mappers.LaundryAssetUseMapper;
import com.laundrysystem.backendapi.mappers.PurchaseMapper;
import com.laundrysystem.backendapi.repositories.interfaces.IBookingRepository;
import com.laundrysystem.backendapi.repositories.interfaces.ILaundryAssetUseRepository;
import com.laundrysystem.backendapi.repositories.interfaces.IPurchaseRepository;
import com.laundrysystem.backendapi.services.interfaces.IBookingService;

import jakarta.transaction.Transactional;

@Service
public class BookingService implements IBookingService {
	@Autowired
	private IBookingRepository bookingRepository;
	@Autowired
	private IPurchaseRepository purchaseRepository;
	@Autowired
	private ILaundryAssetUseRepository laundryAssetUseRepository;
	@Autowired
	private UserDataHelper userDataHelper;
	
	private static final Logger logger = LoggerFactory.getLogger(BookingService.class);
	
	public List<ActivityDto> getUserActivities() throws EntryNotFoundException, DbException {
		User user = userDataHelper.getActiveUser();
		logger.info(String.format("Fetching user activities for the user with userId=%d", user.getId()));
	
		List<ActivityDto> userActivities = ActivityHelper.formatAndMapUserActivities(user);
		
		return userActivities;
	}
	
	public List<TimeslotAvailabilityDto> getLaundryAssetsEarliestAvailability() throws EntryNotFoundException, DbException, ForbiddenActionException {
		User user = userDataHelper.getActiveUser();
		logger.info(String.format("Fetching laundry assets earliest availablities for the user with userId=%d", user.getId()));
		
		List<LaundryAsset> laundryAssets = userDataHelper.getAccessibleLaundryAssets(user);
		
		List<TimeslotAvailabilityDto> earliestAvailabilities = BookingServiceHelper.getEarliestAvailabilities(laundryAssets, user);
		return earliestAvailabilities;
	}
	
	public List<BookingDto> getDailyAssetBookings(BookingRequestDto dailyBookingsRequest) throws DbException, EntryNotFoundException, ForbiddenActionException {
		User user = userDataHelper.getActiveUser();
		logger.info(String.format("Fetching daily asset bookings with for the request: %s", dailyBookingsRequest.toString()));
		
		List<LaundryAsset> laundryAssets = userDataHelper.getAccessibleLaundryAssets(user);
		LaundryAsset targetAsset = BookingServiceHelper.getTargetLaundryAsset(dailyBookingsRequest.getAssetId(), laundryAssets);
		
		List<BookingDto> dailyAssetBookings = BookingServiceHelper.getAssetBookingsOnDate(targetAsset, dailyBookingsRequest.getTimeslot());
		
		return dailyAssetBookings;
	}
	
	@Transactional
	public PurchaseDto purchaseLaundryService(int assetId) throws DbException, EntryNotFoundException {
		User user = userDataHelper.getActiveUser();
		
		List<LaundryAsset> laundryAssets = userDataHelper.getAccessibleLaundryAssets(user);
		PaymentCard activePaymentCard = userDataHelper.getActivePaymentCardFor(user);
		LaundryAsset assetToBeUsed = BookingServiceHelper.getTargetLaundryAsset(assetId, laundryAssets);

		// preparing purchase object for storing
		Purchase newPurchase = new Purchase(
			new Timestamp(System.currentTimeMillis()),
			user,
			activePaymentCard,
			assetToBeUsed
		);
			
		// storing booking/s to the database and connecting purchase object to the later booking
		if (BookingServiceHelper.isAllowedToPurchase(assetToBeUsed, user)) {
			List<Booking> slotsToBook = BookingServiceHelper.getSlotsToBook(user, assetToBeUsed);
			newPurchase.setBooking(slotsToBook.get(0));	// purchase is connected to the first booking slot
			try {
				for (Booking booking: slotsToBook) {
					booking.setUser(user);
					booking.setLaundryAsset(assetToBeUsed);
					bookingRepository.save(booking);
				}				
			} catch (Exception exc) {
				logger.error(String.format("An error occured while storing a booking in the database. - [err=%s]", exc.getStackTrace().toString()));
				throw new DbException();
			}
		} else {
			logger.error(String.format("User id=%d attempted to make a purchase of assetId=%d at the booked/running time.",
				user.getId(), assetToBeUsed.getId()));
			throw new ForbiddenActionException();
		}
		
		// TODO: make a payment card charge!
		logger.info(String.format("The card (cardId=%d) has succesfully been charged.", activePaymentCard.getId()));
		
		// creating purchase in the db
		try {
			purchaseRepository.save(newPurchase);			
		} catch (Exception exc) {
			logger.error(String.format("An error occured while storing a purchase in the database. - [err=%s]", exc.getStackTrace().toString()));
			throw new DbException();
		}
		
		// TODO/NOTE: for the demonstration purposes. The laundryAssetsUse is created here
		try {
			LaundryAssetUse laundryAssetUse = LaundryAssetUseMapper.toMAP(assetToBeUsed, newPurchase.getBooking());
			laundryAssetUseRepository.save(laundryAssetUse);
		} catch (Exception exc) {
			logger.error(String.format("An error occurred while storing a laundry asset use in the database - [err=%s]", exc.getStackTrace().toString()));
		}
		
		logger.info(String.format("The purchase {%s} has been made successfully.", newPurchase.toString()));
		
		return PurchaseMapper.toDTO(newPurchase);
	}

	public List<BookingDto> getUsersFutureBookings() throws EntryNotFoundException {
		User user = userDataHelper.getActiveUser();
		
		List<BookingDto> futureBookings = BookingServiceHelper.getUserFutureBookings(user);
		
		return futureBookings;
	}

	public ActiveBookingsDto getActiveBookings() throws EntryNotFoundException{
		User user = userDataHelper.getActiveUser();
		
		ActiveBookingsDto userActiveBookings = BookingServiceHelper.getUserActiveBookings(user);
		
		return userActiveBookings;
	}
	
	@Transactional
	public List<BookingDto> bookLaundryAsset(BookingRequestDto bookingRequest) throws DbException, EntryNotFoundException {
		User user = userDataHelper.getActiveUser();
		
		List<LaundryAsset> laundryAssets = userDataHelper.getAccessibleLaundryAssets(user);
		LaundryAsset assetToBeUsed = BookingServiceHelper.getTargetLaundryAsset(bookingRequest.getAssetId(), laundryAssets);
		
		BookingServiceHelper.checkAssetAvailabilityAt(assetToBeUsed, bookingRequest.getTimeslot());
		
		List<Booking> necessaryBookingSlots = BookingServiceHelper.getBookingSlots(user, assetToBeUsed, bookingRequest.getTimeslot());
		try {
			necessaryBookingSlots.forEach(bookingSlot -> {
				bookingRepository.save(bookingSlot);				
			});
		} catch (Exception exc) {
			logger.error(String.format("An error occurred while attempting to store the newly created booking,"
					+ "on asset [assetId=%d], at [time=%s].",assetToBeUsed.getId(), bookingRequest.getTimeslot().toString()));
			throw new DbException();
		}
		
		return necessaryBookingSlots.stream().map(booking -> BookingMapper.toDTO(booking)).toList();	// NOTE: return first booking slot only
	}
	
	public List<LaundryAssetDto> getAccessibleLaundryAssets() throws DbException, EntryNotFoundException {
		User user = userDataHelper.getActiveUser();
		List<LaundryAsset> accessibleLaundryAssets = userDataHelper.getAccessibleLaundryAssets(user);
		
		return accessibleLaundryAssets.stream().map((asset) -> LaundryAssetMapper.toDto(asset)).toList();
	}
}
