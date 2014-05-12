package com.shurpo.ghsync.ghsync.fragment;

import android.accounts.*;
import android.annotation.TargetApi;
import android.app.ListFragment;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.shurpo.ghsync.ghsync.account.GitHubAccount;

import java.io.IOException;

/**
 * Created by Maksim on 08.05.2014.
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class AccountList extends ListFragment {

    private Handler handler = new Handler();
    private AccountManager accountManager;
    private ArrayAdapter<Account> listAdapter;

    OnAccountsUpdateListener accountsUpdateListener = new OnAccountsUpdateListener() {
        @Override
        public void onAccountsUpdated(Account[] accounts) {
            listAdapter.setNotifyOnChange(false);
            listAdapter.clear();
            for (Account account : accounts){
                if(TextUtils.equals(account.type, GitHubAccount.TYPE)){
                    listAdapter.add(account);
                }
            }
            listAdapter.setNotifyOnChange(true);
            listAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        accountManager = AccountManager.get(getActivity());
        listAdapter = new ArrayAdapter<Account>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1);
        setListAdapter(listAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        accountManager.addOnAccountsUpdatedListener(accountsUpdateListener, handler, true);
    }

    @Override
    public void onPause() {
        accountManager.removeOnAccountsUpdatedListener(accountsUpdateListener);
        super.onPause();
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        Account account = listAdapter.getItem(position);
        accountManager.getAuthToken(account, GitHubAccount.TOKEN_FULL_ACCESS, new Bundle(), true, new AccountManagerCallback<Bundle>() {
            @Override
            public void run(AccountManagerFuture<Bundle> future) {
                try {
                    Bundle result = future.getResult();
                    Log.d(AccountList.class.getSimpleName(), result.getString(AccountManager.KEY_AUTHTOKEN));
                } catch (AuthenticatorException e) {
                    Log.e(AccountList.class.getSimpleName(), e.getMessage(), e);
                } catch (OperationCanceledException e) {
                    Log.e(AccountList.class.getSimpleName(), e.getMessage(), e);
                } catch (IOException e) {
                    Log.e(AccountList.class.getSimpleName(), e.getMessage(), e);
                }
            }
        }, null);
    }
}
