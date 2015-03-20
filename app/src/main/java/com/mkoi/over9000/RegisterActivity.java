package com.mkoi.over9000;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Bartek on 2015-03-18.
 */
public class RegisterActivity extends Activity {

    TextView registerTitle;
    EditText registerFirstName, registerLastName, registerNick,
                     registerEmail, registerPassword, registerRepeatPswd;
    Button registerBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerTitle = (TextView) findViewById(R.id.registerTitleTxt);
        registerFirstName = (EditText) findViewById(R.id.registerFirstName);
        registerLastName = (EditText) findViewById(R.id.registerLastName);
        registerNick = (EditText) findViewById(R.id.registerNick);
        registerEmail = (EditText) findViewById(R.id.registerEmail);
        registerPassword = (EditText) findViewById(R.id.registerPassword);
        registerRepeatPswd = (EditText) findViewById(R.id.registerRepeatPswd);
        registerBtn = (Button) findViewById(R.id.registerBtn);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    public void addNewAccount(View view){
//        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//        startActivity(intent);
        validateInputFields();
    }

    public void validateInputFields(){
        String password, cpassword;
        if (registerFirstName.getText().toString().trim().equals("")){
            registerFirstName.setError("Podaj imię");
        }
        if (registerLastName.getText().toString().trim().equals("")){
            registerLastName.setError("Podaj nazwisko");
        }
        if (registerNick.getText().toString().trim().equals("")) {
            registerNick.setError("Podaj nick");
        }
        String email = registerEmail.getText().toString().trim();
        boolean validate = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        if (!validate){
            registerEmail.setError("Błędny format maila");
        }
        if (email.equals("")) {
            registerEmail.setError("Podaj email");
        }
        password = registerPassword.getText().toString().trim();
        if (password.equals("")) {
            registerPassword.setError("Podaj hasło");
        }
        cpassword = registerRepeatPswd.getText().toString().trim();
        if (cpassword.equals("")) {
            registerRepeatPswd.setError("Powtórz hasło");
        } else {
            if (!cpassword.equals(password)){
                registerPassword.setText("");
                registerRepeatPswd.setText("");
                registerRepeatPswd.setError("Podałeś dwa różne hasła!");
            }
        }
    }
}
