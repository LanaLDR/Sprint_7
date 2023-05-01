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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class DeleteCourierTest {
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
    @DisplayName("Можно удалить курьера, запрос вернет true")
    public void canDeleteAnExistingCourierTest() {
        courierClient.create(courier);
        courierId = courierClient.login(CourierCredentials.from(courier)).extract().path("id").toString();
        ValidatableResponse deleteCourierResponse = courierClient.delete(courierId);
        deleteCourierResponse.assertThat().statusCode(200).body("ok", is(true));
    }

    @Test
    @DisplayName("Запрос на удаление несуществующего id вернет ошибку")
    public void cannotDeleteCourierWithFakeIdTest() {
        ValidatableResponse deleteCourierResponse = courierClient.delete("20011999");
        deleteCourierResponse.assertThat().statusCode(404).body("message", equalTo("Курьера с таким id нет."));
    }
}
