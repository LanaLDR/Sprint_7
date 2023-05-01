package org.example.courier;

import org.apache.commons.lang3.RandomStringUtils;

public class CourierGenerator {

    public static Courier getRandom() {
        final String login = RandomStringUtils.randomAlphabetic(3, 10);
        final String password = RandomStringUtils.randomAlphabetic(3, 10);
        final String firstName = RandomStringUtils.randomAlphabetic(3, 10);
        return new Courier(login, password, firstName);
    }

    public static Courier getDefault() {
        return new Courier("Amogus123", "123321", "AmogusCourier");
    }
}
