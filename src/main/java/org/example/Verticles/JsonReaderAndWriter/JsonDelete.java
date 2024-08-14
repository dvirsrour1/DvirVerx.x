package org.example.Verticles.JsonReaderAndWriter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.vertx.core.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.Verticles.ToDoEntity.ToDo;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class JsonDelete extends AbstractVerticle {
    private static final Gson gson = new Gson();
    private static final Vertx vertx = Vertx.vertx();
    private final static Logger logger = LogManager.getLogger(JsonDelete.class);
    private final static String FileName = "C:\\Users\\aliza_rvjno4x\\IdeaProjects\\DvirVerx.x\\src\\main\\java\\org\\example\\JsonFiles\\data.json";


    @Override
    public void start() {

    }

    public Future<String> DeleteFromJson(String id) {
        Promise<String> promise = Promise.promise();
        vertx.executeBlocking(PromiseHandler ->{
            try (FileReader reader = new FileReader(FileName)) {
                Map<String, ToDo> todoMap = new HashMap<>();
                Type type = new TypeToken<HashMap<String, ToDo>>() {}.getType();
                todoMap = gson.fromJson(reader, type);
                if(todoMap.containsKey(id))
                {
                    todoMap.remove(id);
                    try(FileWriter file = new FileWriter(FileName)) {
                        gson.toJson(todoMap,file);
                    }catch (Exception e)
                    {
                        logger.error(e);
                        PromiseHandler.fail(e);
                    }

                }
                else {
                    PromiseHandler.fail("id " + id + " not found");
                }
                if(todoMap.isEmpty()) {todoMap = null;};
                PromiseHandler.complete("Item deleted successfully.");
            } catch (IOException e) {
                logger.error(e);
                PromiseHandler.fail(e);
            }
        },promise);
        return promise.future();
    }

}
