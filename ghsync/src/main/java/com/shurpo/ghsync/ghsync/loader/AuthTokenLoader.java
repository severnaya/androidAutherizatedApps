package com.shurpo.ghsync.ghsync.loader;

import android.annotation.TargetApi;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import com.shurpo.ghsync.ghsync.GitHubApp;
import com.shurpo.ghsync.ghsync.R;
import com.shurpo.ghsync.ghsync.utils.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

/**
 * Created by Maksim on 12.05.2014.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class AuthTokenLoader extends AsyncTaskLoader<String> {

    private String obtainTokenUrl;
    private String login;
    private String password;
    private String authToken;


    public AuthTokenLoader(Context context, String login, String password) {
        super(context);
        obtainTokenUrl = context.getString(R.string.github_obtain_token_url, GitHubApp.CLIENT_ID);
        this.login = login;
        this.password = password;
    }

    @Override
    protected void onStartLoading() {
        if(TextUtils.isEmpty(authToken)){
            forceLoad();
        }else {
            deliverResult(authToken);
        }
    }

    @Override
    public void deliverResult(String data) {
        authToken = data;
        super.deliverResult(data);
    }

    @Override
    public String loadInBackground() {
        try {
            return signIn();
        } catch (IOException e) {
            Log.e(AuthTokenLoader.class.getSimpleName(), e.getMessage(), e);
        }
        return null;
    }

    public static String signIn(Context context, String login, String password){
        try {
            return new AuthTokenLoader(context, login, password).signIn();

        } catch(IOException e){
            Log.e(AuthTokenLoader.class.getSimpleName(), e.getMessage(), e) ;
        }
        return null;
    }

    private String signIn() throws IOException {
        HttpURLConnection cn = (HttpURLConnection) new URL(obtainTokenUrl).openConnection();
        cn.setRequestMethod("PUT");
        cn.addRequestProperty("Accept", "application/json");
        cn.addRequestProperty("Authorization", "Basic " + Base64.encodeToString((login + ":" + password).getBytes(), Base64.DEFAULT));
        sendBody(cn);
        return readToken(cn);
    }
    private void sendBody(HttpURLConnection cn) throws IOException {
        JSONObject body = new JSONObject();
        try {
            body.put("client_secret", GitHubApp.CLIENT_SECRET);
            body.put("scopes", new JSONArray(Arrays.asList("repo")));
            byte[] date = body.toString().getBytes();
            cn.setDoOutput(true);
            cn.setFixedLengthStreamingMode(date.length);
            cn.setRequestProperty("Content-Type", "application/json");
            OutputStream out = new BufferedOutputStream(cn.getOutputStream());
            try{
                out.write(date);
            } finally {
                IOUtils.closeQuietly(out);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private String readToken(HttpURLConnection cn) throws IOException {
        InputStream in = new BufferedInputStream(cn.getInputStream());
        try {
            JSONObject json = new JSONObject(IOUtils.toStringQuietly(in));
            if(json.has("token")){
                return json.getString("token");
            }
        }catch (JSONException e){
            Log.e(AuthTokenLoader.class.getSimpleName(), e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(in);
        }
        return null;
    }

}
