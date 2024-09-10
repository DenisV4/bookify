CREATE SCHEMA IF NOT EXISTS booking_schema;

CREATE TABLE IF NOT EXISTS booking_schema.hotels (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    distance FLOAT NOT NULL,
    rating FLOAT DEFAULT 0 NOT NULL,
    ratings_count INTEGER DEFAULT 0 NOT NULL
);

CREATE TABLE IF NOT EXISTS booking_schema.rooms (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    number VARCHAR(255) NOT NULL,
    price FLOAT NOT NULL,
    guests_number INTEGER DEFAULT 1 NOT NULL,
    hotel_id BIGINT NOT NULL,
    FOREIGN KEY (hotel_id) REFERENCES hotels(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS booking_schema.users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255),
    password VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS booking_schema.user_roles (
    id BIGSERIAL PRIMARY KEY,
    authority VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS booking_schema.bookings (
    id BIGSERIAL PRIMARY KEY,
    room_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    FOREIGN KEY (room_id) REFERENCES rooms(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
