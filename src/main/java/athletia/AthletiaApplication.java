package athletia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "athletia")
public class AthletiaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AthletiaApplication.class, args);
	}

}
