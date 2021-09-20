CREATE TABLE reservation(
id SERIAL PRIMARY KEY,
email VARCHAR(255),
first_name VARCHAR(255),
last_name VARCHAR(255),
arrival_date TIMESTAMP,
departure_date TIMESTAMP,
EXCLUDE USING GIST (tsrange(arrival_date, departure_date) WITH &&)
);