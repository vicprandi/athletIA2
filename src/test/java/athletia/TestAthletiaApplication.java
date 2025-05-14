package athletia;

import org.springframework.boot.SpringApplication;
import org.testcontainers.utility.TestcontainersConfiguration;

public class TestAthletiaApplication {

	public static void main(String[] args) {
		SpringApplication.from(AthletiaApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
