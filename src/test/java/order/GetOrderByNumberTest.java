package order;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.order.Order;
import org.example.order.OrderClient;
import org.example.order.OrderGenerator;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;

public class GetOrderByNumberTest {
    private OrderClient orderClient;
    private Order order;
    private String orderTrack;



    @Before
    public void setUp() {
        orderClient = new OrderClient();
        order = OrderGenerator.getOrderWithoutColor(new String[]{"BLACK"});
    }

    @Test
    @DisplayName("Успешное принятие заказа возвращает нужный статус")
    public void getOrderByNumberTest() {
        orderTrack = orderClient.create(order).extract().path("track").toString();
        ValidatableResponse getOrderByNumberResponse = orderClient.getOrderWithTrack(orderTrack);
        getOrderByNumberResponse.assertThat().statusCode(200).body("order", notNullValue());
        orderClient.cancel(orderTrack);
    }

    @Test
    @DisplayName("Нельзя найти заказ с пустым track")
    public void cannotGetOrderWithoutNumberTest() {
        ValidatableResponse getOrderByNumberResponse = orderClient.getOrderWithTrack("");
        getOrderByNumberResponse.assertThat().statusCode(400).body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Нельзя найти заказ с несуществующим track")
    public void cannotGetOrderWithFakeNumberTest() {
        ValidatableResponse getOrderByNumberResponse = orderClient.getOrderWithTrack("123125125");
        getOrderByNumberResponse.assertThat().statusCode(404).body("message", equalTo("Заказ не найден"));
    }
}
