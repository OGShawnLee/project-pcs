package org.example.business;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Util {
  public static String formatDateTimeToSpanish(LocalDateTime dateTime) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy, HH:mm", Locale.forLanguageTag("es-ES"));
    return dateTime.format(formatter);
  }
}