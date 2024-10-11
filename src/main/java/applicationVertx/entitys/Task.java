package applicationVertx.entitys;

public class Task {

    private String taskName;
    private int idOfUser;
    private String taskDescription;

    public void task(String nameOfTask, int idOfUser, String description) {
        this.taskName = nameOfTask;
        this.idOfUser = idOfUser;
        this.taskDescription = description;

    }
    public String getTaskName() {
        return taskName;

    }
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    public int getIdOfUser() {
        return idOfUser;
    }
    public void setIdOfUser(int idOfUser) {
        this.idOfUser = idOfUser;

    }
    public String getTaskDescription() {
        return taskDescription;
    }
    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

}
