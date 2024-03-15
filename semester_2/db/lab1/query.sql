CREATE TABLE planets (
    id INTEGER PRIMARY KEY,
    name TEXT
);

CREATE TABLE locations (
    id INTEGER PRIMARY KEY,
    planet_id INTEGER NOT NULL,
    coordinates TEXT NOT NULL,
    FOREIGN KEY (planet_id) REFERENCES planets(id)
);

CREATE TABLE crews (
    id INTEGER PRIMARY KEY
);

CREATE TABLE humans (
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    crew_id INTEGER,
    FOREIGN KEY (crew_id) REFERENCES crews(id)
);

CREATE TABLE flights (
    id INTEGER PRIMARY KEY,
    crew_id INTEGER,
    speed INTEGER NOT NULL,
    direction TEXT NOT NULL,
    course_for_planet INTEGER,
    FOREIGN KEY (crew_id) REFERENCES crews(id),
    FOREIGN KEY (course_for_planet) REFERENCES planets(id)
);

CREATE TABLE spaceships (
    id INTEGER PRIMARY KEY,
    flight_id INTEGER,
    FOREIGN KEY (flight_id) REFERENCES flights(id)
);

CREATE TABLE computers (
    id INTEGER PRIMARY KEY,
    location_id INTEGER NOT NULL,
    FOREIGN KEY (location_id) REFERENCES locations(id)
);

CREATE TABLE connections (
    id INTEGER PRIMARY KEY,
    computer_id INTEGER NOT NULL,
    spaceship_id INTEGER NOT NULL,
    sent_data TEXT,
    delay INTEGER NOT NULL,
    FOREIGN KEY (computer_id) REFERENCES computers(id),
    FOREIGN KEY (spaceship_id) REFERENCES spaceships(id)
);