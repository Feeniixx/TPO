/**
 *
 *  @author Golunko Lizaveta S19240
 *
 */

package zad1;


import java.time.*;
import java.time.format.*;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class Time {
    public static String passed(String s_from, String s_to) {
        String retString="";
        String dpatt = "d MMMM yyyy (EEEE)";
        String tpatt = "d MMMM yyyy (EEEE) 'godz.' HH:mm";
        if(s_from.contains("T") || s_to.contains("T")) {
            ZonedDateTime from, to;


            try {
                from = ZonedDateTime.of(LocalDateTime.parse(s_from), ZoneId.systemDefault());
                to = ZonedDateTime.of(LocalDateTime.parse(s_to), ZoneId.systemDefault());
            } catch (DateTimeParseException e) {
                return "*** java.time.format.DateTimeParseException: " + e.getMessage();
            }
            Period period = Period.between(from.toLocalDate(), to.toLocalDate());
            String fromDay = from.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("PL"));
            String toDay = to.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("PL"));
            long dni = ChronoUnit.DAYS.between(from.toLocalDate(), to.toLocalDate());
            String years="";
            String months="";
            String days="";


            StringBuilder stringBil = new StringBuilder();
            boolean in = false;

            if(period.getYears() > 0) {
                stringBil.append(period.getYears()).append(period.getYears()!=1?" lata":" rok");
                in = true;
            }
            if(period.getMonths() > 0) {
                stringBil.append(in?", ":"").append(period.getMonths()).append(period.getMonths()!=1?" miesiące":" miesiąc");
                in = true;
            }
            if(period.getDays() > 0) {
                stringBil.append(in?", ":"").append(period.getDays()).append(period.getDays()!=1?" dni":" dzień");
            }
            double tydzien = dni / 7.0;
            return "Od " + from.getDayOfMonth() + " "
                    + from.getMonth().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("PL")) + " " +
                    from.getYear() + " (" + fromDay + ") godz. " + from.getHour()  + ":" +
                    ((String.valueOf(from.getMinute()).length()==1)?"0"+from.getMinute():from.getMinute())
                    + " do " + to.getDayOfMonth() + " "
                    + to.getMonth().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("PL")) + " " +
                    to.getYear() + " (" + toDay + ") godz. " + to.getHour() + ":" +
                    ((String.valueOf(to.getMinute()).length()==1)?"0"+to.getMinute():to.getMinute())
                    + "\n - mija: " + dni + (dni==1?" dzień":" dni")
                    + ", tygodni " + String.format("%.2f", tydzien)
                    + "\n - godzin: " + ChronoUnit.HOURS.between(from, to)
                    + ", minut: " + ChronoUnit.MINUTES.between(from, to) +
                    "\n - stringBil: " + stringBil.toString();
        } else {
            LocalDate from, to;
            try {
                from = LocalDate.parse(s_from);
                to = LocalDate.parse(s_to);
            } catch (DateTimeParseException e) {
                return "*** java.time.format.DateTimeParseException: " + e.getMessage();
            }
            Period period = Period.between(from, to);
            String fromDay = from.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("PL"));
            String toDay = to.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("PL"));
            long days = ChronoUnit.DAYS.between(from, to);
            double weeks = days / 7.0;

            StringBuilder stringBuilder = new StringBuilder();
            boolean in = true;
            if(period.getYears() > 0) {
                stringBuilder.append(period.getYears()).append(period.getYears()==1?" rok":" lata");
                in = false;
            }
            if(period.getMonths() > 0) {
                stringBuilder.append(in?", ":"").append(period.getMonths()).append(period.getMonths()==1?" miesiąc":" miesiące");
                in = false;
            }
            if(period.getDays() > 0) {
                stringBuilder.append(in?", ":"").append(period.getDays()).append(period.getDays()==1?" dzien":" dni");
            }
            return "Od " + from.getDayOfMonth() + " "
                    + from.getMonth().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("PL")) + " " +
                    from.getYear() + " (" + fromDay + ")" + " do " + to.getDayOfMonth() + " "
                    + to.getMonth().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("PL")) + " " +
                    to.getYear() + " (" + toDay + ")" +
                    "\n - mija: " + days + (days!=1?" dni":" dzien") + ", tygodni " + String.format("%.2f", weeks) +
                    "\n - stringBuilder: " + stringBuilder.toString();
        }
    }
}
