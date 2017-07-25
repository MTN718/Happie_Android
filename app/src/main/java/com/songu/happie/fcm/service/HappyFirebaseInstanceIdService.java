package com.songu.happie.fcm.service;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.songu.happie.util.Utility;

/**
 * Created by root on 23/7/17.
 */

public class HappyFirebaseInstanceIdService extends FirebaseInstanceIdService {


    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Utility.setStringPreferences(this, "fcmreftoken", refreshedToken);

        // sending reg id to your server
        sendRegistrationToServer(refreshedToken);


    }

    private void sendRegistrationToServer(final String refreshedToken) {
    }
}
