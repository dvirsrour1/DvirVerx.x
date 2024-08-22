package applicationVertx.verticles.jsonVerticles;
import applicationVertx.utils.Consts;
import com.google.gson.reflect.TypeToken;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import org.apache.logging.log4j.Logger;
import applicationVertx.entitys.User;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static applicationVertx.utils.Consts.gson;
import static applicationVertx.utils.Validations.*;

public class Writer<T>{

    private T item;
    public final static Logger logger = getLoggerFromValidationClass(Writer.class);


    public Future<Void> write(HashMap<String, T> users, Files files) {
        String file = Files.fileType(files);
        Promise<Void> promise = Promise.promise();
        Consts.vertx.executeBlocking(promiseHandler -> {
            Map<String, T> todoMap = new HashMap<>();
            try (FileReader reader = new FileReader(file)) {
                Type type = new TypeToken<HashMap<String, T>>() {
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