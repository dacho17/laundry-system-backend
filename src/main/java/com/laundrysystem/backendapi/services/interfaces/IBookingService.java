package com.laundrysystem.backendapi.services.interfaces;

import java.util.List;

import com.laundrysystem.backendapi.dtos.ActiveBookingsDto;
import com.laundrysystem.backendapi.dtos.ActivityDto;
import com.laundrysystem.backendapi.dtos.BookingDto;
import com.laundrysystem.backendapi.dtos.BookingRequestDto;
import com.laundrysystem.backendapi.dtos.DailyBookingRequestDto;
import com.laundrysystem.backendapi.dtos.LaundryAssetDto;
import com.laundrysystem.backendapi.dtos.PurchaseDto;
import com.laundrysystem.backendapi.dtos.TimeslotAvailabilityDto;
import com.laundrysystem.backendapi.exceptions.DbException;
import com.laundrysystem.backendapi.exceptions.EntryNotFoundException;
import com.laundrysystem.backendapi.exceptions.ForbiddenActionException;

public interface IBookingService {
	List<ActivityDto> getUserActivities() throws EntryNotFoundException, DbException;
	List<TimeslotAvailabilityDto> getLaundryAssetsEarliestAvailability() throws EntryNotFoundException, DbException;
	PurchaseDto purchaseLaundryService(int assetId) throws DbException, EntryNotFoundException;
	List<BookingDto> bookLaundryAsset(BookingRequestDto bookingRequest) throws DbException, EntryNotFoundException;
	List<BookingDto> getDailyAssetBookings(DailyBookingRequestDto dailyBookingsRequest) throws DbException, EntryNotFoundException, ForbiddenActionException;
	List<BookingDto> getUsersFutureBookings() throws EntryNotFoundException;
	List<LaundryAssetDto> getAccessibleLaundryAssets() throws DbException, EntryNotFoundException;
	ActiveBookingsDto getActiveBookings() throws EntryNotFoundException;
}
