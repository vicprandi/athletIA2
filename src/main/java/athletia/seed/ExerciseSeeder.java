package athletia.seed;

import athletia.model.Exercise;
import athletia.repository.ExerciseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
public class ExerciseSeeder implements CommandLineRunner {

    private final ExerciseRepository repository;
    private final ObjectMapper objectMapper;

    public ExerciseSeeder(ExerciseRepository repository) {
        this.repository = repository;
        this.objectMapper = new ObjectMapper();
    }

    public void run(String... args) throws Exception{
        if (repository.count() > 0) return;

        InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream("seed/exercises.json");

        List<Exercise> exercises = objectMapper
                .readerForListOf(Exercise.class)
                .readValue(inputStream);

        repository.saveAll(exercises);
        System.out.print("Exercícios setados com sucesso!");
    }
}
