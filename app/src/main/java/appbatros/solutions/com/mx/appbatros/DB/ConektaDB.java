package appbatros.solutions.com.mx.appbatros.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ConektaDB {

    private ConektaDBHelper dbHelper;

    private SQLiteDatabase database;

    public final static String CON_TABLE="Conekta"; // name of table

    public final static String CON_ID="id";
    public final static String CON_NOMBRE ="nombre";
    public final static String CON_CORREOO ="correo";
    public final static String CON_TELEFONO ="telefono";
    public final static String CON_CONEKID ="conektid";


    public ConektaDB(Context context){
        dbHelper = new ConektaDBHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public long createRecords(int id, String nombre, String correo, String telefono,
                              String conektid) {

        ContentValues values = new ContentValues();
        values.put(CON_ID, id);
        values.put(CON_NOMBRE, nombre);
        values.put(CON_CORREOO, correo);
        values.put(CON_TELEFONO, telefono);
        values.put(CON_CONEKID, conektid);


        return database.insert(CON_TABLE, null, values);
    }

    public Cursor selectRecords() {
        String[] cols = new String[] {CON_ID, CON_NOMBRE, CON_CORREOO,CON_TELEFONO, CON_CONEKID};
        Cursor mCursor = database.query(true, CON_TABLE,null,null
                , null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor; // iterate to get each value.
    }

    public  void actualizarPago(String referencia, String tipoPago){

        //EJEMPLO no esta en uso
        ContentValues cv = new ContentValues();
        cv.put("tipoPago",tipoPago);
        cv.put("status"," ");//These Fields should be your String values of actual column names

        Log.d("Referencias", ""+referencia+" ");

        database.update(CON_TABLE, cv, "referencia = "+ referencia, null );
        //myDB.update(TableName, cv, "_id="+id, null);
    }
}
