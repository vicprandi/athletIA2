package athletia.service;

import athletia.model.response.ExerciseResponse;

import java.util.List;

public interface ExerciseService {
    List<ExerciseResponse> findAll();
}
