package applicationVertx.verticles.http;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import org.apache.logging.log4j.Logger;

import static applicationVertx.validation.validationClass.getLoggerFromValidationClass;


public class httpServer extends AbstractVerticle{

    private static final Logger log = getLoggerFromValidationClass(httpServer.class);
    private final httpClass httpClassInstance = new httpClass();

    @Override
    public void start(Promise<Void> future) {

        Router router = httpClassInstance.createRouter(vertx);
        vertx.createHttpServer() // הגדרת שרת
                .requestHandler(router
                ).listen(config().getInteger("http.port", 9090), //הגדרת PORT
                        result -> {
                            if (result.succeeded()) {
                                log.info("HTTP server started on port 9090");
                                future.complete();
                            } else {
                                log.error("HTTP server failed to start", result.cause());
                                future.fail(result.cause());
                            }
                        }); //בדיקה האם ההאזנה לPORT הצליחה



    }






}
