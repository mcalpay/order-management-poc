INSERT INTO om_user (ID, user_name, password, role) VALUES (1, 'admin', '$2a$10$rBeFTaHfpRkuPo5S4Qx/o.DMzP6zYN9raG/1X9hUO1d5mQ8HQQQbe', 0)

INSERT INTO om_user (ID, user_name, password, role) VALUES (2, 'user', '$2a$10$rBeFTaHfpRkuPo5S4Qx/o.DMzP6zYN9raG/1X9hUO1d5mQ8HQQQbe', 1)
INSERT INTO asset (ID, asset_name, customer_id, size, usable_size) VALUES (1, 'TRY', 2, 1000, 1000);
INSERT INTO asset (ID, asset_name, customer_id, size, usable_size) VALUES (2, 'WV', 2, 1000, 1000);

INSERT INTO om_user (ID, user_name, password, role) VALUES (3, 'investor', '$2a$10$rBeFTaHfpRkuPo5S4Qx/o.DMzP6zYN9raG/1X9hUO1d5mQ8HQQQbe', 1)
INSERT INTO asset (ID, asset_name, customer_id, size, usable_size) VALUES (3, 'APPLE', 3, 1000, 1000);

INSERT INTO om_user (ID, user_name, password, role) VALUES (4, 'trader', '$2a$10$rBeFTaHfpRkuPo5S4Qx/o.DMzP6zYN9raG/1X9hUO1d5mQ8HQQQbe', 1)
INSERT INTO asset (ID, asset_name, customer_id, size, usable_size) VALUES (4, 'TRY', 4, 1000, 1000);
