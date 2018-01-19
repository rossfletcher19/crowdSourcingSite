SET MODE PostgreSQL;

CREATE TABLE IF NOT EXISTS hike (
  id int PRIMARY KEY auto_increment,
  hikeName VARCHAR,
  hikeLocation VARCHAR,
  hikeNotes VARCHAR,
  hikeRating INT,
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