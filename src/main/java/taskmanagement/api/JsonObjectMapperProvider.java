package taskmanagement.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import static com.fasterxml.jackson.databind.MapperFeature.ALLOW_COERCION_OF_SCALARS;

@Provider
public class JsonObjectMapperProvider implements ContextResolver<ObjectMapper> {

    private final ObjectMapper jsonObjectMapper;

    /**
     * Create a custom JSON object mapper provider.
     */
    public JsonObjectMapperProvider() {
        jsonObjectMapper = new ObjectMapper();
        jsonObjectMapper.disable(ALLOW_COERCION_OF_SCALARS);
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return jsonObjectMapper;
    }
}
