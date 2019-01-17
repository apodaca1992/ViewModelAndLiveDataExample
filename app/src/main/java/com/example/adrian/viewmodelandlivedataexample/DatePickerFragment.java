package com.example.adrian.viewmodelandlivedataexample;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by adrian on 20/09/2016.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener,DatePickerDialog.OnCancelListener {

    private SimpleDateFormat dateFormatter;
    private int year;
    private int month;
    private int day;


    // TODO: Rename and change types and number of parameters
    public static DatePickerFragment newInstance(int year,int month,int day) {
        DatePickerFragment oDatePickerFragment = new DatePickerFragment();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("year", year);
        args.putInt("month", month);
        args.putInt("day", day);
        oDatePickerFragment.setArguments(args);
        return oDatePickerFragment;
    }

    public DatePickerFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Use the current date as the default date in the date picker
        if (getArguments() != null) {
            year = getArguments().getInt("year");
            month = getArguments().getInt("month");
            day = getArguments().getInt("day");
        }else{
            final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        }

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                R.style.DialogTheme,this,year,month,day);

        datepickerdialog.getDatePicker().setCalendarViewShown(false);
        datepickerdialog.getDatePicker().setMinimumHeight(230);

        //Obtenemos la View principal del datePicker y lo convertimos a LinearLayout
        LinearLayout llPrincipal = (LinearLayout) datepickerdialog.getDatePicker().getChildAt(0);
        LinearLayout llDatePicker = (LinearLayout) llPrincipal.getChildAt(0);

        //dia
        LinearLayout day = (LinearLayout) llDatePicker.getChildAt(0);
        setPropertiesOnPicker(day, false);
        //mes
        LinearLayout month = (LinearLayout) llDatePicker.getChildAt(1);
        setPropertiesOnPicker(month, false);
        //año
        LinearLayout year = (LinearLayout) llDatePicker.getChildAt(2);
        setPropertiesOnPicker(year, true);

        return datepickerdialog;

    }

    private void setPropertiesOnPicker(LinearLayout ll,Boolean prEnabled) {
        //Boton '+' del LinearLayout
        ImageButton btnMas = (ImageButton) ll.getChildAt(0);
        btnMas.setMinimumHeight(75);

        //EditText del LinearLayout
        EditText editText = (EditText) ll.getChildAt(1);
        editText.setEnabled(prEnabled);
        editText.setMinimumHeight(75);

        //Boton '-' del LinearLayout
        ImageButton btnMenos = (ImageButton) ll.getChildAt(2);
        btnMenos.setMinimumHeight(75);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        TextView tv = (TextView) getActivity().findViewById(R.id.txtFechaDeNacimiento);
        Calendar newDate = Calendar.getInstance();
        newDate.set(year, month, day);
        tv.setText(dateFormatter.format(newDate.getTime()));
        AgregarNuevaAfiliacion.inicializeVarDiaglogFragment();

    }
    public void onCancel(DialogInterface dialog){
        AgregarNuevaAfiliacion.inicializeVarDiaglogFragment();
    }
}
