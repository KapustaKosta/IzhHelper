package ru.konstantin_starikov.samsung.izhhelper.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import ru.konstantin_starikov.samsung.izhhelper.R;
import ru.konstantin_starikov.samsung.izhhelper.models.Account;
import ru.konstantin_starikov.samsung.izhhelper.models.Helper;

public class EditProfileActivity extends AppCompatActivity {

    private final int REQUEST_FILE = 42;

    private Account userAccount;

    private String avatarPath = null;

    private ImageView avatarImageView;
    private TextView displayText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private EditText flatEditText;
    private EditText homeEditText;
    private EditText streetEditText;
    private EditText townEditText;

    private TextView warningText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        tuneActionBar();

        userAccount = (Account) getIntent().getSerializableExtra(MainMenuActivity.USER_ACCOUNT);

        findAndSetViews();

        if(userAccount.getAvatarPath() != null)
            setUserAvatar();

        displayText.setText(userAccount.firstName + " " + userAccount.lastName);
        firstNameEditText.setText(userAccount.firstName);
        lastNameEditText.setText(userAccount.lastName);
        phoneEditText.setText(userAccount.phoneNumber);
        emailEditText.setText(userAccount.email);
        flatEditText.setText(Integer.toString(userAccount.address.flat));
        homeEditText.setText(userAccount.address.home);
        streetEditText.setText(userAccount.address.street);
        townEditText.setText(userAccount.address.town);
    }

    private void findAndSetViews()
    {
        displayText = findViewById(R.id.displayName);
        firstNameEditText = findViewById(R.id.editFirstName);
        lastNameEditText = findViewById(R.id.editLastName);
        phoneEditText = findViewById(R.id.editPhoneNumber);
        emailEditText = findViewById(R.id.editEmail);
        flatEditText = findViewById(R.id.editFlat);
        homeEditText = findViewById(R.id.editHome);
        streetEditText = findViewById(R.id.editStreet);
        townEditText = findViewById(R.id.editTown);
        warningText = findViewById(R.id.warningTextView);
        avatarImageView = findViewById(R.id.change_avatar);
    }

    private void setUserAvatar()
    {
        avatarImageView.setImageDrawable(Drawable.createFromPath((Helper.getFullPathFromDataDirectory(userAccount.getAvatarPath(), this))));
    }

    private void tuneActionBar()
    {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Редактировать профиль");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void changeAvatar(View v) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_FILE && resultCode == Activity.RESULT_OK) {
            Bitmap avatar = null;
            try {
                avatar = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                avatarImageView.setImageBitmap(avatar);
                FileOutputStream fileOutputStream = null;
                try {
                    Log.i("USER_ACCOUNT_ID", userAccount.ID);
                    avatarPath = String.format(getApplicationInfo().dataDir + File.separator + userAccount.ID + ".jpg");
                    Log.i("AVATAR_PATH", avatarPath);
                    fileOutputStream = new FileOutputStream(avatarPath);
                    avatar.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
                    avatarPath = avatarPath.substring(avatarPath.lastIndexOf('/') + 1);
                } catch (IOException exception) {
                    exception.printStackTrace();
                } finally {
                    try {
                        if(fileOutputStream != null) fileOutputStream.close();
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void saveChanges(View v)
    {
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String phoneNumber = phoneEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String flat = flatEditText.getText().toString();
        String home = homeEditText.getText().toString();
        String street = streetEditText.getText().toString();
        String town = townEditText.getText().toString();

        if(isAllFieldsFilled())
        {
            userAccount.firstName = firstName;
            userAccount.lastName = lastName;
            userAccount.email = email;
            userAccount.address.flat = Integer.parseInt(flat);
            userAccount.address.home = home;
            userAccount.address.street = street;
            userAccount.address.town = town;
            if(avatarPath != null) userAccount.setAvatarPath(avatarPath);
            userAccount.updateUserData(this);
            userAccount.updateUserDataOnFirebase();
            Intent intent = new Intent(this, MainMenuActivity.class);
            startActivity(intent);
        }
        else showWarningText();
    }

    private void showWarningText()
    {
        warningText.setVisibility(View.VISIBLE);
    }

    private boolean isAllFieldsFilled()
    {
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String phoneNumber = phoneEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String flat = flatEditText.getText().toString();
        String home = homeEditText.getText().toString();
        String street = streetEditText.getText().toString();
        String town = townEditText.getText().toString();

        boolean result = true;
        if(firstName.isEmpty() || lastName.isEmpty() || phoneNumber.isEmpty() ||
                email.isEmpty() || flat.isEmpty() || home.isEmpty() ||
                street.isEmpty() || town.isEmpty())
            result = false;
        return result;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}