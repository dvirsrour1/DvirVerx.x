package applicationVertx.verticles.http;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.apache.logging.log4j.Logger;

import static applicationVertx.utils.Consts.router;
import static applicationVertx.utils.Validations.getLoggerFromValidationClass;


public class Server extends AbstractVerticle{

    private static final Logger log = getLoggerFromValidationClass(Server.class);
    private final HttpHandlers httpClassInstance = new HttpHandlers();

    @Override
    public void start(Promise<Void> future) {

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
