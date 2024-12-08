package kz.zip.taskmaster.utils;

import java.time.format.DateTimeFormatter;

public class DateTimeUtils {
    public static final DateTimeFormatter formatterDT = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
    public static final DateTimeFormatter formatterT = DateTimeFormatter.ofPattern("HH:mm");

    private DateTimeUtils(){}
}
