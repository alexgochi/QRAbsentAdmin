package com.android.example.qrabsentadmin;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.example.qrabsentadmin.app.AppConfig;
import com.android.example.qrabsentadmin.helper.HttpParse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ShowSingleActivity extends AppCompatActivity {

    HttpParse httpParse = new HttpParse();
    ProgressDialog pDialog;

    // Http Url For Filter Seminar Data from Id Sent from previous activity.
    String HttpURL = AppConfig.URL_FILTER;

    // Http URL for delete Already Open Seminar Record.
    String HttpUrlDeleteRecord = AppConfig.URL_DELETE;

    String finalResult;
    HashMap<String, String> hashMap = new HashMap<>();
    String ParseResult;
    HashMap<String, String> ResultHash = new HashMap<>();
    String FinalJSonObject;
    TextView NAME, DATE, DESCRIPTION;
    String NameHolder, DateHolder, DescriptionHolder;
    Button UpdateButton, DeleteButton;
    String TempItem;
    ProgressDialog progressDialog2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_single);

        NAME = (TextView) findViewById(R.id.textName);
        DATE = (TextView) findViewById(R.id.textDate);
        DESCRIPTION = (TextView) findViewById(R.id.textDescription);

        UpdateButton = (Button) findViewById(R.id.buttonUpdate);
        DeleteButton = (Button) findViewById(R.id.buttonDelete);

        //Receiving the ListView Clicked item value send by previous activity.
        TempItem = getIntent().getStringExtra("ListViewValue");

        //Calling method to filter Seminar Record and open selected record.
        HttpWebCall(TempItem);


        UpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ShowSingleActivity.this, UpdateActivity.class);

                // Sending Student Id, Name, Number and Class to next UpdateActivity.
                intent.putExtra("Id", TempItem);
                intent.putExtra("Name", NameHolder);
                intent.putExtra("Date", DateHolder);
                intent.putExtra("Description", DescriptionHolder);

                startActivity(intent);

                // Finishing current activity after opening next activity.
                finish();

            }
        });

        // Add Click listener on Delete button.
        DeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Calling Student delete method to delete current record using Student ID.
                StudentDelete(TempItem);

            }
        });

    }

    // Method to Delete Student Record
    public void StudentDelete(final String StudentID) {

        @SuppressLint("StaticFieldLeak")
        class StudentDeleteClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog2 = ProgressDialog.show(ShowSingleActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog2.dismiss();

                Toast.makeText(ShowSingleActivity.this, httpResponseMsg, Toast.LENGTH_LONG).show();

                finish();

            }

            @Override
            protected String doInBackground(String... params) {

                // Sending SEMINAR id.
                hashMap.put("id", params[0]);

                finalResult = httpParse.postRequest(hashMap, HttpUrlDeleteRecord);

                return finalResult;
            }
        }

        StudentDeleteClass studentDeleteClass = new StudentDeleteClass();

        studentDeleteClass.execute(StudentID);
    }


    //Method to show current record Current Selected Record
    public void HttpWebCall(final String PreviousListViewClickedItem) {

        @SuppressLint("StaticFieldLeak")
        class HttpWebCallFunction extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                pDialog = ProgressDialog.show(ShowSingleActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                pDialog.dismiss();

                //Storing Complete JSon Object into String Variable.
                FinalJSonObject = httpResponseMsg;

                //Parsing the Stored JSOn String to GetHttpResponse Method.
                new GetHttpResponse(ShowSingleActivity.this).execute();

            }

            @Override
            protected String doInBackground(String... params) {

                ResultHash.put("id", params[0]);

                ParseResult = httpParse.postRequest(ResultHash, HttpURL);

                return ParseResult;
            }
        }

        HttpWebCallFunction httpWebCallFunction = new HttpWebCallFunction();

        httpWebCallFunction.execute(PreviousListViewClickedItem);
    }


    // Parsing Complete JSON Object.
    @SuppressLint("StaticFieldLeak")
    private class GetHttpResponse extends AsyncTask<Void, Void, Void> {
        Context context;

        GetHttpResponse(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                if (FinalJSonObject != null) {
                    JSONArray jsonArray;

                    try {
                        jsonArray = new JSONArray(FinalJSonObject);

                        JSONObject jsonObject;

                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);

                            // Storing Student Name, Phone Number, Class into Variables.
                            NameHolder = jsonObject.getString("name");
                            DateHolder = jsonObject.getString("date");
                            DescriptionHolder = jsonObject.getString("description");

                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            // Setting Student Name, Phone Number, Class into TextView after done all process .
            NAME.setText(NameHolder);
            DATE.setText(DateHolder);
            DESCRIPTION.setText(DescriptionHolder);

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK || super.onKeyDown(keyCode, event);
    }
}
