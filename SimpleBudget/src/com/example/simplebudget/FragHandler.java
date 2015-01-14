package com.example.simplebudget;

import android.app.Fragment;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by bulsatar on 1/13/15.
 */
public class FragHandler extends Fragment {

        // data object we want to retain
        private ArrayList<HashMap<String,String>> data;

        // this method is only called once for this fragment
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // retain this fragment
            setRetainInstance(true);
        }

        public void setData(ArrayList<HashMap<String,String>> data) {
            this.data = data;
        }

        public ArrayList<HashMap<String,String>> getData() {
            return data;
        }




}
