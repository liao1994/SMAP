package dk.group2.smap.assigment2;

/**
 * Created by liao on 07-05-2017.
 */


public class Global {
    public static final String CONNECT = "CONNECTIVITY";

    public static final String ICON_API_CALL = "http://openweathermap.org/img/w/";
    public static final String WEATHER_API_KEY = "75cb2ad18112ad54656e1dc26388f93c";

    public static final long CITY_ID_AARHUS = 2624652;

    public static final String WEATHER_API_CALL = "http://api.openweathermap.org/data/2.5/weather?id=" + CITY_ID_AARHUS + "&APPID=" + WEATHER_API_KEY;
    //private static final String WEATHER_API_CALL = "http://api.openweathermap.org/data/2.5/weather?q=aarhus,dk&APPID=" + WEATHER_API_KEY;

}
