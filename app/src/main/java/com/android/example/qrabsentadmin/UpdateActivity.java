package com.android.example.qrabsentadmin;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.example.qrabsentadmin.app.AppConfig;
import com.android.example.qrabsentadmin.helper.HttpParse;

import java.util.HashMap;

public class UpdateActivity extends AppCompatActivity {

    String HttpURL = AppConfig.URL_UPDATE;
    ProgressDialog progressDialog;
    String finalResult;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    EditText SeminarName, SeminarDate, SeminarDescription;
    Button UpdateStudent;
    String IdHolder, NameHolder, DateHolder, DescriptionHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        SeminarName = (EditText) findViewById(R.id.editName);
        SeminarDate = (EditText) findViewById(R.id.editDate);
        SeminarDescription = (EditText) findViewById(R.id.editDescription);

        UpdateStudent = (Button) findViewById(R.id.UpdateButton);

        // Receive Student ID, Name , Phone Number , Class Send by previous ShowSingleRecordActivity.
        IdHolder = getIntent().getStringExtra("Id");
        NameHolder = getIntent().getStringExtra("Name");
        DateHolder = getIntent().getStringExtra("Date");
        DescriptionHolder = getIntent().getStringExtra("Description");

        // Setting Received Student Name, Phone Number, Class into EditText.
        SeminarName.setText(NameHolder);
        SeminarDate.setText(DateHolder);
        SeminarDescription.setText(DescriptionHolder);

        // Adding click listener to update button .
        UpdateStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Getting data from EditText after button click.
                GetDataFromEditText();

                // Sending Student Name, Phone Number, Class to method to update on server.
                SeminarRecordUpdate(IdHolder, NameHolder, DateHolder, DescriptionHolder);

                Intent intent = new Intent(UpdateActivity.this, ShowActivity.class);
                startActivity(intent);

            }
        });
    }

    // Method to get existing data from EditText.
    public void GetDataFromEditText() {

        NameHolder = SeminarName.getText().toString();
        DateHolder = SeminarDate.getText().toString();
        DescriptionHolder = SeminarDescription.getText().toString();

    }

    // Method to Update Student Record.
    public void SeminarRecordUpdate(final String idData, final String nameData, final String dateData, final String descriptionData) {

        @SuppressLint("StaticFieldLeak")
        class seminarRecordUpdateClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(UpdateActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                Toast.makeText(UpdateActivity.this, "Seminar Updated", Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("idEdited", params[0]);

                hashMap.put("nameEdited", params[1]);

                hashMap.put("dateEdited", params[2]);

                hashMap.put("descriptionEdited", params[3]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        seminarRecordUpdateClass seminarRecordUpdateClass = new seminarRecordUpdateClass();

        seminarRecordUpdateClass.execute(idData, nameData, dateData, descriptionData);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK || super.onKeyDown(keyCode, event);
    }
}
