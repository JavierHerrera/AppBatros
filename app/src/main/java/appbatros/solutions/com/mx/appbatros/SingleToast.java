package appbatros.solutions.com.mx.appbatros;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by JH on 02/09/2017.
 */

public class SingleToast {
    private static Toast mToast;

    public static void show(Context context, String text, int duration) {
        if (mToast != null) mToast.cancel();
        mToast = Toast.makeText(context, text, duration);
        mToast.show();
    }
}
