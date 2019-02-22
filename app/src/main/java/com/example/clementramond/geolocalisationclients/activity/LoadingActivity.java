package com.example.clementramond.geolocalisationclients.activity;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public abstract class LoadingActivity extends AppCompatActivity {

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
        } else {
            Toast.makeText(this, "Chargement en cours...", Toast.LENGTH_LONG).show();
        }
    }

    public void loadingEnd() {
        Toast.makeText(this, "Chargement terminé.", Toast.LENGTH_LONG).show();
    }

    public void setActivity(int resourceId) {
        this.activity = findViewById(resourceId);
    }

    public void setLoading(int resourceId) {
        this.loading = findViewById(resourceId);
    }
}
