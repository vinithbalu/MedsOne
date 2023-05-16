package com.gopher.MedsOne;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.jacksonandroidnetworking.JacksonParserFactory;

import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.Gson;

public class MedicineDetailsActivity extends AppCompatActivity {

    TextView titleDetailsTextView;
    TextView detailsTextView;
    String barcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_details);
        titleDetailsTextView = findViewById(R.id.titleDetailsTextView);
        detailsTextView = findViewById(R.id.detailsTextView);
        Intent intent = getIntent();
        barcode = (String) getIntent().getSerializableExtra("barcode");

        retrieveInfo();
    }
    private void retrieveInfo() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("upc", barcode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //AndroidNetworking.setParserFactory(new JacksonParserFactory());
        AndroidNetworking.post("https://api.upcitemdb.com/prod/trial/lookup")
                .addJSONObjectBody(jsonObject) // posting json
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println("Response:");
                        System.out.println(response);
                        Gson gson = new Gson();
                        UPCItemDBResponse upcItemDBResponse = gson.fromJson(response.toString(), UPCItemDBResponse.class);
                        if (upcItemDBResponse.getTotal() > 0 ) {
                            titleDetailsTextView.setText(upcItemDBResponse.getItems().get(0).getTitle());
                            detailsTextView.setText(response.toString());
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        System.out.println("Error: "+error.getErrorDetail());
                    }
                });
    }
}