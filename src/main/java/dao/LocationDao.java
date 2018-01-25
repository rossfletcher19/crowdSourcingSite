package dao;

import models.Hike;
import models.Location;

import java.util.List;

public interface LocationDao {

    void add (Location location);

    List<Location> getAll();

    List<Hike> getAllHikesByLocation(int locationId);

    Location findById(int id);

    void update(int id, String name);

    void deleteById(int id);

    void clearAllLocations();

}
