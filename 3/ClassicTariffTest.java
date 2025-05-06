/**
 * Тесты для тарифа "Классика"
 */
class ClassicTariffTest extends TariffTestBase {

    /**
     * Тест 1: Исходящий звонок другому оператору
     * Проверяет:
     * 1. Корректность списания (2.5 у.е./мин)
     * 2. Округление длительности вверх (1:30 -> 2 минуты)
     */
    @Test
    void outgoingCallToOtherOperator_ShouldChargeCorrectly() {
        // 1. Подготовка тестовых данных (звонок 1 минута 30 секунд)
        String cdr = "01,89123456789,89219876543,2025-03-01T10:00:00,2025-03-01T10:01:30";
        
        // 2. Получаем исходный баланс
        BigDecimal originalBalance = getBalance("89123456789");
        
        // 3. Отправка CDR в систему
        given()
            .contentType("text/plain")
            .body(cdr)
            .post("/cdr")
        .then()
            .statusCode(200);
        
        // 4. Проверка списания (должно списаться 2*2.5 = 5 у.е.)
        BigDecimal newBalance = getBalance("89123456789");
        assertEquals(originalBalance.subtract(new BigDecimal("5.0")), newBalance);
    }

    /**
     * Тест 2: Входящий звонок
     * Проверяет:
     * 1. Отсутствие списания средств
     */
    @Test
    void incomingCall_ShouldBeFree() {
        // 1. Подготовка тестовых данных (входящий звонок)
        String cdr = "02,89123456789,89219876543,2025-03-01T11:00:00,2025-03-01T11:02:15";
        
        // 2. Получаем исходный баланс
        BigDecimal originalBalance = getBalance("89123456789");
        
        // 3. Отправка CDR
        given()
            .body(cdr)
            .post("/cdr")
        .then()
            .statusCode(200);
        
        // 4. Проверка что баланс не изменился
        assertEquals(originalBalance, getBalance("89123456789"));
    }
}