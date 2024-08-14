package org.example.Verticles.JsonReaderAndWriter;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.Verticles.ToDoEntity.ToDo;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Character.getType;
public class JsonReader extends AbstractVerticle {
    private static final Gson gson = new Gson();
    private static final Vertx vertx = Vertx.vertx();
    private final static Logger logger = LogManager.getLogger(JsonReader.class);
    private final static String FileName = "C:\\Users\\aliza_rvjno4x\\IdeaProjects\\DvirVerx.x\\src\\main\\java\\org\\example\\JsonFiles\\data.json";

    @Override
    public void start() {
        // eventLoop logic here
    }

    public Future<Map<String, ToDo>> readUserFromFile() {
        Promise<Map<String,ToDo>> promise = Promise.promise();
        vertx.executeBlocking(promiseHandler -> {
            try (FileReader reader = new FileReader(FileName)) {
                Map<String, ToDo> todoMap = new HashMap<>();
                Type type = new TypeToken<HashMap<String, ToDo>>() {}.getType();
                todoMap = gson.fromJson(reader, type);
                if(todoMap.isEmpty()) {todoMap = null;};
                promiseHandler.complete(todoMap);
            } catch (IOException e) {
                logger.error(e);
                promiseHandler.fail(e);
            }
        }, promise);

        return promise.future();
    }
}
