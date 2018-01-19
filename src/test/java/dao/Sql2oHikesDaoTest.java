package dao;

import models.Hikes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Sql2o;

import org.sql2o.Connection;

/**
 * Created by Guest on 1/18/18.
 */
public class Sql2oHikesDaoTest {

    private Sql2oHikesDao hikesDao;
    private Connection conn;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        hikesDao = new Sql2oHikesDao(sql2o); //ignore me for now

        //keep connection open through entire test so it does not get erased.
        conn = sql2o.open();

    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    @Test
    public void addingHikeSetsId() throws Exception {
        Hikes hikes = new Hikes("Trillium Lake Loop", "Mt. Hood", "Great View of Mt. Hood", 5, 1);

    }
}
