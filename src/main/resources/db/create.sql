SET MODE PostgreSQL;

CREATE TABLE IF NOT EXISTS hike (
  id int PRIMARY KEY auto_increment,
  nameOfHike VARCHAR,
  notesOnHike VARCHAR,
  ratingHike INT,
  locationId INT
);

CREATE TABLE IF NOT EXISTS location (
  id int PRIMARY KEY auto_increment,
  name VARCHAR
);