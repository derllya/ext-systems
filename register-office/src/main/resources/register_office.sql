DROP TABLE IF EXISTS person;

CREATE TABLE person (
    person_id SERIAL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    PRIMARY KEY (person_id)
);