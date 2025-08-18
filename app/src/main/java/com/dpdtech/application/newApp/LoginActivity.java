package com.dpdtech.application.newApp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.dpdtech.application.network.models.request.LoginRequest;
import com.dpdtech.application.network.models.response.LoginResponse;
import com.dpdtech.application.network.retrofit.APIServices;
import com.dpdtech.application.network.retrofit.RetrofitClass;
import com.google.gson.Gson;
import com.tgw.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity {

    APIServices apiinterface;
    ProgressDialog progressDialog;
    String selectedGate;
    String chainId;
    String storeId;
    String responseString;
    Spinner spinnerGate;
    Button enterButton;
    Button loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        apiinterface = RetrofitClass.getClient().create(APIServices.class);

        EditText etUserName = findViewById(R.id.etUserName);
        EditText etPassword = findViewById(R.id.etPassword);

        loginButton = findViewById(R.id.loginButton);
        enterButton = findViewById(R.id.enterButton);
        spinnerGate = findViewById(R.id.gateSelection);

        loginButton.setOnClickListener(v -> login(etUserName.getText().toString().trim(), etPassword.getText().toString().trim()));

        enterButton.setOnClickListener(v -> saveToSharedPreferences());

        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String selectedGateSp = sharedPreferences.getString("gate", null);
        if (selectedGateSp != null && !selectedGateSp.isEmpty()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        etUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                spinnerGate.setVisibility(View.GONE);
                loginButton.setVisibility(View.VISIBLE);
                enterButton.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                spinnerGate.setVisibility(View.GONE);
                loginButton.setVisibility(View.VISIBLE);
                enterButton.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void login(String userName, String password) {
//        barcodetext.clearFocus();
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        LoginRequest loginRequest = new LoginRequest("QueueBuster", "flap", "model", password, "", androidId, userName);

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String url = "https://" + Constants.API_BASE_URL + "/API/public/v1/merchant/loginApp";
        Call<LoginResponse> getdata = apiinterface.performLogin(userName, url, loginRequest);
        getdata.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progressDialog.dismiss();
                if (response.body() != null) {
                    if (response.body().status) {
                        spinnerGate.setVisibility(View.VISIBLE);
                        chainId = String.valueOf(response.body().chainID);
                        storeId = String.valueOf(response.body().storeList.get(0).storeID);
                        if (!response.body().flapBarrierCounters.isEmpty()) {
                            setSpinner(response.body().flapBarrierCounters);
                        } else {
                            Toast.makeText(getApplicationContext(), "Oops Looks Like No Counters Assigned", Toast.LENGTH_LONG).show();

                        }
                        enterButton.setVisibility(View.VISIBLE);
                        loginButton.setVisibility(View.GONE);
                        Gson gson = new Gson();
                        responseString = gson.toJson(response.body());
                        Toast.makeText(getApplicationContext(), "Api response - " + response.body().status, Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent(LoginActivity.this, StoreSelectionActivity.class);
//                        intent.putExtra("responseModel", response.body());
//                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Api response - " + response.body().status, Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "First api failed with exception - " + t.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
//                barcodetext.setText("");
//                barcodetext.requestFocus();
//                barcodetext.setEnabled(true);
//                barcodetext.setCursorVisible(true);
//
//                barcodetext.setFocusable(true);

            }
        });
    }

    private void setSpinner(ArrayList<LoginResponse.FlapBarrierCounter> flapBarrierCounters) {
        SpinnerAdapter adapter = new SpinnerAdapter(this, flapBarrierCounters);
        spinnerGate.setAdapter(adapter);


        // Set item selected listener
        spinnerGate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                LoginResponse.FlapBarrierCounter selectedCounter = (LoginResponse.FlapBarrierCounter) parent.getItemAtPosition(position);
                selectedGate = ((LoginResponse.FlapBarrierCounter) parent.getItemAtPosition(position)).counterName;
//                saveToSharedPreferences(selectedGate, String.valueOf(chainID), String.valueOf(storeID));
                Toast.makeText(LoginActivity.this, "Selected: " + selectedCounter.counterName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case when no item is selected
            }
        });
    }

    private void saveToSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("storeId", storeId);
        editor.putString("chainId", chainId);
        editor.putString("responseString", responseString);
        editor.putString("gate", selectedGate.trim().replace(" ", "").toLowerCase());
        editor.apply();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }


}