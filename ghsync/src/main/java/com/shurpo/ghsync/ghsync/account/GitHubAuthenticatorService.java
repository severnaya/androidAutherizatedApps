package com.shurpo.ghsync.ghsync.account;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Maksim on 12.05.2014.
 */
public class GitHubAuthenticatorService extends Service {

    GitHubAuthenticator authenticator;

    @Override
    public void onCreate() {
        super.onCreate();
        authenticator = new GitHubAuthenticator(getApplicationContext());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return authenticator.getIBinder();
    }
}
