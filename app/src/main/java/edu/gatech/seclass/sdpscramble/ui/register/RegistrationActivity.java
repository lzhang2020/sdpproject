package edu.gatech.seclass.sdpscramble.ui.register;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.gatech.seclass.sdpscramble.R;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        getSupportFragmentManager().beginTransaction().replace(R.id.registration_fragment_container,
                new RegistrationFragment()).commit();

    }
}