package courier;

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
import static org.hamcrest.CoreMatchers.notNullValue;

public class CourierLoginTest {
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
    @DisplayName("Курьер может залогиниться")
    public void courierCanBeLogin() {
        courierClient.create(courier);
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        loginResponse.assertThat().statusCode(200).body("id", notNullValue());
        courierId = loginResponse.extract().path("id").toString();
    }

    @Test
    @DisplayName("Курьер не может залогиниться без пароля")
    public void courierCannotLoginWithoutPassword() {
        courierClient.create(courier);
        courierId = courierClient.login(CourierCredentials.from(courier)).extract().path("id").toString();
        //Выше вход с корректными данными, чтобы удалить удалить созданного курьера
        courier.setPassword("");
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        loginResponse.assertThat().statusCode(400).body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Курьер не может залогиниться без логина")
    public void courierCannotLoginWithoutLogin() {
        courierClient.create(courier);
        courierId = courierClient.login(CourierCredentials.from(courier)).extract().path("id").toString();
        //Выше вход с корректными данными, чтобы удалить удалить созданного курьера
        courier.setLogin("");
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        loginResponse.assertThat().statusCode(400).body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Несуществующий курьер не может залогиниться")
    public void cannotLoginWithNonExistentCourier() {
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        loginResponse.assertThat().statusCode(404).body("message", equalTo("Учетная запись не найдена"));
    }
}
