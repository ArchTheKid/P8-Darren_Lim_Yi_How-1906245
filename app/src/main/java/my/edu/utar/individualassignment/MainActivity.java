package my.edu.utar.individualassignment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button equalBreakdownButton = findViewById(R.id.EqualBreakDown);
        Button percentageBreakdownButton = findViewById(R.id.PercentageBreakdown);
        Button customBreakdownButton = findViewById(R.id.CustomBreakDown);

        equalBreakdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EqualBreakdown.class);
                startActivity(intent);
            }
        });

        percentageBreakdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PercentageBreakdown.class);
                startActivity(intent);
            }
        });

        customBreakdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CustomBreakdown.class);
                startActivity(intent);
            }
        });
    }
}
