package com.shurpo.ghsync.ghsync.activity;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.shurpo.ghsync.ghsync.R;

/**
 * Created by Maksim on 08.05.2014.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class LoginForm extends Fragment {

    private EditText loginView;
    private EditText passwordView;
    private Button signInView;

    LoaderManager.LoaderCallbacks<String> lofinCallBack = new LoaderManager.LoaderCallbacks<String>() {
        @Override
        public Loader<String> onCreateLoader(int id, Bundle args) {
            return null;
        }

        @Override
        public void onLoadFinished(Loader<String> loader, String data) {

        }

        @Override
        public void onLoaderReset(Loader<String> loader) {

        }
    };

    View.OnClickListener clickListener = new View.OnClickListener() {
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void onClick(View v) {
            if (v == signInView){
                if (TextUtils.isEmpty(loginView.getText())){
                    loginView.setError(getString(R.string.login));
                }else if (TextUtils.isEmpty(passwordView.getText())){
                     passwordView.setError(getString(R.string.password));
                }else {
                    getLoaderManager().restartLoader(R.id.auth_token_loader, null, lofinCallBack);
                }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fmt_login_form, container, false);
        loginView = (EditText) view.findViewById(R.id.login);
        passwordView = (EditText) view.findViewById(R.id.password);
        signInView = (Button) view.findViewById(R.id.btn_sign_in);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        signInView.setOnClickListener(clickListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        signInView.setOnClickListener(null);
    }

}
