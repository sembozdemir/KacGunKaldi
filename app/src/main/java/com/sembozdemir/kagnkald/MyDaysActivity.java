package com.sembozdemir.kagnkald;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;


public class MyDaysActivity extends ActionBarActivity {
    private static final int NEW_DIALOG_ID = 999;
    private static final int EDIT_DIALOG_ID = 998;

    private ListView mListView;
    private MyListAdapter mAdapter;
    private TextView mTextView;
    private DBHelper dbHelper;
    private MyDate mDate;
    private MyDate mCurrentDate;
    private String mDescription;
    private DBDate oldDbDate, newDbDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_days);

    }

    @Override
    protected void onResume() {
        super.onResume();

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1; // In calendar january is month 0, but in jodatime january is month 1
        int day = c.get(Calendar.DAY_OF_MONTH);

        mCurrentDate = new MyDate(year, month, day);
        mDate = mCurrentDate;
        mDescription = getString(R.string.default_description_text);

        // initialize View components
        mTextView = (TextView) findViewById(R.id.textViewEmpty);
        mListView = (ListView) findViewById(R.id.listViewMyDays);

        // opening db
        dbHelper = new DBHelper(getApplicationContext());

        // todo: set visibility of empty text
        mTextView.setVisibility(View.INVISIBLE);

        // update View
        updateListView();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                oldDbDate = mAdapter.getItem(position);
                removeDialog(EDIT_DIALOG_ID);
                showDialog(EDIT_DIALOG_ID);
            }
        });

        mListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                // Capture total checked items
                final int checkedCount = mListView.getCheckedItemCount();
                // Set the CAB title according to total checked items
                mode.setTitle(checkedCount + getString(R.string.context_menu_x_items_selected));
                // Calls toggleSelection method from ListViewAdapter Class
                mAdapter.toggleSelection(position);
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {

                mode.getMenuInflater().inflate(R.menu.menu_my_days_choice_mode, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        // Calls getSelectedIds method from ListViewAdapter Class
                        SparseBooleanArray selected = mAdapter.getSelectedIds();
                        // Captures all selected ids with a loop
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                DBDate selecteditem = (DBDate) mAdapter.getItem(selected.keyAt(i));
                                // Remove selected items following the ids
                                dbHelper.deleteDate(selecteditem);
                            }
                        }
                        updateListView();
                        // Close CAB
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                mAdapter.removeSelection();
            }


        });



    }

    private void updateListView() {
        List<DBDate> listDates = dbHelper.getAllDates();
        mAdapter = new MyListAdapter(this.getApplicationContext(), R.layout.listview_row, listDates);
        mListView.setAdapter(mAdapter);
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
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
                removeDialog(NEW_DIALOG_ID);
                showDialog(NEW_DIALOG_ID);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            if (view.isShown()) {
                mDate.setDateTime(year, monthOfYear + 1, dayOfMonth);
                addNewDate();
            }
        }
    };*/

    /*@Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        // Inflate Layout
        LayoutInflater li = LayoutInflater.from(this);
        View layoutDialog = li.inflate(R.layout.dialog_new_date_description_and_datepicker, null);

        final EditText editText = (EditText) layoutDialog.findViewById(R.id.editTextDialogDatePickerDescription);
        DatePicker datePicker = (DatePicker) layoutDialog.findViewById(R.id.datePicker);

        switch (id) {
            case NEW_DIALOG_ID:
                dialogBuilder.setTitle(getString(R.string.new_date_dialog_title));

                // Set the layout into dialog
                dialogBuilder.setView(layoutDialog);

                dialogBuilder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // cancel
                    }
                });

                dialogBuilder.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // OK
                        mDescription = editText.getText().toString();
                        addNewDate();
                    }
                });

                final Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int monthOfYear = cal.get(Calendar.MONTH);
                int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

                datePicker.init(year, monthOfYear, dayOfMonth, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (view.isShown()) {
                            // date değiştiğinde
                            mDate.setDateTime(year, monthOfYear + 1, dayOfMonth);
                        }
                    }
                });

                dialog = dialogBuilder.create();
                break;

            case EDIT_DIALOG_ID:
                dialogBuilder.setTitle(getString(R.string.edit_date_dialog_title));

                // Set the layout into dialog
                dialogBuilder.setView(layoutDialog);

                dialogBuilder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // cancel
                    }
                });

                dialogBuilder.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // OK
                        mDescription = editText.getText().toString();
                        editDate();
                    }
                });

                // edittext'te açıklama yazsın.
                editText.setText(oldDbDate.getDescription());

                mDate.setDateTime(oldDbDate.getDate());
                year = mDate.getYear();
                monthOfYear = mDate.getMonth() - 1;
                dayOfMonth = mDate.getDay();

                datePicker.init(year, monthOfYear, dayOfMonth, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (view.isShown()) {
                            // date değiştiğinde
                            mDate.setDateTime(year, monthOfYear + 1, dayOfMonth);
                        }
                    }
                });

                dialog = dialogBuilder.create();
                break;
        }

    }*/

    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog alertDialog = null;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        // Inflate Layout
        LayoutInflater li = LayoutInflater.from(this);
        View layoutDialog = li.inflate(R.layout.dialog_new_date_description_and_datepicker, null);

        final EditText editText = (EditText) layoutDialog.findViewById(R.id.editTextDialogDatePickerDescription);
        DatePicker datePicker = (DatePicker) layoutDialog.findViewById(R.id.datePicker);

        switch (id) {
            case NEW_DIALOG_ID:
                dialogBuilder.setTitle(getString(R.string.new_date_dialog_title));

                // Set the layout into dialog
                dialogBuilder.setView(layoutDialog);

                dialogBuilder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // cancel
                    }
                });

                dialogBuilder.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // OK
                        mDescription = editText.getText().toString();
                        addNewDate();
                    }
                });

                final Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int monthOfYear = cal.get(Calendar.MONTH);
                int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

                datePicker.init(year, monthOfYear, dayOfMonth, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (view.isShown()) {
                            // date değiştiğinde
                            mDate.setDateTime(year, monthOfYear + 1, dayOfMonth);
                        }
                    }
                });

                alertDialog = dialogBuilder.create();

                return alertDialog;

            case EDIT_DIALOG_ID:
                dialogBuilder.setTitle(getString(R.string.edit_date_dialog_title));

                // Set the layout into dialog
                dialogBuilder.setView(layoutDialog);

                dialogBuilder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // cancel
                    }
                });

                dialogBuilder.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // OK
                        mDescription = editText.getText().toString();
                        editDate();
                    }
                });

                // edittext'te açıklama yazsın.
                editText.setText(oldDbDate.getDescription());
                editText.setSelection(editText.getText().length());

                mDate.setDateTime(oldDbDate.getDate());
                year = mDate.getYear();
                monthOfYear = mDate.getMonth() - 1;
                dayOfMonth = mDate.getDay();

                datePicker.init(year, monthOfYear, dayOfMonth, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (view.isShown()) {
                            // date değiştiğinde
                            mDate.setDateTime(year, monthOfYear + 1, dayOfMonth);
                        }
                    }
                });

                alertDialog = dialogBuilder.create();

                return alertDialog;

        }
        return null;
    }

    private void editDate() {
        newDbDate = new DBDate(mDate.toString(), mDescription);
        dbHelper.updateDate(oldDbDate, newDbDate);
        updateListView();
    }

    private void addNewDate() {
        // add the date into the database
        DBDate dbDate = new DBDate(mDate.toString(), mDescription);
        dbHelper.insertDate(dbDate);
        updateListView();
    }
}
