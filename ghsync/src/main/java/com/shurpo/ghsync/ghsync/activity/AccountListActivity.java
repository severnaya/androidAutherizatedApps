package com.shurpo.ghsync.ghsync.activity;

import android.accounts.*;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.shurpo.ghsync.ghsync.R;
import com.shurpo.ghsync.ghsync.account.GitHubAccount;
import com.shurpo.ghsync.ghsync.fragment.AccountList;
import org.apache.http.auth.AuthenticationException;

import java.io.IOException;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class AccountListActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_single_frame);
        AccountManager am = AccountManager.get(this);
        if (am.getAccountsByType(GitHubAccount.TYPE).length == 0){
            addNewAccount(am);
        }

        /*if(savedInstanceState == null){
            getFragmentManager().beginTransaction().add(R.id.frame1, new AccountList()).commit();
        }  */
    }

    private void addNewAccount(AccountManager am){
        am.addAccount(GitHubAccount.TYPE, GitHubAccount.TOKEN_FULL_ACCESS, null, null, this, new AccountManagerCallback<Bundle>() {
            @Override
            public void run(AccountManagerFuture<Bundle> future) {
               try {
                    future.getResult();
               }catch (OperationCanceledException e){
                   e.printStackTrace();
                   AccountListActivity.this.finish();
               }catch (IOException e){
                   e.printStackTrace();
                   AccountListActivity.this.finish();
               } catch (AuthenticatorException e) {
                   e.printStackTrace();
                   AccountListActivity.this.finish();
               }
            }
        }, null);

    }

}
