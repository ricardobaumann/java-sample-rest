package com.github.ricbau.conventions.clients;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MockValidationController {

    @PostMapping("/validation")
    public ValidateAddressClient.ExternalServiceResult validate(@RequestBody ValidateAddressClient.ExternalServiceCommand externalServiceCommand) {
        log.info("Received validation command: {}", externalServiceCommand);
        return new ValidateAddressClient.ExternalServiceResult(
                "this just a mocked result",
                true
        );
    }

}
