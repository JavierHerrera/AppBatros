package appbatros.solutions.com.mx.appbatros.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class HistorialDB {

    private HistorialDBHelper dbHelper;

    private SQLiteDatabase database;

    public final static String PAS_TABLE="Pasajero"; // name of table

    public final static String PAS_ID="_id";
    public final static String PAS_NOMBRE ="nombre";
    public final static String PAS_TIPOPASAJERO ="tipoPasajero";
    public final static String PAS_ASIENTO ="asiento";
    public final static String PAS_IMPORTE ="importe";
    public final static String PAS_ORIGEN ="origen";
    public final static String PAS_DESTINO ="destino";
    public final static String PAS_FECHA ="fecha";
    public final static String PAS_HORA ="hora";
    public final static String PAS_TIPOPAGO ="tipopago";
    public final static String PAS_REFERENCIA ="referencia";
    public final static String PAS_STATUS ="status";


    public HistorialDB(Context context){
        dbHelper = new HistorialDBHelper(context);
        database = dbHelper.getWritableDatabase();
    }


    public long createRecords(int id, String nombre, String tipoPasajero, String asiento,
                              String importe, String origen, String destino, String fecha,
                              String hora, String tipoPago, String referencia, String status) {

        ContentValues values = new ContentValues();
        values.put(PAS_ID, id);
        values.put(PAS_NOMBRE, nombre);
        values.put(PAS_TIPOPASAJERO, tipoPasajero);
        values.put(PAS_ASIENTO, asiento);
        values.put(PAS_IMPORTE, importe);
        values.put(PAS_ORIGEN, origen);
        values.put(PAS_DESTINO, destino);
        values.put(PAS_FECHA, fecha);
        values.put(PAS_HORA, hora);
        values.put(PAS_TIPOPAGO, tipoPago);
        values.put(PAS_REFERENCIA, referencia);
        values.put(PAS_STATUS, status);

        return database.insert(PAS_TABLE, null, values);
    }

    public Cursor selectRecords() {
        String[] cols = new String[] {PAS_ID, PAS_NOMBRE, PAS_TIPOPASAJERO,PAS_ASIENTO,PAS_IMPORTE,
                PAS_ORIGEN,PAS_DESTINO,PAS_FECHA, PAS_HORA,PAS_TIPOPAGO,PAS_REFERENCIA, PAS_STATUS};
        Cursor mCursor = database.query(true, PAS_TABLE,null,null
                , null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor; // iterate to get each value.
    }

    public  void actualizarPago(String referencia, String tipoPago){

        ContentValues cv = new ContentValues();
        cv.put("tipoPago",tipoPago);
        cv.put("status"," ");//These Fields should be your String values of actual column names

        Log.d("Referencias", ""+referencia+" ");

        database.update(PAS_TABLE, cv, "referencia = "+ referencia, null );
        //myDB.update(TableName, cv, "_id="+id, null);
    }
}
