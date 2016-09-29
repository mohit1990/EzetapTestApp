package com.demoapp.ezetaptestapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.demoapp.ezetaptestapp.application.ApplicationController;
import com.demoapp.ezetaptestapp.model.ItemsModel;
import com.demoapp.ezetaptestapp.model.ResponseModel;
import com.demoapp.ezetaptestapp.model.ResultModel;
import com.demoapp.ezetaptestapp.utils.AppConstants;
import com.demoapp.ezetaptestapp.utils.NetworkUtils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    LinearLayout lnLayoutParent;

    ArrayList<View> inputViewsList = new ArrayList<>();

    private ResponseModel responseModel;

    private LinearLayout.LayoutParams layoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lnLayoutParent = (LinearLayout) findViewById(R.id.ln_layout_parent);

        getUiElements();

    }

    private void getUiElements() {

        if (!NetworkUtils.getInstance().isNetworkAvailable(ApplicationController.getInstance())){
            Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, AppConstants.REQUEST_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.v("EzeTap", "Response --> " + response.toString());

                Gson gson = new Gson();
                responseModel = gson.fromJson(response.toString(), ResponseModel.class);

                generateLayout();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        ApplicationController.getInstance().addToRequestQueue(jsonObjectRequest);


    }

    private void generateLayout() {

        layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(5, 5, 5, 5);

        if (responseModel.getItems().size() == 0) {
            return;
        }

        for (int i = 0; i < responseModel.getItems().size(); i++) {

            switch (responseModel.getItems().get(i).getItemType()) {

                case AppConstants.ITEM_TYPE_LABEL:
                    addLabel(responseModel.getItems().get(i));
                    break;

                case AppConstants.ITEM_TYPE_TEXT_BOX:
                    addText(responseModel.getItems().get(i), i);
                    break;

                case AppConstants.ITEM_TYPE_DROPDOWN:
                    addDropdown(responseModel.getItems().get(i), i);
                    break;

                case AppConstants.ITEM_TYPE_BUTTON:
                    addButton(responseModel.getItems().get(i));
                    break;

                default:
                    break;

            }
        }


    }

    private void addLabel(ItemsModel itemsModel) {

        TextView tv = new TextView(this);

        tv.setLayoutParams(layoutParams);
        tv.setTextColor(getResources().getColor(android.R.color.black));
        tv.setText(itemsModel.getName());

        this.lnLayoutParent.addView(tv);

    }

    private void addText(ItemsModel itemsModel, int position) {

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView textLabel = new TextView(this);
        textLabel.setLayoutParams(layoutParams);
        textLabel.setTextColor(getResources().getColor(android.R.color.black));
        textLabel.setText(itemsModel.getName() + " :");

        linearLayout.addView(textLabel);

        EditText etValue = new EditText(this);

        etValue.setLayoutParams(layoutParams);
        etValue.setMaxLines(1);
        etValue.setTextColor(getResources().getColor(android.R.color.black));

        if (itemsModel.getType().equalsIgnoreCase(AppConstants.INPUT_TYPE_TEXT)) {

            InputFilter[] FilterArray = new InputFilter[1];
            FilterArray[0] = new InputFilter.LengthFilter(itemsModel.getMaxLength());

            etValue.setFilters(FilterArray);
            etValue.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        } else if (itemsModel.getType().equalsIgnoreCase(AppConstants.INPUT_TYPE_PHONE)) {

            InputFilter[] FilterArray = new InputFilter[1];
            FilterArray[0] = new InputFilter.LengthFilter(itemsModel.getMaxLength());

            etValue.setFilters(FilterArray);
            etValue.setInputType(InputType.TYPE_CLASS_PHONE);

        } else if (itemsModel.getType().equalsIgnoreCase(AppConstants.INPUT_TYPE_NUMBER)) {

            InputFilter[] FilterArray = new InputFilter[1];
            FilterArray[0] = new InputFilter.LengthFilter(itemsModel.getMaxLength());

            etValue.setFilters(FilterArray);
            etValue.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        etValue.setId(position);

        responseModel.getItems().get(position).setViewId(position);

        inputViewsList.add(etValue);

        linearLayout.addView(etValue);

        this.lnLayoutParent.addView(linearLayout);

    }

    private void addDropdown(ItemsModel itemsModel, int position) {


        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView textLabel = new TextView(this);
        textLabel.setLayoutParams(layoutParams);
        textLabel.setTextColor(getResources().getColor(android.R.color.black));
        textLabel.setText(itemsModel.getName() + " :");

        linearLayout.addView(textLabel);

        Spinner spinner = new Spinner(this);

        spinner.setLayoutParams(layoutParams);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, itemsModel.getValues());
        spinner.setAdapter(spinnerAdapter);

        spinner.setId(position);
        inputViewsList.add(spinner);

        linearLayout.addView(spinner);

        this.lnLayoutParent.addView(linearLayout);
    }

    private void addButton(ItemsModel itemsModel) {

        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lparams.gravity = Gravity.CENTER;

        Button button = new Button(this);

        button.setLayoutParams(lparams);

        button.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        button.setTextColor(getResources().getColor(android.R.color.white));

        button.setText(itemsModel.getName());
        button.setGravity(Gravity.CENTER);
        this.lnLayoutParent.addView(button);

        button.setOnClickListener(new RegisterClickListener());


    }

    private class RegisterClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            ArrayList<ResultModel> result = new ArrayList<>();


            for (int i = 0; i < inputViewsList.size(); i++) {


                ResultModel resultModel = new ResultModel();

                resultModel.setKey(responseModel.getItems().get(inputViewsList.get(i).getId()).getName());


                if (inputViewsList.get(i) instanceof EditText) {
                    EditText editText = (EditText) inputViewsList.get(i);
                    resultModel.setValue(editText.getText().toString().trim());

                } else if (inputViewsList.get(i) instanceof Spinner) {
                    Spinner spinner = (Spinner) inputViewsList.get(i);
                    resultModel.setValue(spinner.getSelectedItem().toString());

                }


                result.add(resultModel);

            }

            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
            intent.putExtra("result", result);
            startActivity(intent);

        }
    }
}
