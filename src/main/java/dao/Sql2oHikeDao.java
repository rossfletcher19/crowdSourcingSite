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
        String sql = "INSERT INTO hike (nameOfHike, locationOfHike, notesOnHike, ratingHike) VALUES (:nameOfHike, :locationOfHike, :notesOnHike, :ratingHike)";
        try (Connection con = sql2o.open()){
            int id = (int) con.createQuery(sql)
                    .bind(hike)
                    .executeUpdate()
                    .getKey();
            hike.setId(id);
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }

    }

}
