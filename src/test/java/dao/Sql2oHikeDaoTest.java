package dao;

import models.Hike;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Sql2o;

import org.sql2o.Connection;


import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Guest on 1/18/18.
 */
public class Sql2oHikeDaoTest {

    private Sql2oHikeDao hikeDao;
    private Connection conn;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        hikeDao = new Sql2oHikeDao(sql2o); //ignore me for now

        //keep connection open through entire test so it does not get erased.
        conn = sql2o.open();

    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    public Hike setupNewHike() {
        return new Hike("Trillium Lake Loop", "Mt. Hood", "Great View of Mt. Hood",1);
    }

    @Test
    public void addingHikeSetsId() throws Exception {
        Hike hike = setupNewHike();
        int originalHikeId = hike.getId();
        hikeDao.add(hike);
        assertNotEquals(originalHikeId, hike.getId());
    }

    @Test
    public void existingHikeCanBeFoundById() throws Exception {
        Hike hike = setupNewHike();
        hikeDao.add(hike);
        Hike foundHike = hikeDao.getById(hike.getId());
        assertEquals(hike, foundHike);

    }

    @Test
    public void addedHikesAreReturnedWithGetAll() throws Exception {
        Hike hike = setupNewHike();
        Hike hike2 = setupNewHike();
        hikeDao.add(hike);
        hikeDao.add(hike2);
        assertEquals(2, hikeDao.getAll().size());
    }

    @Test
    public void noHikesReturnsEmptyList() throws Exception {
        assertEquals(0, hikeDao.getAll().size());
    }


}
