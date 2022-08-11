package cz.jbenak.bo;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEncryptableProperties
public class BackofficeServer {

    public static void main(String[] args) {
        SpringApplication.run(BackofficeServer.class, args);
    }

}
