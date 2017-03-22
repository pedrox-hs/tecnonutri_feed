package br.com.pedrosilva.tecnonutri.presentation.ui.listeners;

import android.os.SystemClock;
import android.view.View;

/**
 * Created by psilva on 3/20/17.
 */
public abstract class SingleOnClickListener implements View.OnClickListener {
    private static long lastClickTime = 0;

    @Override
    public void onClick(View view) {
        if (SystemClock.elapsedRealtime() - lastClickTime < 500) {
            return;
        }
        lastClickTime = SystemClock.elapsedRealtime();
        onItemClick(view);
    }

    public abstract void onItemClick(View view);
}
