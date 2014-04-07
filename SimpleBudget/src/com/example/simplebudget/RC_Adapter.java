package com.example.simplebudget;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class RC_Adapter extends SimpleAdapter {
	Context contextpass;

	public RC_Adapter(Context context, List<? extends Map<String, ?>> data,
			int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);
		contextpass = context;
		// TODO Auto-generated constructor stub
	}
	@Override
	public View getView(int position,View convertView,ViewGroup parent){
		View view = super.getView(position, convertView, parent);
		TextView tv = (TextView) view.findViewById(R.id.c1);
		String s = tv.getText().toString();
		if(s.equals("Cost")){
			view.setBackgroundResource(R.color.valencia_red);}
		else{
			view.setBackgroundResource(R.color.t_green);
		}
		return view;
		
	}
	


}
