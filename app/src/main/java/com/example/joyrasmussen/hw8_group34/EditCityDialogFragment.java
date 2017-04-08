package com.example.joyrasmussen.hw8_group34;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;


/**
 * Created by clay on 4/8/17.
 */

public class EditCityDialogFragment extends DialogFragment {

    EditText setCityET;
    EditText setCountryET;
    NoticeDialogListener mListener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layou
        //
        View view = inflater.inflate(R.layout.enter_city_details, null);

        builder.setView(view)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditCityDialogFragment.this.getDialog().cancel();
                    }
                })
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        String city = setCityET.getText().toString();
                        String country = setCountryET.getText().toString();

                        mListener.onDialogPositiveClick(EditCityDialogFragment.this, city, country);


                    }
                });

        setCityET = (EditText) view.findViewById(R.id.enterCityField);
        setCountryET = (EditText) view.findViewById(R.id.enterCountryField);

        return builder.create();

    }

    public interface NoticeDialogListener {

        public void onDialogPositiveClick(DialogFragment dialog, String city, String country);
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);

        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        } catch (Exception e){
            Log.i("exception", " here");
        }

    }
}
