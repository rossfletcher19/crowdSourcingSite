package dao;

import models.Hike;
import models.Location;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Sql2o;

import org.sql2o.Connection;

import static org.junit.Assert.*;

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

    @Test
    public void getAllHikesByLocationReturnsHikesCorrectly() throws Exception {
        Location location = setupNewLocation();
        locationDao.add(location);
        int locationId = location.getId();
        Hike newHike = new Hike("Opal Creek", "Beautiful Blue Pools", 5, locationId);
        Hike otherHike = new Hike("Mt. Hood Hike", "Great forest hike", 5, locationId);
        Hike thirdHike = new Hike("Silver Falls", "Beautiful Water Falls", 5, locationId);
        hikeDao.add(newHike);
        hikeDao.add(otherHike);

        assertTrue(locationDao.getAllHikesByLocation(locationId).size() == 2);
        assertTrue(locationDao.getAllHikesByLocation(locationId).contains(newHike));
        assertTrue(locationDao.getAllHikesByLocation(locationId).contains(otherHike));
        assertFalse(locationDao.getAllHikesByLocation(locationId).contains(thirdHike));
    }

    @Test
    public void updateChangesLocationContent() throws Exception {
        String initialCityName = "Portland";
        Location location = new Location(initialCityName);
        locationDao.add(location);

        locationDao.update(location.getId(), "SW Portland");
        Location updatedLocation = locationDao.findById(location.getId());
        assertNotEquals(initialCityName, updatedLocation.getName());
    }

    @Test
    public void deleteByIdDeletesCorrectLocation() throws Exception {
        Location location = setupNewLocation();
        locationDao.add(location);
        locationDao.deleteById(location.getId());
        assertEquals(0, locationDao.getAll().size());
    }

    @Test
    public void clearAllLocationsClearsLocations() throws Exception {
        Location location = setupNewLocation();
        Location otherLocation = setupNewLocation();
        locationDao.add(location);
        locationDao.add(otherLocation);
        int daoSize = locationDao.getAll().size();
        locationDao.clearAllLocations();
        assertTrue(daoSize > 0 && daoSize > locationDao.getAll().size());
    }


    public Location setupNewLocation() {
        return new Location("Estacada");
    }
}
