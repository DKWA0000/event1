INSERT INTO roles(name)
VALUES("ADMIN"),
       ("USER");

INSERT INTO users(name, password, email) VALUES
("admin", "$2a$12$PWspUnKmwEelbN/vGzfjieucHGmlPLekUx8HuO4DBl/9yx3xVhfmi", "admin@somedomain.com"),
("user", "$2a$12$viNiHjue1ZPmK8JAJgmB5uzOTMO/VK6d7zFp4iZY1IMMb2ogIMjLi", "user@somedomain.com");

INSERT INTO user_roles(user_id, role_id) VALUES
(1, 1),
(2, 2);

INSERT INTO events(title, event_date, max_participants, current_participants)
VALUES("event1", "2025-11-29", 20, 1),
      ("event2", "2025-11-30", 25, 0),
      ("event3", "2025-12-24", 25, 0);

INSERT INTO bookings(booking_time, user_id, event_id)
VALUES("2025-11-25", 2, 1);