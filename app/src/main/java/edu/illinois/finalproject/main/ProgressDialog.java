package edu.illinois.finalproject.main;

import android.content.Context;

import edu.illinois.finalproject.R;

/**
 * Created by Brandon on 12/10/17.
 * This is static class that controls the showing and hiding of ProgressDialogs. This is primarily
 * used when the user is logging in, uploading a picture, or signing out.
 */

public class ProgressDialog {

    private static android.app.ProgressDialog dialog;

    /**
     * Shows a ProgressDialog with a specific message.
     *
     * @param context the context to display the dialog onto
     * @param message the message to display
     */
    public static void show(Context context, String message) {
        dialog = new android.app.ProgressDialog(context, R.style.uploadDialog);
        dialog.setIndeterminate(true);
        dialog.show();
        dialog.setMessage(message);
    }

    /**
     * This method hides the dialog so that another activity can use this class.
     */
    public static void hide() {
        if (dialog != null) {
            dialog.hide();
            dialog = null;
        }
    }
}
