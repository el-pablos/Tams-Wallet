package com.tamswallet.app.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyUtils {
    
    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
    private static final DecimalFormat decimalFormat = new DecimalFormat("#,###");

    public static String formatCurrency(double amount) {
        return "Rp " + decimalFormat.format(amount);
    }

    public static String formatCurrencyWithoutSymbol(double amount) {
        return decimalFormat.format(amount);
    }

    public static double parseCurrency(String currencyString) {
        try {
            String cleanString = currencyString.replaceAll("[Rp,\\s]", "");
            return Double.parseDouble(cleanString);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}