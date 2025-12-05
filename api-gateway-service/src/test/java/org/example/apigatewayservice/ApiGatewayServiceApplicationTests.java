package org.example.apigatewayservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

@SpringBootTest
@Profile("rest")
class ApiGatewayServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}