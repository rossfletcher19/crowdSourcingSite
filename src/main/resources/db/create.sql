SET MODE PostgreSQL;

CREATE TABLE IF NOT EXISTS hikes (
  id int PRIMARY KEY auto_increment,
  hikeName VARCHAR,
  hikeLocation VARCHAR,
  hikeNotes VARCHAR,
  hikeRating INT,
  hikeCompleted BOOLEAN,
  locationId INT,
);

CREATE TABLE IF NOT EXISTS locations (
  id int PRIMARY KEY auto_increment,
  locationDistance INT,
  locationDifficulty INT,
  locationCity VARCHAR,
  locationState VARCHAR,
  locationCountry VARCHAR,
);