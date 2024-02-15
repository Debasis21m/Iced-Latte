package com.zufar.icedlatte.review.endpoint;


import com.zufar.icedlatte.openapi.dto.OrderStatus;
import com.zufar.icedlatte.order.endpoint.OrderEndpoint;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.zufar.icedlatte.test.config.RestAssertion.assertRestApiBodySchemaResponse;
import static com.zufar.icedlatte.test.config.RestUtils.getJwtToken;
import static com.zufar.icedlatte.test.config.RestUtils.getRequestBody;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Testcontainers
@DisplayName("ReviewEndpoint Tests")
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReviewEndpointTest {

    private static final String REVIEW_ADD_BODY= "/review/model/add-review-body.json";
    private static final String REVIEW_ADD_BAD_BODY= "/review/model/add-review-bad-body.json";
    private static final String REVIEW_RESPONSE_SCHEMA = "review/model/schema/review-response-schema.json";
    private static final String FAILED_REVIEW_SCHEMA = "common/model/schema/failed-request-schema.json";
    private static final String EXPECTED_REVIEW = "Wow, Iced Latte is so good!!!";

    private static final String EXISTING_PRODUCT = "e6a4d7f2-d40e-4e5f-93b8-5d56ce6724c5";

    private static final String NON_EXISTING_PRODUCT = "e6g5d65f2-c50e-4e5f-93b8-5d56ce6724c5";

    protected static RequestSpecification specification;
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13.11-bullseye");

    @LocalServerPort
    protected Integer port;

    @Value("${jwt.email}")
    protected String email;

    @Value("${jwt.password}")
    protected String password;

    @BeforeEach
    void tokenAndSpecification() {
        var jwtToken = getJwtToken(port, email, password);
        specification = given()
                .log().all(true)
                .port(port)
                .header("Authorization", "Bearer " + jwtToken)
                .basePath(ReviewEndpoint.REVIEW_URL)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
    }

    @Test
    @DisplayName("Should add review successfully and return object containing review text")
    void shouldAddOrderSuccessfully() {
        String body = getRequestBody(REVIEW_ADD_BODY);

        Response response = given(specification)
                .body(body)
                .post("/{productId}/reviews", EXISTING_PRODUCT);

        assertRestApiBodySchemaResponse(response, HttpStatus.OK, REVIEW_RESPONSE_SCHEMA)
                .body("text", equalTo(EXPECTED_REVIEW));
    }
}