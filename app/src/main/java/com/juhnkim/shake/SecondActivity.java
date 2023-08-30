package com.juhnkim.shake;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Button lastABtn = findViewById(R.id.lastActivityBtn);

        lastABtn.setOnClickListener(v -> {
            Intent intent = new Intent(SecondActivity.this, MainActivity.class);
            startActivity(intent);
        });



        FragmentManager fm = getSupportFragmentManager();

        fm.beginTransaction()
                .add(R.id.fragmentContainerView, CoolFragment.class, null) // gets the first animations
                .commit();

    }
}