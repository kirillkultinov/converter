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
 * Speed fragment is used  to populate view of the temperature conversion section and
 * handle all calculations between currently chosen units
 */
public class SpeedFragment extends Fragment {


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
        //initialize items of the view that belongs to speed fragment and set their default state and behaviour
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
        //add speed units
        tempUnits.add("m/h");
        tempUnits.add("km/h");
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
    private void calculateFrom(EditText from, String strFrom, String strTo){
        if(!from.getText().toString().equals("") && !from.getText().toString().equals("-.") && !from.getText().toString().equals("-") && !from.getText().toString().equals(".")) {
            Double valFrom = Double.parseDouble(from.getText().toString());

            if (strFrom.equals("m/h") && strTo.equals("m/h")) {
                valueTo.setText(valFrom.toString());
            } else if (strFrom.equals("m/h") && strTo.equals("km/h")) {
                valueTo.setText(Double.toString(Double.valueOf(round.format(mphToKmph(valFrom)))));
            } else if (strFrom.equals("km/h") && strTo.equals("km/h")) {
                valueTo.setText(valFrom.toString());
            } else if (strFrom.equals("km/h") && strTo.equals("m/h")) {
                valueTo.setText(Double.toString(Double.valueOf(round.format(kmphToMph(valFrom)))));
            }
        }

    }

    /**
     * Function mphToKmph converts m/h to km/h
     * @param valFrom represents value in m/h as a Double
     * @return degrees in km/h as a Double
     */
    private double mphToKmph(double valFrom){
        double result = valFrom * 1.609344;
        return result;
    }
    /**
     * Function kmphToMph converts km/h to m/h
     * @param valFrom represents value in km/h as a Double
     * @return degrees in m/h as a Double
     */
    private double kmphToMph(double valFrom){
        double result = valFrom * 0.6214;
        return result;
    }

}
