package applicationVertx;

import applicationVertx.verticles.http.Server;
import io.vertx.core.Vertx;

public class Core {
    public static void main(String[] args) {
        //HTTP_Project
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new Server());
    }
}
