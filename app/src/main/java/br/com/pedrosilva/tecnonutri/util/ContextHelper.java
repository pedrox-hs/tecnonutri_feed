package br.com.pedrosilva.tecnonutri.util;

import android.content.Context;

/**
 * Created by psilva on 3/17/17.
 */

public abstract class ContextHelper {

    private static Context APPLICATION_CONTEXT;

    public static void init(final Context context) {
        if (APPLICATION_CONTEXT == null) {
            APPLICATION_CONTEXT = context.getApplicationContext();
        }
    }

    public static Context getApplicationContext() {
        return APPLICATION_CONTEXT;
    }
}
