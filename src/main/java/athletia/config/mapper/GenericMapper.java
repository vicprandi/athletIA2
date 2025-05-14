package athletia.config.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class GenericMapper {

    private final ObjectMapper objectMapper;

    public GenericMapper(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }


    public <T> T map(Object source, Class<T> target) {
        return objectMapper.convertValue(source, target);
    }
}
