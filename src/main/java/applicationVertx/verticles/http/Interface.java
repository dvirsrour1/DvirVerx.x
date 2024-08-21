package applicationVertx.verticles.http;

import io.vertx.ext.web.RoutingContext;

public interface Interface {
    void getAll(RoutingContext routingContext);
    void newUser(RoutingContext routingContext);
    void deleteUser(RoutingContext routingContext);
    void updateUserDescription(RoutingContext routingContext);
    //adding new functions
}
