package com.tiketin.util;

import java.text.NumberFormat;
import java.util.Locale;

public final class Rupiah {
    private static final NumberFormat FORMAT = NumberFormat.getCurrencyInstance(Locale.of("id", "ID"));

    private Rupiah() {
    }

    public static String dari(int nominal) {
        return FORMAT.format(nominal);
    }
}
