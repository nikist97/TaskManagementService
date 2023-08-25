package taskmanagement.storage;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonProperty;
import taskmanagement.service.Task;
import taskmanagement.service.TaskManagementRepository;

import javax.inject.Inject;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

public class MongoDBTaskManagementRepository implements TaskManagementRepository {

    private final MongoCollection<MongoDBTask> tasksCollection;

    @Inject
    public MongoDBTaskManagementRepository(MongoCollection<MongoDBTask> tasksCollection) {
        this.tasksCollection = tasksCollection;
    }

    @Override
    public void save(Task task) {
        MongoDBTask mongoDBTask = toMongoDBTask(task);
        ReplaceOptions replaceOptions = new ReplaceOptions()
                .upsert(true);
        tasksCollection.replaceOne(eq("_id", task.getIdentifier()), mongoDBTask, replaceOptions);
    }

    @Override
    public List<Task> getAll() {
        FindIterable<MongoDBTask> mongoDBTasks = tasksCollection.find();

        List<Task> tasks = new ArrayList<>();
        for (MongoDBTask mongoDBTask: mongoDBTasks) {
            tasks.add(fromMongoDBTask(mongoDBTask));
        }

        return tasks;
    }

    @Override
    public Optional<Task> get(String taskID) {
        Optional<MongoDBTask> mongoDBTask = Optional.ofNullable(
                tasksCollection.find(eq("_id", taskID)).first());

        return mongoDBTask.map(this::fromMongoDBTask);
    }

    @Override
    public void delete(String taskID) {
        Document taskIDFilter = new Document("_id", taskID);
        tasksCollection.deleteOne(taskIDFilter);
    }

    private Task fromMongoDBTask(MongoDBTask mongoDBTask) {
        return Task.builder(mongoDBTask.getTitle(), mongoDBTask.getDescription(), mongoDBTask.getCreatedBy())
                .withIdentifier(mongoDBTask.getIdentifier())
                .withCompleted(mongoDBTask.isCompleted())
                .withCreatedAt(Instant.ofEpochMilli(mongoDBTask.getCreatedAt()))
                .build();
    }

    private MongoDBTask toMongoDBTask(Task task) {
        MongoDBTask mongoDBTask = new MongoDBTask();
        mongoDBTask.setIdentifier(task.getIdentifier());
        mongoDBTask.setTitle(task.getTitle());
        mongoDBTask.setDescription(task.getDescription());
        mongoDBTask.setCreatedBy(task.getCreatedBy());
        mongoDBTask.setCreatedAt(task.getCreatedAt().toEpochMilli());
        mongoDBTask.setCompleted(task.isCompleted());

        return mongoDBTask;
    }

    public static class MongoDBTask {
        @BsonProperty("_id")
        private String identifier;

        private String title;

        private String description;

        @BsonProperty("created_by")
        private String createdBy;

        @BsonProperty("created_at")
        private long createdAt;

        private boolean completed;

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public long getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(long createdAt) {
            this.createdAt = createdAt;
        }

        public boolean isCompleted() {
            return completed;
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            MongoDBTask that = (MongoDBTask) o;
            return createdAt == that.createdAt
                    && completed == that.completed
                    && Objects.equals(identifier, that.identifier)
                    && Objects.equals(title, that.title)
                    && Objects.equals(description, that.description)
                    && Objects.equals(createdBy, that.createdBy);
        }

        @Override
        public int hashCode() {
            return Objects.hash(identifier, title, description, createdBy, createdAt, completed);
        }
    }
}
