package com.mkoi.over9000;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_login)
public class LoginActivity extends Activity {

    @Click(R.id.registerBtn)
    public void goToRegister(View view){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity_.class);
        startActivity(intent);
    }

}
