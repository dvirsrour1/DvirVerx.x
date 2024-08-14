package applicationVerx.verticles.http;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public interface httpInterface {
    void getList(RoutingContext routingContext);
    void postNewUser(RoutingContext routingContext);
    void postDeleteUser(RoutingContext routingContext);
    void postUpdateUserDescription(RoutingContext routingContext);
    Router createRouter(Vertx vertx);
}
