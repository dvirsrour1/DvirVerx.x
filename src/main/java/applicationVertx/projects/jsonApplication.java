package applicationVertx.projects;

import applicationVertx.verticles.jsonVerticles.jsonDelete;
import applicationVertx.verticles.jsonVerticles.jsonReader;
import applicationVertx.verticles.jsonVerticles.jsonWriter;
import applicationVertx.verticles.todoEntity.ToDo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static applicationVertx.verticles.jsonVerticles.jsonWriter.FirstBlock;

public class jsonApplication {

    public static void main(String[] args) {
        jsonWriter jsonWriter = new jsonWriter();
        jsonReader jsonReader = new jsonReader();
        jsonDelete jsonDelete = new jsonDelete();
        Logger logger = LogManager.getLogger(jsonApplication.class);
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

            applicationVertx.verticles.jsonVerticles.jsonWriter.FUNCTIONS FromChoice = applicationVertx.verticles.jsonVerticles.jsonWriter.FUNCTIONS.fromInt(0);
            int choose = 0;
            choose = scanner.nextInt();
            FromChoice = applicationVertx.verticles.jsonVerticles.jsonWriter.FUNCTIONS.fromInt(choose);
            switch (FromChoice) {
                case applicationVertx.verticles.jsonVerticles.jsonWriter.FUNCTIONS.WRITE_TO_FILE:
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
                    jsonWriter.write(users).onComplete(result -> {
                        if (result.succeeded()) {
                            logger.info("Writed user to file");
                        } else {
                            logger.info("Write Failed");
                        }
                    });
                    break;

                case applicationVertx.verticles.jsonVerticles.jsonWriter.FUNCTIONS.READ_FROM_FILE:
                    jsonReader.readJson().onComplete(readResult -> {
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


                case applicationVertx.verticles.jsonVerticles.jsonWriter.FUNCTIONS.DELETE_FROM_FILE:
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
                        jsonDelete.deleteUser(choice).onComplete(result -> {
                            if (result.succeeded()) {
                                logger.info("Delete Successful");
                            }
                            if (result.failed()) {
                                logger.info("Delete Failed" + result.cause().getMessage());
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
