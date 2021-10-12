package edu.gatech.seclass.sdpscramble.ui.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

//import com.facebook.stetho.Stetho;

import edu.gatech.seclass.sdpscramble.R;
import edu.gatech.seclass.sdpscramble.webservice.sync.ScrambleSyncUtils;

public class HomeActivity extends AppCompatActivity {

    private final String LOG_TAG = HomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Log.d(LOG_TAG, "Home activity started");

        getSupportFragmentManager().beginTransaction().replace(R.id.auth_fragment_container,
                new HomeFragment()).commit();

        /*
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());
        */
        ScrambleSyncUtils.startImmediateSync(this);


        Log.d(LOG_TAG, "Home activity ended");
    }

}
