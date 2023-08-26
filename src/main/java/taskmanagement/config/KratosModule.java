package taskmanagement.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import sh.ory.kratos.ApiClient;
import sh.ory.kratos.api.FrontendApi;

public class KratosModule extends AbstractModule {

    @Provides
    private FrontendApi provideKratosFrontendApi() {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(System.getenv("Kratos_Base_Path"));

        return new FrontendApi(apiClient);
    }
}
