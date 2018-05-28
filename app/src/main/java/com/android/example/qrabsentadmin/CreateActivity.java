package com.android.example.qrabsentadmin;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.example.qrabsentadmin.app.AppConfig;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CreateActivity extends AppCompatActivity {

    EditText name, description;
    TextView date;
    String nameHolder, dateHolder, descriptionHolder;
    Boolean CheckEditText;
    Button create_seminar;
    RequestQueue requestQueue;
    Map<String, String> parameters;
    DatePickerDialog.OnDateSetListener mDataListener;

    String createSeminar = AppConfig.URL_CREATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        name = (EditText) findViewById(R.id.seminar_name);
        date = (TextView) findViewById(R.id.seminar_date);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        CreateActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDataListener, year, month, day);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDataListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String dateFormat = day + "-"+ month +"-"+ year;
                date.setText(dateFormat);
            }
        };

        description = (EditText) findViewById(R.id.seminar_description);
        create_seminar = (Button) findViewById(R.id.btnCreate);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        create_seminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckEditTextIsEmptyOrNot();

                if (CheckEditText) {
                    StringRequest request = new StringRequest(Request.Method.POST, createSeminar, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            parameters = new HashMap<String, String>();

                            parameters.put("name", name.getText().toString());
                            parameters.put("date", date.getText().toString());
                            parameters.put("description", description.getText().toString());

                            return parameters;
                        }
                    };
                    requestQueue.add(request);
                    Toast.makeText(getApplicationContext(), "Seminar Created, Check your Databases", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(CreateActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(CreateActivity.this, "Please Fill Seminar Details.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK || super.onKeyDown(keyCode, event);
    }

    public void CheckEditTextIsEmptyOrNot() {

        nameHolder = name.getText().toString();
        dateHolder = date.getText().toString();
        descriptionHolder = description.getText().toString();

        CheckEditText = !TextUtils.isEmpty(nameHolder) && !TextUtils.isEmpty(dateHolder) && !TextUtils.isEmpty(descriptionHolder);

    }
}
