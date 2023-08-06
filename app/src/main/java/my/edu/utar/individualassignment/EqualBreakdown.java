package my.edu.utar.individualassignment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

public class EqualBreakdown extends AppCompatActivity {

    private EditText totalBillInput;
    private EditText totalPersonInput;
    private TextView equalBreakdownResult;
    private Button emailButton,equalButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equalbreakdown);

        totalBillInput = findViewById(R.id.totalBill);
        totalPersonInput = findViewById(R.id.totalPerson);
        equalBreakdownResult = findViewById(R.id.equalBreakdownResult);
        emailButton = findViewById(R.id.emailBtn);
        equalButton = findViewById(R.id.equalBtn);

        equalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateEqualBreakdown();
            }
        });

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
    }

    private void calculateEqualBreakdown() {
        String billInput = totalBillInput.getText().toString();
        String peopleInput = totalPersonInput.getText().toString();

        if (!billInput.isEmpty() && !peopleInput.isEmpty()) {
            double totalBill = Double.parseDouble(billInput);
            int totalPeople = Integer.parseInt(peopleInput);

            if (totalPeople > 0) {
                double perPersonPayment = totalBill / totalPeople;
                DecimalFormat df = new DecimalFormat("0.00");

                StringBuilder resultBuilder = new StringBuilder();
                for (int i = 0; i < totalPeople; i++) {
                    resultBuilder.append("Person ").append(i + 1).append(" pays RM ").append(df.format(perPersonPayment));
                    if (i < totalPeople - 1) {
                        resultBuilder.append("\n");
                    }
                }

                String result = "Result:\n" + resultBuilder.toString();
                equalBreakdownResult.setText(result);

                emailButton.setVisibility(View.VISIBLE);
            } else {
                equalBreakdownResult.setText("Invalid: Total people must be greater than 0");
            }
        } else {
            equalBreakdownResult.setText("Please input value");
        }
    }

    private void sendEmail() {
        String content = equalBreakdownResult.getText().toString();
        if (!content.isEmpty()) {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Equal Breakdown Result");
            emailIntent.putExtra(Intent.EXTRA_TEXT, content);
            startActivity(Intent.createChooser(emailIntent, "Send Email"));
        }
    }
}
