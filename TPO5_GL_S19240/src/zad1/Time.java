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
    public static String passed(String[] time) {
        String s_from = time[0];
        String s_to = time[1];
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
            long days = ChronoUnit.DAYS.between(from.toLocalDate(), to.toLocalDate());
            double weeks = days / 7.0;

            StringBuilder kalendarzowo = new StringBuilder();
            boolean alreadyIs = false;
            if(period.getYears() > 0) {
                kalendarzowo.append(period.getYears()).append(period.getYears()!=1?" lata":" rok");
                alreadyIs = true;
            }
            if(period.getMonths() > 0) {
                kalendarzowo.append(alreadyIs?", ":"").append(period.getMonths()).append(period.getMonths()!=1?" miesiące":" miesiąc");
                alreadyIs = true;
            }
            if(period.getDays() > 0) {
                kalendarzowo.append(alreadyIs?", ":"").append(period.getDays()).append(period.getDays()!=1?" dni":" dzień");
            }
            return "Od " + from.getDayOfMonth() + " "
                    + from.getMonth().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("PL")) + " " +
                    from.getYear() + " (" + fromDay + ") godz. " + from.getHour()  + ":" +
                    ((String.valueOf(from.getMinute()).length()==1)?"0"+from.getMinute():from.getMinute())
                    + " do " + to.getDayOfMonth() + " "
                    + to.getMonth().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("PL")) + " " +
                    to.getYear() + " (" + toDay + ") godz. " + to.getHour() + ":" +
                    ((String.valueOf(to.getMinute()).length()==1)?"0"+to.getMinute():to.getMinute())
                    + "\n - mija: " + days + (days==1?" dzień":" dni")
                    + ", tygodni " + String.format("%.2f", weeks)
                    + "\n - godzin: " + ChronoUnit.HOURS.between(from, to)
                    + ", minut: " + ChronoUnit.MINUTES.between(from, to) +
                    "\n - kalendarzowo: " + kalendarzowo.toString();
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

            StringBuilder kalendarzowo = new StringBuilder();
            boolean alreadyIs = false;
            if(period.getYears() > 0) {
                kalendarzowo.append(period.getYears()).append(period.getYears()!=1?" lata":" rok");
                alreadyIs = true;
            }
            if(period.getMonths() > 0) {
                kalendarzowo.append(alreadyIs?", ":"").append(period.getMonths()).append(period.getMonths()!=1?" miesiące":" miesiąc");
                alreadyIs = true;
            }
            if(period.getDays() > 0) {
                kalendarzowo.append(alreadyIs?", ":"").append(period.getDays()).append(period.getDays()!=1?" dni":" dzień");
            }
            return "Od " + from.getDayOfMonth() + " "
                    + from.getMonth().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("PL")) + " " +
                    from.getYear() + " (" + fromDay + ")" + " do " + to.getDayOfMonth() + " "
                    + to.getMonth().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("PL")) + " " +
                    to.getYear() + " (" + toDay + ")\n - mija: " + days + (days==1?" dzień":" dni") + ", tygodni " + String.format("%.2f", weeks) +
                    "\n - kalendarzowo: " + kalendarzowo.toString();
        }
    }
}
