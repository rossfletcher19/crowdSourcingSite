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
        hikeDao = new Sql2oHikeDao(sql2o);
        conn = sql2o.open();

    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    public Hike setupNewHike() {
        return new Hike("Trillium Lake Loop", "Great View of Mt. Hood",1, 1);
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

    @Test
    public void updateHikeContent() throws Exception {
        String initialNotes = "Great family spot";
        Hike hike = new Hike("Haag Lake", initialNotes, 5,1);
        hikeDao.add(hike);

        hikeDao.update(hike.getId(),"Great Family spot, Good Fishing",1);
        Hike updatedHike = hikeDao.getById(hike.getId());
        assertNotEquals(initialNotes, updatedHike.getNotesOnHike());
    }

    @Test
    public void deleteByIdDeletesCorrectHike() throws Exception {
        Hike hike = setupNewHike();
        hikeDao.add(hike);
        hikeDao.deleteById(hike.getId());
        assertEquals(0, hikeDao.getAll().size());
    }

    @Test
    public void clearAllClearsAllHikes() throws Exception {
        Hike hike = setupNewHike();
        Hike oterHike = setupNewHike();
        hikeDao.add(hike);
        hikeDao.add(oterHike);

        int daoSize = hikeDao.getAll().size();
        hikeDao.clearAllHikes();
        assertTrue(daoSize > 0 && daoSize > hikeDao.getAll().size());
    }

    @Test
    public void locationIdIsReturnedCorrectly() throws Exception {
        Hike hike = setupNewHike();
        int originalLocationId = hike.getLocationId();
        hikeDao.add(hike);
        assertEquals(originalLocationId, hikeDao.getById(hike.getId()).getLocationId());
    }


}
