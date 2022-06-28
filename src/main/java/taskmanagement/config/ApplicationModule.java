package taskmanagement.config;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import taskmanagement.service.TaskManagementRepository;
import taskmanagement.storage.InMemoryTaskManagementRepository;

public class ApplicationModule extends AbstractModule {

    @Override
    public void configure() {
        bind(TaskManagementRepository.class).to(InMemoryTaskManagementRepository.class).in(Singleton.class);
    }

}
