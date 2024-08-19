package applicationVertx.verticles.jsonVerticles;

import applicationVertx.validation.validationClass;
import applicationVertx.Entitys.toDoUserEntity.toDoUser;
import com.google.gson.reflect.TypeToken;
import io.vertx.core.*;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static applicationVertx.validation.validationClass.fileNameOfUsers;
import static applicationVertx.validation.validationClass.gson;
import static applicationVertx.validation.validationClass.*;

public class jsonDelete extends AbstractVerticle {

    private final static Logger logger = getLoggerFromValidationClass(jsonDelete.class);
    @Override
    public void start() {

    }

    public Future<String> deleteUser(String id,Files files) {
        String FileAddress = files.getFileName(files);
        Promise<String> promise = Promise.promise();
        validationClass.vertx.executeBlocking(PromiseHandler ->{
            try (FileReader reader = new FileReader(FileAddress)) {
                Map<String, toDoUser> todoMap = new HashMap<>();
                Type type = new TypeToken<HashMap<String, toDoUser>>() {}.getType();
                todoMap = gson.fromJson(reader, type);
                if(todoMap.containsKey(id))
                {
                    todoMap.remove(id);
                    try(FileWriter file = new FileWriter(FileAddress)) {
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
