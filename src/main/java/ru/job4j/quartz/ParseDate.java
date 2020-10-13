package ru.job4j.quartz;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ParseDate {

    public Date parseDate(String dateIn) {
        Locale locale = new Locale("ru", "RU");
        DateFormatSymbols dfs = new DateFormatSymbols(locale);
        Date date = null;
        String[] months = {
                "января", "февраля", "марта", "апреля", "мая", "июня",
                "июля", "августа", "сентября", "октября", "ноября", "декабря"};
        String[] shortMonths = {
                "янв", "фев", "мар", "апр", "май", "июн",
                "июл", "авг", "сен", "окт", "ноя", "дек"};
        dfs.setMonths(months);
        dfs.setShortMonths(shortMonths);
        SimpleDateFormat formatterIn = new SimpleDateFormat("dd MMM yy, HH:mm", dfs);
        try {
            if (dateIn.contains("сегодня")) {
                date = new Date(System.currentTimeMillis());
            } else if (dateIn.contains("вчера")) {
                date = new Date(System.currentTimeMillis() - 86400000);
            } else {
                date = formatterIn.parse(dateIn);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
