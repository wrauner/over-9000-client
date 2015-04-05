package com.mkoi.over9000;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.mkoi.over9000.http.RestClient;
import com.mkoi.over9000.message.response.RegisterResponse;
import com.mkoi.over9000.model.User;
import com.mkoi.over9000.preferences.UserPreferences_;
import com.mkoi.over9000.secure.PasswordUtil;
import com.mkoi.over9000.socket.SocketConnection;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.security.NoSuchAlgorithmException;

/**
 * Created by Bartek on 2015-03-18.
 */
@EActivity(R.layout.activity_register)
public class RegisterActivity extends Activity {
    public static final String LOG_TAG = "Over9000.RegisterActivity";

    @ViewById(R.id.registerFirstName)
    EditText registerFirstName;

    @ViewById(R.id.registerLastName)
    EditText registerLastName;

    @ViewById(R.id.registerNick)
    EditText registerNick;

    @ViewById(R.id.registerEmail)
    EditText registerEmail;

    @ViewById(R.id.registerPassword)
    EditText registerPassword;

    @ViewById(R.id.registerRepeatPswd)
    EditText registerRepeatPswd;

    @Pref
    UserPreferences_ userPreferences;

    @Bean
    SocketConnection connection;

    @RestService
    RestClient restClient;


    @Click(R.id.registerBtn)
    public void addNewAccount(View view) {
        if (validateUser()) {
            registerAccount(getUser());
        }
    }

    @Background
    public void registerAccount(User user) {
        RegisterResponse response = restClient.userRegister(user);
        Log.d(LOG_TAG, "Register Response:" + response.toString());
        registerResult(response);
    }

    @UiThread
    public void registerResult(RegisterResponse response) {
        if (response.getError().equals("0")) {
            Log.d(LOG_TAG, "User registered");
            Intent intent = new Intent(RegisterActivity.this, LoginActivity_.class);
            startActivity(intent);
        } else {
            AlertDialog dialog = new AlertDialog.Builder(this).setMessage("Błąd podczas " +
                    "rejestracji: " + response.getDescription()).show();
        }
    }

    private boolean validateUser() {
        if (registerFirstName.getText().toString().trim().equals("")){
            registerFirstName.setError("Podaj imię");
            return false;
        }
        if (registerLastName.getText().toString().trim().equals("")){
            registerLastName.setError("Podaj nazwisko");
            return false;
        }
        if (registerNick.getText().toString().trim().equals("")) {
            registerNick.setError("Podaj nick");
            return false;
        }
        String email = registerEmail.getText().toString().trim();

        boolean validate = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        if (!validate){
            registerEmail.setError("Błędny format maila");
            return false;
        }
        if (email.equals("")) {
            registerEmail.setError("Podaj email");
            return false;
        }

        String password = registerPassword.getText().toString().trim();
        if (password.equals("")) {
            registerPassword.setError("Podaj hasło");
            return false;
        } else {
            if (password.length() < 6){
                registerPassword.setError("Za krótkie hasło");
            }
        }
        String cpassword = registerRepeatPswd.getText().toString().trim();
        if (cpassword.equals("")) {
            registerRepeatPswd.setError("Powtórz hasło");
            return false;
        } else {
            if (!cpassword.equals(password)){
                registerPassword.setText("");
                registerRepeatPswd.setText("");
                registerRepeatPswd.setError("Podałeś dwa różne hasła!");
                return false;
            }
        }
        return true;
    }

    private User getUser() {
        User user = new User();
        try {
            user.setName(registerFirstName.getText().toString().trim());
            user.setLastname(registerLastName.getText().toString().trim());
            user.setNick(registerNick.getText().toString().trim());
            user.setEmail(registerEmail.getText().toString().trim());
            String password = registerPassword.getText().toString().trim();
            byte[] salt = PasswordUtil.generateSalt();
            String hash = PasswordUtil.getHashWithSalt(password, salt);
            user.setPassword(hash);
            userPreferences.salt().put(Base64.encodeToString(salt, Base64.DEFAULT));
            userPreferences.email().put(user.getEmail());
            userPreferences.nick().put(user.getNick());
        } catch (NoSuchAlgorithmException e) {
            Log.e(LOG_TAG, "Error while calculating hash ", e);
        }
        return user;
    }
}
