package com.example.clementramond.geolocalisationclients.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.clementramond.geolocalisationclients.R;

public abstract class OptionsActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        new MenuInflater(this).inflate(R.menu.menu_option, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent;
        switch (item.getItemId()){
            case R.id.parametres:
                //intent = new Intent(this, ParametresActivity.class);
                //startActivity(intent);
                Toast.makeText(this, "Non implémenté.", Toast.LENGTH_SHORT).show();
                break;
        }
        return (super.onOptionsItemSelected(item));
    }

}
