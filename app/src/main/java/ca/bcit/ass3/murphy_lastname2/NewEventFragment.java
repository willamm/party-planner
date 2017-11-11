package ca.bcit.ass3.murphy_lastname2;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewEventFragment extends DialogFragment {

    private EditText nameInput;
    private EditText dateInput;
    private EditText timeInput;

    private SQLiteDatabase db;
    private Cursor cursor;

    public NewEventFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_new_event, container, false);

        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.title_fragment_event));

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        }
        setHasOptionsMenu(true);

        nameInput = rootView.findViewById(R.id.event_name);

        dateInput = rootView.findViewById(R.id.event_date);
        final Calendar cDate = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                cDate.set(Calendar.YEAR, year);
                cDate.set(Calendar.MONTH, month);
                cDate.set(Calendar.DAY_OF_MONTH, day);
                updateDateLabel(cDate);
            }
        };

        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), dateSetListener, cDate.get(Calendar.YEAR), cDate.get(Calendar.MONTH), cDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        timeInput = rootView.findViewById(R.id.event_time);
        final Calendar cTime = Calendar.getInstance();

        final TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                cTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                cTime.set(Calendar.MINUTE, minute);
                updateTimeLabel(cTime);
            }
        };

        timeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(getContext(), timeSetListener, cTime.get(Calendar.HOUR_OF_DAY), cTime.get(Calendar.MINUTE), DateFormat.is24HourFormat(getContext())).show();
            }
        });

        return rootView;
    }

    //updates the edit text label for the selected date
    private void updateDateLabel(Calendar c) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.US);
        dateInput.setText(sdf.format(c.getTime()));
    }

    //updates the edit text label for the selected time
    private void updateTimeLabel(Calendar c) {
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.US);
        timeInput.setText(sdf.format(c.getTime()));
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.create_event_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save) {
            // handle confirmation button click here
            if (!isEmpty(nameInput) && !isEmpty(dateInput) && !isEmpty(timeInput)) {
                String name = nameInput.getText().toString();
                String date = dateInput.getText().toString();
                String time = timeInput.getText().toString();
                insertNewEvent(name, date, time);
                db.close();
                dismiss();
            }
            return true;
        } else if (id == android.R.id.home) {
            // handle close button click here
            getActivity().onBackPressed();
            dismiss();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void insertNewEvent(String name, String date, String time) {
        SQLiteOpenHelper helper = PartyDbHelper.getInstance(getContext());
        db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PartyContract.EventMaster.NAME, name);
        values.put(PartyContract.EventMaster.DATE, date);
        values.put(PartyContract.EventMaster.TIME, time);

        db.insert(PartyContract.EventMaster.TABLE_NAME, null, values);
    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0) {
            return false;
        } else {
            Toast.makeText(getContext(), "Inputs cannot be empty.", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

}
