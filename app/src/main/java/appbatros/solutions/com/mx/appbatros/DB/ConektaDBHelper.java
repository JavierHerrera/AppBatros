package appbatros.solutions.com.mx.appbatros.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class ConektaDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ConectaBD";

    private static final int DATABASE_VERSION = 3;

    // Database creation sql statement
    private static final String DATABASE_CREATE =
            "create table "+ ConektaDB.CON_TABLE +
             "("+ConektaDB.CON_ID +" integer primary key," +
                    ConektaDB.CON_NOMBRE +" text not null, "+
                    ConektaDB.CON_CORREOO +" text not null, "+
                    ConektaDB.CON_TELEFONO +" text not null, "+
                    ConektaDB.CON_CONEKID +" text not null);";

    public ConektaDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    // Method is called during an upgrade of the database,
    @Override
    public void onUpgrade(SQLiteDatabase database,int oldVersion,int newVersion){
        Log.w(ConektaDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS "+ ConektaDB.CON_TABLE);
        onCreate(database);
    }
}