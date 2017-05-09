package dk.group2.smap.assigment2;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by liao on 09-05-2017.
 */

public class IconDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "icon.db";
    private static final String TABLE_NAME = "icon_path";
    private static final String ID = "icon";
    private static final String PATH = "path";
    private static final int DATABASE_VERSION = 1;
    private Context context;

    public IconDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ( " + ID + " STRING PRIMARY KEY, " + PATH + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF EXIST " + TABLE_NAME);
    }

    public int updateIcon(String iconId, Bitmap icon) {
        // Saves the new picture to the internal storage with the unique identifier of the report as
        // the name. That way, there will never be two report pictures with the same name.
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
            Log.d("DATABASE", "Problem updating", ex);
        }

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID, iconId);
        values.put(PATH, iconPath);
        return db.update(TABLE_NAME, values, ID + " = ?",
                new String[] {iconId});
    }

    public boolean IconIsNull(String iconId) {
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
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID + " = '" + iconId +"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);
        c.getCount();
        if (c.getCount() != 0) {
            c.moveToFirst();
            return c.getString(c.getColumnIndex(PATH));
        }
        c.close();
        return null;

    }
    public void deleteIcon(String iconId) {
        // Remove picture for report from internal storage
        String iconPath = getIconPath(iconId);
        if (iconPath != null && iconPath.length() != 0) {
            File icon = new File(iconPath);
            icon.delete();
        }

        // Remove the report from the database
        SQLiteDatabase db = getWritableDatabase();

        db.delete(TABLE_NAME,
                ID + " = ?",
                new String[]{iconId});
    }
}

