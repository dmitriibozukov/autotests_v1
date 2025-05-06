/**
 * Тесты для тарифа "Помесячный"
 */
class MonthlyTariffTest extends TariffTestBase {

    /**
     * Тест 1: Звонки в пределах пакета минут
     * Проверяет:
     * 1. Баланс не изменяется
     * 2. Минуты корректно учитываются
     */
    @Test
    void callWithinIncludedMinutes_ShouldNotChargeExtra() {
        // 1. Подготовка тестовых данных (20-минутный звонок)
        String cdr = "01,89123456789,89219876543,2025-03-01T12:00:00,2025-03-01T12:20:00";
        
        // 2. Получаем исходный баланс
        BigDecimal originalBalance = getBalance("89123456789");
        
        // 3. Отправка CDR
        given()
            .body(cdr)
            .post("/cdr")
        .then()
            .statusCode(200);
        
        // 4. Проверки
        assertEquals(originalBalance, getBalance("89123456789")); // Баланс не изменился
        
        // Проверка учета минут (через API HRS)
        int usedMinutes = given()
            .baseUri(HRS_URL)
            .get("/usage/89123456789")
            .then()
            .extract()
            .path("usedMinutes");
        
        assertEquals(20, usedMinutes);
    }

    /**
     * Тест 2: Превышение лимита минут
     * Проверяет:
     * 1. Доплата по тарифу "Классика" за превышение
     */
    @Test
    void exceededCall_ShouldChargeClassicRate() {
        // 1. Подготовка тестовых данных (60-минутный звонок при лимите 50 мин)
        String cdr = "01,89123456789,89219876543,2025-03-01T13:00:00,2025-03-01T14:00:00";
        
        // 2. Получаем исходный баланс
        BigDecimal originalBalance = getBalance("89123456789");
        
        // 3. Отправка CDR
        given()
            .body(cdr)
            .post("/cdr")
        .then()
            .statusCode(200);
        
        // 4. Проверка списания (10 лишних минут * 1.5 у.е.)
        BigDecimal expectedCharge = new BigDecimal("15.0");
        BigDecimal actualBalance = getBalance("89123456789");
        assertEquals(originalBalance.subtract(expectedCharge), actualBalance);
    }
}