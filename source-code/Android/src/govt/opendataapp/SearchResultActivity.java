package govt.opendataapp;

import govt.opendataapp.adapter.ViewFormListAdapter;
import govt.opendataapp.database.ApplicationDatabase;
import govt.opendataapp.parsing.WebParsing;
import govt.opendataapp.progressdialog.ProgressDialog;
import govt.opendataapp.utils.Constants;
import govt.opendataapp.utils.FormList;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class SearchResultActivity extends ListActivity implements View.OnClickListener{
	
	private String strKeyword;
	private String strLevel;
	private String strCategoryID;
	private String strCountryID;
	private String strStateID;
	private String strDistrictID;
	private WebParsing jsonParser;
	private ArrayList<HashMap<String, String>> getFormsList;
	private ArrayList<FormList> displayFormsList;
	private ViewFormListAdapter formsListAdapter;
	private AlertDialog.Builder dialogAlert;
	private ApplicationDatabase dbDataBaseObject;
	private TextView layoutHeaderText;
	private Button btnBack;
	private ProgressDialog customProgressDialog;
	private HashMap<String, String> parameters;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.forms_list);
		Bundle extras = getIntent().getExtras();
		layoutHeaderText = (TextView)findViewById(R.id.listTitle);
		btnBack =(Button)findViewById(R.id.backbtn);
		layoutHeaderText.setText(getResources().getString(R.string.search_result_screen_heading));
		layoutHeaderText.setTextColor(getResources().getColor(R.color.White));
		strCategoryID = extras.getString(Constants.URL_PARAMETERS_CATEGORY_ID);
		strKeyword = extras.getString(Constants.URL_PARAMETERS_KEYWORD);
		strLevel = extras.getString(Constants.URL_PARAMETERS_LEVEL);
		strCountryID = extras.getString(Constants.COUNTRY_ID);
		strStateID = extras.getString(Constants.STATE_ID);
		strDistrictID = extras.getString(Constants.DISTRICT_ID);
	    dialogAlert =new AlertDialog.Builder(SearchResultActivity.this);
		jsonParser = new WebParsing();
		parameters = new HashMap<String, String>();
		parameters.put(Constants.URL_PARAMETERS_CATEGORY_ID, strCategoryID);
		parameters.put(Constants.URL_PARAMETERS_KEYWORD, strKeyword);
		parameters.put(Constants.URL_PARAMETERS_LEVEL, strLevel);
		parameters.put(Constants.COUNTRY_ID, strCountryID);
		parameters.put(Constants.STATE_ID, strStateID);
		parameters.put(Constants.DISTRICT_ID, strDistrictID);
		displayFormsList = new ArrayList<FormList>();
		customProgressDialog = new ProgressDialog(this, R.drawable.spinner);
		customProgressDialog.show();
		Thread background = new Thread(new Runnable() {
            
            public void run() {
                	getFormsList = jsonParser.getSearchFormList(Constants.METHOD_SELECTED_FORMS, parameters);
                	Message msgObj = handlerDialog.obtainMessage();
                    Bundle b = new Bundle();
                    b.putString("message", "ok");
                    msgObj.setData(b);
					handlerDialog.sendMessage(msgObj);
            }
            private Handler handlerDialog = new Handler() {
           	 
                public void handleMessage(Message msg) {
                     
                    String aResponse = msg.getData().getString("message");

                    if (aResponse.equals("ok")) {
                    	customProgressDialog.dismiss();
                    	for(int index = 0;index < getFormsList.size();index++){
                			if(getFormsList.get(index).get(Constants.JSON_ERRORMESSAGE).equals(Constants.ERROR_MESSAGE)){
                				Log.e(Constants.JSON_ERRORMESSAGE, getFormsList.get(index).get(Constants.JSON_ERRORMESSAGE));
                			}
                			else{
                				FormList currentForm = new FormList();
                				currentForm.setFormId(Integer.valueOf(getFormsList.get(index).get(Constants.FORM_ID)));
                				currentForm.setFormName(getFormsList.get(index).get(Constants.FORM_NAME));
                				currentForm.setFormLocation(getFormsList.get(index).get(Constants.FORM_LOCATION));
                				currentForm.setFormDownloadTime(Constants.SEARCH_FORM);
                				displayFormsList.add(currentForm);
                				Log.e(Constants.FORM_ID, getFormsList.get(index).get(Constants.FORM_ID));				
                				Log.e(Constants.FORM_NAME, getFormsList.get(index).get(Constants.FORM_NAME));
                				Log.e(Constants.FORM_LOCATION, getFormsList.get(index).get(Constants.FORM_LOCATION));
                			}
                		}
                		if(displayFormsList.size() < 1){
                			dialogAlert.setTitle(getResources().getString(R.string.login_screen_heading));
                			dialogAlert.setMessage(getResources().getString(R.string.no_forms_found));
                			dialogAlert.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                					
                					@Override
                					public void onClick(DialogInterface dailog, int arg1) {
                						// TODO Auto-generated method stub
                						dailog.cancel();
                						finish();
                					}
                				});
                			dialogAlert.show();
                		}
                		else{
                			formsListAdapter = new ViewFormListAdapter(SearchResultActivity.this, R.layout.list_item, displayFormsList);
                	        setListAdapter(formsListAdapter);
                		}
                    }
        		    else{
        		    	customProgressDialog.dismiss();
                    }
                }
            };
		});
		background.start();
		
		
		btnBack.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View currentView) {
		if(currentView == btnBack){
			finish();
		}
		
	}

}
