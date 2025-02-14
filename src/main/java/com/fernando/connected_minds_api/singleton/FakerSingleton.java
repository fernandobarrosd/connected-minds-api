package com.fernando.connected_minds_api.singleton;

import com.github.javafaker.Faker;

public final class FakerSingleton {
    public static Faker INSTANCE;

    private FakerSingleton() throws Exception {
        throw new Exception("FakerInstance must not be instanced");
    }

    public static Faker getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Faker();
        }
        return INSTANCE;
    }
}