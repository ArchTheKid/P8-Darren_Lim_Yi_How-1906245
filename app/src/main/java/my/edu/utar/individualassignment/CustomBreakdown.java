package my.edu.utar.individualassignment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomBreakdown extends AppCompatActivity {

    private EditText totalBillEditText;
    private EditText totalPersonEditText;
    private EditText totalCustomEditText;
    private Button confirmationButton;
    private Button customButton;
    private Button emailButton;
    private TextView customView;
    private TextView customBreakdownResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custombreakdown);

        totalBillEditText = findViewById(R.id.totalBill3);
        totalPersonEditText = findViewById(R.id.totalPerson);
        totalCustomEditText = findViewById(R.id.totalCustom);
        confirmationButton = findViewById(R.id.confirmationBtn);
        customButton = findViewById(R.id.customBtn);
        emailButton = findViewById(R.id.emailBtn);
        customView = findViewById(R.id.customView);
        customBreakdownResult = findViewById(R.id.customBreakdownResult);

        confirmationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmButtonClick();
            }
        });

        customButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customButtonClick();
            }
        });

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
    }

    private void confirmButtonClick() {
        String totalBillStr = totalBillEditText.getText().toString();
        String totalPersonStr = totalPersonEditText.getText().toString();

        if (TextUtils.isEmpty(totalBillStr) || TextUtils.isEmpty(totalPersonStr)) {
            // Show error message if any field is empty
            customBreakdownResult.setText("Please enter total bill and total person.");
            customButton.setVisibility(View.GONE);
            emailButton.setVisibility(View.GONE);
        } else {
            double totalBill = Double.parseDouble(totalBillStr);
            int totalPerson = Integer.parseInt(totalPersonStr);

            if (totalBill <= 0 || totalPerson <= 0) {
                // Show error message if total bill or total person is not valid
                customBreakdownResult.setText("Invalid total bill or total person value.");
                customButton.setVisibility(View.GONE);
                emailButton.setVisibility(View.GONE);
            } else {
                // Show custom view with hint
                customView.setText("Please enter " + totalPerson + " custom amounts separated by '/'");
                totalCustomEditText.setHint("If " + totalPerson + " person: " + getHintExample(totalPerson));
                customButton.setVisibility(View.VISIBLE);
                emailButton.setVisibility(View.GONE);
            }
        }
    }

    private String getHintExample(int totalPerson) {
        StringBuilder hintExample = new StringBuilder();
        for (int i = 0; i < totalPerson; i++) {
            hintExample.append("2");
            if (i < totalPerson - 1) {
                hintExample.append("/");
            }
        }
        return hintExample.toString();
    }

    private void customButtonClick() {
        String customValuesStr = totalCustomEditText.getText().toString();
        String[] customValuesArray = customValuesStr.split("/");
        int totalPerson = Integer.parseInt(totalPersonEditText.getText().toString());

        if (customValuesArray.length != totalPerson) {
            // Show error message if the number of custom values does not match total person
            customBreakdownResult.setText("Please enter " + totalPerson + " custom amounts.");
            emailButton.setVisibility(View.GONE);
        } else {
            // Calculate sum of custom values
            double sum = 0;
            List<Double> customValuesList = new ArrayList<>();

            for (String customValue : customValuesArray) {
                try {
                    double value = Double.parseDouble(customValue);
                    if (value < 0) {
                        // Show error message if a negative custom value is entered
                        customBreakdownResult.setText("Error, invalid custom value detected.");
                        emailButton.setVisibility(View.GONE);
                        return;
                    }
                    customValuesList.add(value);
                    sum += value;
                } catch (NumberFormatException e) {
                    // Show error message if invalid custom value is entered
                    customBreakdownResult.setText("Error, invalid character detected.");
                    emailButton.setVisibility(View.GONE);
                    return;
                }
            }

            double totalBill = Double.parseDouble(totalBillEditText.getText().toString());

            if (sum == totalBill) {
                // Display custom breakdown result if the sum of custom values matches the total bill
                StringBuilder resultBuilder = new StringBuilder();
                for (int i = 0; i < customValuesList.size(); i++) {
                    double value = customValuesList.get(i);
                    String formattedValue = String.format("%.2f", value);
                    resultBuilder.append("Person ").append(i + 1).append(" pays RM ").append(formattedValue).append("\n");
                }
                customBreakdownResult.setText(resultBuilder.toString());
                emailButton.setVisibility(View.VISIBLE);
            } else {
                // Show error message if the sum of custom values does not match the total bill
                customBreakdownResult.setText("The numbers entered do not match the total bill!");
                emailButton.setVisibility(View.GONE);
            }
        }
    }

    private void sendEmail() {
        String content = customBreakdownResult.getText().toString();
        if (!content.isEmpty()) {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Custom Breakdown Result");
            emailIntent.putExtra(Intent.EXTRA_TEXT, content);
            startActivity(Intent.createChooser(emailIntent, "Send Email"));
        }
    }
}
