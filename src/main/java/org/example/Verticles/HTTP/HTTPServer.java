package org.example.Verticles.HTTP;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.example.Verticles.JsonReaderAndWriter.JsonDelete;
import org.example.Verticles.JsonReaderAndWriter.JsonReader;
import org.example.Verticles.JsonReaderAndWriter.JsonWriter;
import org.example.Verticles.ToDoEntity.ToDo;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


public class HTTPServer extends AbstractVerticle {

    private Logger log = Logger.getLogger(this.getClass().getName());

    @Override
    public void start(Promise<Void> future) {

        Router router = Router.router(vertx);
        router.get("/thelist").handler(this::GETTheList);
        router.get("/newUser/:userId/:userName/:userDescription").handler(this::POSTNewUser);  //change to post
        router.get("/deleteUser/:userId").handler(this::POSTDeleteUser); //change to post
        router.get("/updateUser/:userId/:userNewDescription").handler(this::POSTUpdateUser); //chamge to post

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
    public void POSTNewUser(RoutingContext routingContext) {
        JsonWriter jsonWriter = new JsonWriter();
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
    public void GETTheList(RoutingContext routingContext) {
        JsonReader jsonReader = new JsonReader();
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
    public void POSTUpdateUser(RoutingContext routingContext) {
        JsonDelete jsonDelete = new JsonDelete();
        JsonWriter jsonWriter = new JsonWriter();
        JsonReader jsonReader = new JsonReader();
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
                POSTDeleteUser(routingContext); //deleting the one that exists
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
    public void POSTDeleteUser(RoutingContext routingContext) {
        JsonDelete jsonDelete = new JsonDelete();
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




    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new HTTPServer());
    }


}
