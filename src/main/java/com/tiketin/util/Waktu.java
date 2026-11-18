package com.tiketin.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class Waktu {
    private static final DateTimeFormatter TAMPILAN =
            DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm", Locale.of("id", "ID"));

    private Waktu() {
    }

    public static String format(LocalDateTime waktu) {
        return waktu == null ? "-" : waktu.format(TAMPILAN);
    }
}
