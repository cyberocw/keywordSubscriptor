package com.creativeoh.keywordsubscriptor.keyword.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import com.crashlytics.android.Crashlytics;
import com.creativeoh.keywordsubscriptor.Const;
import com.creativeoh.keywordsubscriptor.R;
import com.creativeoh.keywordsubscriptor.util.CommonUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.ButterKnife;

/**
 * Created by cyber on 2017-07-08.
 */

public class KeywrordCalendarDialog extends DialogFragment {
    Calendar mCalendar;
    NumberPicker mNumberPickerHour;
    NumberPicker mNumberPickerMinute;
    CalendarView mCalendarView;
    String mSelectedDate ;
    String mStrToday;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        android.support.v7.app.AlertDialog.Builder b = new android.support.v7.app.AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.dialog_alarm_date_pick))
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                returnData();
                            }
                        }
                )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                );

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.framgent_dialog_keyword_calendar, null);

        Bundle bundle = getArguments();
        if(bundle != null){
            mCalendar = (Calendar) bundle.getSerializable("selectedDate");
        }

        mNumberPickerHour = ButterKnife.findById(view, R.id.numberPickerHour);
        mNumberPickerMinute = ButterKnife.findById(view, R.id.numberPickerMinute);
        mCalendarView = ButterKnife.findById(view, R.id.datePicker);

        final Calendar today = Calendar.getInstance();
        //mCalendarView.setMinDate();
        mStrToday = CommonUtils.convertDateType(today); //today.get(Calendar.YEAR) + "" + today.get(Calendar.MONTH) + today.get(Calendar.DAY_OF_MONTH);

        String minDate = "2017/07/11";
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        try {
            mCalendarView.setMinDate(format.parse(minDate).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mCalendarView.setMaxDate(today.getTimeInMillis());

        if(mCalendar.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH))
            mNumberPickerHour.setMaxValue(today.get(Calendar.HOUR_OF_DAY));
        else
            mNumberPickerHour.setMaxValue(23);


        setTimePickMinuteDefault();

        mSelectedDate = CommonUtils.convertDateType(mCalendar);// mCalendar.get(Calendar.YEAR) + "" + mCalendar.get(Calendar.MONTH) + mCalendar.get(Calendar.DAY_OF_MONTH);

        Crashlytics.log(Log.DEBUG, this.toString(), "mStrToday="+mStrToday);

        mCalendarView.setDate(mCalendar.getTime().getTime());
        mNumberPickerHour.setValue(mCalendar.get(Calendar.HOUR_OF_DAY));
        if((int) mCalendar.get(Calendar.MINUTE) == 30)
            mNumberPickerMinute.setValue(1);
        else
            mNumberPickerMinute.setValue(0);

       mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                mSelectedDate = year + "" + CommonUtils.numberDigit(2, month+1) + CommonUtils.numberDigit(2, dayOfMonth);
                Crashlytics.log(Log.DEBUG, this.toString(), "mSelectedDate="+mSelectedDate);

                setTimePickValue(today);
            }
        });


        mNumberPickerHour.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                setTimePickValue(today);
            }
        });

        b.setView(view);
        Dialog dialog = b.create();
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        return dialog;

    }

    private void setTimePickValue(Calendar today){
        int newH = mNumberPickerHour.getValue();
        int h = today.get(Calendar.HOUR_OF_DAY);

        setTimePickMinuteDefault();

        Crashlytics.log(Log.DEBUG, this.toString(), "mStrToday = " + mStrToday +  "  mSelectedDate =" + mSelectedDate);

        if(mStrToday.equals(mSelectedDate)) {
            if (h <= newH && today.get(Calendar.MINUTE) < 30) {
                String[] vals = new String[]{"0"};
                mNumberPickerMinute.setMaxValue(0);
                mNumberPickerMinute.setDisplayedValues(vals);
            }
            mNumberPickerHour.setMaxValue(today.get(Calendar.HOUR_OF_DAY));
        }
        else
            mNumberPickerHour.setMaxValue(23);
    }
    private void setTimePickMinuteDefault(){
        String[] vals = new String[]{"0", "30"};
        mNumberPickerMinute.setDisplayedValues(vals);
        mNumberPickerMinute.setMaxValue(1);
    }

    private void returnData(){
        Calendar resultCalendar = CommonUtils.convertDateType(mSelectedDate);
        resultCalendar.set(Calendar.HOUR_OF_DAY, mNumberPickerHour.getValue());
        int minute;
        if(mNumberPickerMinute.getValue() == 1)
            minute = 30;
        else
            minute = 0;
        resultCalendar.set(Calendar.MINUTE, minute);

        Bundle bundle = new Bundle();
        bundle.putSerializable("selectedDate", resultCalendar);
        Intent intent = new Intent();
        intent.putExtras(bundle);

        int returnCode = Const.KEYWORD.API.CALENDAR_INTERFACE_CODE;
        getTargetFragment().onActivityResult(getTargetRequestCode(), returnCode, intent);
    }
}
