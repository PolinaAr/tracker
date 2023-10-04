CREATE TABLE IF NOT EXISTS users (
   id BIGSERIAL PRIMARY KEY,
   name VARCHAR(50) NOT NULL,
   lastname VARCHAR (50) NOT NULL,
   email VARCHAR(50) UNIQUE NOT NULL,
   password VARCHAR(50) NOT NULL
);


CREATE TABLE IF NOT EXISTS tracks (
    id BIGSERIAL PRIMARY KEY,
    time double precision NOT NULL,
    note text NOT NULL,
    date date NOT NULL,
    user_id BIGINT REFERENCES "users"(id)
);

CREATE TABLE IF NOT EXISTS chat_ids(
    id BIGINT PRIMARY KEY
);

DROP table tracks;
DROP table users;
