package ru.drsk.httptest2.activity.app;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by sergei on 11.02.2016.
 */
public class OpenFileDialog extends AlertDialog.Builder {
    public OpenFileDialog(Context context) {
        super(context);
        setPositiveButton("Ok", null)
                .setNegativeButton("Отмена", null);
    }
}
