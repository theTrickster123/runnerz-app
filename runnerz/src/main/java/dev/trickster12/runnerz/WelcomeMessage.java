package dev.trickster12.runnerz;

import org.springframework.stereotype.Component;

@Component
public class WelcomeMessage {


    public String getWelcomeMessage(){
        return "Welcome to Runnerz App !";

    }
}
