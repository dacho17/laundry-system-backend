-- use laundry_system_db;

select 'Population script started executing' AS '';

INSERT INTO users (created_date, username, `password`, `role`, `name`, surname, email, mobile_number, loyalty_points)
VALUES (CURRENT_TIMESTAMP(), 'resadmin', '$2a$10$Btc3JSGM5X7kNg6xEsf4HO57TX2nxZA/YFpruLccjRFqjf.v5yvLa', 2, 'Residence', 'Admin', 'admin@admin.my', '12344321', 0);
SET @resAdminId := (SELECT id FROM users where username = 'resadmin');

INSERT INTO addresses (created_date, street_name, street_number, postal_code, city, country)
VALUES (CURRENT_TIMESTAMP(), 'Lorong Damai 13 Kiri, Kampung Datuk Keramat', '1', '55000', 'Kuala Lumpur', 'Malaysia');
SET @resAddressId := (SELECT id FROM addresses
    where street_name = 'Lorong Damai 13 Kiri, Kampung Datuk Keramat'
    and street_number = '1');

INSERT INTO residences (created_date, `name`, address_id)
VALUES (CURRENT_TIMESTAMP(), 'Coliv Damai Residence', @resAddressId);

INSERT INTO tenancies (created_date, tenancy_start, tenancy_end, user_id, residence_id)
VALUES (CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), TIMESTAMPADD(MONTH, 24, CURRENT_TIMESTAMP()), @resAdminId, @resAddressId);

INSERT INTO laundry_assets (created_date, `name`, asset_type, 
running_time, service_price, currency, is_operational, residence_id)
VALUES
(CURRENT_TIMESTAMP(), 'Washing Machine 1', 1, 40, 5, 'MYR', 1, @resAddressId),
(CURRENT_TIMESTAMP(), 'Washing Machine 2', 1, 45, 5, 'MYR', 1, @resAddressId),
(CURRENT_TIMESTAMP(), 'Drying Machine 1', 2, 30, 5, 'MYR', 1, @resAddressId),
(CURRENT_TIMESTAMP(), 'Drying Machine 2', 2, 35, 5, 'MYR', 1, @resAddressId),
(CURRENT_TIMESTAMP(), 'Washing Machine 3', 1, 45, 5, 'MYR', 1, @resAddressId),
(CURRENT_TIMESTAMP(), 'Drying Machine 3', 2, 35, 5, 'MYR', 1, @resAddressId),
(CURRENT_TIMESTAMP(), 'Washing Machine 4', 1, 45, 5, 'MYR', 0, @resAddressId);

INSERT INTO loyalty_offers (created_date, name, price, currency, loyalty_points, expiry_date)
VALUES
(CURRENT_TIMESTAMP(), '10 for 8', 40, 'MYR', 50, TIMESTAMPADD(MONTH, 6, CURRENT_TIMESTAMP())),
(CURRENT_TIMESTAMP(), '20 for 12', 60, 'MYR', 100, TIMESTAMPADD(MONTH, 6, CURRENT_TIMESTAMP()));

select 'Population script finished executing' AS '';
