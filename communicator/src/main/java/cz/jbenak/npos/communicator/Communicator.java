package cz.jbenak.npos.communicator;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEncryptableProperties
public class Communicator {

    public static void main(String[] args) {
        SpringApplication.run(Communicator.class, args);
    }

}
