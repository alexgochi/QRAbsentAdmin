package com.android.example.qrabsentadmin.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.example.qrabsentadmin.R;
import com.android.example.qrabsentadmin.app.Student;

import java.util.List;
import java.util.Objects;

public class ListAdapterClass extends BaseAdapter {

    private Context context;
    private List<Student> valueList;

    public ListAdapterClass(List<Student> listValue, Context context) {
        this.context = context;
        this.valueList = listValue;
    }

    @Override
    public int getCount() {
        return this.valueList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.valueList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewItem viewItem;

        if (convertView == null) {
            viewItem = new ViewItem();

            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = Objects.requireNonNull(layoutInflater).inflate(R.layout.list_seminar_layout, null);

            viewItem.TextViewStudentName = (TextView) Objects.requireNonNull(convertView).findViewById(R.id.textViewName);

            Objects.requireNonNull(convertView).setTag(viewItem);
        } else {
            viewItem = (ViewItem) convertView.getTag();
        }

        viewItem.TextViewStudentName.setText(valueList.get(position).SeminarName);

        return convertView;
    }
}

class ViewItem {
    TextView TextViewStudentName;

}