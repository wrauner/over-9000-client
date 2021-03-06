package com.mkoi.over9000;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;

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

/**
 * Logowanie użytkownika
 */
@SuppressLint("Registered")
@EActivity(R.layout.activity_login)
public class LoginActivity extends Activity {

    public static final String LOG_TAG = "Over9000.LoginActivity";

    @ViewById(R.id.txtOne)
    EditText loginNick;

    @RestService
    RestClient restClient;

    @Bean
    ErrorHandler errorHandler;

    @Pref
    UserPreferences_ userPreferences;

    /**
     * Ustawienie handlera do błędów z połączeniem
     */
    @AfterInject
    void afterInject(){
        restClient.setRestErrorHandler(errorHandler);
    }

    /**
     * Loguje użytkownika
     */
    @Click(R.id.loginBtn)
    public void loginMe(){
        String nick = loginNick.getText().toString();
        if (nick.length() != 0 && nick.length() < 15) {
            userPreferences.nick().put(nick);
            loginUser(nick);
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Niepoprawna długość nicku").setPositiveButton("OK", null).setIcon(android.R.drawable.ic_dialog_info);
            dialog.show();
        }
    }

    /**
     * Przesłanie nicku użytkownika do serwera,
     * ze względu na połączenie z Internetem odbywa się w osobnym wątku
     * @param nick nick użytkownika
     */
    @Background
    public void loginUser(String nick) {
        LoginResponse loginResponse = restClient.userLogin(nick);
        if(loginResponse!=null) {
            Log.d(LOG_TAG, "Login Response: " + loginResponse.toString());
            loginResult(loginResponse);
        }
    }

    /**
     * Odpowiedź serwera na zalogowanie, jeżeli jest ok to pojawia się token JWT
     * @param response odpowiedź serwera
     */
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
