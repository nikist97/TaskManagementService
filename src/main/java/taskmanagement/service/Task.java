package taskmanagement.service;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import static taskmanagement.utils.Validator.validateArgNotNull;
import static taskmanagement.utils.Validator.validateArgNotNullOrBlank;


public class Task {

    private final String identifier;
    private final String title;
    private final String description;
    private final String createdBy;
    private final Instant createdAt;
    private final boolean completed;

    private Task(TaskBuilder builder) {
        this.identifier = builder.identifier;
        this.title = builder.title;
        this.description = builder.description;
        this.createdBy = builder.createdBy;
        this.createdAt = builder.createdAt;
        this.completed = builder.completed;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public boolean isCompleted() {
        return completed;
    }

    public Task update(TaskUpdate update) {
        TaskBuilder newTaskBuilder = new TaskBuilder(title, description, createdBy)
                .withIdentifier(identifier)
                .withCreatedAt(createdAt)
                .withCompleted(completed);

        if (update.getTitle() != null) {
            newTaskBuilder = newTaskBuilder.withTitle(update.getTitle());
        }

        if (update.getDescription() != null) {
            newTaskBuilder = newTaskBuilder.withDescription(update.getDescription());
        }

        if (update.getCompleted() != null) {
            newTaskBuilder = newTaskBuilder.withCompleted(update.getCompleted());
        }

        return newTaskBuilder.build();
    }

    public static TaskBuilder builder(String title, String description, String createdBy) {
        return new TaskBuilder(title, description, createdBy);
    }

    @Override
    public String toString() {
        return "Task{" +
                "identifier='" + identifier + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdAt=" + createdAt +
                ", completed=" + completed +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        return completed == task.completed
                && identifier.equals(task.identifier)
                && title.equals(task.title)
                && description.equals(task.description)
                && createdBy.equals(task.createdBy)
                && createdAt.equals(task.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, title, description, createdBy, createdAt, completed);
    }

    public static class TaskBuilder {
        private final String createdBy;
        private String title;
        private String description;

        private String identifier;
        private Instant createdAt;
        private boolean completed;

        private TaskBuilder(String title, String description, String createdBy) {
            validateArgNotNullOrBlank(title, "title");
            validateArgNotNullOrBlank(description, "description");
            validateArgNotNullOrBlank(createdBy, "createdBy");

            this.title = title;
            this.description = description;
            this.createdBy = createdBy;
            this.identifier = UUID.randomUUID().toString();
            this.createdAt = Instant.now();
            this.completed = false;
        }

        public TaskBuilder withTitle(String title) {
            validateArgNotNullOrBlank(title, "title");

            this.title = title;
            return this;
        }

        public TaskBuilder withDescription(String description) {
            validateArgNotNullOrBlank(description, "description");

            this.description = description;
            return this;
        }

        public TaskBuilder withIdentifier(String identifier) {
            validateArgNotNullOrBlank(identifier, "identifier");

            this.identifier = identifier;
            return this;
        }

        public TaskBuilder withCreatedAt(Instant createdAt) {
            validateArgNotNull(createdAt, "createdAt");

            this.createdAt = createdAt;
            return this;
        }

        public TaskBuilder withCompleted(boolean completed) {
            this.completed = completed;
            return this;
        }

        public Task build() {
            return new Task(this);
        }
    }
}
