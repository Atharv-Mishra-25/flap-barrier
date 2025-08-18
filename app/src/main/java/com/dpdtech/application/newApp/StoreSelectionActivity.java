package com.dpdtech.application.newApp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


import com.dpdtech.application.network.models.response.LoginResponse;
import com.dpdtech.application.network.retrofit.APIServices;
import com.dpdtech.application.network.retrofit.RetrofitClass;
import com.tgw.R;

public class StoreSelectionActivity extends Activity {

    APIServices apiinterface;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_selection);
        apiinterface = RetrofitClass.getClient().create(APIServices.class);

        Spinner storeSelection = findViewById(R.id.storeSelection);
        Spinner etPassword = findViewById(R.id.gateSelection);
        Button enterButton = findViewById(R.id.enterButton);

        LoginResponse responseModel = getIntent().getParcelableExtra("responseModel");
        Log.d("TAGGGGGGGG", "onResponse:" + responseModel);

        if (responseModel != null) {

            ArrayAdapter<LoginResponse.FlapBarrierCounter> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    responseModel.flapBarrierCounters
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            storeSelection.setAdapter(adapter);
        }
        // Set the adapter to the spinner
        storeSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                login(etUserName.getText().toString().trim(), etPassword.getText().toString().trim());
            }
        });
    }


}