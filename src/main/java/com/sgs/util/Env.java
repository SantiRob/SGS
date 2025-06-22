package com.sgs.util;

import java.io.InputStream;
import java.util.Properties;

public class Env {

    private static final Properties props = new Properties();

    static {
        try (InputStream input = Env.class.getClassLoader().getResourceAsStream("env.properties")) {
            if (input == null) {
                throw new RuntimeException("No se encontró env.properties en resources.");
            }
            props.load(input);
        } catch (Exception e) {
            System.err.println("Error al cargar env.properties:");
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}
