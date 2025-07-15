package com.tamswallet.app.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyUtils {
    
    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
    private static final DecimalFormat decimalFormat = new DecimalFormat("#,###");
    private static final DecimalFormat shortFormat = new DecimalFormat("#.#");

    public static String formatCurrency(double amount) {
        return "Rp " + decimalFormat.format(amount);
    }

    public static String formatCurrencyWithoutSymbol(double amount) {
        return decimalFormat.format(amount);
    }
    
    public static String formatCurrencyShort(double amount) {
        if (amount >= 1_000_000_000) {
            return "Rp " + shortFormat.format(amount / 1_000_000_000.0) + "M";
        } else if (amount >= 1_000_000) {
            return "Rp " + shortFormat.format(amount / 1_000_000.0) + "Jt";
        } else if (amount >= 1_000) {
            return "Rp " + shortFormat.format(amount / 1_000.0) + "rb";
        } else {
            return "Rp " + decimalFormat.format(amount);
        }
    }
    
    public static String formatCurrencyWithSign(double amount) {
        String formatted = formatCurrency(Math.abs(amount));
        return amount >= 0 ? "+" + formatted : "-" + formatted;
    }
    
    public static String formatPercentage(double percentage) {
        return String.format("%.1f%%", percentage);
    }
    
    public static String formatNumber(double number) {
        return decimalFormat.format(number);
    }

    public static double parseCurrency(String currencyString) {
        try {
            String cleanString = currencyString.replaceAll("[Rp,\\s]", "");
            return Double.parseDouble(cleanString);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    
    public static boolean isValidAmount(String amountString) {
        try {
            double amount = Double.parseDouble(amountString);
            return amount >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public static String formatAmountWithColor(double amount) {
        return amount >= 0 ? "+" + formatCurrency(amount) : formatCurrency(amount);
    }
}