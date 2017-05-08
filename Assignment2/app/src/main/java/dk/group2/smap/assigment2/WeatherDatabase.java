package dk.group2.smap.assigment2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by liao on 08-05-2017.
 */

public class WeatherDatabase  extends SQLiteOpenHelper {

    private static final String CREATE_DB_STRING = "CREATE TABLE ";
    private static final String DATABASE_NAME = "dbo.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "weather_info";
    private static final String ID = "id";
    private static final String DESCRIPTION = "description";
    private static final String TEMP = "temperature";
    private static final String TIMESTAMP = "timestamp";
    private static final String ICON = "icon";
    private SQLiteDatabase myDB;

    String queryTable = CREATE_DB_STRING + TABLE_NAME +
            " (" + ID + " INTEGER PRIMARY KEY," +
            DESCRIPTION + "TEXT," +
            TEMP + "DECIMAL," +
            ICON + "TEXT," +
            TIMESTAMP + "DATETIME" + ")";




    public WeatherDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
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
        // insert row
        return myDB.insert(TABLE_NAME, null, values);
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

    public List<WeatherInfo> getWeatherInfoList() {
        if(myDB == null){
            openDB();
        }
        List<WeatherInfo> weatherInfoList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                WeatherInfo w = new WeatherInfo();
                w.setId(c.getInt((c.getColumnIndex(ID))));
                w.setDescription(c.getString(c.getColumnIndex(DESCRIPTION)));
                w.setTemp(c.getDouble(c.getColumnIndex(TEMP)));
                w.setIcon(c.getString(c.getColumnIndex(ICON)));
                String x = c.getString(c.getColumnIndex(TIMESTAMP));
                Calendar c1 = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                try {
                    c1.setTime(sdf.parse(x));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                w.setTimeStamp(c1);
                // adding to weatherInfoList list
                weatherInfoList.add(w);
            } while (c.moveToNext());
        }
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
