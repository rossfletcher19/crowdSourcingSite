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

        // Get Routes for location

        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            List<Location> allLocations = locationDao.getAll();
            model.put("locations", allLocations);

            List<Hike> hikes = hikeDao.getAll();
            model.put("hikes", hikes);
            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());

        // show new location form to create a new location
        get("/locations/new", (request, response) -> {
            Map<String, Object> model = new HashMap<>();

            List<Location> locations = locationDao.getAll();
            model.put("locations", locations);

            return new ModelAndView(model, "location-form.hbs");
        }, new HandlebarsTemplateEngine());


        //get a specific location (and the hikes it contains)
        get("/locations/:id", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfLocationToFind = Integer.parseInt(request.params("id"));

            List<Location> locations = locationDao.getAll();
            model.put("locations", locations);

            Location foundLocation = locationDao.findById(idOfLocationToFind);
            model.put("category", foundLocation);
            List<Hike> allHikesByCategory = locationDao.getAllHikesByLocation(idOfLocationToFind);
            model.put("hikes", allHikesByCategory);

            return new ModelAndView(model, "location-detail.hbs");
        }, new HandlebarsTemplateEngine());





        // Post Routes for location

        post("/locations", (request, response) -> { //new
            Map<String, Object> model = new HashMap<>();
            String name = request.queryParams("name");
            Location newLocation = new Location(name, 1);
            locationDao.add(newLocation);

            List<Location> locations = locationDao.getAll(); //refresh list of links for navbar.
            model.put("locations", locations);

            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());


        // GET ROUTES for hikes

        //get: show all hikes in all locations and show all locations
        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<Hike> hikes = hikeDao.getAll();
            model.put("hikes", hikes);
            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());

        //get: show new hike form
        get("/hikes/new", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "hike-form.hbs");
        }, new HandlebarsTemplateEngine());

        //get: delete all hikes
        get("/hikes/delete", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            hikeDao.clearAllHikes();
            return new ModelAndView(model, "success2.hbs");
        }, new HandlebarsTemplateEngine());

        //get: delete an individual hike
        get("/locations/:id/hikes/:id/delete", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfHikeToDelete = Integer.parseInt(req.params("id"));
            Hike deleteHike = hikeDao.getById(idOfHikeToDelete);
            hikeDao.deleteById(idOfHikeToDelete);
            return new ModelAndView(model, "success3.hbs");
        }, new HandlebarsTemplateEngine());

        //get: show an individual hike that is nested in a location
        // "/locations/{{locationid}}/hikes/{{id}}"
        get("/locations/:id/hikes/:id", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfHikeToFind = Integer.parseInt(req.params("id"));
            Hike foundHike = hikeDao.getById(idOfHikeToFind);
            model.put("hike", foundHike);
            return new ModelAndView(model, "hike-detail.hbs");
        }, new HandlebarsTemplateEngine());

        //get: show a form to update a hike
        get("/hikes/:id/update", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfHikeToEdit = Integer.parseInt(req.params("id"));
            Hike editHike = hikeDao.getById(idOfHikeToEdit);
            model.put("editHike", editHike);
            return new ModelAndView(model, "hike-form.hbs");
        }, new HandlebarsTemplateEngine());


        // POST ROUTES

        //post: process new hike form
        post("/hikes/new", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            String nameOfHike = request.queryParams("nameOfHike");
            String locationOfHike = request.queryParams("locationOfHike");
            String notesOnHike = request.queryParams("notesOnHike");
            int ratingHike = Integer.parseInt(request.queryParams("ratingHike"));
            Hike newHike = new Hike(nameOfHike, locationOfHike, notesOnHike, ratingHike, 1);
            hikeDao.add(newHike);
            model.put("newHike", newHike);
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());

        //post: process a form to update a hike
        post("/hikes/:id/update", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            String newNotes = req.queryParams("notesOnHike");
            int idOfHikeToEdit = Integer.parseInt(req.queryParams("id"));
            Hike editHike = hikeDao.getById(idOfHikeToEdit);
            hikeDao.update(idOfHikeToEdit, newNotes, 1); //ignore the hardcoded categoryId for now.
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());

    }
}
