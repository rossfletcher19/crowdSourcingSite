import java.util.ArrayList;
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

/**
 * Created by Guest on 1/18/18.
 */
public class App {

    public static void main(String[] args) {
        staticFileLocation("/public");
        String connectionString = "jdbc:h2:~/hikingLog.db;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        Sql2oHikeDao hikeDao = new Sql2oHikeDao(sql2o);
        Sql2oLocationDao locationDao = new Sql2oLocationDao(sql2o);

//        // Get Routes for location
//        // OK

        //get: show all tasks in all categories and show all categories ck
        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            List<Location> allLocations = locationDao.getAll();
            model.put("locations", allLocations);

            List<Hike> hikes = hikeDao.getAll();
            model.put("hikes", hikes);
            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());

//        // show new location form to create a new location OK
        get("/locations/new", (request, response) -> {
            Map<String, Object> model = new HashMap<>();

            List<Location> locations = locationDao.getAll();
            model.put("locations", locations);

            return new ModelAndView(model, "location-form.hbs");
        }, new HandlebarsTemplateEngine());
//
//
//        //get a specific location (and the hikes it contains) OK
        get("/locations/:id", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfLocationToFind = Integer.parseInt(req.params("id"));

            List<Location> locations = locationDao.getAll();
            model.put("locations", locations);

            Location foundLocation = locationDao.findById(idOfLocationToFind);
            model.put("category", foundLocation);
            List<Hike> allHikesByCategory = locationDao.getAllHikesByLocation(idOfLocationToFind);
            model.put("hikes", allHikesByCategory);

            return new ModelAndView(model, "location-detail.hbs");
        }, new HandlebarsTemplateEngine());
//
//
//        //get: show a form to update a location ck
        get("/locations/update", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            model.put("editLocation", true);

            List<Location> allLocations = locationDao.getAll();
            model.put("locations", allLocations);

            return new ModelAndView(model, "location-form.hbs");
        }, new HandlebarsTemplateEngine());

        // Post Routes for location
       //post: process a form to add a location ck
        post("/locations", (request, response) -> { //new
            Map<String, Object> model = new HashMap<>();
            String name = request.queryParams("name");
            Location newLocation = new Location(name);
            locationDao.add(newLocation);

            List<Location> locations = locationDao.getAll(); //refresh list of links for navbar.
            model.put("locations", locations);

            return new ModelAndView(model, "success4.hbs");
        }, new HandlebarsTemplateEngine());

        //post: process a form to update a location and hikes it contains
        post("/locations/update", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfLocationToEdit = Integer.parseInt(req.queryParams("editLocationId"));
            String newName = req.queryParams("newLocationName");
            locationDao.update(locationDao.findById(idOfLocationToEdit).getId(), newName);

            List<Location> locations = locationDao.getAll(); //refresh list of links for navbar.
            model.put("locations", locations);

            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());


        // GET ROUTES for hikes


        //get: show new hike form ck
        get("/hikes/new", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            List<Location> locations = locationDao.getAll();
            model.put("locations", locations);

            return new ModelAndView(model, "hike-form.hbs");
        }, new HandlebarsTemplateEngine());
//
        //get: delete all hikes ck
        get("/hikes/delete", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            hikeDao.clearAllHikes();
            return new ModelAndView(model, "success2.hbs");
        }, new HandlebarsTemplateEngine());
//
//        //get: delete an individual hike ck
        get("/locations/:location_id/hikes/:hike_id/delete", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfHikeToDelete = Integer.parseInt(req.params("hike_id"));
            Hike deleteHike = hikeDao.getById(idOfHikeToDelete);
            hikeDao.deleteById(idOfHikeToDelete);
            return new ModelAndView(model, "success3.hbs");
        }, new HandlebarsTemplateEngine());
//
//        //get: show an individual hike that is nested in a location ck
//        // "/locations/{{locationid}}/hikes/{{id}}"
        get("/locations/:location_id/hikes/:hike_id", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            List<Location> locations = locationDao.getAll();
            model.put("locations", locations);

            int idOfHikeToFind = Integer.parseInt(req.params("hike_id"));
            Hike foundHike = hikeDao.getById(idOfHikeToFind);
            model.put("hike", foundHike);
            return new ModelAndView(model, "hike-detail.hbs");
        }, new HandlebarsTemplateEngine());
//
//        //get: show a form to update a hike within a location ck
        get("/hikes/update", (request, response) -> {
            Map<String, Object> model = new HashMap<>();

            List<Location> allLocations = locationDao.getAll();
            model.put("locations", allLocations);
//
            List<Hike> allHikes = hikeDao.getAll();
            model.put("hikes", allHikes);

//            int idOfTaskToEdit = Integer.parseInt(request.queryParams("id"));
//            Hike editHike = hikeDao.getById(idOfTaskToEdit);

            model.put("editHike", true);
            return new ModelAndView(model, "hike-form.hbs");
        }, new HandlebarsTemplateEngine());
//
//
//        // POST ROUTES
//
//        //post: process new hike form ck
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
//
//        //post: process a form to update a hike ck
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

    }
}
