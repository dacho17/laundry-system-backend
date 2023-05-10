package com.laundrysystem.backendapi.helpers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.laundrysystem.backendapi.dtos.ActivityDto;
import com.laundrysystem.backendapi.entities.Booking;
import com.laundrysystem.backendapi.entities.Purchase;
import com.laundrysystem.backendapi.entities.User;
import com.laundrysystem.backendapi.mappers.BookingMapper;
import com.laundrysystem.backendapi.mappers.PurchaseMapper;

public class ActivityHelper {

	private static final Logger logger = LoggerFactory.getLogger(ActivityHelper.class);
	
	public static List<ActivityDto> formatAndMapUserActivities(User user) {
		List<Booking> bookings = user.getBookings();
		List<Purchase> purchases = user.getPurchases();
		
		bookings.sort((first, second) -> {
			int firstComp = second.getCreatedDate().compareTo(first.getCreatedDate());
			if (firstComp != 0) return firstComp;
			return second.getTimeslot().compareTo(first.getTimeslot());
		});

		// only one purchase connected to the timeslot atm
		purchases.sort((first, second) -> second.getCreatedDate().compareTo(first.getCreatedDate()));

		Iterator<Booking> bookIter = bookings.iterator();
		Iterator<Purchase> purIter = purchases.iterator();
		
		Booking curBook = getNextIterElement(bookIter);
		Purchase curPur = getNextIterElement(purIter);
		List<ActivityDto> activities = new ArrayList<>();
		while (curBook != null || curPur != null) {
			ActivityDto newActivity;
			if (curBook == null) {
				newActivity = (ActivityDto)PurchaseMapper.toDTO(curPur);
				activities.add(newActivity);
				curPur = getNextIterElement(purIter);
			} else if (curPur == null) {
				newActivity = (ActivityDto)BookingMapper.toDTO(curBook);
				activities.add(newActivity);
				curBook = getNextIterElement(bookIter);
			} else {	// compare the elements
				long curBookingTime = curBook.getCreatedDate().getTime();
				long curPurchaseTime = curPur.getCreatedDate().getTime();
				if (curBookingTime > curPurchaseTime) {
					newActivity = (ActivityDto)BookingMapper.toDTO(curBook);
					activities.add(newActivity);
					curBook = getNextIterElement(bookIter);
				} else if (curPurchaseTime > curBookingTime) {
					newActivity = (ActivityDto)PurchaseMapper.toDTO(curPur);
					activities.add(newActivity);
					curPur = getNextIterElement(purIter);
				} else {	// booking and purchase were created at the same time booking purchase will be added first
					activities.add((ActivityDto)PurchaseMapper.toDTO(curPur));
					activities.add((ActivityDto)BookingMapper.toDTO(curBook));
					curBook = getNextIterElement(bookIter);
					curPur = getNextIterElement(purIter);
				}
			}
		}
		
		logger.info(String.format("Activities fetched for the user with userId=%d. %d bookings and %d purchases.",
				user.getId(), bookings.size(), purchases.size()));
		
		return activities;
	}
	
	private static <T> T getNextIterElement(Iterator<T> iter) {
		try {
			return iter.next();
		} catch(Exception exc) {
			return null;
		}
	}
}
