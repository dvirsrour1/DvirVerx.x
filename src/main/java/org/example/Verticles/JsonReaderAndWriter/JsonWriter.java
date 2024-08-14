package org.example.Verticles.JsonReaderAndWriter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.Verticles.ToDoEntity.ToDo;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class JsonWriter extends AbstractVerticle {

    private final static Gson gson = new Gson();
    private final static Vertx vertx = Vertx.vertx();
    private final static Logger logger = LogManager.getLogger(JsonWriter.class);
    private final static String FileName = "C:\\Users\\aliza_rvjno4x\\IdeaProjects\\DvirVerx.x\\src\\main\\java\\org\\example\\JsonFiles\\data.json";

    public enum FUNCTIONS {
        WRONG_SYMBOL(0),
        WRITE_TO_FILE(1),
        READ_FROM_FILE(2),
        DELETE_FROM_FILE(3),
        STOP_FUNCTIONS(-1);

        private int FUNC;

        FUNCTIONS(int func) {
            this.FUNC = func;
        }

        public int getFunc() {
            return FUNC;
        }

        public static FUNCTIONS fromInt(int func) {
            for (FUNCTIONS f : FUNCTIONS.values()) {
                if (f.getFunc() == func) {
                    return f;
                }
            }
            return FUNCTIONS.WRONG_SYMBOL;
        }

    }

    private final static String FirstBlock = """
            ---------------------------------------------------------------------------------------
                       
            Programmer: Dvir Srour
            Date: 1.8.2024
            KerenOrFirstProject_VertX
                       
                       
            --------------------------------------------------------------------------------------- 
             """;

    @Override
    public void start() {

    } //eventLoop


    public Future<Void> writeUserToFile(HashMap<String, ToDo> users) {
        Promise<Void> promise = Promise.promise();
        vertx.executeBlocking(promiseHandler -> {
            Map<String, ToDo> todoMap = new HashMap<>();
            try (FileReader reader = new FileReader(FileName)) {
                Type type = new TypeToken<HashMap<String, ToDo>>() {
                }.getType();
                todoMap = gson.fromJson(reader, type);
                if (todoMap == null) {
                    todoMap = new HashMap<>();
                }
            } catch (IOException e) {
                logger.error(e);
                promiseHandler.fail(e);
                return;
            }

            todoMap.putAll(users);
            try (FileWriter file = new FileWriter(FileName)) {
                gson.toJson(todoMap, file);
                promiseHandler.complete();
            } catch (IOException e) {
                logger.error(e);
                promiseHandler.fail(e);
            }
        }, promise);

        return promise.future();
    }

    public static void main(String[] args) {
        JsonWriter jsonWriter = new JsonWriter();
        JsonReader jsonReader = new JsonReader();
        JsonDelete jsonDelete = new JsonDelete();

        Scanner scanner = new Scanner(System.in);
        HashMap<String, ToDo> users = new HashMap<>();
        System.out.println(FirstBlock);
        while (true) {
            logger.info("Welcome to the ToDo Manager!");
            logger.info("Please select an option:");
            logger.info("1 - Add information to the JSON file");
            logger.info("2 - Read information from the JSON file");
            logger.info("3 - Delete information from the JSON file");
            logger.info("-1 - Exit");
            logger.info("Your choice: ");

            FUNCTIONS FromChoice = FUNCTIONS.fromInt(0);
            int choose = 0;
            choose = scanner.nextInt();
            FromChoice = FUNCTIONS.fromInt(choose);
            switch (FromChoice) {
                case FUNCTIONS.WRITE_TO_FILE:
                    String Stopper = "y";
                    while (!Stopper.equals("done") && !Stopper.equals("Done") && !Stopper.equals("DONE")) {
                        logger.info("Write your Name: ");
                        logger.warn("NOTICE: the name can't contain numbers or symbols.");
                        String name = scanner.next();
                        if (!name.matches("[a-zA-Z]+")) {
                            logger.error("ERROR: the name can't contain numbers or symbols.");
                            System.exit(0);
                        }
                        logger.info("write your ID: ");
                        String id = scanner.next();
                        try {
                            Integer.parseInt(id);
                        } catch (NumberFormatException e) {
                            logger.error("Invalid id number");
                            System.exit(0);
                        } catch (Exception e)
                        {
                            logger.error("ERROR");
                            System.exit(0);
                            break;
                        }
                        ToDo user1 = new ToDo();
                        user1.ToDo(name, Integer.parseInt(id), "Student");
                        users.put(id, user1);
                        logger.info("Write 'done' if you would like to stop adding students.");
                        Stopper = scanner.next();
                    }
                    jsonWriter.writeUserToFile(users).onComplete(result -> {
                        if (result.succeeded()) {
                            logger.info("Writed user to file");
                        } else {
                            logger.info("Write Failed");
                        }
                    });
                    break;

                case FUNCTIONS.READ_FROM_FILE:
                    jsonReader.readUserFromFile().onComplete(readResult -> {
                        if (readResult.succeeded()) {

                            logger.info("Read Successful");
                            for (Map.Entry<String, ToDo> entry : readResult.result().entrySet()) {
                                logger.info("Key: " + entry.getKey() + ", name: " + entry.getValue().getName() + ", id: " + entry.getValue().getId() + ", Description: " + entry.getValue().getDescription());
                            }
                        } else {
                            logger.info("Read Failed");
                        }
                    });
                    break;


                case FUNCTIONS.DELETE_FROM_FILE:
                    logger.info("Which one would you like to delete? write his ID.");
                    String choice = scanner.next();
                    try {
                        Integer.parseInt(choice);
                    } catch (Exception e) {
                        logger.error("Wrong ID. Exiting application.");
                        System.exit(0);
                    }
                    logger.warn("Are you sure you want to delete the information from the JSON file? (1 for yes 0 for no)");
                    String YesNo = scanner.next();
                    if (Integer.parseInt(YesNo) == 1) {
                        jsonDelete.DeleteFromJson(choice).onComplete(result -> {
                            if (result.succeeded()) {
                                logger.info("Delete Successful");
                            }
                            if (result.failed()) {
                                logger.info("Delete Failed");
                            }
                        });
                    }
                    break;


                case STOP_FUNCTIONS:
                    logger.info("Exiting application.");
                    System.exit(0);
                    break;

                case WRONG_SYMBOL:
                    logger.error("Symbol is not correct, Exiting application.");
                    System.exit(0);
                    break;

                default:
                    logger.error("Error");
                    System.exit(0);
                    break;
            }


        }


    }


}