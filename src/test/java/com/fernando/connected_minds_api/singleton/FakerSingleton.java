package com.fernando.connected_minds_api.singleton;

import com.github.javafaker.Faker;

public class FakerSingleton {
    public static Faker INSTANCE = null;

    public static Faker getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Faker();
        }
        return INSTANCE;
    }
}