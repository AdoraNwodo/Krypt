package krypt.com.krypt.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by hackean on 4/28/17.
 */

public class MessageToast {

    public static void showSnackBar(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
