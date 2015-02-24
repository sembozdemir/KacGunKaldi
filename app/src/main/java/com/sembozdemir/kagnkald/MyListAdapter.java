package com.sembozdemir.kagnkald;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.joda.time.Days;

import java.util.List;

/**
 * Created by Semih Bozdemir on 24.2.2015.
 */
public class MyListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<DBDate> mDateList;
    private MyDate mCurrentDate;

    public MyListAdapter(Activity activity, List<DBDate> dates, MyDate currentDate) {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDateList = dates;
        mCurrentDate = currentDate;
    }

    @Override
    public int getCount() {
        return mDateList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDateList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.listview_row, null); // create layout from

        TextView txtDate = (TextView) vi.findViewById(R.id.row_textviewDate);
        TextView txtDescription = (TextView) vi.findViewById(R.id.row_textviewDescription);
        TextView txtResult = (TextView) vi.findViewById(R.id.row_textvieResult);

        DBDate dbDate = mDateList.get(position);
        txtDate.setText(dbDate.getDate());
        txtDescription.setText(dbDate.getDescription());

        // Calculate Result
        MyDate date = new MyDate(dbDate.getDate());
        int days = Days.daysBetween(date.getDateTime(), mCurrentDate.getDateTime()).getDays();
        txtResult.setText(String.valueOf(days));

        return vi;
    }
}
