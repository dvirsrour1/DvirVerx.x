package applicationVertx.utils;

import applicationVertx.entitys.Task;
import applicationVertx.entitys.User;
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
    public static final Writer<User> userWriter = new Writer<User>();
    public static final Reader<User> userReader = new Reader<User>();
    public static final Delete<User> userDeleter = new Delete<User>();
    public static final Writer<Task> taskWriter = new Writer<Task>();
    public static final Reader<Task> taskReader = new Reader<Task>();
    public static final Delete<Task> taskDeleter = new Delete<Task>();
}
