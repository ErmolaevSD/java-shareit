DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS request;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED ALWAYS AS identity PRIMARY KEY,
    name varchar(40) NOT NULL,
    email varchar(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS items (
    id BIGINT GENERATED ALWAYS AS identity PRIMARY KEY,
    name varchar(40) NOT NULL,
    description varchar(255),
    is_available BOOLEAN DEFAULT FALSE,
    owner_id BIGINT REFERENCES users(id),
    request_id BIGINT
);

CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT GENERATED ALWAYS AS identity PRIMARY KEY,
    start_date TIMESTAMP WITHOUT TIME ZONE,
    end_date TIMESTAMP WITHOUT TIME ZONE,
    item_id BIGINT REFERENCES items(id),
    booker_id BIGINT REFERENCES users(id),
    status varchar(40)
);

CREATE TABLE IF NOT EXISTS request (
    id BIGINT GENERATED ALWAYS AS identity PRIMARY KEY,
    description varchar(255),
    item_id BIGINT REFERENCES items(id),
    requestor_id BIGINT REFERENCES users(id),
    created TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE IF NOT EXISTS comments (
    id BIGINT GENERATED ALWAYS AS identity PRIMARY KEY,
    text varchar(255),
    item_id BIGINT REFERENCES items(id),
    author_id BIGINT REFERENCES users(id),
    created TIMESTAMP WITHOUT TIME ZONE
);