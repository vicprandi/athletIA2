package athletia.service.impl;

import athletia.config.mapper.GenericMapper;
import athletia.model.response.ExerciseResponse;
import athletia.repository.ExerciseRepository;
import athletia.service.ExerciseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseRepository repository;
    private final GenericMapper mapper;

    public ExerciseServiceImpl(ExerciseRepository repository, GenericMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<ExerciseResponse> findAll() {
        return repository.findAll().stream()
                .map(e -> mapper.map(e, ExerciseResponse.class))
                .toList();
    }
}
