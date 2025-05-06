/**
 * Базовый класс для E2E тестов тарификации
 * Содержит общие настройки и утилитные методы
 */
public class TariffTestBase {
    protected static final String BRT_URL = "http://localhost:8080";
    protected static final String HRS_URL = "http://localhost:8081";

    @BeforeEach
    void setup() {
        RestAssured.baseURI = BRT_URL;
    }

    // Метод для получения текущего баланса абонента из BRT
    protected BigDecimal getBalance(String msisdn) {
        return given()
            .get("/subscribers/" + msisdn + "/balance")
            .then()
            .extract()
            .path("balance");
    }
}