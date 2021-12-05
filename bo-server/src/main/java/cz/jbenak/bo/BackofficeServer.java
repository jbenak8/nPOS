package cz.jbenak.bo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackofficeServer {

    public static void main(String[] args) {
        System.setProperty("jasypt.encryptor.password", "PosC_srv1369a,82");
        SpringApplication.run(BackofficeServer.class, args);
    }

}
