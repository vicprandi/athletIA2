package athletia.controller;

import athletia.model.request.WorkoutSessionRequest;
import athletia.model.response.WorkoutSessionResponse;
import athletia.service.WorkoutSessionService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WorkoutSessionController {

    private final WorkoutSessionService service;

    public WorkoutSessionController(WorkoutSessionService service) {
        this.service = service;
    }

    public WorkoutSessionResponse register(String planId, WorkoutSessionRequest request) {
        return service.register(planId, request);
    }

    public List<WorkoutSessionResponse> listAllFromCurrentUser() {
        return service.findAllByAuthenticatedUser();
    }

    public void deleteById(String sessionId) {
        service.deleteById(sessionId);
    }
}