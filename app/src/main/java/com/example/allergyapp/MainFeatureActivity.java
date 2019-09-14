package com.example.allergyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.allergyapp.ui.main.SectionsPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class MainFeatureActivity extends AppCompatActivity {

    private CheckBox check1, check2, check3, check4, check5, check6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feature);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);
        addListenerOnButton();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void addListenerOnButton() {
        String s = "";
        check1 = (CheckBox) findViewById(R.id.checkBox);
        check2 = (CheckBox) findViewById(R.id.checkBox);
        check3 = (CheckBox) findViewById(R.id.checkBox);
        check4 = (CheckBox) findViewById(R.id.checkBox);
        check5 = (CheckBox) findViewById(R.id.checkBox);
        check6 = (CheckBox) findViewById(R.id.checkBox);

        if (check1.isChecked()) {
            s += "Dairy";
        }if (check2.isChecked()) {
            s += "Peanuts";
        }if (check3.isChecked()) {
            s += "Tree Nuts";
        }if (check4.isChecked()) {
            s += "Soy";
        }if (check5.isChecked()) {
            s += "Shellfish";
        }if (check6.isChecked()) {
            s += "Eggs";
        }
        s = s.substring(0,s.length()-1);
        Toast.makeText(MainFeatureActivity.this,s, Toast.LENGTH_LONG).show();

    }
}