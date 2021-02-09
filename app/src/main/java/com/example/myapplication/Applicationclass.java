package com.example.myapplication;

import android.app.Application;

import androidx.lifecycle.ProcessLifecycleOwner;

import com.example.myapplication.chatModule.manager.BackgroundListener;
import com.example.myapplication.utils.ConstantString;
import com.quickblox.auth.session.QBSettings;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

import static com.example.myapplication.utils.ConstantString.CHAT_PORT;


public class Applicationclass extends Application {


    private static Applicationclass instance;

    //App credentials
    private static final String APPLICATION_ID = "88044";
    private static final String AUTH_KEY = "p8rP5PBvgkKPWhU";
    private static final String AUTH_SECRET = "2gjYJA56AAjLkcP";
    private static final String ACCOUNT_KEY = "fUrXud7sQr6_LxM7Gv6z";

    //Chat settings range
    private static final int MAX_PORT_VALUE = 65535;
    private static final int MIN_PORT_VALUE = 1000;
    private static final int MIN_SOCKET_TIMEOUT = 300;
    private static final int MAX_SOCKET_TIMEOUT = 100000;



//    private static final String APPLICATION_ID = "";
//    private static final String AUTH_KEY = "";
//    private static final String AUTH_SECRET = "2gjYJA56AAjLkcP";
//    private static final String ACCOUNT_KEY = "fUrXud7sQr6_LxM7Gv6z";

    @Override
    public void onCreate() {
        super.onCreate();
        set_Calligraphy();
        checkAppCredentials();
        checkChatSettings();
        initCredentials();
//        ProcessLifecycleOwner.get().getLifecycle().addObserver(new BackgroundListener());

    }



    private void checkAppCredentials() {
        if (APPLICATION_ID.isEmpty() || AUTH_KEY.isEmpty() || AUTH_SECRET.isEmpty() || ACCOUNT_KEY.isEmpty()) {
            throw new AssertionError(getString(R.string.error_qb_credentials_empty));
        }
    }


    private void checkChatSettings() {
        if (ConstantString.USER_DEFAULT_PASSWORD.isEmpty() || (ConstantString.CHAT_PORT < MIN_PORT_VALUE ||
                CHAT_PORT > MAX_PORT_VALUE)
                || (ConstantString.SOCKET_TIMEOUT < MIN_SOCKET_TIMEOUT || ConstantString.SOCKET_TIMEOUT > MAX_SOCKET_TIMEOUT)) {
            throw new AssertionError(getString(R.string.error_chat_credentails_empty));
        }
    }
    /**
     * set quickBlox credentials
     */
    private void initCredentials() {
        QBSettings.getInstance().init(getApplicationContext(), APPLICATION_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);

        // Uncomment and put your Api and Chat servers endpoints if you want to point the sample
        // against your own server.
        //
        // QBSettings.getInstance().setEndpoints("https://your_api_endpoint.com", "your_chat_endpoint", ServiceZone.PRODUCTION);
        // QBSettings.getInstance().setZone(ServiceZone.PRODUCTION);
    }


    /**
     * set font calligraphy
     */
    private void set_Calligraphy() {

        // initalize Calligraphy
//            CalligraphyConfig.(
//                    new CalligraphyConfig.Builder()
//                            .setDefaultFontPath("fonts/librereg.ttf")
//                            .setFontAttrId(R.attr.fontPath)
//                            .build()
//            );

        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()

                                .setDefaultFontPath("fonts/OpenSans-Semibold.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());
    }

//    public static Applicationclass getInstance() {
//        return instance;
//    }

}
