package com.mkoi.over9000;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Bartek on 2015-03-18.
 */
public class RegisterActivity extends Activity {

    private TextView registerTitle;
    private EditText registerFirstName, registerLastName, registerNick,
                     registerEmail, registerPassword, registerRepeatPswd;
    private Button registerBtn;
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

    public void addNewAccount(){

    };
}
