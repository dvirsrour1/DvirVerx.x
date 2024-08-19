package applicationVertx.verticles.jsonVerticles;
import applicationVertx.validation.validationClass;
import com.google.gson.reflect.TypeToken;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import org.apache.logging.log4j.Logger;
import applicationVertx.verticles.todoEntity.ToDo;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static applicationVertx.validation.validationClass.*;

public class jsonWriter extends AbstractVerticle {

    public final static Logger logger = getLoggerFromValidationClass(jsonWriter.class);

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


    public Future<Void> write(HashMap<String, ToDo> users) {
        Promise<Void> promise = Promise.promise();
        validationClass.vertx.executeBlocking(promiseHandler -> {
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