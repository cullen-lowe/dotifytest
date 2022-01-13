package com.dotify.music.dotify;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Login extends AppCompatActivity {

    private TextInputLayout usernameInput;
    private TextInputLayout passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_display);

        boolean isLoggedin = getSharedPreferences("SIGN", MODE_PRIVATE).getBoolean(MainActivity.SIGN_RESTORE, false);
        if (isLoggedin) {
            Log.i("Login", "User logged in");
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        Button loginButton = findViewById(R.id.loginButton);
        TextView newUserButton = findViewById(R.id.newUserButton);
        TextView forgotPassword = findViewById(R.id.forgotPasswordLabel);
        usernameInput = findViewById(R.id.userIDBox);
        passwordInput = findViewById(R.id.passwordBox);

        loginButton.setOnClickListener((v) -> {
            if (credentialsAreValid()) {
                Log.i("Login", "User logged in");
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        newUserButton.setOnClickListener((v) -> {
            Intent intent = new Intent(this, Register.class);
            startActivity(intent);
        });
    }

    public boolean credentialsAreValid() {
        boolean rv = true;
        String username = usernameInput.getEditText().getText().toString().trim();
        String password = passwordInput.getEditText().getText().toString().trim();

        if (username.isEmpty()) {
            usernameInput.setError("Field can't be empty");
            rv = false;
        } else if (credentialExists(username, "isUnique.php?unique=")) {
            usernameInput.setError("User doesn't exist");
            rv = false;
        } else {
            usernameInput.setError(null);
        }

        if (password.isEmpty()) {
            passwordInput.setError("Field can't be empty");
            rv = false;
        } else  {
            passwordInput.setError(null);
        }

        if (rv && !loginUser(username, password)) {
            passwordInput.setError("Wrong password");
            rv = false;
        }

        return rv;
    }

    private boolean credentialExists(String uniqueStr, String url) {
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

    private boolean loginUser(String username, String password) {
        DatabaseRetriever retriever = new DatabaseRetriever();
        retriever.execute("GET", "loginUser.php?username=" + username + "&password=" + password);
        JSONArray data;
        try {
            data = retriever.get(2000, TimeUnit.MILLISECONDS);
            if (data == null) {
                Toast.makeText(this, "Failed to connect to server", Toast.LENGTH_SHORT).show();
                return false;
            }
            JSONObject object = data.getJSONObject(0);
            return object.getBoolean("login");
        } catch (ExecutionException | InterruptedException | TimeoutException | JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
}
