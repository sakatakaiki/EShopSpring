package com.example.belle.config;

import com.twilio.Twilio;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioConfig {

    @Value("${twilio.accountSid}")
    private String accountSid;

    @Value("${twilio.authToken}")
    private String authToken;

    @Getter
    @Value("${twilio.verifyServiceSid}")
    private String verifyServiceSid;

    @Bean
    public Twilio twilio() {
        Twilio.init(accountSid, authToken);
        return null; // No need to return anything here
    }


}
