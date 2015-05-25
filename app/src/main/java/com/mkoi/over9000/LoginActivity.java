package com.mkoi.over9000;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;

import com.mkoi.over9000.connection.ConnectionDetector;
import com.mkoi.over9000.handler.ErrorHandler;
import com.mkoi.over9000.http.RestClient;
import com.mkoi.over9000.message.response.LoginResponse;
import com.mkoi.over9000.preferences.UserPreferences_;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;

@SuppressLint("Registered")
@EActivity(R.layout.activity_login)
public class LoginActivity extends Activity {

    public static final String LOG_TAG = "Over9000.LoginActivity";
    @ViewById(R.id.loginNick)
    EditText loginNick;

    @RestService
    RestClient restClient;

    @Bean
    ErrorHandler errorHandler;

    @Pref
    UserPreferences_ userPreferences;

    @Bean
    ConnectionDetector connectionDetector;

    @AfterInject
    void afterInject(){
        restClient.setRestErrorHandler(errorHandler);
    }

    @Click(R.id.loginBtn)
    public void loginMe(){
        String nick = loginNick.getText().toString();
        if(nick.length() != 0 && nick.length() <15){
            if(connectionDetector.isConnectedToInternet()){
                userPreferences.nick().put(nick);
                loginUser(nick);
            } else {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setMessage("Brak połączenia z Internetem");
                dialog.setPositiveButton("OK",null);
                dialog.show();
            }
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Niepoprawna długość nicku");
            dialog.setPositiveButton("OK", null);
            dialog.show();
        }
    }

    @Background
    public void loginUser(String nick) {
        LoginResponse loginResponse = restClient.userLogin(nick);
        Log.d(LOG_TAG, "Login Response: " + loginResponse.toString());
        loginResult(loginResponse);
    }

    @UiThread
    public void loginResult(LoginResponse response) {
        if(response.getError().equals("0")) {
            userPreferences.token().put(response.getToken());
            Intent intent = new Intent(LoginActivity.this, UsersActivity_.class);
            startActivity(intent);
        } else {
            AlertDialog dialog = new AlertDialog.Builder(this).setMessage("Błąd podczas " +
                    "logowania: " + response.getDescription()).show();
        }
    }
}
