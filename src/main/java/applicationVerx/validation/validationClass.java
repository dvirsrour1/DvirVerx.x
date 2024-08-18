package applicationVerx.validation;

import applicationVerx.verticles.jsonVerticles.jsonDelete;
import applicationVerx.verticles.jsonVerticles.jsonReader;
import applicationVerx.verticles.jsonVerticles.jsonWriter;
import com.google.gson.Gson;
import io.vertx.core.Vertx;
import org.apache.logging.log4j.LogManager;

import java.util.logging.Logger;

public class validationClass {
    public static final Gson gson = new Gson();
    public static final Vertx vertx = Vertx.vertx();
    public static final Logger log = Logger.getLogger(validationClass.class.getName());
    public static final String FileName = "C:\\Users\\dvirs\\IdeaProjects\\DvirVerx.x\\src\\main\\resources\\JsonFiles\\data.json";
    public static final jsonWriter jsonWriter = new jsonWriter();
    public static final jsonDelete jsonDelete = new jsonDelete();
    public static final jsonReader jsonReader = new jsonReader();

    public static org.apache.logging.log4j.Logger getLoggerFromValidationClass(Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }
}
