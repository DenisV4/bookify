insert into hotels (name, title, city, address, distance, rating, ratings_count) values ('testHotel', 'title', 'city', 'address', 1.0, 5.0, 10);
insert into rooms (name, description, number, price, guests_number, hotel_id) values ('testRoom', 'description', 10, 10.0, 1, 1);
insert into users (name, email, password) values ('testUser', 'testUser@email.com', 'cdfd28f61974b2a3651231a4cb179a6946d679384738c77983b495d61a349c44');
insert into bookings (room_id, user_id, check_in_date, check_out_date) values (1, 1, '2024-09-10', '2024-09-15');
insert into bookings (room_id, user_id, check_in_date, check_out_date) values (1, 1, '2024-09-20', '2024-09-25');
