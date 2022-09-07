package cz.jbenak.bo.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/heartbeat", produces = MediaType.TEXT_PLAIN_VALUE)
public class HeartBeatController {

    @GetMapping
    public String getHeartBeat() {
        return "Hello, I am nPOS Back Office Server.";
    }
}
