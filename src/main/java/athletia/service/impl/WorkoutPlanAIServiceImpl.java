package athletia.service.impl;

import athletia.model.User;
import athletia.model.WorkoutExerciseGenerated;
import athletia.model.WorkoutPlanGenerated;
import athletia.service.WorkoutPlanAIService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkoutPlanAIServiceImpl implements WorkoutPlanAIService {

    @Override
    public WorkoutPlanGenerated generateWorkoutPlan(User user) {
        return new WorkoutPlanGenerated(
                "Plano Iniciante para Emagrecimento",
                "Treino de corpo inteiro 3x por semana focado em resistência e queima de gordura.",
                4,
                List.of(
                        new WorkoutExerciseGenerated("Agachamento Livre", "Pernas", 3, 15, 60, 30.0),
                        new WorkoutExerciseGenerated("Supino Reto com Barra", "Peito", 3, 12, 60, 20.0),
                        new WorkoutExerciseGenerated("Remada Curvada", "Costas", 3, 10, 60, 25.0)
                )
        );
    }
}