package com.sembozdemir.kagnkald;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.joda.time.Days;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Semih Bozdemir on 24.2.2015.
 */
public class MyListAdapter extends ArrayAdapter<DBDate> {
    Context mContext;
    LayoutInflater inflater;
    private List<DBDate> mDateList;
    private MyDate mCurDate;
    private SparseBooleanArray mSelectedItemsIds;

    public MyListAdapter(Context context, int resourceId, List<DBDate> dates) {
        super(context, resourceId, dates);
        mSelectedItemsIds = new SparseBooleanArray();
        mContext = context;
        mDateList = dates;
        inflater = LayoutInflater.from(context);

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1; // In calendar january is month 0, but in jodatime january is month 1
        int day = c.get(Calendar.DAY_OF_MONTH);

        mCurDate = new MyDate(year, month, day);
    }

    /*public MyListAdapter(Activity activity, List<DBDate> dates, MyDate currentDate) {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDateList = dates;
        mCurDate = currentDate;
    }*/

    @Override
    public int getCount() {
        return mDateList.size();
    }

    @Override
    public DBDate getItem(int position) {
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
        int days = Days.daysBetween(date.getDateTime(), mCurDate.getDateTime()).getDays();
        txtResult.setText(String.valueOf(Math.abs(days)));

        return vi;
    }


    // MultiChoiceListener için
    public void remove(DBDate object) {
        mDateList.remove(object);
        notifyDataSetChanged();
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public List<DBDate> getDateList() {
        return mDateList;
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }
}
