package com.laundrysystem.backendapi.helpers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.laundrysystem.backendapi.dtos.ActivityHistoryEntryDto;
import com.laundrysystem.backendapi.entities.Booking;
import com.laundrysystem.backendapi.entities.LoyaltyOfferPurchase;
import com.laundrysystem.backendapi.entities.Purchase;
import com.laundrysystem.backendapi.entities.User;
import com.laundrysystem.backendapi.enums.ActivityHistoryEntryType;
import com.laundrysystem.backendapi.mappers.BookingMapper;
import com.laundrysystem.backendapi.mappers.LoyaltyOfferMapper;
import com.laundrysystem.backendapi.mappers.PurchaseMapper;

public class ActivityHelper {

	private static final Logger logger = LoggerFactory.getLogger(ActivityHelper.class);
	
	public static List<ActivityHistoryEntryDto> formatAndMapUserActivities(User user) {
		List<Booking> bookings = user.getBookings();
		List<Purchase> purchases = user.getPurchases();
		List<LoyaltyOfferPurchase> loyaltyOfferPurchases = user.getLoyaltyOfferPurchases();
		
		bookings.sort((first, second) -> {
			int firstComp = second.getCreatedDate().compareTo(first.getCreatedDate());
			if (firstComp != 0) return firstComp;
			return second.getTimeslot().compareTo(first.getTimeslot());
		});

		// only one purchase connected to the timeslot atm
		purchases.sort((first, second) -> second.getCreatedDate().compareTo(first.getCreatedDate()));

		loyaltyOfferPurchases.sort((first, second) -> second.getCreatedDate().compareTo(first.getCreatedDate()));

		Iterator<Booking> bookIter = bookings.iterator();
		Iterator<Purchase> purIter = purchases.iterator();
		Iterator<LoyaltyOfferPurchase> lopIter = loyaltyOfferPurchases.iterator();
		
		Booking curBook = getNextIterElement(bookIter);
		Purchase curPur = getNextIterElement(purIter);
		LoyaltyOfferPurchase curLop = getNextIterElement(lopIter);
		List<ActivityHistoryEntryDto> activities = new ArrayList<>();
		while (curBook != null || curPur != null || curLop != null) {
			ActivityHistoryEntryDto newActivity;
			
			System.out.println(String.format("Before getNextType curBook=%b, curPur=%b, curLop=%b\n", curBook != null, curPur != null, curLop != null));
			ActivityHistoryEntryType entryType = getNextType(curBook, curPur, curLop);
			System.out.println(String.format("After getNextType curBook=%b, curPur=%b, curLop=%b\n", curBook != null, curPur != null, curLop != null));
			System.out.println(String.format("EntryType=%s\n", entryType));
			switch (entryType) {
				case ASSET_BOOKING:
					newActivity = BookingMapper.toActivityHistoryEntry(curBook);
					activities.add(newActivity);
					curBook = getNextIterElement(bookIter);
					break;
				case ASSET_PURCHASE:
					newActivity = PurchaseMapper.toActivityHistoryEntry(curPur);
					activities.add(newActivity);
					curPur = getNextIterElement(purIter);
					break;
				case OFFER_PURCHASE:
					newActivity = LoyaltyOfferMapper.toActivityHistoryEntry(curLop);
					activities.add(newActivity);
					curLop = getNextIterElement(lopIter);
					break;
				default:
					break;
			}
		}
		
		logger.info(String.format("Activities fetched for the user with userId=%d. %d bookings, %d purchases, and %d loyalty offer purchases.",
				user.getId(), bookings.size(), purchases.size(), loyaltyOfferPurchases.size()));
		
		return activities;
	}

	private static ActivityHistoryEntryType getNextType(Booking booking, Purchase purchase, LoyaltyOfferPurchase loyaltyOfferPurchase) {
		// compare them between each other and return the following type
		class ActivityHistoryPair {
			ActivityHistoryEntryType type;
			Long createdDateTs;
			
			ActivityHistoryPair(ActivityHistoryEntryType type, Long createdDateTs) {
				this.type = type;
				this.createdDateTs = createdDateTs;
			}
		}
		
		List<ActivityHistoryPair> compList = new ArrayList<>();
		if (purchase != null) {	// NOTE: order of adding purchases and bookings in this list matters because sorting is not done on type if timestamps are the same.
			ActivityHistoryPair pur = new ActivityHistoryPair(
				ActivityHistoryEntryType.ASSET_PURCHASE, Long.valueOf(purchase.getCreatedDate().getTime()));
			compList.add(pur);
		}
		if (booking != null) {
			ActivityHistoryPair book = new ActivityHistoryPair(
				ActivityHistoryEntryType.ASSET_BOOKING, Long.valueOf(booking.getCreatedDate().getTime()));
			compList.add(book);
		}
		if (loyaltyOfferPurchase != null) {
			ActivityHistoryPair lop = new ActivityHistoryPair(
				ActivityHistoryEntryType.OFFER_PURCHASE, Long.valueOf(loyaltyOfferPurchase.getCreatedDate().getTime()));
			compList.add(lop);
		}
		
		ActivityHistoryPair maxRes = compList.stream().max((first, second) -> first.createdDateTs.compareTo(second.createdDateTs)).get();
		return maxRes.type;
	}
	
	private static <T> T getNextIterElement(Iterator<T> iter) {
		try {
			return iter.next();
		} catch(Exception exc) {
			return null;
		}
	}
}
