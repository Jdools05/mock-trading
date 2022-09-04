package tests;


import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Map;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserResourceTest {

    @Test
    @Order(1)
    public void createUser() {
        given().queryParams(Map.of("cash", 999999, "email", "test@gmail.com", "firstName", "test", "lastName", "user", "password", "password", "role", "user"))
                .when().post("/api/v1/user").then().statusCode(200);
    }
}
