package com.mkoi.over9000;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.mkoi.over9000.http.RestClient;
import com.mkoi.over9000.message.LoginMessage;
import com.mkoi.over9000.message.response.LoginResponse;
import com.mkoi.over9000.preferences.UserPreferences_;
import com.mkoi.over9000.secure.PasswordUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.security.NoSuchAlgorithmException;

@EActivity(R.layout.activity_login)
public class LoginActivity extends Activity {

    public static final String LOG_TAG = "Over9000.LoginActivity";
    @ViewById(R.id.loginEmail)
    EditText loginEmail;

    @ViewById(R.id.loginPswd)
    EditText loginPswd;

    @RestService
    RestClient restClient;

    @Pref
    UserPreferences_ userPreferences;

    @Click(R.id.registerBtn)
    public void goToRegister(View view){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity_.class);
        startActivity(intent);
    }

    @Click(R.id.loginBtn)
    @Background
    public void loginMe(){
        if(validateInput()){
            loginUser(getLoginMessage());
        }
    }

    @Background
    public void loginUser(LoginMessage message) {
        LoginResponse loginResponse = restClient.userLogin(message);
        Log.d(LOG_TAG, "Login Response: " + loginResponse.toString());
        loginResult(loginResponse);
    }

    @UiThread
    public void loginResult(LoginResponse response) {
        if(response.getError().equals("0")) {
            userPreferences.token().put(response.getToken());
            //TODO setup socket and start new activity
            Intent intent = new Intent(LoginActivity.this, ChatActivity_.class);
            startActivity(intent);
        } else {
            AlertDialog dialog = new AlertDialog.Builder(this).setMessage("Błąd podczas " +
                    "logowania: " + response.getDescription()).show();
        }
    }

    public boolean validateInput(){
        String email = loginEmail.getText().toString().trim();
        String password = loginPswd.getText().toString().trim();

        if(email.equals("")){
            loginEmail.setError("Wpisz adres mailowy");
            return false;
        } else {
            boolean validate = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
            if (!validate){
                loginEmail.setError("Błędny format maila");
                return false;
            }
        }

        if(password.equals("")){
            loginPswd.setError("Podaj haslo");
            return false;
        }
        return true;
    }

    private LoginMessage getLoginMessage(){
        LoginMessage loginMessage = new LoginMessage();
        try {
            loginMessage.setEmail(loginEmail.getText().toString().trim());
            String password = loginPswd.getText().toString().trim();
            String salt = userPreferences.salt().get();
            byte[] pswdSalt = Base64.decode(salt, Base64.DEFAULT);
            String hash = PasswordUtil.getHashWithSalt(password, pswdSalt);
            loginMessage.setPassword(hash);
        } catch (NoSuchAlgorithmException e) {
            Log.e(LOG_TAG, "Error while calculating hash", e);
        }
        return loginMessage;
    }

    @AfterViews
    public void updateEmail() {
        loginEmail.setText(userPreferences.email().getOr(""));
    }

}
