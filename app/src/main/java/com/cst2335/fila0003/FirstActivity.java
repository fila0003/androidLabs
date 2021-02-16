package com.cst2335.fila0003;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FirstActivity extends AppCompatActivity {
    SharedPreferences prefs = null;
    EditText textEmail;
    EditText textPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //your programm start here
        super.onCreate(savedInstanceState);
        //setContentView loads obj onto the screen
        setContentView(R.layout.activity_main_linear);

        prefs = getSharedPreferences("mySettings", Context.MODE_PRIVATE);

        textEmail = findViewById(R.id.editText1);
        textEmail.setText(prefs.getString("email", null));
        textPassword = findViewById(R.id.editText2);

        /*String savedString =prefs.getString("ReserveName", "Default Value");
        EditText typeField = findViewById(R.id.inputButton);
        typeField.setText(savedString);
         Button saveButton = (Button)findViewById(R.id.saveButton);
         saveButton.setOnClickListener(bt ->
         saveSharedPrefs(typeField.getText().toString()));*/
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
       /* SharedPreferences.Editor editor = prefs.edit();
        editor.putString("email", String.valueOf(textEmail.getText()));
        editor.putString("password", String.valueOf(textPassword.getText()));
        editor.commit();*/
    }

    private  void  saveSharedPrefs(String stringToSave) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("email", String.valueOf(textEmail.getText()));
        editor.putString("password", String.valueOf(textPassword.getText()));
        editor.commit();
    }

    public void onLogin(View view) {
        Intent goToProfile = new Intent(FirstActivity.this, ProfileActivity.class);
        goToProfile.putExtra("EMAIL", String.valueOf(textEmail.getText()));
        startActivity(goToProfile);
    }
}
