package applicationVertx.verticles.jsonVerticles;

import applicationVertx.validation.validationClass;
import com.google.gson.reflect.TypeToken;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import org.apache.logging.log4j.Logger;
import applicationVertx.Entitys.toDoUserEntity.toDoUser;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static applicationVertx.validation.validationClass.*;
import static java.lang.Character.getType;
import static org.apache.logging.log4j.LogManager.getLogger;

public class jsonReader extends AbstractVerticle {
    private final static Logger log = getLoggerFromValidationClass(jsonReader.class);
    @Override
    public void start() {
        // eventLoop logic here
    }

    public Future<Map<String, toDoUser>> readJson(Files files) {
        String file = Files.getFileName(files);
        Promise<Map<String, toDoUser>> promise = Promise.promise();
        validationClass.vertx.executeBlocking(promiseHandler -> {
            try (FileReader reader = new FileReader(file)) {
                Map<String, toDoUser> todoMap = new HashMap<>();
                Type type = new TypeToken<HashMap<String, toDoUser>>() {}.getType();
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
