package com.demoapp.ezetaptestapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demoapp.ezetaptestapp.model.ResultModel;

import java.util.ArrayList;

public class ResultActivity extends Activity {

    LinearLayout lnLayoutParent;

    private ArrayList<ResultModel> results = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        results = getIntent().getParcelableArrayListExtra("result");

        initUI();

    }

    private void initUI() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setContentInsetsAbsolute(0, 0);

        TextView txtHeader = (TextView) findViewById(R.id.txt_title);
        txtHeader.setText(R.string.result_activity_header);

        lnLayoutParent = (LinearLayout) findViewById(R.id.ln_layout_result);

        ImageButton iBtnBack = (ImageButton) findViewById(R.id.ibtn_back);
        iBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        displayResult();

    }

    private void displayResult() {

        for (int i = 0; i < results.size(); i++) {


            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            lparams.setMargins(10, 10, 10, 10);


            TextView tv = new TextView(this);
            tv.setLayoutParams(lparams);

            tv.setTextColor(getResources().getColor(android.R.color.black));

            tv.setText(results.get(i).getKey() + ": " + results.get(i).getValue());

            this.lnLayoutParent.addView(tv);
        }

    }

}
