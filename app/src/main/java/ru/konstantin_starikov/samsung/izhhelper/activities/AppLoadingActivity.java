package ru.konstantin_starikov.samsung.izhhelper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ru.konstantin_starikov.samsung.izhhelper.R;
import ru.konstantin_starikov.samsung.izhhelper.models.Account;

public class AppLoadingActivity extends AppCompatActivity {

    private CountDownTimer timer;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_loading);

        timer = new CountDownTimer(3 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                //getApplicationContext().deleteDatabase( "users.db");
                firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                Intent intent = null;
                if(currentUser != null) {
                    Account userAccount = new Account(currentUser);
                    if (userAccount.isUserHasDataInDatabase(getApplicationContext()))
                        intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                    else
                        intent = new Intent(getApplicationContext(), AccountCreationActivity.class);
                }
                else
                    intent = new Intent(getApplicationContext(), LoginWithPhoneNumberActivity.class);

                startActivity(intent);
            }
        };

        timer.start();
    }
}