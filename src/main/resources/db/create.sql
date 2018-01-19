SET MODE PostgreSQL;

CREATE TABLE IF NOT EXISTS hike (
  id int PRIMARY KEY auto_increment,
  nameOfHike VARCHAR,
  locationOfHike VARCHAR,
  notesOnHike VARCHAR,
  ratingHike INT,
  hikeCompleted BOOLEAN,
  locationId INT,
);

CREATE TABLE IF NOT EXISTS location (
  id int PRIMARY KEY auto_increment,
  locationDistance INT,
  locationDifficulty INT,
  locationCity VARCHAR,
  locationState VARCHAR,
  locationCountry VARCHAR,
);