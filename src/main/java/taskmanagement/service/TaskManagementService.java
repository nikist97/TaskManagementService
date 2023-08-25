package taskmanagement.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import taskmanagement.exceptions.TaskNotFoundException;

import javax.inject.Inject;
import java.util.List;

import static taskmanagement.utils.Validator.validateArgNotNull;

public class TaskManagementService {

    private static final Logger LOGGER = LogManager.getLogger(TaskManagementService.class);

    private final TaskManagementRepository repository;

    @Inject
    public TaskManagementService(TaskManagementRepository repository) {
        this.repository = repository;
    }

    public Task create(String title, String description, User creator) {
        validateArgNotNull(creator, "creator");

        Task task = Task.builder(title, description, creator.getUserID()).build();

        repository.save(task);

        LOGGER.info("Successfully created new task {}", task);

        return task;
    }

    public Task update(String taskID, TaskUpdate taskUpdate) {
        Task oldTask = retrieve(taskID);

        Task newTask = oldTask.update(taskUpdate);
        repository.save(newTask);

        return newTask;
    }

    public List<Task> retrieveAll() {
        return repository.getAll();
    }

    public Task retrieve(String taskID) {
        return repository.get(taskID).orElseThrow(() ->
                new TaskNotFoundException("Task with the given identifier cannot be found - " + taskID));
    }

    public void delete(String taskID) {
        repository.delete(taskID);
    }
}
