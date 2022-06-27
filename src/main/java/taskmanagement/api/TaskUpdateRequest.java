package taskmanagement.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TaskUpdateRequest {

    private final String title;
    private final String description;
    private final Boolean completed;

    @JsonCreator
    public TaskUpdateRequest(@JsonProperty(value = "title") String title,
                             @JsonProperty(value = "description") String description,
                             @JsonProperty(value = "completed") Boolean completed) {
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

    public Boolean isCompleted() {
        return completed;
    }
}
