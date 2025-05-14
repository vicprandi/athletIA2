package athletia;

import org.springframework.boot.SpringApplication;

public class TestAthletiaApplication {

	public static void main(String[] args) {
		SpringApplication.from(AthletiaApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
