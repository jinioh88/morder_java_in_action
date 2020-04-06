package ch12;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static java.time.temporal.TemporalAdjusters.nextOrSame;

public class Main {

    public static void main(String[] args) {
        LocalDate date = LocalDate.now();

        int year  = date.getYear();

        // ChronoField 사용방법
        int year2 = date.get(ChronoField.YEAR);

        LocalTime time = LocalTime.of(13, 45, 20);
        int hour = time.getHour();

        // 문자열로 만드는 방법 parse이용
        LocalDate date2 = LocalDate.parse("2020-04-06");

        LocalDateTime ldt = LocalDateTime.of(2020, Month.MARCH, 4, 12, 20, 0);
        LocalDate ld = ldt.toLocalDate();
        LocalTime lt = ldt.toLocalTime();

        Duration threeMinutes = Duration.ofMinutes(3);
        Duration threeMinutes2 = Duration.of(3, ChronoUnit.MINUTES);

        Period tenDays = Period.ofDays(10);

        LocalDate date3 = date.with(nextOrSame(DayOfWeek.SUNDAY));
        LocalDate date4 = date.with(lastDayOfMonth());

        LocalDate date5 = LocalDate.parse("20200406", DateTimeFormatter.BASIC_ISO_DATE);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date6 = LocalDate.of(2020,4,6);
        String formattedDate = date6.format(formatter);
        LocalDate date7 = LocalDate.parse(formattedDate, formatter);
    }
}
