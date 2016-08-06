package com.canpolata.canpolat.mortgagesabc;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "LifecycleMessage";
    public EditText inputName, inputEmail, inputPassword;
    public TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutPassword;
    private String sUsername, sEmail,sPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.material_text_field);

        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        inputName = (EditText) findViewById(R.id.loan_amount);
        inputEmail = (EditText) findViewById(R.id.interest_rate);
        inputPassword = (EditText) findViewById(R.id.loan_period);

        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "onRestoreInstanceState");
    }


    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(inputName);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {


        if (inputEmail.getText().toString().trim().isEmpty()) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(inputPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);


        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.loan_amount:
                    validateName();
                    break;
                case R.id.interest_rate:
                    validateEmail();
                    break;
                case R.id.loan_period:
                    validatePassword();
                    break;
            }
        }
    }

    public void showLoanPayments(View v) {
        sUsername = inputName.getText().toString();
        sEmail = inputEmail.getText().toString();
        sPassword = inputPassword.getText().toString();

        if (sUsername.trim().equals("")|| sEmail.trim().equals("")|| sPassword.trim().equals("")){
            Toast.makeText(this, "Please complete all fields", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent(MainActivity.this, ResultsMain.class);
            //Intent intentData = new Intent(MainActivity.this, MyData.class);
            Bundle b = new Bundle();


            //P
            double loanAmount = Double.parseDouble(inputName.getText().toString());

            //i = interest rate / 12
            double interestRate = (Double.parseDouble(inputEmail.getText().toString()));

            // N = Period * 12
            double loanPeriod = Double.parseDouble(inputPassword.getText().toString());

            // the equation is M = P [i(1+i)powerN/ [(1+i)powerN - 1]

            double i = interestRate / 1200;
            double r1 = Math.pow(i + 1, loanPeriod * 12);
            double g = r1 * i;
            double h = r1 - 1;

            double monthlyPayment = ((g / h) * loanAmount);
            double totalPayment = monthlyPayment * 12;

            double totalMortgage = monthlyPayment * 12 * loanPeriod;
            double totalInterest = totalMortgage - loanAmount;

            double percentInterest = totalInterest / totalMortgage * 100;
            double percentRepayment = loanAmount / totalMortgage * 100;
            double percentTotal = totalPayment / totalMortgage * 100;

            double total3 = percentTotal + percentInterest + percentRepayment;

            b.putDouble("amount", monthlyPayment);
            b.putDouble("interest", totalPayment);
            b.putDouble("totalMortgage", totalMortgage);
            b.putDouble("totalInterest", totalInterest);
            b.putDouble("percentInterest", percentInterest);
            b.putDouble("percentRepayment", percentRepayment);
            b.putDouble("percentTotal", percentTotal);
            b.putDouble("total3", total3);

            intent.putExtras(b);
            startActivity(intent);

        }






        }



}
