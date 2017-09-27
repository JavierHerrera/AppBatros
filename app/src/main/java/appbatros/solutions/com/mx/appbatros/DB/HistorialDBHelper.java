package appbatros.solutions.com.mx.appbatros.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class HistorialDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ReferenciaPago";

    private static final int DATABASE_VERSION = 3;

    // Database creation sql statement
    private static final String DATABASE_CREATE =
            "create table "+ HistorialDB.PAS_TABLE +
             "( "+HistorialDB.PAS_ID +" integer primary key," +
                    HistorialDB.PAS_NOMBRE+" text not null, "+
                    HistorialDB.PAS_TIPOPASAJERO+" text not null, "+
                    HistorialDB.PAS_ASIENTO+" text not null, "+
                    HistorialDB.PAS_IMPORTE+" text not null, "+
                    HistorialDB.PAS_ORIGEN+" text not null, "+
                    HistorialDB.PAS_DESTINO+" text not null, "+
                    HistorialDB.PAS_FECHA+" text not null, "+
                    HistorialDB.PAS_HORA+" text not null, "+
                    HistorialDB.PAS_TIPOPAGO+" text not null, "+
                    HistorialDB.PAS_REFERENCIA+" text not null, "+
                    HistorialDB.PAS_STATUS +" text not null);";

    public HistorialDBHelper(Context context) {
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
        Log.w(HistorialDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS "+ HistorialDB.PAS_TABLE);
        onCreate(database);
    }
}