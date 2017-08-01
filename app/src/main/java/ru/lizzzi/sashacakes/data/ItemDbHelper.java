package ru.lizzzi.sashacakes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import ru.lizzzi.sashacakes.data.ItemConract.ItemList;

/**
 * Created by Liza on 25.04.2017.
 */

public class ItemDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = ItemDbHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "item.db";
    private static final int DATABASE_VERSION = 1;

    public ItemDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ITEM_TABLE = " CREATE TABLE " + ItemList.TABLE_NAME + " ("
                + ItemConract.ItemList._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ItemList.COLUMN_NAME + " TEXT NOT NULL, "
                + ItemList.COLUMN_ABOUT + " TEXT NOT NULL, "
                + ItemList.COLUMN_PRICE + " INTEGER NOT NULL DEFAULT 0, "
                + ItemList.COLUMN_QUANTITY + " INTEGER NOT NULL DEFAULT 0);";

        db.execSQL(SQL_CREATE_ITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Запишем в журнал
        Log.w("SQLite", "Обновляемся с версии " + oldVersion + " на версию " + newVersion);

        // Удаляем старую таблицу и создаём новую
        db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_NAME);
        // Создаём новую таблицу
        onCreate(db);
    }
}
