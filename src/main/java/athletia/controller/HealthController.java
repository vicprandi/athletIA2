package athletia.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/")
    public String root() {
        return "AthletIA backend online!";
    }

    @GetMapping("/status")
    public String status() {
        return "OK";
    }
}