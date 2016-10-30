package com.example.karan.bookdemo;


import android.content.Context;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class CustDialog extends DialogFragment {

    private RadioGroup rg;
    private EditText et;
    private LinearLayout ll;
    private Button sbt,can;
    private RadioButton rb;
    private String action = "";
    private MyClickEvent myClickEvent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_cust_dialog, container, false);
        getDialog().setTitle("Action Dialog");
        rg = (RadioGroup) view.findViewById(R.id.rdgrp);
        et = (EditText) view.findViewById(R.id.udateprice);
        ll = (LinearLayout) view.findViewById(R.id.updatelayout);
        sbt = (Button) view.findViewById(R.id.subupdate);
        can = (Button) view.findViewById(R.id.cancelupdate);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                rb = (RadioButton) view.findViewById(id);
                action = (String) rb.getText();
               // Toast.makeText(getContext(),action,Toast.LENGTH_SHORT).show();
                if (action.equals("Update")){
                    ll.setVisibility(View.VISIBLE);
                }
                else {
                    ll.setVisibility(View.GONE);
                }
            }
        });

        sbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = rg.getCheckedRadioButtonId();
                rb = (RadioButton) view.findViewById(id);
                action = (String) rb.getText();
                String yp = et.getText().toString();
                boolean b = false;
                if (action.equals("Update")){
                    b=true;
                }
                myClickEvent.onsubmit1(b,yp);
                getDialog().dismiss();
            }
        });

        can.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });



        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myClickEvent = (MyClickEvent) context;
    }

    public interface MyClickEvent{
        public void onsubmit1(boolean update,String price);
    }

}
