package courier;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.courier.Courier;
import org.example.courier.CourierClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.CoreMatchers.is;

@RunWith(Parameterized.class)
public class CourierNotCreateParamTest {
    private final String login;
    private final String password;
    private CourierClient courierClient;
    private Courier courier;

    public CourierNotCreateParamTest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Parameterized.Parameters
    public static Object[][] getCourierData() {
        return new Object[][] {
                {RandomStringUtils.randomAlphabetic(3, 10), null},
                {null, RandomStringUtils.randomAlphabetic(3, 10)},
                {null, null},
        };
    }

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = new Courier(login, password);
    }

    @Test
    @DisplayName("Нелья создать курьера без обязательных полей")
    public void courierNotCreatedWithoutRequiredFieldsTest(){
        ValidatableResponse createResponse = courierClient.create(courier);
        createResponse.assertThat().statusCode(400).body("message", is("Недостаточно данных для создания учетной записи"));
    }
}
