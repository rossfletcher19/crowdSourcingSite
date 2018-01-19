package dao;


import models.Hike;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

/**
 * Created by Guest on 1/18/18.
 */
public class Sql2oHikeDao implements HikeDao {


    private final Sql2o sql2o;

    public Sql2oHikeDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void add(Hike hike) {
        String sql = "INSERT INTO hike (nameOfHike, locationOfHike, notesOnHike, ratingHike, locationId) VALUES (:nameOfHike, :locationOfHike, :notesOnHike, :ratingHike, :locationId)";
        try (Connection con = sql2o.open()){
            int id = (int) con.createQuery(sql)
                    .bind(hike)
//                    .addParameter("nameOfHike", hike.getNameOfHike())
//                    .addParameter("locationOfHike", hike.getLocationOfHike())
//                    .addParameter("notesOnHike", hike.getNotesOnHike())
//                    .addParameter("ratingHike", hike.getRatingHike())
//                    .addParameter("locationId", hike.getLocationId())
//                    .addColumnMapping("NAMEOFHIKE", "nameOfHike")
//                    .addColumnMapping("LOCATIONOFHIKE", "locationOfHike")
//                    .addColumnMapping("NOTESONHIKE", "notesOnHike")
//                    .addColumnMapping("RATINGHIKE", "ratingHike")
//                    .addColumnMapping("LOCATIONID","locationId")
                    .executeUpdate()
                    .getKey();
            hike.setId(id);
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }

    }

    @Override
    public Hike getById(int id) {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM hike WHERE id = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(Hike.class);
        }
    }

    @Override
    public List<Hike> getAll() {
        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM hike")
                    .executeAndFetch(Hike.class);
        }
    }

    @Override
    public void update(int id, String newNotesOnHike, int locationId) {
        String sql = "UPDATE hike SET notesOnHike = :notesOnHike WHERE id=:id";
        try(Connection con = sql2o.open()){
            con.createQuery(sql)
                    .addParameter("notesOnHike", newNotesOnHike)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }

    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE from hike WHERE id=:id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex){
            System.out.println(ex);
        }
    }

    @Override
    public void clearAllHikes(){
        String sql = "DELETE from hike";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

}
