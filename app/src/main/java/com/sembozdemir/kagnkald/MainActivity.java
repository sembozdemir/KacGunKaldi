package com.sembozdemir.kagnkald;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import org.joda.time.Days;

import java.util.Calendar;


public class MainActivity extends ActionBarActivity {

    private static final int DATE1_DIALOG_ID = 999;
    private static final int DATE2_DIALOG_ID = 998;
    private TextView mTextViewDate1;
    private TextView mTextViewDate2;
    private TextView mTextViewResult;

    private MyDate mStart, mEnd, mCurrentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initiliaze View components
        mTextViewDate1 = (TextView) findViewById(R.id.textViewDate1);
        mTextViewDate2 = (TextView) findViewById(R.id.textViewDate2);
        mTextViewResult = (TextView) findViewById(R.id.textViewResult);

        //initiliaze current date
        setCurrentDate();

        // Textviewları bugüne ayarla
        mTextViewDate1.setText(mCurrentDate.formatDateTR());

        mTextViewDate2.setText(mCurrentDate.formatDateTR());

        //initiliaze mStart and mEnd to current date
        setMyDates();

        // initialiaze Textview
        setResult();

        mTextViewDate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE1_DIALOG_ID);
            }
        });

        mTextViewDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE2_DIALOG_ID);
            }
        });

    }

    private void setMyDates() {
        mStart = new MyDate(mCurrentDate.getDate());
        mEnd = new MyDate(mCurrentDate.getDate());
    }

    private void setResult() {
        int days = Days.daysBetween(mStart.getDate(), mEnd.getDate()).getDays();
        String s = Integer.toString(Math.abs(days));
        mTextViewResult.setText(s);
    }

    private void setCurrentDate() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1; // In calendar january is month 0, but in jodatime january is month 1
        int day = c.get(Calendar.DAY_OF_MONTH);

        mCurrentDate = new MyDate(year, month, day);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE1_DIALOG_ID:
                // set date1 picker
                return new DatePickerDialog(this, datePickerListener1,
                        mStart.getYear(), mStart.getMonth() - 1, mStart.getDay());
            case DATE2_DIALOG_ID:
                // set date2 picker
                return new DatePickerDialog(this, datePickerListener2,
                       mEnd.getYear(), mEnd.getMonth() - 1, mEnd.getDay());
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mStart.setDate(year, monthOfYear + 1, dayOfMonth);

            mTextViewDate1.setText(mStart.formatDateTR());

            setResult();
        }
    };

    private DatePickerDialog.OnDateSetListener datePickerListener2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mEnd.setDate(year, monthOfYear + 1, dayOfMonth);

            mTextViewDate2.setText(mEnd.formatDateTR());

            setResult();
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_my_days) {
            /*Intent intent = new Intent(this, MyDaysActivity.class);
            startActivity(intent);*/
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void todayClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.imageViewToday1:
                mStart.setDate(mCurrentDate.getDate());
                mTextViewDate1.setText(mStart.formatDateTR());
                break;
            case R.id.imageViewToday2:
                mEnd.setDate(mCurrentDate.getDate());
                mTextViewDate2.setText(mEnd.formatDateTR());
                break;
        }

        setResult();
    }
}
