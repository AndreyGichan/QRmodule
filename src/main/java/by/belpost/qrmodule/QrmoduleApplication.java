package by.belpost.qrmodule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class QrmoduleApplication {

	public static void main(String[] args) {
		SpringApplication.run(QrmoduleApplication.class, args);
	}

}
