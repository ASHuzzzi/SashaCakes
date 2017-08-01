package ru.lizzzi.sashacakes.data;

/**
 * Created by Liza on 25.04.2017.
 */

import android.provider.BaseColumns;

public class ItemConract {

    private ItemConract(){
    };

    public static final class ItemList implements BaseColumns{
        public final static String TABLE_NAME = "item";

        public final static String _ID = BaseColumns._ID;
        public static String COLUMN_NAME = "name";
        public final static String COLUMN_ABOUT = "about";
        public final static String COLUMN_PRICE = "price";
        public final static String COLUMN_QUANTITY = "quantity";
    }
}
