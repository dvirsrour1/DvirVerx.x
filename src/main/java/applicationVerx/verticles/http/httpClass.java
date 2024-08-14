package applicationVerx.verticles.http;

import applicationVerx.verticles.jsonVerticles.jsonDelete;
import applicationVerx.verticles.jsonVerticles.jsonReader;
import applicationVerx.verticles.jsonVerticles.jsonWriter;
import applicationVerx.verticles.todoEntity.ToDo;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.util.HashMap;
import java.util.Map;

public class httpClass implements httpInterface{

    public Router createRouter(Vertx vertx) {
        Router router = Router.router(vertx);

        router.get("/List").handler(this::getList);
        router.get("/NewUser/:userId/:userName/:userDescription").handler(this::postNewUser);
        router.get("/DeleteUser/:userId").handler(this::postDeleteUser);
        router.get("/UpdateUserDescription/:userId/:userNewDescription").handler(this::postUpdateUserDescription);

        return router;
    }

    public void postNewUser(RoutingContext routingContext) {
        jsonWriter jsonWriter = new jsonWriter();
        HashMap<String, ToDo> users= new HashMap<>();
        String userId = routingContext.request().getParam("userId");
        String userName = routingContext.request().getParam("userName");
        String description = routingContext.request().getParam("userDescription");
        try{
            Integer.parseInt(userId);

        }catch (Exception e){
            routingContext.response().putHeader("content-type", "text/plain").end("Invalid user ID");
        }
        if(userName.matches("[0-9]*"))
        {
            routingContext.response().putHeader("content-type", "text/plain").end("Invalid user ID");
        }
        ToDo user1 = new ToDo();
        user1.ToDo(userName,Integer.parseInt(userId),description);
        users.put(userId,user1);
        jsonWriter.writeUserToFile(users).onComplete(rc -> {
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
        jsonReader jsonReader = new jsonReader();
        jsonReader.readUserFromFile().onComplete(rc -> {
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
        jsonDelete jsonDelete = new jsonDelete();
        jsonWriter jsonWriter = new jsonWriter();
        jsonReader jsonReader = new jsonReader();
        HashMap<String,ToDo> hashMapFromJson = new HashMap<>();

        jsonReader.readUserFromFile().onComplete(rc -> {
            if(rc.succeeded())
            {
                String results = new String();
                for (Map.Entry<String, ToDo> entry : rc.result().entrySet()) {
                    ToDo userHelp = new ToDo();
                    userHelp.ToDo(entry.getValue().getName(),entry.getValue().getId(),entry.getValue().getDescription());
                    hashMapFromJson.put(entry.getKey(),userHelp);
                } //reading it all to a ToDoHashMap
                if(hashMapFromJson.get(routingContext.request().getParam("userId"))==null)
                {
                    routingContext.response().putHeader("content-type", "text/plain").end("User updating has failed.");
                    return;
                }
                ToDo TheUser = hashMapFromJson.get(routingContext.request().getParam("userId"));
                TheUser.setDescription(routingContext.request().getParam("userNewDescription")); //Making a new ToDo with fixed description
                postDeleteUser(routingContext); //deleting the one that exists
                //adding
                HashMap<String,ToDo> users= new HashMap<>();
                users.put(routingContext.request().getParam("userId"),TheUser);
                jsonWriter.writeUserToFile(users).onComplete(Result -> {
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
        jsonDelete jsonDelete = new jsonDelete();
        String id = routingContext.request().getParam("userId");
        try {
            Integer.parseInt(id);
        }catch (Exception e)
        {
            routingContext.response().putHeader("content-type", "text/plain").end("Id is not correct");
        }
        jsonDelete.DeleteFromJson(id).onComplete(rc ->{
            if(rc.succeeded())
            {
                routingContext.response().putHeader("content-type", "text/plain").end("Delete Succesfully");
            }else{
                routingContext.response().putHeader("content-type", "text/plain").end("ERROR: Deleting Failed");
            }
        });
    }
}
