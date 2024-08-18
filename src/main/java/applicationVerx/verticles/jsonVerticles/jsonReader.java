package applicationVerx.verticles.jsonVerticles;

import applicationVerx.validation.validationClass;
import com.google.gson.reflect.TypeToken;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import org.apache.logging.log4j.Logger;
import applicationVerx.verticles.todoEntity.ToDo;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static applicationVerx.validation.validationClass.*;
import static java.lang.Character.getType;
import static org.apache.logging.log4j.LogManager.getLogger;

public class jsonReader extends AbstractVerticle {
    private final static Logger log = getLoggerFromValidationClass(jsonReader.class);
    @Override
    public void start() {
        // eventLoop logic here
    }

    public Future<Map<String, ToDo>> readJson() {
        Promise<Map<String,ToDo>> promise = Promise.promise();
        validationClass.vertx.executeBlocking(promiseHandler -> {
            try (FileReader reader = new FileReader(FileName)) {
                Map<String, ToDo> todoMap = new HashMap<>();
                Type type = new TypeToken<HashMap<String, ToDo>>() {}.getType();
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
