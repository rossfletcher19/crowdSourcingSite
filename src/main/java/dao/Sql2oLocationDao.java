package dao;

import models.Location;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

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


}
