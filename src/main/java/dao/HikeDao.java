package dao;

import models.Hike;

import java.util.List;

public interface HikeDao {

    void add(Hike hike);

    List<Hike> getAll();

    Hike getById(int id);

    void update(int id, String notesOnHike, int locationId);

    void deleteById(int id);

    void clearAllHikes();
}
