package dk.group2.smap.assigment2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dk.group2.smap.assigment2.generatedfiles.Weather;

/**
 * Created by liao on 08-05-2017.
 */

public class WeatherDatabase  extends SQLiteOpenHelper {

    private static final String CREATE_DB_STRING = "CREATE TABLE ";
    private static final String DATABASE_NAME = "weather.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NAME = "weather_info";
    private static final String ID = "id";
    private static final String DESCRIPTION = "description";
    private static final String TEMP = "temperature";
    private static final String TIMESTAMP = "time_stamp";
    private static final String ICON = "icon";
    private static final String MAIN = "main";
    private SQLiteDatabase myDB;

    private String queryTable = CREATE_DB_STRING + TABLE_NAME +
            " (" + ID + " INTEGER PRIMARY KEY, " +
            MAIN + " TEXT, " +
            DESCRIPTION + " TEXT, " +
            TEMP + " DECIMAL, " +
            ICON + " TEXT, " +
            TIMESTAMP + " DATETIME" + ")";




    public WeatherDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DBhelper.LOG", queryTable);
        db.execSQL(queryTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void openDB(){
        myDB = getWritableDatabase();
    }
    public void closeDB(){
        if (myDB != null && myDB.isOpen())
            myDB.close();
    }
    public long InsertWeatherInfo(WeatherInfo weatherInfo){
        if(myDB == null){
            openDB();
        }
        ContentValues values = new ContentValues();
        values.put(ID, weatherInfo.getId());
        values.put(DESCRIPTION, weatherInfo.getDescription());
        values.put(TEMP, weatherInfo.getTemp());
        values.put(TIMESTAMP, getDateTime());
        values.put(ICON, weatherInfo.getIcon());
        values.put(MAIN, weatherInfo.getMain());
        // insert row
        long result = myDB.insert(TABLE_NAME, null, values);
        return result;
    }

    public long updateWeatherInfo(WeatherInfo weatherInfo){
        if(myDB == null){
            openDB();
        }
        ContentValues values = new ContentValues();
        values.put(ID, weatherInfo.getId());
        values.put(DESCRIPTION, weatherInfo.getDescription());
        values.put(TEMP, weatherInfo.getTemp());
        values.put(TIMESTAMP, getDateTime());
        values.put(ICON, weatherInfo.getIcon());
        values.put(MAIN, weatherInfo.getMain());
        String WHERE = ID + " = " + weatherInfo.getId();

        // update row
        return myDB.update(TABLE_NAME, values, WHERE ,null);
    }
    //TODO consider WeatherInfo or Int
    public long delete(int id){
        if(myDB == null){
            openDB();
        }
        String WHERE = ID + " = " + id;
        // update row
        return myDB.delete(TABLE_NAME, WHERE ,null);
    }

    public ArrayList<WeatherInfo> getWeatherInfoList() {
        if(myDB == null){
            openDB();
        }
        ArrayList<WeatherInfo> weatherInfoList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + TIMESTAMP + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);
        if(c.getCount() == 0){
            Log.d("Null.Error", "nothing in db");
            return weatherInfoList;
        }
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                WeatherInfo w = new WeatherInfo(
                        c.getInt(c.getColumnIndex(ID)),
                        c.getString(c.getColumnIndex(MAIN)),
                        c.getString(c.getColumnIndex(DESCRIPTION)),
                        c.getDouble(c.getColumnIndex(TEMP)),
                        c.getString(c.getColumnIndex(ICON))
                );
                String x = c.getString(c.getColumnIndex(TIMESTAMP));
                Calendar c1 = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                try {
                    c1.setTime(sdf.parse(x));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                w.setTimeStamp(c1);

//                 adding to weatherInfoList list
                weatherInfoList.add(w);
            } while (c.moveToNext());
        }

        Log.d("LOG","weather list count:" + weatherInfoList.size());
        return weatherInfoList;
    }
    public String getDateTime() {
        Date date = new Date();
        return createDateTimeString(date);
    }

    private String createDateTimeString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(date);
    }
}
