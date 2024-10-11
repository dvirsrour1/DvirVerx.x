package applicationVertx.verticles.http;

import applicationVertx.entitys.Task;
import applicationVertx.utils.Consts;
import applicationVertx.utils.Validations;
import applicationVertx.entitys.User;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.HashMap;
import java.util.Map;

import static applicationVertx.utils.Consts.gson;
import static applicationVertx.utils.Validations.intToString;

public class HttpHandlers implements Interface {


    public void newUser(RoutingContext routingContext) {
        HashMap<String, User> users= new HashMap<>();
        User newUser = new User();
        try{
            newUser = gson.fromJson(routingContext.getBodyAsString(), User.class);
            if(newUser==null)
            {
                routingContext.response().putHeader(Consts.CONTENT_TYPE, Consts.TEXT_PLAIN).end("ERROR: The body is empty.");
            }
        }catch (NullPointerException e){
            routingContext.response().putHeader(Consts.CONTENT_TYPE, Consts.TEXT_PLAIN).end("ERROR: The body is empty.");
            return;
        }catch (Exception e){
            routingContext.response().putHeader(Consts.CONTENT_TYPE, Consts.TEXT_PLAIN).end("ERROR: An error occurred.");
            return;
        }
        String key = intToString(newUser.getId());
        users.put(key,newUser);
        Consts.userWriter.write(users, Validations.Files.USERS).onComplete(rc -> {
            if (rc.succeeded()) {
                routingContext.response()
                        .putHeader(Consts.CONTENT_TYPE,Consts.TEXT_PLAIN).end("User added correctly");
            }
            if (rc.failed()) {
                routingContext.response().putHeader(Consts.CONTENT_TYPE, Consts.TEXT_PLAIN).end("User adding has failed");
            }
        });

    }
    public void getAll(RoutingContext routingContext) {
        Consts.userReader.readJsonGeneric(Validations.Files.USERS).onComplete(rc -> {
            if(rc.succeeded())
            {
                if(rc.result()==null)
                {
                    routingContext.response().putHeader(Consts.CONTENT_TYPE, Consts.TEXT_PLAIN).end("No user found.");
                }
                JsonArray jsonArray = new JsonArray();
                for (Map.Entry<String, User> entry : rc.result().entrySet()) {
                    JsonObject json = new JsonObject();
                    json.addProperty("id", entry.getKey());
                    json.addProperty("name", entry.getValue().getName());
                    json.addProperty("description", entry.getValue().getDescription());
                    jsonArray.add(json);
                }
                routingContext.response().putHeader(Consts.CONTENT_TYPE, Consts.APPLICATION_JSON).end(jsonArray.toString());
            }else {
                routingContext.response().putHeader(Consts.CONTENT_TYPE, Consts.TEXT_PLAIN).end("Reading has Failed.");
            }
        });
    }
    public void updateUserDescription(RoutingContext routingContext) {
        HashMap<String, User> hashMapFromJson = new HashMap<>();

        Consts.userReader.readJsonGeneric(Validations.Files.USERS).onComplete(rc -> {
            if(rc.succeeded())
            {
                String userId = routingContext.getBodyAsJson().getString("userId");
                String description = routingContext.getBodyAsJson().getString("description");
                String results = new String();
                for (Map.Entry<String, User> entry : rc.result().entrySet()) {
                    User userHelp = new User();
                    userHelp.User(entry.getValue().getName(),entry.getValue().getId(),entry.getValue().getDescription());
                    hashMapFromJson.put(entry.getKey(),userHelp);
                }
                if(hashMapFromJson.get(userId)==null)
                {
                    routingContext.response().putHeader(Consts.CONTENT_TYPE, Consts.TEXT_PLAIN).end("User updating has failed.");
                    return;
                }
                User TheUser = hashMapFromJson.get(userId);
                TheUser.setDescription(description);
                Consts.userDeleter.deleteUser(userId, Validations.Files.USERS).onComplete(rc1 ->{
                    if(rc1.succeeded())
                    {
                        routingContext.response().putHeader(Consts.CONTENT_TYPE, Consts.TEXT_PLAIN).end("The list updated successfully");
                    }else{
                        routingContext.response().putHeader(Consts.CONTENT_TYPE, Consts.TEXT_PLAIN).end("ERROR: Deleting Failed");
                    }
                });
                HashMap<String, User> users= new HashMap<>();
                users.put(userId,TheUser);
                Consts.userWriter.write(users, Validations.Files.USERS).onComplete(Result -> {
                    if (Result.succeeded()) {
                        routingContext.response()
                                .putHeader(Consts.CONTENT_TYPE, Consts.TEXT_PLAIN).end("User updated correctly.");
                    }
                    if (Result.failed()) {
                        routingContext.response().putHeader(Consts.CONTENT_TYPE, Consts.TEXT_PLAIN).end("User updating has failed");
                    }
                });
            }else {
                routingContext.response().putHeader(Consts.CONTENT_TYPE, Consts.TEXT_PLAIN).end("User updating has failed.");
            }
        });

    }
    public void deleteUser(RoutingContext routingContext) {
        String id = routingContext.request().getParam("userId");
        try {
            Integer.parseInt(id);
        }catch (Exception e)
        {
            routingContext.response().putHeader(Consts.CONTENT_TYPE, Consts.TEXT_PLAIN).end("Id is not correct");
        }
        Consts.userDeleter.deleteUser(id, Validations.Files.USERS).onComplete(rc ->{

            if(rc.failed())
            {
                routingContext.response().putHeader(Consts.CONTENT_TYPE, Consts.TEXT_PLAIN).end("ERROR: Deleting Failed");

            }

        });

        //delete the tasks he had



        Consts.userReader.readJsonTask(Validations.Files.TASKS).onComplete(rc -> {
            if (rc.succeeded()) {
                for (Map.Entry<String, Task> entry : rc.result().entrySet()) {
                    if(entry.getValue().getIdOfUser() == Integer.parseInt(id)){
                        Consts.taskDeleter.deleteUser(entry.getKey(), Validations.Files.TASKS).onComplete(rc1 ->{
                        });
                    }
                }
                routingContext.response().putHeader(Consts.CONTENT_TYPE, Consts.TEXT_PLAIN).end("The list updated successfully");

            }
            if (rc.failed()) {
                routingContext.response().putHeader(Consts.CONTENT_TYPE, Consts.TEXT_PLAIN).end("ERROR: Deleting tasks Failed");

            }
        });
    }

