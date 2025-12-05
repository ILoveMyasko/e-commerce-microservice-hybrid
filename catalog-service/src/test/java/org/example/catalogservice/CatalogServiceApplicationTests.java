package org.example.catalogservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

@SpringBootTest(properties = "grpc.server.port=-1")
@Profile("rest")
class CatalogServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
