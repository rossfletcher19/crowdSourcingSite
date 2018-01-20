import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.Sql2oHikeDao;
import models.Hike;
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
        String connectionString = "jdbc:h2:~/todolist.db;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        Sql2oHikeDao hikeDao = new Sql2oHikeDao(sql2o);

        // GET ROUTES

        //get: show all tasks in all categories and show all categories
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






        // POST ROUTES

        //post: process new hike form
        post("/hikes/new", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            String nameOfHike = request.queryParams("nameOfHike");
            String locationOfHike = request.queryParams("locationOfHike");
            String notesOnHike = request.queryParams("notesOnHike");
            int ratingHike = Integer.parseInt(request.queryParams("ratingHike"));
            Hike newHike = new Hike(nameOfHike, locationOfHike, notesOnHike, ratingHike, 1); //ignore the hardcoded categoryId
            hikeDao.add(newHike);
            model.put("hike", newHike);
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());

    }
}
