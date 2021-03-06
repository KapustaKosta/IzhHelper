package ru.konstantin_starikov.samsung.izhhelper.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import ru.konstantin_starikov.samsung.izhhelper.R;

public class LoginActivity extends AppCompatActivity {

    private final static String LOGIN = "Kapusta";
    private final static String PASSWORD = "12345";

    private final static int REGISTRATION_REQUEST_CODE = 1061;

    public final static String ACCOUNT_ID = "accountID";

    private TextView phoneNumberText;
    private TextView passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        phoneNumberText = (TextView) findViewById(R.id.loginText);
        passwordText = (TextView) findViewById(R.id.passwordText);
    }

    public void LogIntoAccount(View view)
    {
        String inputPhoneNumber = phoneNumberText.getText().toString();
        String inputPassword = passwordText.getText().toString();

        TextView isRightText = (TextView) findViewById(R.id.isRightText);

        if(isInputDataRight(inputPhoneNumber, inputPassword)) {
            Intent loginIntent = new Intent(this, MainMenuActivity.class);
            loginIntent.putExtra(ACCOUNT_ID, GetAccountID(inputPhoneNumber, inputPassword));
            startActivity(loginIntent);
        }
        else
            {
                isRightText.setVisibility(View.VISIBLE);
                isRightText.setText(R.string.false_input_text);
                isRightText.setTextColor(Color.RED);

                phoneNumberText.setText("");
                passwordText.setText("");
            }
    }

    public void RegisterUser(View v)
    {
        Intent registrationIntent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivityForResult(registrationIntent, REGISTRATION_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REGISTRATION_REQUEST_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    phoneNumberText.setText(data.getStringExtra(RegistrationActivity.REGISTERED_LOGIN_KEY));
                    passwordText.setText(data.getStringExtra(RegistrationActivity.REGISTERED_PASSWORD_KEY));
                    break;
            }
        }
    }

    private boolean isInputDataRight(String login, String password)
    {

        if(login.equals(LOGIN) && password.equals(PASSWORD)) return true;
        return false;
    }

    private long GetAccountID(String login, String password)
    {
        return 1234567;
    }
}