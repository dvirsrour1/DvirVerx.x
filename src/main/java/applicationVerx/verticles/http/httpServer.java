package applicationVerx.verticles.http;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;


public class httpServer extends AbstractVerticle{

    private final httpClass httpClassInstance = new httpClass();

    @Override
    public void start(Promise<Void> future) {

        Router router = httpClassInstance.createRouter(vertx);
        vertx.createHttpServer() // הגדרת שרת
                .requestHandler(router // הגדרת בקשה המטפלת בכל פנייה לשרת
                ).listen(config().getInteger("http.port", 9090), //הגדרת PORT
                        result -> {
                            if (result.succeeded()) {
                                future.complete();
                            } else {
                                future.fail(result.cause());
                            }
                        }); //בדיקה האם ההאזנה לPORT הצליחה



    }






}
