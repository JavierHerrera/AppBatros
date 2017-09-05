package appbatros.solutions.com.mx.appbatros.parceables;

import android.os.Parcel;
import android.os.Parcelable;

import appbatros.solutions.com.mx.appbatros.objetos.Pasajero;

public class MyParcelable implements Parcelable {
    private int mData;
    private Pasajero pasajero1;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mData);
        out.writeParcelable((Parcelable) pasajero1,flags);
    }

    public static final Parcelable.Creator<MyParcelable> CREATOR = new Parcelable.Creator<MyParcelable>() {

        public MyParcelable createFromParcel(Parcel in) {
            return new MyParcelable(in);
        }

        public MyParcelable[] newArray(int size) {
            return new MyParcelable[size];
        }
    };

    public MyParcelable(Parcel in) {
        mData = in.readInt();
        pasajero1 = in.readParcelable(ClassLoader.getSystemClassLoader());

    }
}