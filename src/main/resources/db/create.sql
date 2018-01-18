SET MODE PostgreSQL;

CREATE TABLE IF NOT EXISTS hikes (
  id int PRIMARY KEY auto_increment,
  description VARCHAR,
  location VARCHAR,
  distance INT,
  season VARCHAR,
  difficulty VARCHAR
);