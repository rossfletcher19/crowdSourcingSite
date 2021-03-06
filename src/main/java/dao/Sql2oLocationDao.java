package dao;

import models.Hike;
import models.Location;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

public class Sql2oLocationDao implements LocationDao {

    private final Sql2o sql2o;

    public Sql2oLocationDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void add(Location location) {
        String sql = "INSERT INTO location (name) VALUES (:name)";
        try (Connection con = sql2o.open()) {
            int id = (int) con.createQuery(sql)
                    .bind(location)
//                    .addParameter("name", location.getName())
//                    .addColumnMapping("NAME", "name")
                    .executeUpdate()
                    .getKey();
            location.setId(id);
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public List<Location> getAll() {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM location")
                    .executeAndFetch(Location.class);
        }
    }

    @Override
    public Location findById(int locationId) {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM location WHERE id = :id")
                    .addParameter("id", locationId)
                    .executeAndFetchFirst(Location.class);
        }
    }

    @Override
    public  List<Hike> getAllHikesByLocation(int locationId) {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM hike WHERE locationId = :locationId")
                    .addParameter("locationId", locationId)
                    .executeAndFetch(Hike.class);
        }
    }


    @Override
    public void update(int id, String newName) {
        String sql = "UPDATE location SET name = :name WHERE id=:id";
        try(Connection con = sql2o.open()){
            con.createQuery(sql)
                    .addParameter("name", newName)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }

    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE from location WHERE id=:id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void clearAllLocations() {
        String sql = "DELETE from location";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }


}
