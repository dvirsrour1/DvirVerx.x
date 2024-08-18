package applicationVerx.verticles.jsonVerticles;

import applicationVerx.validation.validationClass;
import applicationVerx.verticles.todoEntity.ToDo;
import com.google.gson.reflect.TypeToken;
import io.vertx.core.*;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static applicationVerx.validation.validationClass.FileName;
import static applicationVerx.validation.validationClass.gson;
import static applicationVerx.validation.validationClass.*;

public class jsonDelete extends AbstractVerticle {

    private final static Logger logger = getLoggerFromValidationClass(jsonDelete.class);
    @Override
    public void start() {

    }

    public Future<String> deleteUser(String id) {
        Promise<String> promise = Promise.promise();
        validationClass.vertx.executeBlocking(PromiseHandler ->{
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
