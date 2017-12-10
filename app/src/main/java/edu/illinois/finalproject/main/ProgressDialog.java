package edu.illinois.finalproject.main;

import android.content.Context;

import edu.illinois.finalproject.R;

/***
 * Created by Brandon on 12/10/17.
 */

public class ProgressDialog {

    private static android.app.ProgressDialog dialog;

    public static void show(Context context, String message) {
        dialog = new android.app.ProgressDialog(context);
        dialog = new android.app.ProgressDialog(context, R.style.uploadDialog);
        dialog.setIndeterminate(true);
        dialog.show();
        dialog.setMessage(message);
    }

    public static void hide() {
        if (dialog != null) {
            dialog.hide();
            dialog = null;
        }
    }
}
