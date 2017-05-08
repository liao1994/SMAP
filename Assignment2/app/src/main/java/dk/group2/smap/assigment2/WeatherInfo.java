package dk.group2.smap.assigment2;

import android.net.NetworkInfo;

import java.sql.Date;
import java.util.Calendar;

/**
 * Created by liao on 08-05-2017.
 */

public class WeatherInfo {
    private int id;
    private String icon;
    private String description;
    private double temp;
    private Calendar timeStamp;
    private String icon;
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

    public Calendar getTimeStamp() {
        return timeStamp;
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
