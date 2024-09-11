package applicationVertx.verticles.http;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

public class RouterClass extends HttpHandlers {

    public Router createRouter(Vertx vertx) {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create()); // מטפל בגוף הבקשות. BODY
        router.get("/List").handler(this::getAll);
        router.post("/NewUser").handler(this::newUser);
        router.delete("/DeleteUser/:userId").handler(this::deleteUser);
        router.post("/UpdateUserDescription").handler(this::updateUserDescription);
        router.post("/AddTask").handler(this::addTask);
        router.delete("/DeleteTask").handler(this::deleteTask);
        router.get("/Tasks").handler(this::showTask);


        router.route().handler(CorsHandler.create("*")  // מאפשר לכל מקור לגשת
                .allowedMethod(io.vertx.core.http.HttpMethod.GET)
                .allowedMethod(io.vertx.core.http.HttpMethod.POST)
                .allowedMethod(io.vertx.core.http.HttpMethod.OPTIONS)
                .allowedMethod(io.vertx.core.http.HttpMethod.DELETE) // אם נדרש
                .allowedHeader("Content-Type") // כותרת שמתאימה לנתונים שנשלחים
                .allowedHeader("Authorization") // אם אתה שולח אישורים
                .allowCredentials(true)); // אם יש דרישה ל-Credentials כמו עוגיות
 //allow everyone
        return router;
    }
}
