package applicationVertx.verticles.http;

import applicationVertx.validation.validationClass;
import applicationVertx.verticles.todoEntity.ToDo;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.HashMap;
import java.util.Map;

import static applicationVertx.validation.validationClass.gson;
import static applicationVertx.validation.validationClass.intToString;

public class httpClass implements httpInterface{

    public Router createRouter(Vertx vertx) {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create()); // מטפל בגוף הבקשות. BODY
        router.get("/List").handler(this::getList);
        router.post("/NewUser").handler(this::postNewUser);
        router.delete("/DeleteUser/:userId").handler(this::postDeleteUser);
        router.post("/UpdateUserDescription").handler(this::postUpdateUserDescription);

        return router;
    }

    public void postNewUser(RoutingContext routingContext) {
        HashMap<String, ToDo> users= new HashMap<>();
        ToDo user1 = new ToDo();
        try{
            user1 = gson.fromJson(routingContext.getBodyAsString(), ToDo.class);
            if(user1==null)
            {
                routingContext.response().putHeader("content-type", "text/plain").end("ERROR: The body is empty.");
            }
        }catch (NullPointerException e){
            routingContext.response().putHeader("content-type", "text/plain").end("ERROR: The body is empty.");
            return;
        }catch (Exception e){
            routingContext.response().putHeader("content-type", "text/plain").end("ERROR: An error occurred.");
            return;
        }
        String key = intToString(user1.getId());
        users.put(key,user1);
        validationClass.jsonWriter.write(users).onComplete(rc -> {
            if (rc.succeeded()) {
                routingContext.response()
                        .putHeader("content-type", "text/plain").end("User added correctly");
            }
            if (rc.failed()) {
                routingContext.response().putHeader("content-type", "text/plain").end("User adding has failed");
            }
        });

    }
    public void getList(RoutingContext routingContext) {
        validationClass.jsonReader.readJson().onComplete(rc -> {
            if(rc.succeeded())
            {
                String results = new String();
                for (Map.Entry<String, ToDo> entry : rc.result().entrySet()) {
                    results = results +("name: " + entry.getValue().getName() +", id: " + entry.getValue().getId() +", Description: " + entry.getValue().getDescription() + System.getProperty("line.separator"));
                }
                routingContext.response().putHeader("content-type", "text/plain").end(results);
            }else {
                routingContext.response().putHeader("content-type", "text/plain").end("Reading has Failed.");
            }
        });
    }
    public void postUpdateUserDescription(RoutingContext routingContext) {
        HashMap<String,ToDo> hashMapFromJson = new HashMap<>();

        validationClass.jsonReader.readJson().onComplete(rc -> {
            if(rc.succeeded())
            {
                String userId = routingContext.getBodyAsJson().getString("userId");
                String description = routingContext.getBodyAsJson().getString("description");
                String results = new String();
                for (Map.Entry<String, ToDo> entry : rc.result().entrySet()) {
                    ToDo userHelp = new ToDo();
                    userHelp.ToDo(entry.getValue().getName(),entry.getValue().getId(),entry.getValue().getDescription());
                    hashMapFromJson.put(entry.getKey(),userHelp);
                }
                if(hashMapFromJson.get(userId)==null)
                {
                    routingContext.response().putHeader("content-type", "text/plain").end("User updating has failed.");
                    return;
                }
                ToDo TheUser = hashMapFromJson.get(userId);
                TheUser.setDescription(description);
                validationClass.jsonDelete.deleteUser(userId).onComplete(rc1 ->{
                    if(rc1.succeeded())
                    {
                        routingContext.response().putHeader("content-type", "text/plain").end("The list updated successfully");
                    }else{
                        routingContext.response().putHeader("content-type", "text/plain").end("ERROR: Deleting Failed");
                    }
                });
                //adding
                HashMap<String,ToDo> users= new HashMap<>();
                users.put(userId,TheUser);
                validationClass.jsonWriter.write(users).onComplete(Result -> {
                    if (Result.succeeded()) {
                        routingContext.response()
                                .putHeader("content-type", "text/plain").end("User updated correctly.");
                    }
                    if (Result.failed()) {
                        routingContext.response().putHeader("content-type", "text/plain").end("User updating has failed");
                    }
                }); //adding the new user to the Map
            }else {
                routingContext.response().putHeader("content-type", "text/plain").end("User updating has failed.");
            }
        });

    }
    public void postDeleteUser(RoutingContext routingContext) {
        String id = routingContext.request().getParam("userId");
        try {
            Integer.parseInt(id);
        }catch (Exception e)
        {
            routingContext.response().putHeader("content-type", "text/plain").end("Id is not correct");
        }
        validationClass.jsonDelete.deleteUser(id).onComplete(rc ->{
            if(rc.succeeded())
            {
                routingContext.response().putHeader("content-type", "text/plain").end("The list updated successfully");
            }else{
                routingContext.response().putHeader("content-type", "text/plain").end("ERROR: Deleting Failed");
            }
        });
    }
}
