package kultinov.kirill.converter;


import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Weight fragment is used  to populate view of the temperature conversion section and
 * handle all calculations between currently chosen units
 */
public class WeightFragment extends Fragment {


    private static final ArrayList<String> tempUnits = new ArrayList<>();
    EditText valueFrom;
    EditText valueTo;
    Spinner spinnerFrom;
    Spinner spinnerTo;
    private int flag;
    DecimalFormat round = new DecimalFormat("###.##");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //initialize items of the view that belongs to time fragment and set their default state and behaviour
        final View time_fragment = inflater.inflate(R.layout.fragmet_convert, container, false);
        spinnerFrom = (Spinner) time_fragment.findViewById(R.id.fromSpinner);
        spinnerTo = (Spinner) time_fragment.findViewById(R.id.toSpinner);
        final ImageButton swap = (ImageButton) time_fragment.findViewById(R.id.swap);
        final TextView from = (TextView) time_fragment.findViewById(R.id.from_prompt);
        from.setText("from:");
        final TextView to = (TextView) time_fragment.findViewById(R.id.to_prompt);
        to.setText("to:");
        /**final TextView eqFrom = (TextView) time_fragment.findViewById(R.id.from_eq);
         eqFrom.setText("sample equation for 1 unit");
         final TextView eqTo = (TextView) time_fragment.findViewById(R.id.to_eq);
         eqTo.setText("sample equation for 1 unit");*/
        valueFrom = (EditText) time_fragment.findViewById(R.id.edit_from);
        valueTo = (EditText) time_fragment.findViewById(R.id.edit_to);
        valueFrom.setText(Double.toString(0));
        valueFrom.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        valueTo.setText(Double.toString(0));
        valueTo.setKeyListener(null);
        valueTo.setFocusable(false);
        valueTo.setClickable(false);
        /**
         * empty array list to prevent it from adding multiple instances of the same items
         * when transactions among fragments happen
         */
        tempUnits.clear();
        //add weight units
        tempUnits.add("kg");
        tempUnits.add("lb");
        tempUnits.add("oz");
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
                calculateFrom(valueFrom, valueTo, spinnerFrom.getSelectedItem().toString(), spinnerTo.getSelectedItem().toString());
            }
        });
        //listen for changes in the "from" section of the Spinner view
        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                calculateFrom(valueFrom, valueTo, spinnerFrom.getSelectedItem().toString(), spinnerTo.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        //listen for changes in the "to" section of the Spinner view
        spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                calculateFrom(valueFrom, valueTo, spinnerFrom.getSelectedItem().toString(), spinnerTo.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        //listen for swap button clicks
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
    private void calculateFrom(EditText from, EditText to, String strFrom, String strTo) {
        if (!from.getText().toString().equals("") && !from.getText().toString().equals("-.") && !from.getText().toString().equals("-") && !from.getText().toString().equals(".")) {
            Double valFrom = Double.parseDouble(from.getText().toString());

            if (strFrom.equals("kg") && strTo.equals("kg")) {
                valueTo.setText(valFrom.toString());
            } else if (strFrom.equals("kg") && strTo.equals("lb")) {
                valueTo.setText(Double.toString(Double.valueOf(round.format(kgToLb(valFrom)))));
            } else if (strFrom.equals("kg") && strTo.equals("oz")) {
                valueTo.setText(Double.toString(Double.valueOf(round.format(kgToOz(valFrom)))));
            } else if (strFrom.equals("lb") && strTo.equals("lb")) {
                valueTo.setText(valFrom.toString());
            } else if (strFrom.equals("lb") && strTo.equals("kg")) {
                valueTo.setText(Double.toString(Double.valueOf(round.format(lbToKg(valFrom)))));
            } else if (strFrom.equals("lb") && strTo.equals("oz")) {
                valueTo.setText(Double.toString(Double.valueOf(round.format(lbToOz(valFrom)))));
            } else if (strFrom.equals("oz") && strTo.equals("kg")) {
                valueTo.setText(Double.toString(Double.valueOf(round.format(ozToKg(valFrom)))));
            } else if (strFrom.equals("oz") && strTo.equals("lb")) {
                valueTo.setText(Double.toString(Double.valueOf(round.format(ozToLb(valFrom)))));
            } else if (strFrom.equals("oz") && strTo.equals("oz")) {
                valueTo.setText(valFrom.toString());
            }
        }

    }
    /**
     * Function kgToLb converts kilograms to pounds
     * @param varFrom represents value in kilograms as a Double
     * @return degrees in pounds as a Double
     */
    private double kgToLb(double varFrom) {
        double result = varFrom * 2.20462;
        return result;
    }
    /**
     * Function lbToKg converts pounds to kilograms
     * @param varFrom represents value in pounds as a Double
     * @return degrees in kilograms as a Double
     */
    private double lbToKg(double varFrom) {
        double result = varFrom / 2.20462;
        return result;
    }
    /**
     * Function kgToOz converts kilograms to ounces
     * @param varFrom represents value in kilograms as a Double
     * @return degrees in ounces as a Double
     */
    private double kgToOz(double varFrom) {
        double result = varFrom * 35.274;
        return result;
    }
    /**
     * Function ozToKg converts ounces to kilograms
     * @param varFrom represents value in ounces as a Double
     * @return degrees in kilograms as a Double
     */
    private double ozToKg(double varFrom) {
        double result = varFrom / 35.274;
        return result;
    }
    /**
     * Function lbToOz converts pounds to ounces
     * @param varFrom represents value in pounds as a Double
     * @return degrees in ounces as a Double
     */
    private double lbToOz(double varFrom) {
        double result = varFrom / 16;
        return result;
    }
    /**
     * Function ozToLb converts ounces
     * @param varFrom represents value in ounces as a Double
     * @return degrees in pounds as a Double
     */
    private double ozToLb(double varFrom) {
        double result = varFrom / 16;
        return result;
    }

}
