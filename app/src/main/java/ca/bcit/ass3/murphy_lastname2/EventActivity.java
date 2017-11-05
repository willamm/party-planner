package ca.bcit.ass3.murphy_lastname2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);


        Toolbar toolbar = findViewById(R.id.toolbar_event_activity);
        setSupportActionBar(toolbar);

    }

}
