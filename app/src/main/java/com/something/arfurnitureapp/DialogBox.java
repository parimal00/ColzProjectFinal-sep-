package com.something.arfurnitureapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatDialogFragment;

public class DialogBox extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.dialoglayout,null);
        builder.setView(view)
                .setTitle("waaaaa");


        return  builder.create();



    }
}
