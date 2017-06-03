package dk.group2.smap.assigment2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import dk.group2.smap.assigment2.generatedfiles.Weather;

import static android.os.Build.ID;



// I assume you had DAB..
public class WeatherDatabase  extends SQLiteOpenHelper {

    private static final String TAG = "LOG/" + WeatherDatabase.class.getName();
    private static final String CREATE_TABLE = "CREATE TABLE ";

    private static final String DATABASE_NAME = "weather.db";
    private static final String TABLE_NAME_ICONS = "icon_path";
    private static final String TABLE_NAME_WEATHER = "weather_info";

    private static final int DATABASE_VERSION = 1;

    // weather table
    private static final String WEATHER_ID = "id";
    private static final String WEATHER_DESCRIPTION = "description";
    private static final String WEATHER_TEMP = "temperature";
    private static final String WEATHER_TIMESTAMP = "time_stamp";
    private static final String WEATHER_ICON = "icon";
    private static final String WEATHER_MAIN = "main";

    // icon table
    private static final String ICON_ID = "icon";
    private static final String ICON_PATH = "path";

    private String queryWeatherTable = CREATE_TABLE + TABLE_NAME_WEATHER +
            " (" + WEATHER_ID + " STRING PRIMARY KEY, " +
            WEATHER_MAIN + " TEXT, " +
            WEATHER_DESCRIPTION + " TEXT, " +
            WEATHER_TEMP + " DECIMAL, " +
            WEATHER_ICON + " TEXT, " +
            WEATHER_TIMESTAMP + " DATETIME" + ")";

    private String queryIconTable = CREATE_TABLE + TABLE_NAME_ICONS +
            " (" + ICON_ID + " STRING PRIMARY KEY, " +
            ICON_PATH + " TEXT" +")";
    private Context context;


    public WeatherDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG,"onCreate");
        db.execSQL(queryWeatherTable);
        db.execSQL(queryIconTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_WEATHER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ICONS);

        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public long insertWeatherInfo(WeatherInfo weatherInfo){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WEATHER_ID, String.valueOf(weatherInfo.getId()));
        values.put(WEATHER_DESCRIPTION, weatherInfo.getDescription());
        values.put(WEATHER_TEMP, weatherInfo.getTemp());
        values.put(WEATHER_TIMESTAMP, getDateTime());
        values.put(WEATHER_ICON, weatherInfo.getIcon());
        values.put(WEATHER_MAIN, weatherInfo.getMain());
        // insert row
        long result = db.insert(TABLE_NAME_WEATHER, null, values);
        db.close();
        return result;
    }

    public long updateWeatherInfo(WeatherInfo weatherInfo){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(WEATHER_ID, String.valueOf(weatherInfo.getId()));
        values.put(WEATHER_DESCRIPTION, weatherInfo.getDescription());
        values.put(WEATHER_TEMP, weatherInfo.getTemp());
        values.put(WEATHER_TIMESTAMP, getDateTime());
        values.put(WEATHER_ICON, weatherInfo.getIcon());
        values.put(WEATHER_MAIN, weatherInfo.getMain());
        String WHERE = WEATHER_ID + " = " + weatherInfo.getId();
        // update row
        long result = db.update(TABLE_NAME_WEATHER, values, WHERE ,null);
        db.close();
        return result;
    }
//    public long delete(int id){
////        if(myDB == null){
////            openDB();
////        }
////        String WHERE = WEATHER_ID + " = " + id;
////        // update row
////        return myDB.delete(TABLE_NAME_WEATHER, WHERE ,null);
//    }

    public ArrayList<WeatherInfo> getWeatherInfoList() {
        ArrayList<WeatherInfo> weatherInfoList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_WEATHER +" WHERE "+ WEATHER_TIMESTAMP +" >= datetime('now','-1 day')"+ " ORDER BY " + WEATHER_TIMESTAMP + " DESC";
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
                        UUID.fromString(c.getString(c.getColumnIndex(WEATHER_ID))),
                        c.getString(c.getColumnIndex(WEATHER_MAIN)),
                        c.getString(c.getColumnIndex(WEATHER_DESCRIPTION)),
                        c.getDouble(c.getColumnIndex(WEATHER_TEMP)),
                        c.getString(c.getColumnIndex(WEATHER_ICON))
                );
                String x = c.getString(c.getColumnIndex(WEATHER_TIMESTAMP));
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
        db.close();
        c.close();
        Log.d(TAG," weather list count:" + weatherInfoList.size());
        return weatherInfoList;
    }
    public String getDateTime() {
        Date date = new Date();
        return createDateTimeString(date);
    }

    public int insertIcon(String iconId, Bitmap icon) {
        // write to file, save filename then into db
        File internalStorage = context.getDir("WeatherIcons", Context.MODE_PRIVATE);
        File iconFilePath = new File(internalStorage, iconId + ".png");
        String iconPath = iconFilePath.toString();

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(iconFilePath);
            icon.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();


        }
        catch (Exception ex) {
            Log.d(TAG, "Problem updating", ex);
        }

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ICON_ID, iconId);
        values.put(ICON_PATH, iconPath);
        long result =  db.insert(TABLE_NAME_ICONS, null, values );
        db.close();
        return (int)result;
    }
    public boolean iconIsNull(String iconId) {
        String iconPath = getIconPath(iconId);
        if (iconPath == null || iconPath.length() == 0)
            return true;
        return false;
    }
    public Bitmap getIcon(String iconId) {
        String iconPath = getIconPath(iconId);
        if (iconPath == null || iconPath.length() == 0)
            return (null);

        return BitmapFactory.decodeFile(iconPath);
    }


    private String getIconPath(String iconId) {
        // Gets the database in the current database helper in read-only mode
        String result = null;

        String selectQuery = "SELECT * FROM " + TABLE_NAME_ICONS + " WHERE " + ICON_ID + " = ?";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery(selectQuery, new String[]{iconId});

        c.getCount();
        if (c.getCount() == 1) {
            c.moveToFirst();
            result = c.getString(c.getColumnIndex(ICON_PATH));
        }
        c.close();
        db.close();
        return result;

    }
    public void deleteIcon(String iconId) {
        String iconPath = getIconPath(iconId);
        if (iconPath != null && iconPath.length() != 0) {
            File icon = new File(iconPath);
            icon.delete();
        }
        SQLiteDatabase db = getWritableDatabase();

        db.delete(TABLE_NAME_ICONS,
                ID + " = ?",
                new String[]{iconId});
        db.close();
    }
    private String createDateTimeString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(date);
    }
}
