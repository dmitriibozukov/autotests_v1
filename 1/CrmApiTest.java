public class CrmApiTest extends ApiTestBase {

    @Test
    @DisplayName("Создание нового абонента")
    void createSubscriber_ShouldReturnCreated() {
        given()
            .auth().basic("manager", "password")
            .contentType(ContentType.JSON)
            .body(TestData.newSubscriber())
        .when()
            .post("/api/subscribers")
        .then()
            .statusCode(201);
    }

    @Test
    @DisplayName("Пополнение баланса")
    void topUpBalance_ShouldUpdateBalance() {
        given()
            .auth().basic("manager", "password")
            .contentType(ContentType.JSON)
            .pathParam("msisdn", "89991112233")
            .body(Collections.singletonMap("amount", 100))
        .when()
            .post("/api/subscribers/{msisdn}/topup")
        .then()
            .statusCode(200)
            .body("newBalance", greaterThan(0f));
    }
}