package applicationVertx.verticles.jsonVerticles;
import applicationVertx.validation.validationClass;
import com.google.gson.reflect.TypeToken;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import org.apache.logging.log4j.Logger;
import applicationVertx.Entitys.toDoUserEntity.toDoUser;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static applicationVertx.validation.validationClass.*;

public class jsonWriter extends AbstractVerticle {

    public final static Logger logger = getLoggerFromValidationClass(jsonWriter.class);

    @Override
    public void start() {

    } //eventLoop


    public Future<Void> write(HashMap<String, toDoUser> users,Files files) {
        String file = Files.getFileName(files);
        Promise<Void> promise = Promise.promise();
        validationClass.vertx.executeBlocking(promiseHandler -> {
            Map<String, toDoUser> todoMap = new HashMap<>();
            try (FileReader reader = new FileReader(file)) {
                Type type = new TypeToken<HashMap<String, toDoUser>>() {
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
            try (FileWriter fileWriter = new FileWriter(file)) {
                gson.toJson(todoMap, fileWriter);
                promiseHandler.complete();
            } catch (IOException e) {
                logger.error(e);
                promiseHandler.fail(e);
            }
        }, promise);

        return promise.future();
    }



}