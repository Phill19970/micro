package com.capstone.appointmentservice.util;

import java.util.function.Consumer;
public class UpdateUtil {

    public static <T> void updateHelper(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }

}
