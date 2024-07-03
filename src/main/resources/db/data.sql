TRUNCATE TABLE organizers CASCADE;
TRUNCATE TABLE events CASCADE;
TRUNCATE TABLE event_guests CASCADE;
TRUNCATE TABLE tickets CASCADE;

INSERT INTO organizers(id, username, password, date_registered)VALUES
(100, 'username', '$2a$10$zyOFSLtxrnoRW6LAvim2puMDrG9cnYAbY.jrZfkn1un9hcGyX.uVW', '2024-07-03 13:17:21.985453'),
(101, 'username2', '$2a$10$zyOFSLtxrnoRW6LAvim2puMDrG9cnYAbY.jrZfkn1un9hcGyX.uVW', '2024-07-03 13:17:21.985453');

INSERT INTO events(id, organizer_id, title, description, event_date)VALUES
(200, 100, 'title', 'description', '2024-07-03 13:17:21.985453'),
(201, 100, 'title', 'description', '2024-07-03 13:17:21.985453'),
(202, 101, 'title', 'description', '2024-07-03 13:17:21.985453'),
(203, 100, 'title', 'description', '2024-07-03 13:17:21.985453');

INSERT INTO event_guests(event_id, guests)VALUES
(200, 'guest'),
(200, 'guest2'),
(201, 'guest'),
(200, 'guest3');

INSERT INTO tickets(id, event_id, price, discount, type, date_created)VALUES
(300, 200, 3500, 500, 'REGULAR', '2024-07-03 13:17:21.985453'),
(301, 200, 15000, 2500, 'VVIP', '2024-07-03 13:17:21.985453'),
(302, 200, 8000, 1500, 'VIP', '2024-07-03 13:17:21.985453'),
(303, 201, 8000, 1500, 'REGULAR', '2024-07-03 13:17:21.985453'),
(304, 202, 8000, 1500, 'VIP', '2024-07-03 13:17:21.985453');
