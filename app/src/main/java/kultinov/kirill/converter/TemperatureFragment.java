package kultinov.kirill.converter;


import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Temperature fragment is used  to populate view of the temperature conversion section and
 * handle all calculations between currently chosen units
 */
public class TemperatureFragment extends Fragment {

    private static final ArrayList<String> tempUnits = new ArrayList<>();
    EditText valueFrom;
    EditText valueTo;
    Spinner spinnerFrom;
    Spinner spinnerTo;
    private int flag;
    DecimalFormat round = new DecimalFormat ("###.##");
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //initialize items of the view that belongs to temperature fragment and set their default state and behaviour
        final View time_fragment = inflater.inflate(R.layout.fragmet_convert, container, false);
        spinnerFrom = (Spinner) time_fragment.findViewById(R.id.fromSpinner);
        spinnerTo = (Spinner) time_fragment.findViewById(R.id.toSpinner);
        final ImageButton swap = (ImageButton) time_fragment.findViewById(R.id.swap);
        final TextView from = (TextView) time_fragment.findViewById(R.id.from_prompt);
        from.setText("from:");
        final TextView to = (TextView) time_fragment.findViewById(R.id.to_prompt);
        to.setText("to:");
        valueFrom = (EditText) time_fragment.findViewById(R.id.edit_from);
        valueTo = (EditText) time_fragment.findViewById(R.id.edit_to);
        valueFrom.setText(Double.toString(0));
        valueTo.setText(Double.toString(0));
        valueTo.setKeyListener(null);
        valueTo.setFocusable(false);
        valueTo.setClickable(false);
        /**
         * empty array list to prevent it from adding multiple instances of the same items
         * when transactions among fragments happen
         */
        tempUnits.clear();
        //add temperature units
        tempUnits.add("Celsius");
        tempUnits.add("Fahrenheit");
        tempUnits.add("Kelvin");

        //set adapter for the spinner views
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_row, tempUnits);
        spinnerFrom.setAdapter(spinnerAdapter);
        spinnerTo.setAdapter(spinnerAdapter);
        spinnerFrom.setSelection(0);
        spinnerTo.setSelection(1);


        //listen for changes in the "from" section of the EditText view
        valueFrom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    calculateFrom(valueFrom, spinnerFrom.getSelectedItem().toString(), spinnerTo.getSelectedItem().toString());
            }
        });
        //listen for changes in the "from" section of the Spinner view
        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                calculateFrom(valueFrom, spinnerFrom.getSelectedItem().toString(), spinnerTo.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        //listen for changes in the "to" section of the Spinner view
        spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                calculateFrom(valueFrom, spinnerFrom.getSelectedItem().toString(), spinnerTo.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        //swap all values in the "to" and "from" section
        swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String to = spinnerTo.getSelectedItem().toString();
                Double valTo = Double.parseDouble(valueTo.getText().toString());
                String toAfter = spinnerFrom.getSelectedItem().toString();
                //swap
                spinnerFrom.setSelection(tempUnits.indexOf(to));
                valueFrom.setText(Double.toString(valTo));
                spinnerTo.setSelection(tempUnits.indexOf(toAfter));
            }
        });

        return time_fragment;
    }
    /**
     * calculateFrom function is used to set the result
     * of corresponding conversion inside the target EditText view
     * @param from is a parameter used to get the current value that needs to be converted
     * @param strFrom is a parameter contains the type of original units
     * @param strTo is a parameter contains the type of target units
     */
    private void calculateFrom(EditText from, String strFrom, String strTo){
        if(!from.getText().toString().equals("") && !from.getText().toString().equals("-.")
                && !from.getText().toString().equals("-") && !from.getText().toString().equals(".")) {
            Double valFrom = Double.parseDouble(from.getText().toString());

            if (strFrom.equals("Celsius") && strTo.equals("Celsius")) {
                valueTo.setText(valFrom.toString());
            } else if (strFrom.equals("Celsius") && strTo.equals("Fahrenheit")) {
                valueTo.setText(Double.toString(Double.valueOf(round.format(celToFar(valFrom)))));
            } else if (strFrom.equals("Celsius") && strTo.equals("Kelvin")) {
                valueTo.setText(Double.toString(Double.valueOf(round.format(celToKel(valFrom)))));
            } else if (strFrom.equals("Fahrenheit") && strTo.equals("Celsius")) {
                valueTo.setText(Double.toString(Double.valueOf(round.format(farToCel(valFrom)))));
            } else if (strFrom.equals("Fahrenheit") && strTo.equals("Fahrenheit")) {
                valueTo.setText(valFrom.toString());
            } else if (strFrom.equals("Fahrenheit") && strTo.equals("Kelvin")) {
                valueTo.setText(Double.toString(Double.valueOf(round.format(farToKel(valFrom)))));
            } else if (strFrom.equals("Kelvin") && strTo.equals("Celsius")) {
                valueTo.setText(Double.toString(Double.valueOf(round.format(kelToCel(valFrom)))));
            } else if (strFrom.equals("Kelvin") && strTo.equals("Fahrenheit")) {
                valueTo.setText(Double.toString(Double.valueOf(round.format(kelToFar(valFrom)))));
            } else if (strFrom.equals("Kelvin") && strTo.equals("Kelvin")) {
                valueTo.setText(valFrom.toString());
            }
        }

    }

    /**
     * Function celToFar converts Celsius to Fahrenheit
     * @param valFrom represents value in Celsius as a Double
     * @return degrees in Fahrenheit as a Double
     */
    private double celToFar(double valFrom){
        double result = (valFrom * 9/5.0) +32;
        return result;
    }
    /**
     * Function farToCel converts Fahrenheit to Celsius
     * @param valFrom represents value in Fahrenheit as a Double
     * @return degrees in Celsius as a Double
     */
    private double farToCel(double valFrom){
        double result = (valFrom - 32) * 5 / 9;
        return result;
    }
    /**
     * Function celToKel converts Celsius to Kelvin
     * @param valFrom represents value in Celsius as a Double
     * @return degrees in Kelvin as a Double
     */
    private double celToKel(double valFrom){
        double result = valFrom + 273.15;
        return result;
    }
    /**
     * Function kelToCel converts Kelvin to Celsius
     * @param valFrom represents value in Kelvin as a Double
     * @return degrees in Celsius as a Double
     */
    private double kelToCel(double valFrom){
        double result = valFrom - 273.15;
        return result;
    }
    /**
     * Function farToKel converts Fahrenheit to Kelvin
     * @param valFrom represents value in Fahrenheit as a Double
     * @return degrees in Kelvin as a Double
     */
    private double farToKel(double valFrom){
        double result = (valFrom + 459.67) * 5 / 9;
        return result;
    }
    /**
     * Function kelToFar converts Kelvin to Fahrenheit
     * @param valFrom represents value in Kelvin as a Double
     * @return degrees in Fahrenheit as a Double
     */
    private double kelToFar(double valFrom){
        double result = (valFrom * 9 / 5) - 459.67;
        return result;
    }


}
