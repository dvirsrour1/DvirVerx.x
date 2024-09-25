package applicationVertx.verticles.jsonVerticles;

import applicationVertx.utils.Consts;
import applicationVertx.entitys.User;
import com.google.gson.reflect.TypeToken;
import io.vertx.core.*;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static applicationVertx.utils.Consts.gson;
import static applicationVertx.utils.Validations.*;

public class Delete<T> {

    private T item;
    private final static Logger logger = getLoggerFromValidationClass(Delete.class);


    public Future<String> deleteUser(String id,Files files) {
        String FileAddress = files.fileType(files);
        Promise<String> promise = Promise.promise();
        Consts.vertx.executeBlocking(PromiseHandler ->{
            try (FileReader reader = new FileReader(FileAddress)) {
                Map<String, T> todoMap = new HashMap<>();
                Type type = new TypeToken<HashMap<String, T>>() {}.getType();
                todoMap = gson.fromJson(reader, type);
                if(!todoMap.containsKey(id))
                {
                    PromiseHandler.fail("id " + id + " not found");
                }
                else
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
