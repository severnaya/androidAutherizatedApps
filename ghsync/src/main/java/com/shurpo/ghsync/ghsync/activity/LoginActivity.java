package com.shurpo.ghsync.ghsync.activity;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import com.shurpo.ghsync.ghsync.R;

/**
 * Created by Maksim on 08.05.2014.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class LoginActivity extends AccountAuthenticatorActivity {

    public static final String EXTRA_TOKEN_TYPE = "com.github.elegion.EXTRA_TOKEN_TYPE";

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.ac_single_frame);
        if(icicle == null){
            getFragmentManager().beginTransaction().replace(R.id.frame1, new LoginForm()).commit();
        }
    }

    public void onTokenReceived(Account account, String password, String token){
        AccountManager am = AccountManager.get(this);
        Bundle result = new Bundle();
        if(am.addAccountExplicitly(account, password, new Bundle())){
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            result.putString(AccountManager.KEY_AUTHTOKEN, token);
            am.setAuthToken(account, account.type, token);
        }else {
            result.putString(AccountManager.KEY_ERROR_MESSAGE, getString(R.string.account_already_exists));
        }
        setAccountAuthenticatorResult(result);
        setResult(RESULT_OK);
        finish();
    }


}
