package com.tamswallet.app.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyUtils {
    
    private static final Locale INDONESIAN_LOCALE = new Locale("id", "ID");
    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(INDONESIAN_LOCALE);
    private static final DecimalFormat decimalFormat = new DecimalFormat("#,###");
    private static final DecimalFormat shortFormat = new DecimalFormat("#.#");
    private static final DecimalFormat preciseFormat = new DecimalFormat("#,###.00");

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
    
    public static String formatCurrencyPrecise(double amount) {
        return "Rp " + preciseFormat.format(amount);
    }
    
    public static String formatCurrencyIndonesian(double amount) {
        return currencyFormat.format(amount);
    }
    
    public static String formatCurrencyCompact(double amount) {
        if (amount >= 1_000_000_000_000L) {
            return "Rp " + shortFormat.format(amount / 1_000_000_000_000.0) + "T";
        } else if (amount >= 1_000_000_000) {
            return "Rp " + shortFormat.format(amount / 1_000_000_000.0) + "M";
        } else if (amount >= 1_000_000) {
            return "Rp " + shortFormat.format(amount / 1_000_000.0) + "Jt";
        } else if (amount >= 1_000) {
            return "Rp " + shortFormat.format(amount / 1_000.0) + "rb";
        } else {
            return "Rp " + decimalFormat.format(amount);
        }
    }
    
    public static String formatToWords(double amount) {
        if (amount == 0) return "nol rupiah";
        
        long intAmount = (long) amount;
        String[] ones = {"", "satu", "dua", "tiga", "empat", "lima", "enam", "tujuh", "delapan", "sembilan",
                        "sepuluh", "sebelas", "dua belas", "tiga belas", "empat belas", "lima belas", 
                        "enam belas", "tujuh belas", "delapan belas", "sembilan belas"};
        
        String[] tens = {"", "", "dua puluh", "tiga puluh", "empat puluh", "lima puluh", 
                        "enam puluh", "tujuh puluh", "delapan puluh", "sembilan puluh"};
        
        if (intAmount < 20) {
            return ones[(int) intAmount] + " rupiah";
        } else if (intAmount < 100) {
            return tens[(int) intAmount / 10] + " " + ones[(int) intAmount % 10] + " rupiah";
        } else if (intAmount < 1000) {
            return ones[(int) intAmount / 100] + " ratus " + formatToWords(intAmount % 100).replace(" rupiah", "") + " rupiah";
        } else if (intAmount < 1000000) {
            return formatToWords(intAmount / 1000).replace(" rupiah", "") + " ribu " + formatToWords(intAmount % 1000).replace(" rupiah", "") + " rupiah";
        } else if (intAmount < 1000000000) {
            return formatToWords(intAmount / 1000000).replace(" rupiah", "") + " juta " + formatToWords(intAmount % 1000000).replace(" rupiah", "") + " rupiah";
        } else {
            return formatToWords(intAmount / 1000000000).replace(" rupiah", "") + " miliar " + formatToWords(intAmount % 1000000000).replace(" rupiah", "") + " rupiah";
        }
    }
    
    public static String getFormattedDateRange(long startDate, long endDate) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd MMM yyyy", INDONESIAN_LOCALE);
        return sdf.format(new java.util.Date(startDate)) + " - " + sdf.format(new java.util.Date(endDate));
    }
    
    public static String formatCurrencyWithTrend(double currentAmount, double previousAmount) {
        String formatted = formatCurrency(currentAmount);
        if (previousAmount == 0) return formatted;
        
        double changePercent = ((currentAmount - previousAmount) / previousAmount) * 100;
        String trendIcon = changePercent > 0 ? "↗" : changePercent < 0 ? "↘" : "→";
        
        return formatted + " " + trendIcon + " " + formatPercentage(Math.abs(changePercent));
    }
}