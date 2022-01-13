package com.dotify.music.dotify;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    private TextInputLayout firstNameInput;
    private TextInputLayout lastNameInput;
    private TextInputLayout usernameInput;
    private TextInputLayout emailInput;
    private TextInputLayout passwordInput;
    private TextInputLayout confirmPasswordInput;

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_display);

        Button registerButton = findViewById(R.id.registerButton);
        firstNameInput = findViewById(R.id.first_name_input);
        lastNameInput = findViewById(R.id.last_name_input);
        usernameInput = findViewById(R.id.username_input);
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        confirmPasswordInput = findViewById(R.id.confirm_password_input);

        registerButton.setOnClickListener((v) -> {
            //check if all inputs are valid
            //check if username or email is already taken
            //if taken, display error message
            //otherwise, create user and go to newsfeed
            if (confirmRegistration()) {
                Log.i("Register", "New user registered");
                Intent intent = new Intent(this, Login.class);
                startActivity(intent);
            }
        });
    }

    private boolean confirmRegistration() {
        if (!(confirmFirstLastName() & confirmUsername() & confirmEmail() & confirmPassword()))
            return false;
        return createNewUser();
    }

    private boolean confirmUsername() {
        String username = usernameInput.getEditText().getText().toString().trim();
        boolean rv = true;

        if (username.isEmpty() && rv) {
            usernameInput.setError("Field can't be empty");
            rv = false;
        }

        if (rv && !isUnique(username, "isUnique.php?unique=")) {
            usernameInput.setError("Username already used");
            rv = false;
        }

        if (rv) {
            usernameInput.setError(null);
        }

        return rv;
    }

    private boolean confirmFirstLastName() {
        String firstName = firstNameInput.getEditText().getText().toString().trim();
        String lastName = lastNameInput.getEditText().getText().toString().trim();
        boolean rv = true;

        if (firstName.isEmpty()) {
            firstNameInput.setError("Field can't be empty");
            rv = false;
        } else {
            firstNameInput.setError(null);
        }

        if (lastName.isEmpty()) {
            lastNameInput.setError("Field can't be empty");
            rv = false;
        } else {
            lastNameInput.setError(null);
        }

        return rv;
    }

    private boolean confirmPassword() {
        String password = passwordInput.getEditText().getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getEditText().getText().toString().trim();
        boolean rv = true;
        if (password.isEmpty() && rv) {
            passwordInput.setError("Field can't be empty");
            rv = false;
        } else {
            passwordInput.setError(null);
        }

        if (confirmPassword.isEmpty()) {
            confirmPasswordInput.setError("Field can't be empty");
            rv = false;
        } else {
            confirmPasswordInput.setError(null);
        }

        if (!password.equals(confirmPassword) && rv) {
            passwordInput.setError("Passwords do not match");
            confirmPasswordInput.setError("Passwords do not match");
            rv = false;
        }

        if (rv) {
            passwordInput.setError(null);
            confirmPasswordInput.setError(null);
        }

        return rv;
    }

    private boolean confirmEmail() {
        String email = emailInput.getEditText().getText().toString().trim();
        boolean rv = true;

        if (email.isEmpty() && rv) {
            emailInput.setError("Field can't be empty");
            rv = false;
        }

        if (!isValidEmail(email) && rv) {
            emailInput.setError("Invalid email");
            rv = false;
        }

        if (rv && !isUnique(email, "isUnique.php?unique=")) {
            emailInput.setError("Email already used");
            rv = false;
        }

        if (rv) {
            emailInput.setError(null);
        }

        return rv;
    }

    private boolean isValidEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

    private boolean isUnique(String uniqueStr, String url) {
        DatabaseRetriever retriever = new DatabaseRetriever();
        retriever.execute("GET", url + uniqueStr.toLowerCase());
        JSONArray data;
        try {
            data = retriever.get(1000, TimeUnit.MILLISECONDS);
            if (data == null) {
                Toast.makeText(this, "Failed to connect to server", Toast.LENGTH_SHORT).show();
                return false;
            }
            JSONObject object = data.getJSONObject(0);
            boolean hasRows = object.getBoolean("exists");
            return !hasRows;
        } catch (ExecutionException | InterruptedException | TimeoutException | JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean createNewUser() {
        String firstName = firstNameInput.getEditText().getText().toString().trim();
        String lastName = lastNameInput.getEditText().getText().toString().trim();
        String password = passwordInput.getEditText().getText().toString().trim();
        String username = usernameInput.getEditText().getText().toString().trim();
        String email = emailInput.getEditText().getText().toString().trim();

        DatabaseRetriever retriever = new DatabaseRetriever();
        String url = "createUser.php?"
                + "firstname=" + firstName + "&"
                + "lastname=" + lastName + "&"
                + "username=" + username + "&"
                + "password=" + password + "&"
                + "email=" + email;
        retriever.execute("POST", url);
        try {
            JSONArray data = retriever.get(1000, TimeUnit.MILLISECONDS);
            JSONObject object = data.getJSONObject(0);
            return object.getBoolean("created");
        } catch (ExecutionException | InterruptedException | TimeoutException | JSONException | NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to create user", Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
