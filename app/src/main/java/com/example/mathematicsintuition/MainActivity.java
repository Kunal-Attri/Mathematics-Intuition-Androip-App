package com.example.mathematicsintuition;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Gson gson;
    public static final String FILE_NAME = "INTUITION";
    public static final String KEY_NAME = "COUNTED";
    private Integer count, value, givenValue;
    private Dialog loadingDialog;
    private String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SeekBar seekBar = findViewById(R.id.seekBar);
        TextView textView = findViewById(R.id.textView);
        Button button = findViewById(R.id.button);
        TextView counter = findViewById(R.id.counter);
        Button uploadBtn = findViewById(R.id.submit);

        preferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        gson = new Gson();

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corners));
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);

        Random rand = new Random();

        getCount();
        if (count > 0) {
            value = rand.nextInt(101);
            textView.setText(value.toString());

            button.setText("Confirm");
            counter.setText(count.toString());

            //uploadBtn.setVisibility(View.VISIBLE);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button.getText().equals("Let's Start")) {
                    button.setText("Confirm");

                    count++;
                    counter.setText(count.toString());
                    storeCount();

                    value = rand.nextInt(101);
                    textView.setText(value.toString());

                    //uploadBtn.setVisibility(View.VISIBLE);
                }
                else {

                    givenValue = seekBar.getProgress();
                    id = UUID.randomUUID().toString();
                    FirebaseDatabase.getInstance().getReference().
                            child(id).child(value.toString()).setValue(givenValue.toString());
                    
                    count++;
                    counter.setText(count.toString());
                    storeCount();

                    value = rand.nextInt(101);
                    textView.setText(value.toString());

                }
            }
        });
    }


    private void getCount(){
        int json = preferences.getInt(KEY_NAME, 0);
        Type type = new TypeToken<Integer>(){}.getType();
        count = gson.fromJson(String.valueOf(json), type);
    }

    private void storeCount(){
        String json = gson.toJson(count);
        editor.putInt(KEY_NAME, Integer.parseInt(json));
        editor.commit();
    }

}