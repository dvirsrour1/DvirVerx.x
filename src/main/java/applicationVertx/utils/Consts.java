package applicationVertx.utils;

import applicationVertx.verticles.http.RouterClass;
import applicationVertx.verticles.jsonVerticles.Delete;
import applicationVertx.verticles.jsonVerticles.Reader;
import applicationVertx.verticles.jsonVerticles.Writer;
import com.google.gson.Gson;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

import java.util.logging.Logger;

public class Consts {
    public static final Gson gson = new Gson();
    public static final Vertx vertx = Vertx.vertx();
    public static final Logger log = Logger.getLogger(Validations.class.getName());
    public static final String fileNameOfUsers = "C:\\Users\\dvirs\\IdeaProjects\\DvirVerx.x\\src\\main\\resources\\JsonFiles\\usersData.json";
    public static final String fileNameOfTasks = "C:\\Users\\dvirs\\IdeaProjects\\DvirVerx.x\\src\\main\\resources\\JsonFiles\\tasksData.json";
    public static final RouterClass routerClass = new RouterClass();
    public static final Router router = routerClass.createRouter(vertx);
    public static final String CONTENT_TYPE = "content-type";
    public static final String APPLICATION_JSON = "application/json";
    public static final String TEXT_PLAIN = Consts.TEXT_PLAIN;

    public static final Writer jsonWriter = new Writer();
    public static final Delete jsonDelete = new Delete();
    public static final Reader jsonReader = new Reader();

}
