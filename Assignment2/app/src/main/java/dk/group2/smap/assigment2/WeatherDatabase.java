package dk.group2.smap.assigment2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by liao on 08-05-2017.
 */

public class WeatherDatabase  extends SQLiteOpenHelper {

    private static final String CREATE_DB_STRING = "CREATE TABLE *";
    private static final String DATABASE_NAME = "dbo.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "weather_info";


    public WeatherDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
