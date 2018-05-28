package com.android.example.qrabsentadmin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.android.example.qrabsentadmin.app.AppConfig;
import com.android.example.qrabsentadmin.app.Student;
import com.android.example.qrabsentadmin.helper.HttpServicesClass;
import com.android.example.qrabsentadmin.helper.ListAdapterClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShowActivity extends AppCompatActivity {
    ListView SeminarListView;
    ProgressBar progressBar;
    String HttpUrl = AppConfig.URL_SEMINAR;
    List<String> IdList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show);

        SeminarListView = (ListView) findViewById(R.id.show_seminar);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        new GetHttpResponse(ShowActivity.this).execute();

        //Adding ListView Item click Listener.
        SeminarListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // TODO Auto-generated method stub

                Intent intent = new Intent(ShowActivity.this, ShowSingleActivity.class);

                // Sending ListView clicked value using intent.
                intent.putExtra("ListViewValue", IdList.get(position));

                startActivity(intent);

                //Finishing current activity after open next activity.
                finish();

            }
        });
    }

    // JSON parse class started from here.
    @SuppressLint("StaticFieldLeak")
    private class GetHttpResponse extends AsyncTask<Void, Void, Void> {
        Context context;

        String JSonResult;

        List<Student> seminarList;

        GetHttpResponse(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Passing HTTP URL to HttpServicesClass Class.
            HttpServicesClass httpServicesClass = new HttpServicesClass(HttpUrl);
            try {
                httpServicesClass.ExecutePostRequest();

                if (httpServicesClass.getResponseCode() == 200) {
                    JSonResult = httpServicesClass.getResponse();

                    if (JSonResult != null) {
                        JSONArray jsonArray;

                        try {
                            jsonArray = new JSONArray(JSonResult);

                            JSONObject jsonObject;

                            Student seminarData;

                            seminarList = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                seminarData = new Student();

                                jsonObject = jsonArray.getJSONObject(i);

                                // Adding Student Id TO IdList Array.
                                IdList.add(jsonObject.getString("id"));

                                //Adding Student Name.
                                seminarData.SeminarName = jsonObject.getString("name");

                                seminarList.add(seminarData);

                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(context, httpServicesClass.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)

        {
            progressBar.setVisibility(View.GONE);

            SeminarListView.setVisibility(View.VISIBLE);

            ListAdapterClass adapter = new ListAdapterClass(seminarList, context);

            SeminarListView.setAdapter(adapter);

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK || super.onKeyDown(keyCode, event);
    }
}
