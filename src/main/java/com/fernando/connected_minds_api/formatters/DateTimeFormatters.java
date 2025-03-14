package com.fernando.connected_minds_api.formatters;

import java.time.format.DateTimeFormatter;

public final class DateTimeFormatters {
    public static final DateTimeFormatter LOCAL_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static final DateTimeFormatter LOCAL_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");


    private DateTimeFormatters() throws Exception {
        throw new Exception("DateTimeFormatters must not be instanced");
    }

}