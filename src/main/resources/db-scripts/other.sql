-- INSERT INTO bookings (created_date, timeslot, user_id, laundry_asset_id)
-- VALUES
-- (TIMESTAMP('2023-04-29 01:23:11'), TIMESTAMP('2023-04-29 10:00:00'), 1, 2),
-- (TIMESTAMP('2023-04-29 01:14:11'), TIMESTAMP('2023-04-29 12:00:00'), 1, 2),
-- (TIMESTAMP('2023-04-29 01:11:09'), TIMESTAMP('2023-04-29 11:00:00'), 1, 3),
-- (TIMESTAMP('2023-04-29 01:23:11'), TIMESTAMP('2023-04-29 09:00:00'), 1, 4),
-- (TIMESTAMP('2023-04-29 01:14:11'), TIMESTAMP('2023-04-29 09:00:00'), 1, 5),
-- (TIMESTAMP('2023-04-29 01:11:09'), TIMESTAMP('2023-04-29 10:00:00'), 1, 5),
-- (TIMESTAMP('2023-04-29 01:23:11'), TIMESTAMP('2023-04-29 11:00:00'), 1, 5),
-- (TIMESTAMP('2023-04-29 01:14:11'), TIMESTAMP('2023-04-29 13:00:00'), 1, 5),
-- (TIMESTAMP('2023-04-29 01:11:09'), TIMESTAMP('2023-04-29 09:00:00'), 1, 6),
-- (TIMESTAMP('2023-04-29 01:23:11'), TIMESTAMP('2023-04-29 10:00:00'), 1, 6),
-- (TIMESTAMP('2023-04-29 01:14:11'), TIMESTAMP('2023-04-29 11:00:00'), 1, 6),
-- (TIMESTAMP('2023-04-29 01:11:09'), TIMESTAMP('2023-04-29 10:00:00'), 1, 7),
-- (TIMESTAMP('2023-04-29 01:23:11'), TIMESTAMP('2023-04-29 11:00:00'), 1, 7),
-- (TIMESTAMP('2023-04-29 01:14:11'), TIMESTAMP('2023-04-29 12:00:00'), 1, 7);


-- INSERT INTO payment_cards (created_date, is_being_used, token, 
-- card_holder_name, expiry_date, last_four_digits, user_id)
-- VALUES
-- (CURRENT_TIMESTAMP(), 1, 'Token of trust', 'DummyCard Hogar', '07/24', '1234', 1),
-- (CURRENT_TIMESTAMP(), 0, 'Expired card token', 'DummyCard prevHolder', '03/22', '9999', 1);

-- INSERT INTO bookings (created_date, timeslot, user_id, laundry_asset_id)
-- VALUES
-- (TIMESTAMP('2023-03-28 15:23:11'), TIMESTAMP('2023-03-28 18:00:00'), 1, 1),
-- (TIMESTAMP('2023-03-18 16:14:11'), TIMESTAMP('2023-03-18 18:00:00'), 1, 1),
-- (TIMESTAMP('2023-04-13 22:11:09'), TIMESTAMP('2023-04-19 06:00:00'), 1, 1),
-- (TIMESTAMP('2023-04-13 22:12:03'), TIMESTAMP('2023-04-19 07:00:00'), 1, 3),
-- (TIMESTAMP('2023-04-28 07:33:03'), TIMESTAMP('2023-04-28 11:00:00'), 1, 1),
-- (TIMESTAMP('2023-04-28 07:32:24'), TIMESTAMP('2023-04-28 10:00:00'), 1, 3);
-- -- user_id and laundry_asset_id need to be added

-- INSERT INTO purchases (created_date, user_id, payment_card_id, laundry_asset_id, booking_id)
-- VALUES
-- (TIMESTAMP('2023-04-19 06:03:36'), 1, 2, 1, 3),
-- (TIMESTAMP('2023-04-19 07:11:06'), 1, 2, 3, 4),
-- (TIMESTAMP('2023-03-18 18:03:22'), 1, 2, 1, 2),
-- (TIMESTAMP('2023-03-28 18:01:14'), 1, 2, 1, 1),
-- (TIMESTAMP('2023-04-28 11:11:11'), 1, 2, 3, 6),
-- (TIMESTAMP('2023-04-28 10:11:11'), 1, 2, 1, 5);
