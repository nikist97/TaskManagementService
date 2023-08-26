package taskmanagement.config;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import taskmanagement.dependencies.kratos.KratosWrapper;
import taskmanagement.service.TaskManagementRepository;
import taskmanagement.service.UserManagementService;
import taskmanagement.storage.MongoDBTaskManagementRepository;

public class ApplicationModule extends AbstractModule {

    @Override
    public void configure() {
        bind(TaskManagementRepository.class).to(MongoDBTaskManagementRepository.class).in(Singleton.class);
        bind(UserManagementService.class).to(KratosWrapper.class).in(Singleton.class);

        install(new MongoDBModule());
        install(new KratosModule());
    }

}
