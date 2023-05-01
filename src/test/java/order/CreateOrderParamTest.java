package order;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.order.Order;
import org.example.order.OrderClient;
import org.example.order.OrderGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderParamTest {
    private OrderClient orderClient;
    private String track;
    private final String[] color;
    private Order order;

    public CreateOrderParamTest(String[] color){
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getOrderColor() {
        return new Object[][]{
                {null},
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{"BLACK", "GREY"}},
        };
    }

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        order = OrderGenerator.getOrderWithoutColor(color);
    }

    @After
    public void cleanUp() {
        orderClient.cancel(track);
    }
    @Test
    @DisplayName("Можно создать заказ")
    public void createOrderTest() {
        ValidatableResponse createOrderResponse = orderClient.create(order);
        createOrderResponse.assertThat().statusCode(201).body("track", notNullValue());
        track = createOrderResponse.extract().path("track").toString();
    }
}
