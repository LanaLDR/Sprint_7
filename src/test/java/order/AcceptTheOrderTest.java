package order;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.courier.Courier;
import org.example.courier.CourierClient;
import org.example.courier.CourierCredentials;
import org.example.courier.CourierGenerator;
import org.example.order.Order;
import org.example.order.OrderClient;
import org.example.order.OrderGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;

public class AcceptTheOrderTest {
    private OrderClient orderClient;
    private CourierClient courierClient;
    private Courier courier;
    private String courierId;
    private Order order;
    private String orderId;
    private String orderTrack;



    @Before
    public void setUp() {
        orderClient = new OrderClient();
        order = OrderGenerator.getOrderWithoutColor(new String[]{"BLACK"});
        orderTrack = orderClient.create(order).extract().path("track").toString();
        orderId = orderClient.getOrderWithTrack(orderTrack).extract().path("order.id").toString();


        courierClient = new CourierClient();
        courier = CourierGenerator.getRandom();
        courierClient.create(courier);
        courierId = courierClient.login(CourierCredentials.from(courier)).extract().path("id").toString();
    }

    @After
    public void cleanUp() {
        orderClient.cancel(orderTrack);
    }

    @Test
    @DisplayName("Успешное принятие заказа возвращает нужный статус")
    public void acceptTheOrderTest() {
        ValidatableResponse acceptOrderResponse = orderClient.acceptOrder(orderId, courierId);
        acceptOrderResponse.assertThat().statusCode(200).body("ok", is(true));
    }

    @Test
    @DisplayName("Нельзя принять заказ без id курьера")
    public void cannotAcceptTheOrderWithoutCourierIdTest() {
        ValidatableResponse acceptOrderResponse = orderClient.acceptOrder(orderId, "");
        acceptOrderResponse.assertThat().statusCode(400).body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Нельзя принять несуществующий заказ")
    public void cannotAcceptTheOrderWithFakeOrderIdTest() {
        ValidatableResponse acceptOrderResponse = orderClient.acceptOrder("123123123", courierId);
        acceptOrderResponse.assertThat().statusCode(404).body("message", equalTo("Заказа с таким id не существует"));
    }

    @Test
    @DisplayName("Нельзя принять заказ несуществующим курьером")
    public void cannotAcceptTheOrderWithFakeCourierIdTest() {
        ValidatableResponse acceptOrderResponse = orderClient.acceptOrder(orderId, "1231234");
        acceptOrderResponse.assertThat().statusCode(404).body("message", equalTo("Курьера с таким id не существует"));
    }
}
