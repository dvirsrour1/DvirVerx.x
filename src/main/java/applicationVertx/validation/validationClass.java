package applicationVertx.validation;

import applicationVertx.verticles.jsonVerticles.jsonDelete;
import applicationVertx.verticles.jsonVerticles.jsonReader;
import applicationVertx.verticles.jsonVerticles.jsonWriter;
import com.google.gson.Gson;
import io.vertx.core.Vertx;
import org.apache.logging.log4j.LogManager;

import java.util.logging.Logger;

public class validationClass {
    public static final Gson gson = new Gson();
    public static final Vertx vertx = Vertx.vertx();
    public static final Logger log = Logger.getLogger(validationClass.class.getName());
    public static final String fileNameOfUsers = "C:\\Users\\dvirs\\IdeaProjects\\DvirVerx.x\\src\\main\\resources\\JsonFiles\\usersData.json";
    public static final String fileNameOfTasks = "C:\\Users\\dvirs\\IdeaProjects\\DvirVerx.x\\src\\main\\resources\\JsonFiles\\tasksData.json";
    public static final jsonWriter jsonWriter = new jsonWriter();
    public static final jsonDelete jsonDelete = new jsonDelete();
    public static final jsonReader jsonReader = new jsonReader();
    public enum Files{
        USERS,
        TASKS;

        public static String getFileName(Files file) {
            if(file == USERS)
                return fileNameOfUsers;
            if(file == TASKS)
                return fileNameOfTasks;

            return null;
        }
    }

    public static org.apache.logging.log4j.Logger getLoggerFromValidationClass(Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }
    public static String intToString(int number) {
        return String.valueOf(number);
    }
}
