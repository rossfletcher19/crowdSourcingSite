import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.Sql2oHikeDao;
import dao.Sql2oLocationDao;
import models.Hike;
import models.Location;
import org.sql2o.Sql2o;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import static spark.Spark.*;


public class App {

    public static void main(String[] args) {
        staticFileLocation("/public");
        String connectionString = "jdbc:h2:~/hikingLog.db;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        Sql2oHikeDao hikeDao = new Sql2oHikeDao(sql2o);
        Sql2oLocationDao locationDao = new Sql2oLocationDao(sql2o);


        //get: show all hikes in all locations ck
        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            List<Location> allLocations = locationDao.getAll();
            model.put("locations", allLocations);

            List<Hike> hikes = hikeDao.getAll();
            model.put("hikes", hikes);
            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());

        //get: delete all hikes ck
        get("/hikes/delete", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            hikeDao.clearAllHikes();
            return new ModelAndView(model, "success2.hbs");
        }, new HandlebarsTemplateEngine());


        //get: show new hike form ck
        get("/hikes/new", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            List<Location> locations = locationDao.getAll();
            model.put("locations", locations);

            return new ModelAndView(model, "hike-form.hbs");
        }, new HandlebarsTemplateEngine());

        //post: process new hike form ck
        post("/hikes/new", (request, response) -> {
            Map<String, Object> model = new HashMap<>();

            List<Location> allLocations = locationDao.getAll();
            model.put("locations", allLocations);

            String nameOfHike = request.queryParams("nameOfHike");
            String notesOnHike = request.queryParams("notesOnHike");
            int ratingHike = Integer.parseInt(request.queryParams("ratingHike"));
            int locationId = Integer.parseInt(request.queryParams("locationId"));

            Hike newHike = new Hike(nameOfHike, notesOnHike, ratingHike, locationId);
            hikeDao.add(newHike);
            model.put("newHike", newHike);
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());


     //get: show a form to update a hike within a location OK
        get("/hikes/update", (request, response) -> {
            Map<String, Object> model = new HashMap<>();

            List<Location> allLocations = locationDao.getAll();
            model.put("locations", allLocations);

            List<Hike> allHikes = hikeDao.getAll();
            model.put("hikes", allHikes);

            model.put("editHike", true);
            return new ModelAndView(model, "hike-form.hbs");
        }, new HandlebarsTemplateEngine());


        //post: process a form to update a hike ck
        post("/hikes/update", (request, response) -> {
            Map<String, Object> model = new HashMap<>();

            List<Location> allLocations = locationDao.getAll();
            model.put("locations", allLocations);

            String newNotes = request.queryParams("notesOnHike");
            int newLocationId = Integer.parseInt(request.queryParams("locationId"));
            int idOfHikeToEdit = Integer.parseInt(request.queryParams("idOfHikeToEdit"));
            Hike editHike = hikeDao.getById(idOfHikeToEdit);
            hikeDao.update(idOfHikeToEdit, newNotes, newLocationId); //ignore the hardcoded categoryId for now.
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());



        // Post Routes for location

        // show new location form to create a new location OK
        get("/locations/new", (request, response) -> {
            Map<String, Object> model = new HashMap<>();

            List<Location> locations = locationDao.getAll();
            model.put("locations", locations);

            return new ModelAndView(model, "location-form.hbs");
        }, new HandlebarsTemplateEngine());

        //post: process a form to add a location ck
        post("/locations", (req, res) -> { //new
            Map<String, Object> model = new HashMap<>();
            String name = req.queryParams("name");
            Location newLocation = new Location(name);
            locationDao.add(newLocation);

            List<Location> allLocations = locationDao.getAll(); //refresh list of links for navbar.
            model.put("locations", allLocations);

            return new ModelAndView(model, "success4.hbs");
        }, new HandlebarsTemplateEngine());

        //get: show a form to update a location ck
        get("/locations/update", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            model.put("editLocation", true);

            List<Location> allLocations = locationDao.getAll();
            model.put("locations", allLocations);

            return new ModelAndView(model, "location-form.hbs");
        }, new HandlebarsTemplateEngine());

        //post: process a form to update a location and hikes it contains
        post("/locations/update", (request, response) -> {
            Map<String, Object> model = new HashMap<>();

            List<Location> allLocations = locationDao.getAll();
            model.put("locations", allLocations);
            String newName = request.queryParams("newLocationName");
            int idOfLocationToEdit = Integer.parseInt(request.queryParams("editLocationId"));
            Location editLocName = locationDao.findById(idOfLocationToEdit);
            locationDao.update(locationDao.findById(idOfLocationToEdit).getId(), newName);

            return new ModelAndView(model, "success4.hbs");
        }, new HandlebarsTemplateEngine());


        // Dynamic Routes

        //get: show an individual hike that is nested in a location ck
        get("/locations/:location_id/hikes/:hike_id", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            List<Location> locations = locationDao.getAll();
            model.put("locations", locations);

            int idOfHikeToFind = Integer.parseInt(req.params("hike_id"));
            Hike foundHike = hikeDao.getById(idOfHikeToFind);
            model.put("hike", foundHike);
            return new ModelAndView(model, "hike-detail.hbs");
        }, new HandlebarsTemplateEngine());

        //get a specific location (and the hikes it contains) OK
        get("/locations/:id", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfLocationToFind = Integer.parseInt(req.params("id"));

            List<Location> locations = locationDao.getAll();
            model.put("locations", locations);

            Location foundLocation = locationDao.findById(idOfLocationToFind);
            model.put("locations", foundLocation);
            List<Hike> allHikesByCategory = locationDao.getAllHikesByLocation(idOfLocationToFind);
            model.put("hikes", allHikesByCategory);

            return new ModelAndView(model, "location-detail.hbs");
        }, new HandlebarsTemplateEngine());

        //get: delete an individual hike ck
        get("/locations/:location_id/hikes/:hike_id/delete", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfHikeToDelete = Integer.parseInt(req.params("hike_id"));
            Hike deleteHike = hikeDao.getById(idOfHikeToDelete);
            hikeDao.deleteById(idOfHikeToDelete);
            return new ModelAndView(model, "success3.hbs");
        }, new HandlebarsTemplateEngine());

        // Delete all locations and hikes
        get("/locations/delete", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            hikeDao.clearAllHikes();
            locationDao.clearAllLocations();

            List<Location> allLocations = locationDao.getAll();
            model.put("locations", allLocations);

            return new ModelAndView(model, "success5.hbs");
        }, new HandlebarsTemplateEngine());

        //get: delete an individual location
        get("/locations/:location_id/delete", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfLocToDelete = Integer.parseInt(req.params("location_id"));
            Location deleteLoc = locationDao.findById(idOfLocToDelete);
            locationDao.deleteById(idOfLocToDelete);
            return new ModelAndView(model, "success6.hbs");
        }, new HandlebarsTemplateEngine());

    }
}
