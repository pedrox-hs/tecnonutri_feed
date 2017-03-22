package br.com.pedrosilva.tecnonutri.util;

import android.util.TypedValue;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import br.com.pedrosilva.tecnonutri.R;

import static br.com.pedrosilva.tecnonutri.util.ContextHelper.getApplicationContext;

/**
 * Created by psilva on 3/17/17.
 */

public abstract class AppUtil {
    private static final String PATTERN_NUMBER = "#.###";
    private static final String PATTERN_KCAL = "#.#";
    private static final String PATTERN_WEIGHT = "#.#";
    private static final String PATTERN_MONEY = "#0.00";

    public static <T> T get(Object element) {
        return (T) element;
    }

    public static String formatDate(Date date) {
        return formatDate(date, getApplicationContext().getString(R.string.date_format));
    }

    private static String formatDate(Date date, String pattern) {
        DateFormat dateTimeFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        return dateTimeFormat.format(date);
    }

    private static String formatDecimal(Number number, String pattern) {
        final DecimalFormat decimalFormat = AppUtil.get(NumberFormat.getNumberInstance(Locale.getDefault()));
        decimalFormat.applyPattern(pattern);
        return decimalFormat.format(number);
    }

    public static String formatDecimal(Number number) {
        return formatDecimal(number, AppUtil.PATTERN_NUMBER);
    }

    public static String formatKcal(Number number) {
        return formatDecimal(number, AppUtil.PATTERN_WEIGHT);
    }

    public static String formatWeight(Number number) {
        return formatDecimal(number, AppUtil.PATTERN_WEIGHT);
    }

    public static int dp2Px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 500, ContextHelper.getApplicationContext().getResources().getDisplayMetrics());
    }
}
