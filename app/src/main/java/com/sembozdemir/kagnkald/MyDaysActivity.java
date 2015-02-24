package com.sembozdemir.kagnkald;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;


public class MyDaysActivity extends ActionBarActivity {
    private static final int DATE_DIALOG_ID = 999;

    private ListView mListView;
    private TextView mTextView;
    private DBHelper dbHelper;
    private MyDate mDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_days);

    }

    @Override
    protected void onResume() {
        super.onResume();

        mDate = new MyDate("01.01.2015");

        // initialize View components
        mTextView = (TextView) findViewById(R.id.textViewEmpty);
        mListView = (ListView) findViewById(R.id.listViewMyDays);

        // opening db
        dbHelper = new DBHelper(getApplicationContext());

        // todo: set visibility of empty text
        mTextView.setVisibility(View.INVISIBLE);

        // update View
        updateListView();

    }

    private void updateListView() {
        List<DBDate> listDates = dbHelper.getAllDates();
        MyDate currentDate = (MyDate) getIntent().getSerializableExtra("currentDate");
        MyListAdapter adapter = new MyListAdapter(this, listDates, currentDate);
        mListView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_days, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_new:
                showDialog(DATE_DIALOG_ID);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            if (view.isShown()) {
                mDate.setDateTime(year, monthOfYear + 1, dayOfMonth);
                // TODO: dialogla açıklama eklenmesi istenmeli
                addNewDate();
            }
        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker
                return new DatePickerDialog(this, datePickerListener,
                        mDate.getYear(), mDate.getMonth() - 1, mDate.getDay());
        }
        return null;
    }

    private void addNewDate() {
        // add the date into the database
        DBDate dbDate = new DBDate(mDate.toString(), "Açıklama ekleyin");
        dbHelper.insertDate(dbDate);
        updateListView();
    }
}
