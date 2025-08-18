package com.dpdtech.application.newApp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.dpdtech.application.network.models.response.LoginResponse;
import com.google.gson.Gson;
import com.tgw.R;

import java.util.ArrayList;

public class SettingsActivity extends Activity {

    String etChainId = "3025";
    String etStoreId = "4980";

    private String selectedGate;
    private String enteredTiming;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final EditText etTiming = findViewById(R.id.etTiming);
//        final EditText chainIdEditText = findViewById(R.id.chainIdEditText);
        Button enterButton = findViewById(R.id.enterButton);
        Spinner gateSpinner = findViewById(R.id.gateSpinner);
        ImageView btnLogout = findViewById(R.id.btnLogout);


        btnLogout.setOnClickListener(v -> logout());

        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String selectedGateSp = sharedPreferences.getString("gate", null);
        String jsonString = sharedPreferences.getString("responseString", null);
        LoginResponse loginResponse;

        if (jsonString != null && selectedGateSp != null) {
            Gson gson = new Gson();
            loginResponse = gson.fromJson(jsonString, LoginResponse.class);
            SpinnerAdapter adapter = new SpinnerAdapter(this, loginResponse.flapBarrierCounters);
            gateSpinner.setAdapter(adapter);

            ArrayList<LoginResponse.FlapBarrierCounter> flapBarrierCounters = loginResponse.flapBarrierCounters;

            int preselectIndex = -1;
            for (int i = 0; i < flapBarrierCounters.size(); i++) {
                if ((flapBarrierCounters.get(i).counterName.toLowerCase().replace(" ", "").trim()).equals(selectedGateSp)) {
                    preselectIndex = i;
                    break;
                }
            }

            if (preselectIndex != -1) {
                gateSpinner.setSelection(preselectIndex);
            }

            gateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedGate = ((LoginResponse.FlapBarrierCounter) parent.getItemAtPosition(position)).counterName;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    selectedGate = null;
                }
            });

        }


        enterButton.setOnClickListener(v -> {
//            String storeId = storeIdEditText.getText().toString().trim();
//            String chainId = chainIdEditText.getText().toString().trim();

//            if (isValidInput(storeId) && isValidInput(chainId)) {
//                etChainId = chainIdEditText.getText().toString();
//                etStoreId = storeIdEditText.getText().toString();
//
//            } else {
//                // Show an error message
//                Toast.makeText(this, "Please enter valid Ids numbers!", Toast.LENGTH_SHORT).show();
//            }
        });


        // Set up spinner


        // Handle spinner item selection


        // Handle button click
        enterButton.setOnClickListener(v -> {
//            String storeId = storeIdEditText.getText().toString().trim();
//            String chainId = chainIdEditText.getText().toString().trim();

            enteredTiming = etTiming.getText().toString().trim();

            // Validate input
            if (isValidInput(enteredTiming) && selectedGate != null) {
                saveToSharedPreferences((selectedGate).trim().toLowerCase().replace(" ", ""));
                saveTimingToSharedPrefs(enteredTiming);
                Toast.makeText(this, "Data saved successfully!", Toast.LENGTH_SHORT).show();
                Intent itnent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(itnent);
                finish();
            } else {
                Toast.makeText(this, "Please enter valid fields", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirmation")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        dialog.dismiss();
                        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle No button action
                        dialog.dismiss(); // Dismiss the dialog
                    }
                })
                .setCancelable(false); // Prevent closing the dialog by tapping outside

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void saveToSharedPreferences(String gate) {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("gate", gate);
        editor.apply(); // Save changes asynchronously
//        Intent itnent = new Intent(SettingsActivity.this, MainActivity.class);
//        startActivity(itnent);
//        finish();

    }

    private void saveTimingToSharedPrefs(String timing) {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("timing", timing);
        editor.apply(); // Save changes asynchronously
        Intent itnent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(itnent);
        finish();

    }

    private void getPhoneState() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }
    }

    private boolean isValidInput(String input) {
        // Check if the input is a 4-digit number
        return !TextUtils.isEmpty(input) && TextUtils.isDigitsOnly(input);
    }
}