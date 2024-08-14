package org.example.Verticles.HelloVerticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

public class HelloVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> future) {
        System.out.println("Hello World!");
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new HelloVerticle());
    }
}
