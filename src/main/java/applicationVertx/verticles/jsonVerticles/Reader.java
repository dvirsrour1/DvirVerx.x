package applicationVertx.verticles.jsonVerticles;

import applicationVertx.entitys.Task;
import applicationVertx.utils.Consts;
import com.google.gson.reflect.TypeToken;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import org.apache.logging.log4j.Logger;
import applicationVertx.entitys.User;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static applicationVertx.utils.Consts.gson;
import static applicationVertx.utils.Validations.*;
import static java.lang.Character.getType;
import static org.apache.logging.log4j.LogManager.getLogger;

public class Reader<T>{
    private T item;
    private final static Logger log = getLoggerFromValidationClass(Reader.class);

    public Future<Map<String, T>> readJsonGeneric(Files files) {
        String file = Files.fileType(files);
        Promise<Map<String, T>> promise = Promise.promise();
        Consts.vertx.executeBlocking(promiseHandler -> {
            try (FileReader reader = new FileReader(file)) {
                Map<String, T> todoMap = new HashMap<>();
                Type type = new TypeToken<HashMap<String, User>>() {}.getType(); //Error
                todoMap = gson.fromJson(reader, type);
                if(todoMap.isEmpty()) {todoMap = null;};
                promiseHandler.complete(todoMap);
            } catch (IOException e) {
                log.error(e);
                promiseHandler.fail(e);
            }
        }, promise);

        return promise.future();
    }

    public Future<Map<String, Task>> readJsonTask(Files files) {
        String file = Files.fileType(files);
        Promise<Map<String, Task>> promise = Promise.promise();
        Consts.vertx.executeBlocking(promiseHandler -> {
            try (FileReader reader = new FileReader(file)) {
                Map<String, Task> todoMap = new HashMap<>();
                Type type = new TypeToken<HashMap<String, Task>>() {}.getType();
                todoMap = gson.fromJson(reader, type);
                if(todoMap.isEmpty()) {todoMap = null;};
                promiseHandler.complete(todoMap);
            } catch (IOException e) {
                log.error(e);
                promiseHandler.fail(e);
            }
        }, promise);

        return promise.future();
    }
}