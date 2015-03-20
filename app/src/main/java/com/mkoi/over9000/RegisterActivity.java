package com.mkoi.over9000;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Bartek on 2015-03-18.
 */
@EActivity(R.layout.activity_register)
public class RegisterActivity extends Activity {

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

    @Click(R.id.registerBtn)
    public void addNewAccount(View view){
        if (validateInputFields()) {
            //TODO register
        }
    }

    private boolean validateInputFields() {
        boolean result = true;

        if (registerFirstName.getText().toString().trim().equals("")){
            registerFirstName.setError("Podaj imię");
            result = false;
        }
        if (registerLastName.getText().toString().trim().equals("")){
            registerLastName.setError("Podaj nazwisko");
            result = false;
        }
        if (registerNick.getText().toString().trim().equals("")) {
            registerNick.setError("Podaj nick");
            result = false;
        }
        String email = registerEmail.getText().toString().trim();

        boolean validate = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        if (!validate){
            registerEmail.setError("Błędny format maila");
            result = false;
        }
        if (email.equals("")) {
            registerEmail.setError("Podaj email");
            result = false;
        }

        String password = registerPassword.getText().toString().trim();
        if (password.equals("")) {
            registerPassword.setError("Podaj hasło");
            result = false;
        }
        String cpassword = registerRepeatPswd.getText().toString().trim();
        if (cpassword.equals("")) {
            registerRepeatPswd.setError("Powtórz hasło");
            result = false;
        } else {
            if (!cpassword.equals(password)){
                registerPassword.setText("");
                registerRepeatPswd.setText("");
                registerRepeatPswd.setError("Podałeś dwa różne hasła!");
                result = false;
            }
        }

        return result;
    }
}
