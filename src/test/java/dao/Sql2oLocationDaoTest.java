package dao;

import models.Location;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Sql2o;

import org.sql2o.Connection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by Guest on 1/18/18.
 */
public class Sql2oLocationDaoTest {

    private Sql2oLocationDao locationDao;
    private Sql2oHikeDao hikeDao;
    private Connection conn;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        locationDao = new Sql2oLocationDao(sql2o);
        hikeDao = new Sql2oHikeDao(sql2o);

        conn = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    @Test
    public void addingLocationSetsId() throws Exception {
        Location location = setupNewLocation();
        int originalLocationId = location.getId();
        locationDao.add(location);
        assertNotEquals(originalLocationId, location.getId());
    }

    @Test
    public void addedLocationsCanBeReturnedByGetAll() throws Exception {
        Location location = setupNewLocation();
        locationDao.add(location);
        assertEquals(1, locationDao.getAll().size());
    }

    @Test
    public void noLocationsReturnsEmptyList() throws Exception {
        assertEquals(0, locationDao.getAll().size());
    }

    @Test
    public void existingLocationsCanBeFoundById() throws Exception {
        Location location = setupNewLocation();
        locationDao.add(location);
        Location foundLocation = locationDao.findById(location.getId());
        assertEquals(location, foundLocation);
    }




    public Location setupNewLocation() {
        return new Location("Estacada", "Oregon", "United States", 0);
    }
}
