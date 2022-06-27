package taskmanagement.service;

public class TaskUpdate {

    private final String title;
    private final String description;
    private final Boolean completed;

    public TaskUpdate(String title, String description, Boolean completed) {
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getCompleted() {
        return completed;
    }

}
