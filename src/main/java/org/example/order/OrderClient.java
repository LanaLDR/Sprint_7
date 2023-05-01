package org.example.order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.example.RestClient;

import static io.restassured.RestAssured.*;

public class OrderClient extends RestClient {
    private static final String ORDER_PATH = "api/v1/orders/";
    private static final String CANCEL_ORDER_PATH = "api/v1/orders/cancel/";
    private static final String ACCEPT_ORDER_PATH = "/api/v1/orders/accept/";
    private static final String ORDER_TRACK_PATH = "/api/v1/orders/track";


    @Step("Создание заказа по пути api/v1/orders/")
    public ValidatableResponse create(Order order) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step("Отмена заказа по пути api/v1/orders/cancel")
    public ValidatableResponse cancel(String track) {
        return given()
                .spec(getBaseSpec())
                .when()
                .body(track)
                .put(CANCEL_ORDER_PATH)
                .then();
    }

    @Step("Получение списка заказов по пути api/v1/orders/")
    public ValidatableResponse getOrders() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(ORDER_PATH)
                .then();
    }

    @Step("Принятие заказа по пути api/v1/orders/accept/")
    public ValidatableResponse acceptOrder(String orderId, String courierId) {
        return given()
                .spec(getBaseSpec())
                .when()
                .queryParam("courierId", courierId)
                .put(ACCEPT_ORDER_PATH+orderId)
                .then();
    }

    @Step("Получение заказа через track по пути api/v1/orders/track")
    public ValidatableResponse getOrderWithTrack(String track) {
        return given()
                .spec(getBaseSpec())
                .when()
                .queryParam("t", track)
                .get(ORDER_TRACK_PATH)
                .then();
    }
}
