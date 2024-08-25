package applicationVertx.utils;

import applicationVertx.entitys.User;
import io.vertx.core.net.impl.pool.Task;
import org.apache.logging.log4j.LogManager;

import javax.swing.text.html.parser.Entity;

import static applicationVertx.utils.Consts.fileNameOfTasks;
import static applicationVertx.utils.Consts.fileNameOfUsers;

public class Validations {

    public enum Files{
        USERS,
        TASKS;

        public static String fileType(Files file) {
            if(file == USERS)
                return fileNameOfUsers;
            if(file == TASKS)
                return fileNameOfTasks;

            return fileNameOfUsers;
        }

    }

    public static org.apache.logging.log4j.Logger getLoggerFromValidationClass(Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }
    public static String intToString(int number) {
        return String.valueOf(number);
    }
}
