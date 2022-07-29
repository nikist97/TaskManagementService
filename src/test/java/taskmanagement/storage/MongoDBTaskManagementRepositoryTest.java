package taskmanagement.storage;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.MockitoJUnitRunner;
import taskmanagement.service.Task;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class MongoDBTaskManagementRepositoryTest {

    @Test
    public void testSave() {
        MongoCollection<MongoDBTaskManagementRepository.MongoDBTask> collection = mock(MongoCollection.class);
        MongoDBTaskManagementRepository repository = new MongoDBTaskManagementRepository(collection);

        Instant now = Instant.parse("2022-07-27T19:09:04.120Z");
        Task task = Task.builder("test-title", "test-description")
                .withCreatedAt(now)
                .withCompleted(true)
                .build();

        repository.save(task);

        MongoDBTaskManagementRepository.MongoDBTask mongoDBTask = new MongoDBTaskManagementRepository.MongoDBTask();
        mongoDBTask.setCompleted(task.isCompleted());
        mongoDBTask.setCreatedAt(now.toEpochMilli());
        mongoDBTask.setIdentifier(task.getIdentifier());
        mongoDBTask.setTitle(task.getTitle());
        mongoDBTask.setDescription(task.getDescription());

        ArgumentCaptor<ReplaceOptions> replaceOptionsArgumentCaptor = ArgumentCaptor.forClass(ReplaceOptions.class);

        verify(collection).replaceOne(eq(Filters.eq("_id", task.getIdentifier())), eq(mongoDBTask), replaceOptionsArgumentCaptor.capture());
        verifyNoMoreInteractions(collection);

        assertTrue(replaceOptionsArgumentCaptor.getValue().isUpsert());
    }

    @Test
    public void testGetAll() {
        Instant now = Instant.parse("2022-07-27T19:09:04.120Z");
        Task task = Task.builder("test-title", "test-description")
                .withCreatedAt(now)
                .withCompleted(true)
                .build();

        MongoDBTaskManagementRepository.MongoDBTask mongoDBTask = new MongoDBTaskManagementRepository.MongoDBTask();
        mongoDBTask.setCompleted(task.isCompleted());
        mongoDBTask.setCreatedAt(now.toEpochMilli());
        mongoDBTask.setIdentifier(task.getIdentifier());
        mongoDBTask.setTitle(task.getTitle());
        mongoDBTask.setDescription(task.getDescription());

        MongoCollection<MongoDBTaskManagementRepository.MongoDBTask> collection = mock(MongoCollection.class);
        MongoDBTaskManagementRepository repository = new MongoDBTaskManagementRepository(collection);


        FindIterable<MongoDBTaskManagementRepository.MongoDBTask> findIterable = mock(FindIterable.class);
        MongoCursor<MongoDBTaskManagementRepository.MongoDBTask> mongoCursor = mock(MongoCursor.class);

        when(mongoCursor.hasNext()).thenReturn(true, false);
        when(mongoCursor.next()).thenReturn(mongoDBTask);
        when(findIterable.iterator()).thenReturn(mongoCursor);
        when(collection.find()).thenReturn(findIterable);

        List<Task> tasks = repository.getAll();

        assertEquals(Collections.singletonList(task), tasks);
        verify(collection).find();
        verifyNoMoreInteractions(collection);
    }

    @Test
    public void testGet() {
        MongoCollection<MongoDBTaskManagementRepository.MongoDBTask> collection = mock(MongoCollection.class);
        MongoDBTaskManagementRepository repository = new MongoDBTaskManagementRepository(collection);

        Instant now = Instant.parse("2022-07-27T19:09:04.120Z");

        Task expectedTask = Task.builder("test-title", "test-description")
                .withCreatedAt(now)
                .withCompleted(true)
                .build();

        MongoDBTaskManagementRepository.MongoDBTask mongoDBTask = new MongoDBTaskManagementRepository.MongoDBTask();
        mongoDBTask.setCompleted(expectedTask.isCompleted());
        mongoDBTask.setCreatedAt(now.toEpochMilli());
        mongoDBTask.setIdentifier(expectedTask.getIdentifier());
        mongoDBTask.setTitle(expectedTask.getTitle());
        mongoDBTask.setDescription(expectedTask.getDescription());

        FindIterable<MongoDBTaskManagementRepository.MongoDBTask> findIterable = mock(FindIterable.class);
        when(findIterable.first()).thenReturn(mongoDBTask);
        when(collection.find(Filters.eq("_id", expectedTask.getIdentifier()))).thenReturn(findIterable);

        Optional<Task> actualTask = repository.get(expectedTask.getIdentifier());

        assertTrue(actualTask.isPresent());
        assertEquals(expectedTask, actualTask.get());

        verify(collection).find(Filters.eq("_id", expectedTask.getIdentifier()));
        verifyNoMoreInteractions(collection);
    }

    @Test
    public void testGetForNullTask() {
        MongoCollection<MongoDBTaskManagementRepository.MongoDBTask> collection = mock(MongoCollection.class);
        MongoDBTaskManagementRepository repository = new MongoDBTaskManagementRepository(collection);

        String taskID = "test-task-id";
        FindIterable<MongoDBTaskManagementRepository.MongoDBTask> findIterable = mock(FindIterable.class);
        when(findIterable.first()).thenReturn(null);
        when(collection.find(Filters.eq("_id", taskID))).thenReturn(findIterable);

        Optional<Task> task = repository.get(taskID);

        assertFalse(task.isPresent());

        verify(collection).find(Filters.eq("_id", taskID));
        verifyNoMoreInteractions(collection);
    }

    @Test
    public void testDelete() {
        MongoCollection<MongoDBTaskManagementRepository.MongoDBTask> collection = mock(MongoCollection.class);
        MongoDBTaskManagementRepository repository = new MongoDBTaskManagementRepository(collection);

        String taskID = "test-task-id";

        repository.delete(taskID);

        verify(collection).deleteOne(new Document("_id", taskID));
        verifyNoMoreInteractions(collection);
    }
}