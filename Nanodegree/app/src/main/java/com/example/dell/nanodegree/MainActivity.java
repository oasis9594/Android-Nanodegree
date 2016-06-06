package com.example.dell.nanodegree;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button button_movie, button_stock, button_big, button_material, button_ubiquitous, button_capstone;
    TextView nanodegree_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("My App Portfolio");
        nanodegree_text=(TextView)findViewById(R.id.nanodegree_text);
        button_movie=(Button)findViewById(R.id.button_movie);
        button_stock=(Button)findViewById(R.id.button_stock);
        button_big=(Button)findViewById(R.id.button_big);
        button_material=(Button)findViewById(R.id.button_material);
        button_ubiquitous=(Button)findViewById(R.id.button_ubiquitous);
        button_capstone=(Button)findViewById(R.id.button_capstone);

        button_movie.setOnClickListener(this);
        button_stock.setOnClickListener(this);
        button_big.setOnClickListener(this);
        button_material.setOnClickListener(this);
        button_ubiquitous.setOnClickListener(this);
        button_capstone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        String s="This button will launch ";
        if(id==R.id.button_movie) {
            Toast.makeText(this, s+getResources().getString(R.string.popular_movies)+" app", Toast.LENGTH_SHORT).show();
        }
        else if(id==R.id.button_stock) {
            Toast.makeText(this, s+getResources().getString(R.string.stock_hawk)+" app", Toast.LENGTH_SHORT).show();
        }
        else if(id==R.id.button_big) {
            Toast.makeText(this, s+getResources().getString(R.string.build_it_bigger)+" app", Toast.LENGTH_SHORT).show();
        }

        else if(id==R.id.button_material) {
            Toast.makeText(this, s+getResources().getString(R.string.make_your_app_material)+" app", Toast.LENGTH_SHORT).show();
        }
        else if(id==R.id.button_ubiquitous) {
            Toast.makeText(this, s+getResources().getString(R.string.go_ubiquitous)+" app", Toast.LENGTH_SHORT).show();
        }
        else if(id==R.id.button_capstone) {
            Toast.makeText(this, s+getResources().getString(R.string.capstone)+" app", Toast.LENGTH_SHORT).show();
        }
    }
}

