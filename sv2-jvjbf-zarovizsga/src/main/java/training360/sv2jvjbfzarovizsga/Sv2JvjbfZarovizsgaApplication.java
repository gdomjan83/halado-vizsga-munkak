package training360.sv2jvjbfzarovizsga;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Sv2JvjbfZarovizsgaApplication {

    public static void main(String[] args) {
        SpringApplication.run(Sv2JvjbfZarovizsgaApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
