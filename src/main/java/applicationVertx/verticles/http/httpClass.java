package applicationVertx.verticles.http;

import applicationVertx.Entitys.tasksEntity.task;
import applicationVertx.validation.validationClass;
import applicationVertx.Entitys.toDoUserEntity.toDoUser;
import io.vertx.core.Vertx;
import io.vertx.core.net.impl.pool.Task;
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
        HashMap<String, toDoUser> users= new HashMap<>();
        toDoUser user1 = new toDoUser();
        try{
            user1 = gson.fromJson(routingContext.getBodyAsString(), toDoUser.class);
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
        validationClass.jsonWriter.write(users, validationClass.Files.USERS).onComplete(rc -> {
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
        validationClass.jsonReader.readJson(validationClass.Files.USERS).onComplete(rc -> {
            if(rc.succeeded())
            {
                String results = new String();
                for (Map.Entry<String, toDoUser> entry : rc.result().entrySet()) {
                    results = results +("name: " + entry.getValue().getName() +", id: " + entry.getValue().getId() +", Description: " + entry.getValue().getDescription() + System.getProperty("line.separator"));
                }
                routingContext.response().putHeader("content-type", "text/plain").end(results);
            }else {
                routingContext.response().putHeader("content-type", "text/plain").end("Reading has Failed.");
            }
        });
    }
    public void postUpdateUserDescription(RoutingContext routingContext) {
        HashMap<String, toDoUser> hashMapFromJson = new HashMap<>();

        validationClass.jsonReader.readJson(validationClass.Files.USERS).onComplete(rc -> {
            if(rc.succeeded())
            {
                String userId = routingContext.getBodyAsJson().getString("userId");
                String description = routingContext.getBodyAsJson().getString("description");
                String results = new String();
                for (Map.Entry<String, toDoUser> entry : rc.result().entrySet()) {
                    toDoUser userHelp = new toDoUser();
                    userHelp.ToDoUser(entry.getValue().getName(),entry.getValue().getId(),entry.getValue().getDescription());
                    hashMapFromJson.put(entry.getKey(),userHelp);
                }
                if(hashMapFromJson.get(userId)==null)
                {
                    routingContext.response().putHeader("content-type", "text/plain").end("User updating has failed.");
                    return;
                }
                toDoUser TheUser = hashMapFromJson.get(userId);
                TheUser.setDescription(description);
                validationClass.jsonDelete.deleteUser(userId, validationClass.Files.USERS).onComplete(rc1 ->{
                    if(rc1.succeeded())
                    {
                        routingContext.response().putHeader("content-type", "text/plain").end("The list updated successfully");
                    }else{
                        routingContext.response().putHeader("content-type", "text/plain").end("ERROR: Deleting Failed");
                    }
                });
                HashMap<String, toDoUser> users= new HashMap<>();
                users.put(userId,TheUser);
                validationClass.jsonWriter.write(users, validationClass.Files.USERS).onComplete(Result -> {
                    if (Result.succeeded()) {
                        routingContext.response()
                                .putHeader("content-type", "text/plain").end("User updated correctly.");
                    }
                    if (Result.failed()) {
                        routingContext.response().putHeader("content-type", "text/plain").end("User updating has failed");
                    }
                });
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
        validationClass.jsonDelete.deleteUser(id, validationClass.Files.USERS).onComplete(rc ->{
            if(rc.succeeded())
            {
                routingContext.response().putHeader("content-type", "text/plain").end("The list updated successfully");
            }else{
                routingContext.response().putHeader("content-type", "text/plain").end("ERROR: Deleting Failed");
            }
        });

        //delete the tasks he had
    }

    public void postAddTask(RoutingContext routingContext) {
        HashMap<String, toDoUser> hashMapFromJson = new HashMap<>();
        task task1 = new task();
        try{
            task1 = gson.fromJson(routingContext.getBodyAsString(), task.class);
        }catch (Exception e)
        {

        }
        validationClass.jsonReader.readJson(validationClass.Files.USERS).onComplete(rc -> {
            if (rc.succeeded()) {
                String userId = routingContext.getBodyAsJson().getString("userId");
                String description = routingContext.getBodyAsJson().getString("description");
                String results = new String();
                for (Map.Entry<String, toDoUser> entry : rc.result().entrySet()) {
                    toDoUser userHelp = new toDoUser();
                    userHelp.ToDoUser(entry.getValue().getName(), entry.getValue().getId(), entry.getValue().getDescription());
                    hashMapFromJson.put(entry.getKey(), userHelp);
                }
                if (hashMapFromJson.get(userId) == null) {
                    routingContext.response().putHeader("content-type", "text/plain").end("ERROR: There is no User with this ID in the system");
                    return;
                }
            }else {
                routingContext.response().putHeader("content-type", "text/plain").end("Reading has failed.");
            }
        });

        //לכתוב לתיקיה


    }
    public void postDeleteTask(RoutingContext routingContext) {
        //deleting task
    }


}
