package com.example.clementramond.geolocalisationclients.activity;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

public abstract class LoadingActivity extends OptionsActivity {

    private View activity = null,
                 loading = null;

    public void loading(boolean b) {
        if (activity != null && loading != null) {
            if (b) {
                activity.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
            } else {
                activity.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
            }
        }
    }

    public void setActivity(int resourceId) {
        this.activity = findViewById(resourceId);
    }

    public void setLoading(int resourceId) {
        this.loading = findViewById(resourceId);
    }
}