    public void addTask(RoutingContext routingContext) {
        HashMap<String, User> hashMapFromJson = new HashMap<>();
        Task task1 = new Task();
        try{
            task1 = gson.fromJson(routingContext.getBodyAsString(), Task.class);
        }catch (Exception e)
        {

        }
        Consts.userReader.readJsonGeneric(Validations.Files.USERS).onComplete(rc -> {
            if (rc.succeeded()) {
                int userId = routingContext.getBodyAsJson().getInteger("idOfUser");
                String description = routingContext.getBodyAsJson().getString("taskDescription");
              //  String results = new String();
                for (Map.Entry<String, User> entry : rc.result().entrySet()) {
                    User userHelp = new User();
                    userHelp.User(entry.getValue().getName(), entry.getValue().getId(), entry.getValue().getDescription());
                    hashMapFromJson.put(entry.getKey(), userHelp);
                }

                if (hashMapFromJson.get(userId)==null) {
                    routingContext.response().putHeader(Consts.CONTENT_TYPE, Consts.TEXT_PLAIN).end("ERROR: There is no User with this ID in the system");
                    return;
                }
            }else {
                routingContext.response().putHeader(Consts.CONTENT_TYPE, Consts.TEXT_PLAIN).end("Reading has failed.");
            }
        });
        HashMap<String, Task> tasks= new HashMap<>();
        tasks.put(task1.getTaskName(),task1);
        Consts.taskWriter.write(tasks,Validations.Files.TASKS).onComplete(rc -> {
            if (rc.succeeded()) {
                routingContext.response().putHeader(Consts.CONTENT_TYPE, Consts.TEXT_PLAIN).end("Task added successfully");
            }
            if (rc.failed()) {
                routingContext.response().putHeader(Consts.CONTENT_TYPE, Consts.TEXT_PLAIN).end("Error adding task");
            }
        });


    }
    public void deleteTask(RoutingContext routingContext) {
        Consts.taskDeleter.deleteUser(routingContext.getBodyAsJson().getString("nameOfTask"), Validations.Files.TASKS).onComplete(rc -> {
            if (rc.succeeded()) {

                routingContext.response().putHeader(Consts.CONTENT_TYPE, Consts.TEXT_PLAIN).end("Task deleted");
            }
            if (rc.failed()) {
                routingContext.response().putHeader(Consts.CONTENT_TYPE, Consts.TEXT_PLAIN).end("Error deleting task").failed();

            }
        });
    }

    @Override
    public void showTask(RoutingContext routingContext) {
        Consts.userReader.readJsonTask(Validations.Files.TASKS).onComplete(rc -> {
            if (rc.succeeded()) {
                if(rc.result()==null)
                {
                    routingContext.response().putHeader(Consts.CONTENT_TYPE, Consts.TEXT_PLAIN).end("No tasks found");
                }
                JsonArray jsonArray = new JsonArray();
                for (Map.Entry<String, Task> entry : rc.result().entrySet()) {
                    Task taskHelp = new Task();
                    taskHelp.task(entry.getValue().getTaskName(),entry.getValue().getIdOfUser(),entry.getValue().getTaskDescription());
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("taskName", entry.getKey());
                    jsonObject.addProperty("idOfUser", taskHelp.getIdOfUser());
                    jsonObject.addProperty("taskDescription", taskHelp.getTaskDescription());
                    jsonArray.add(jsonObject);
                }
                routingContext.response().putHeader(Consts.CONTENT_TYPE, Consts.APPLICATION_JSON).end(jsonArray.toString());
            }
            if (rc.failed()) {
                routingContext.response().putHeader(Consts.CONTENT_TYPE, Consts.TEXT_PLAIN).end("Reading has failed");
            }
        });
    }
}
