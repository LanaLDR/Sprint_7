package org.example.courier;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.example.RestClient;

import static io.restassured.RestAssured.*;

public class CourierClient extends RestClient {
    private static final String COURIER_PATH = "api/v1/courier/";
    private static final String LOGIN_PATH = "api/v1/courier/login/";
    private static final String DELETE_PATH = "api/v1/courier/";

    @Step("Создание курьер по пути api/v1/courier/")
    public ValidatableResponse create(Courier courier) {
        return given()
                .spec(getBaseSpec())
                .body(courier)
                .when()
                .post(COURIER_PATH)
                .then();
    }
    @Step("Авторизация курьера по пути api/v1/courier/login/")
    public ValidatableResponse login(CourierCredentials credentials) {
        return given()
                .spec(getBaseSpec())
                .when()
                .body(credentials)
                .post(LOGIN_PATH)
                .then();
    }

    @Step("Удаление курьера по пути api/v1/courier/")
    public ValidatableResponse delete(String id) {
        return given()
                .spec(getBaseSpec())
                .when()
                .delete(DELETE_PATH + id)
                .then();
    }
}
