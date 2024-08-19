package applicationVertx.projects;

import applicationVertx.verticles.http.httpServer;
import io.vertx.core.Vertx;

public class httpApplication {
    public static void main(String[] args) {
        //HTTP_Project
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new httpServer());
    }
}
