package rip.crystal.practice.api.utilities;

import lombok.experimental.UtilityClass;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@UtilityClass
public class Server {

    public String getDate(String dateFormat, String timeZone) {
        Date date = new Date();
        SimpleDateFormat dtf = new SimpleDateFormat(dateFormat);
        dtf.setTimeZone(TimeZone.getTimeZone(timeZone));
        return dtf.format(date);
    }

    public String getHour(String hourFormat, String timeZone) {
        Date date = new Date();
        SimpleDateFormat dtf = new SimpleDateFormat(hourFormat);
        dtf.setTimeZone(TimeZone.getTimeZone(timeZone));
        return dtf.format(date);
    }
}
