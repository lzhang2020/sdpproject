package edu.gatech.seclass.sdpscramble.ui.create;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.gatech.seclass.sdpscramble.R;

public class CreateScrambleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_scramble);

        getSupportFragmentManager().beginTransaction().replace(R.id.create_fragment_container,
                new CreateScrambleFragment()).commit();
    }
}