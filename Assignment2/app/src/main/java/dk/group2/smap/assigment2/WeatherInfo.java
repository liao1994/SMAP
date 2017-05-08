package dk.group2.smap.assigment2;

import java.sql.Date;
import java.util.Calendar;

/**
 * Created by liao on 08-05-2017.
 */

public class WeatherInfo {
    int Id;
    String Description;
    double Tempe;
    Calendar TimeStamp;
    WeatherInfo(){
        TimeStamp = Calendar.getInstance();
    };


}
