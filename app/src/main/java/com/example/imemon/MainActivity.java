package com.example.imemon;

import android.content.Intent;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private Response makePostRequest(String url, RequestBody formBody) {
        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        try {
            return client.newCall(request).execute();

            //if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void loginButtonClick(View view) {
        EditText emailEdittext = findViewById(R.id.email_input);
        EditText passwordEdittext = findViewById(R.id.password_input);
        String email = emailEdittext.getText().toString();
        String password = passwordEdittext.getText().toString();

        RequestBody formBody = new FormBody.Builder()
                .add("email", email)
                .add("password", password)
                .build();

        Response response = makePostRequest("https://www.example.com/index.php", formBody);

        if(response.message().equals("400")) {
            Headers responseHeaders = response.headers();
            Intent i = new Intent(this, MainMenu.class);
            i.putExtra("sessionId", responseHeaders.get("sessionId"));
            i.putExtra("username", responseHeaders.get("username"));
            startActivity(i);
        }

        else {
            Toast.makeText(this, "Usuário inexistente ou senha inválida!", Toast.LENGTH_LONG).show();
        }
    }

    public void gotoSignupButtonClick(View view) {
        Intent i = new Intent(this, SignupMenu.class);
        startActivity(i);
    }
}