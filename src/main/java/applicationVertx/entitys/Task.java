package applicationVertx.entitys;

public class Task {

    private String nameOfTask;
    private int idOfUser;
    private String description;

    public void task(String nameOfTask, int idOfUser, String description) {
        this.nameOfTask = nameOfTask;
        this.idOfUser = idOfUser;
        this.description = description;

    }
    public String getNameOfTask() {
        return nameOfTask;

    }
    public void setNameOfTask(String nameOfTask) {
        this.nameOfTask = nameOfTask;
    }
    public int getIdOfUser() {
        return idOfUser;
    }
    public void setIdOfUser(int idOfUser) {
        this.idOfUser = idOfUser;

    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

}
