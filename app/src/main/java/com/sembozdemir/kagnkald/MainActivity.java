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

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.Calendar;


public class MainActivity extends ActionBarActivity {

    private static final int DATE1_DIALOG_ID = 999;
    private static final int DATE2_DIALOG_ID = 998;
    private TextView mTextViewDate1;
    private TextView mTextViewDate2;
    private TextView mTextViewResult;

    int mYear,mMonth,mDay;
    int mYear2,mMonth2,mDay2;

    DateTime start, end;

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

        //initiliaze DateTime start and end
        setDateTimes();

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

    private void setDateTimes() {
        start = new DateTime(mYear,mMonth,mDay,0,0,0);
        end = new DateTime(mYear2,mMonth2,mDay2,0,0,0);
        setResult();
    }

    private void setResult() {
        int days = Days.daysBetween(start, end).getDays();
        String s = Integer.toString(days);
        mTextViewResult.setText(s);
    }

    private void setCurrentDate() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mYear2 = c.get(Calendar.YEAR);
        mMonth2 = c.get(Calendar.MONTH);
        mDay2 = c.get(Calendar.DAY_OF_MONTH);

        // Textviewları ayarla
        mTextViewDate1.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(mMonth + 1).append("-").append(mDay).append("-")
                .append(mYear).append(" "));

        mTextViewDate2.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(mMonth2 + 1).append("-").append(mDay2).append("-")
                .append(mYear2).append(" "));
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
                // set date1 picker as current date
                return new DatePickerDialog(this, datePickerListener1,
                        mYear, mMonth,mDay);
            case DATE2_DIALOG_ID:
                // set date2 picker as current date
                return new DatePickerDialog(this, datePickerListener2,
                        mYear2, mMonth2,mDay2);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            setDateTimes();
            mTextViewDate1.setText(new StringBuilder()
                    // Month is 0 based, just add 1
                    .append(monthOfYear + 1).append("-").append(dayOfMonth).append("-")
                    .append(year).append(" "));
        }
    };

    private DatePickerDialog.OnDateSetListener datePickerListener2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear2 = year;
            mMonth2 = monthOfYear;
            mDay2 = dayOfMonth;
            setDateTimes();
            mTextViewDate2.setText(new StringBuilder()
                    // Month is 0 based, just add 1
                    .append(monthOfYear + 1).append("-").append(dayOfMonth).append("-")
                    .append(year).append(" "));
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
