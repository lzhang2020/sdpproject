package edu.gatech.seclass.sdpscramble.webservice.remote;

import edu.gatech.seclass.sdpscramble.BuildConfig;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExternalWebService {
    private static final String LOG_TAG = ExternalWebService.class.getSimpleName();

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // use gson converter
            .build();

    private static WebServiceInterface service = null;

    public static WebServiceInterface getInstance() {
        if (service == null) {
            service = retrofit.create(WebServiceInterface.class);
        }
        return service;
    }
}
