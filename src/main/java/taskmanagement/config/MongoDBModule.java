package taskmanagement.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import taskmanagement.storage.MongoDBTaskManagementRepository;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoDBModule extends AbstractModule {

    @Provides
    private MongoCollection<MongoDBTaskManagementRepository.MongoDBTask> provideMongoCollection() {
        ConnectionString connectionString = new ConnectionString(System.getenv("MongoDB_URI"));

        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);

        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();

        MongoClient mongoClient = MongoClients.create(mongoClientSettings);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("tasks_management_db");
        return mongoDatabase.getCollection("tasks", MongoDBTaskManagementRepository.MongoDBTask.class);
    }
}
