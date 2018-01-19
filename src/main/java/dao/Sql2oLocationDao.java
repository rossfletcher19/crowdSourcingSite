package dao;

import models.Hike;
import models.Location;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

/**
 * Created by Guest on 1/18/18.
 */
public class Sql2oLocationDao implements LocationDao {

    private final Sql2o sql2o;

    public Sql2oLocationDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void add(Location location) {
        String sql = "INSERT INTO location (nameCity, nameState, nameCountry) VALUES (:nameCity, :nameState, :nameCountry)";
        try (Connection con = sql2o.open()) {
            int id = (int) con.createQuery(sql)
                    .bind(location)
//                    .addParameter("nameCity", location.getNameCity())
//                    .addParameter("nameState", location.getNameState())
//                    .addParameter("nameCountry", location.getNameCountry())
//                    .addColumnMapping("NAMECITY", "nameCity")
//                    .addColumnMapping("NAMESTATE", "nameState")
//                    .addColumnMapping("NAMECOUNTRY", "nameCountry")
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
    public void update(int id, String newNameCity, String newNameState, String newNameCountry) {
        String sql = "UPDATE location SET nameCity = :nameCity, nameState = :nameState, nameCountry = :nameCountry WHERE id=:id";
        try(Connection con = sql2o.open()){
            con.createQuery(sql)
                    .addParameter("nameCity", newNameCity)
                    .addParameter("nameState", newNameState)
                    .addParameter("nameCountry", newNameCountry)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }

    }


}
