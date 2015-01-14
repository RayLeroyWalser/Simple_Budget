package com.example.simplebudget;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.FragmentManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.*;
import android.content.res.Configuration;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View.*;







public class MainActivity extends Activity {
ListView lv;
SharedPreferences mPrefs;
ArrayList<HashMap<String,String>> arrCosts = new ArrayList<HashMap<String,String>>();
    private FragHandler dataFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set class variables that will hold info
		lv = (ListView)findViewById(R.id.lvCosts);

        // find the retained fragment on activity restarts
        FragmentManager fm = getFragmentManager();
        dataFragment = (FragHandler) fm.findFragmentByTag("data");

        // create the fragment and data the first time
        if (dataFragment == null) {
            // add the fragment
            dataFragment = new FragHandler();
            fm.beginTransaction().add(dataFragment, "data").commit();
            // load the data from the web
            dataFragment.setData(arrCosts);
        }

        arrCosts = dataFragment.getData();


		//get preference for welcome screen
		mPrefs=PreferenceManager.getDefaultSharedPreferences(this);
		Boolean WS_shown =mPrefs.getBoolean("welcome",false);

		//if it hasnt been shown then show the welcome screen
		if(!WS_shown){ShowWelcome();}

		//set listener for listview
		lv.setOnItemClickListener(new ListView.OnItemClickListener(){
			public void onItemClick(AdapterView<?> arg0,View view,int position,long id){
				if(!lv.isItemChecked(position)){
					view.setBackgroundResource(R.color.valencia_red);
				}else{
				    view.setBackgroundResource(R.color.o_red);
				}
			}
		});
        if (!arrCosts.isEmpty()){DisplayItems();}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
	
    @Override
    public void onConfigurationChanged(Configuration newConfig){
    	
    }
	public void fnAddCost(View view){
		getInfo("Cost");
	}
	
	public void fnAddPay(View view){
		getInfo("Pay");
	}

	public void fnDeleteCost(View view){
		for (int i = arrCosts.size()-1; i >=0; i--){
			if(lv.isItemChecked(i)){arrCosts.remove(i);}
		}
		DisplayItems();
	}

	public void fnClear(View view){
		//arrItems.clear();
		arrCosts.clear();
		DisplayItems();
	}
	
	public void fnPrevBal(View view){
		//create a new hashmap to hold info
		HashMap<String,String> map = new HashMap<String,String>();
		String tmpType;
		if (mPrefs.getFloat("prevValue",0)<0){tmpType="Cost";}
			else{tmpType="Pay";}
		map.put("id",tmpType);
		map.put("desc","Previous Balance");
		map.put("cost",Float.toString(mPrefs.getFloat("prevValue", 0)));
		//add hashmap to list
		arrCosts.add(map);
		//call function to set view
		DisplayItems();
	}
	
	public void DisplayItems(){
		TextView tv_amt =  (TextView) findViewById(R.id.tvRemain);
		//set new totaling variable
		double CostTotal = 0;
		double tmpCost = 0;

		//cycle thru array to get totals
		for (HashMap<String,String> map : arrCosts){
			tmpCost=Double.parseDouble(map.get("cost"));
			CostTotal = CostTotal+tmpCost;
		}

		//set format of value and display total
		tv_amt.setText(String.format("%.2f", CostTotal));
		Editor edit=mPrefs.edit();
		edit.putFloat("prevValue", (float) CostTotal);
		edit.commit();

		//change background if negative
		if(CostTotal<0){tv_amt.setBackgroundResource(R.color.o_red);}
		  else {tv_amt.setBackgroundResource(R.color.blue_bayoux);}
		//add array to ListView and set layout
		RC_Adapter sd=new RC_Adapter(this,arrCosts,R.layout.costlist,
				             new String[]{"id","desc","cost"},
				             new int[]{R.id.c1,R.id.c2,R.id.c3});
		lv.setAdapter(sd);
	}
	
	
	public void getInfo(final String tmpType){
		//set dialog box up
		final Context con =this;
		final Dialog dialog = new Dialog(con);
		dialog.setContentView(R.layout.addcost);
		dialog.setTitle(tmpType);
		dialog.getWindow().setSoftInputMode
		(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		Button btndiag =(Button) dialog.findViewById(R.id.diagBtnOk);
		btndiag.setOnClickListener(new OnClickListener(){
				public void onClick(View v){
					//grab the info from the textviews of the dialog
					EditText tvDesc = (EditText) dialog.findViewById(R.id.tv_Desc);
					EditText tvCost = (EditText) dialog.findViewById(R.id.tv_Cost);
					String sDesc = tvDesc.getText().toString();
					String sCost = tvCost.getText().toString();

                    //chech for empty and provide defaults
                    if(sDesc.equals("")){sDesc="NA";}
                    if(sCost.equals("")){sCost="0";}

					double tmpCost=Double.parseDouble(sCost);
					if(tmpType.equals("Cost")){tmpCost=tmpCost*-1;}
					sCost=Double.toString(tmpCost);

					//create a new hashmap to hold info
					HashMap<String,String> map = new HashMap<String,String>();
					map.put("id",tmpType);
					map.put("desc",sDesc);
					map.put("cost",sCost);

					//add hashmap to list
					arrCosts.add(map);

					//call function to set view
					DisplayItems();

					//close dialog
					dialog.cancel();
				}

			});
		dialog.show();
	}
	
	
	public void ShowWelcome(){
		String howto = getResources().getString(R.string.how_to);
		new AlertDialog.Builder(this)
		  .setTitle("How To...")
		  .setMessage(Html.fromHtml(howto))
		  .setPositiveButton("OK",new DialogInterface.OnClickListener(){
			  public void onClick(DialogInterface dialog,int which){
				  dialog.dismiss();
			  }
		  }).show();
	      Editor edit=mPrefs.edit();
		  edit.putBoolean("welcome",true);
		  edit.commit(); 
	}


    @Override
    public void onDestroy() {
        super.onDestroy();
        // store the data in the fragment
        dataFragment.setData(arrCosts);
    }
	
}
