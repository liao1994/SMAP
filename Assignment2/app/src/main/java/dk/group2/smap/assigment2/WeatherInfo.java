package dk.group2.smap.assigment2;

import android.net.NetworkInfo;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by liao on 08-05-2017.
 */

public class WeatherInfo {
    private int id;
    private String icon;
    private String description;
    private double temp;
    private Calendar timeStamp;

    public WeatherInfo(){
        timeStamp = Calendar.getInstance();
    };
    public WeatherInfo(int id, String description, double temp,String icon){
        this.id = id;
        this.description = description;
        this.temp = temp;
        this.icon = icon;
        timeStamp = Calendar.getInstance();
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public String getTimeStamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(timeStamp.getTime());
    }

    public void setTimeStamp(Calendar timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
