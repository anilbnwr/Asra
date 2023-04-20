package com.asra.mobileapp.common.statepicker;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import com.asra.mobileapp.R;
import com.asra.mobileapp.model.State;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.appcompat.app.AppCompatDialog;
import androidx.core.view.ViewCompat;

public class ETStatePicker extends AppCompatDialog {
    private List<State> states;
    private StatePickerCallback callbacks;
    private ListView listview;

    public ETStatePicker(Context context, List<State> states,
                         StatePickerCallback callbacks) {

        super(context);
        this.states = states;
        this.callbacks = callbacks;
        Collections.sort(states, new Comparator<State>() {
            public int compare(State result1, State result2) {
                return result1.name.compareTo(result2.name);
            }
        });


    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_country_picker);
        ViewCompat.setElevation(this.getWindow().getDecorView(), 3.0F);
        this.listview = (ListView) this.findViewById(R.id.country_picker_listview);
        StatePickerAdapter adapter = new StatePickerAdapter(this.getContext(), this.states);
        this.listview.setAdapter(adapter);
        this.listview.setOnItemClickListener((parent, view, position, id) -> {
            ETStatePicker.this.hide();
            State state = ETStatePicker.this.states.get(position);
            ETStatePicker.this.callbacks.onStateSelected(state, position);
        });
    }


}