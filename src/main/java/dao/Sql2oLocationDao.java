package dao;

import org.sql2o.Sql2o;

/**
 * Created by Guest on 1/18/18.
 */
public class Sql2oLocationDao implements LocationDao {

    private final Sql2o sql2o;

    public Sql2oLocationDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }
}
