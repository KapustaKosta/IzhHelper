package ru.konstantin_starikov.samsung.izhhelper.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.willena.phoneinputview.CountryConfigurator;
import com.github.willena.phoneinputview.PhoneInputView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import ru.konstantin_starikov.samsung.izhhelper.R;

public class LoginWithPhoneNumberActivity extends AppCompatActivity {

    private static final String TAG = "PhoneAuthActivity";

    private PhoneInputView phoneInputView;

    private String verificationId;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private FirebaseAuth firebaseAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_phone_number);
        phoneInputView = findViewById(R.id.phoneInputView);
        CountryConfigurator config = new CountryConfigurator();
        config.setDisplayFlag(true);
        config.setDisplayCountryCode(true);
        config.setDisplayDialingCode(true);
        config.setDefaultCountry("RU"); //Set the default country that will be selected when loading
        phoneInputView.setConfig(config);

        firebaseAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);

                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

                // Show a message and update the UI
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                LoginWithPhoneNumberActivity.this.verificationId = verificationId;
                resendToken = token;
            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    public void verifyNumber(View view)
    {
        String phoneNumber = phoneInputView.getFormatedNumber();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
}