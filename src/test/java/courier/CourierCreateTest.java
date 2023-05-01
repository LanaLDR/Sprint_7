package courier;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.courier.Courier;
import org.example.courier.CourierClient;
import org.example.courier.CourierCredentials;
import org.example.courier.CourierGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;

public class CourierCreateTest {
    private CourierClient courierClient;
    private String courierId;
    private Courier courier;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = CourierGenerator.getRandom();
    }

    @After
    public void cleanUp() {
        if (courierId != null) courierClient.delete(courierId);
    }

    @Test
    @DisplayName("Курьер создается")
    @Description("Курьер создается при корректных данных")
    public void courierCanBeCreatedTest() {
        //Вызвать наш эндпоинт
        ValidatableResponse createResponse = courierClient.create(courier);
        //Проверить, что курьер существует
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id").toString();
        createResponse.assertThat().statusCode(201).body("ok", is(true));
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    public void cannotCreateTwoIdenticalCouriersTest() {
        courierClient.create(courier);
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id").toString();
        ValidatableResponse createIdenticalCourierResponse = courierClient.create(courier);
        createIdenticalCourierResponse.assertThat().statusCode(409).body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }
}
