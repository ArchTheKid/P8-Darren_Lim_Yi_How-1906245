package my.edu.utar.individualassignment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

public class PercentageBreakdown extends AppCompatActivity {

    private EditText totalBillInput;
    private EditText totalPersonInput;
    private TextView percentView;
    private EditText totalPercentageInput;
    private Button percentageBtn;
    private TextView percentageBreakdownResult;
    private Button emailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.percentagebreakdown);

        totalBillInput = findViewById(R.id.totalBill2);
        totalPersonInput = findViewById(R.id.totalPerson);
        percentView = findViewById(R.id.percentView);
        totalPercentageInput = findViewById(R.id.totalPercentage);
        percentageBtn = findViewById(R.id.percentageBtn);
        percentageBreakdownResult = findViewById(R.id.percentageBreakdownResult);
        emailButton = findViewById(R.id.emailBtn);

        Button confirmationButton = findViewById(R.id.confirmationBtn);
        confirmationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleConfirmationButtonClick();
            }
        });

        percentageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePercentage();
            }
        });

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
    }

    private void handleConfirmationButtonClick() {
        String billInput = totalBillInput.getText().toString();
        String peopleInput = totalPersonInput.getText().toString();

        if (!billInput.isEmpty() && !peopleInput.isEmpty()) {
            int totalPeople = Integer.parseInt(peopleInput);

            if (totalPeople > 0) {
                percentView.setText("Please enter " + totalPeople + " percentage numbers");
                totalPercentageInput.setHint("If 2 person: 25% 25% (Do not enter letters!)");
                percentageBtn.setVisibility(View.VISIBLE);
            } else {
                percentView.setText("Invalid: Total people must be greater than 0");
            }
        } else {
            percentView.setText("");
        }
    }

    private void handlePercentage() {
        String totalPercentageText = totalPercentageInput.getText().toString();
        String[] percentages = totalPercentageText.split(" ");

        int totalPeople = Integer.parseInt(totalPersonInput.getText().toString());
        double totalPercentage = 0.0;
        for (String percentage : percentages) {
            try {
                double value = Double.parseDouble(percentage.replace("%", ""));
                totalPercentage += value;
            } catch (NumberFormatException e) {
                percentView.setText("Invalid");
                percentageBreakdownResult.setText("");
                return;
            }
        }

        if (totalPercentage == 100.0) {
            calculatePercentageBreakdown(totalPeople, percentages);
        } else {
            percentView.setText("Invalid: Total percentage must be 100%");
            percentageBreakdownResult.setText("");
        }
    }

    private void calculatePercentageBreakdown(int totalPeople, String[] percentages) {
        String billInput = totalBillInput.getText().toString();
        double totalBill = Double.parseDouble(billInput);

        DecimalFormat df = new DecimalFormat("0.00");
        StringBuilder resultBuilder = new StringBuilder();

        for (int i = 0; i < totalPeople; i++) {
            double personPercentage = Double.parseDouble(percentages[i].replace("%", ""));
            double personPayment = (personPercentage / 100.0) * totalBill;
            resultBuilder.append("Person ").append(i + 1).append(" pays RM ").append(df.format(personPayment));
            if (i < totalPeople - 1) {
                resultBuilder.append("\n");
            }
        }

        String result = "Result:\n" + resultBuilder.toString();
        percentageBreakdownResult.setText(result);
        emailButton.setVisibility(View.VISIBLE);
    }

    private void sendEmail() {
        String content = percentageBreakdownResult.getText().toString();
        if (!content.isEmpty()) {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Percentage Breakdown Result");
            emailIntent.putExtra(Intent.EXTRA_TEXT, content);
            startActivity(Intent.createChooser(emailIntent, "Send Email"));
        }
    }
}
