package applicationVerx.verticles.jsonVerticles;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import applicationVerx.verticles.todoEntity.ToDo;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class jsonWriter extends AbstractVerticle {

    private final static Gson gson = new Gson();
    private final static Vertx vertx = Vertx.vertx();
    public final static Logger logger = LogManager.getLogger(jsonWriter.class);
    private final static String FileName = "C:\\Users\\aliza_rvjno4x\\IdeaProjects\\DvirVerx.x\\src\\main\\resources\\JsonFiles\\data.json";

    public enum FUNCTIONS {
        WRONG_SYMBOL(0),
        WRITE_TO_FILE(1),
        READ_FROM_FILE(2),
        DELETE_FROM_FILE(3),
        STOP_FUNCTIONS(-1);

        private int FUNC;

        FUNCTIONS(int func) {
            this.FUNC = func;
        }

        public int getFunc() {
            return FUNC;
        }

        public static FUNCTIONS fromInt(int func) {
            for (FUNCTIONS f : FUNCTIONS.values()) {
                if (f.getFunc() == func) {
                    return f;
                }
            }
            return FUNCTIONS.WRONG_SYMBOL;
        }

    }

    public final static String FirstBlock = """
            ---------------------------------------------------------------------------------------
                       
            Programmer: Dvir Srour
            Date: 1.8.2024
            KerenOrFirstProject_VertX
                       
            
            --------------------------------------------------------------------------------------- 
             """;

    @Override
    public void start() {

    } //eventLoop


    public Future<Void> writeUserToFile(HashMap<String, ToDo> users) {
        Promise<Void> promise = Promise.promise();
        vertx.executeBlocking(promiseHandler -> {
            Map<String, ToDo> todoMap = new HashMap<>();
            try (FileReader reader = new FileReader(FileName)) {
                Type type = new TypeToken<HashMap<String, ToDo>>() {
                }.getType();
                todoMap = gson.fromJson(reader, type);
                if (todoMap == null) {
                    todoMap = new HashMap<>();
                }
            } catch (IOException e) {
                logger.error(e);
                promiseHandler.fail(e);
                return;
            }

            todoMap.putAll(users);
            try (FileWriter file = new FileWriter(FileName)) {
                gson.toJson(todoMap, file);
                promiseHandler.complete();
            } catch (IOException e) {
                logger.error(e);
                promiseHandler.fail(e);
            }
        }, promise);

        return promise.future();
    }



}