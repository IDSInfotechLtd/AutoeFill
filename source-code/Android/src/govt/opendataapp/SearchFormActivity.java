package govt.opendataapp;

import govt.opendataapp.database.ApplicationDatabase;
import govt.opendataapp.utils.Category;
import govt.opendataapp.utils.Constants;
import govt.opendataapp.utils.Country;
import govt.opendataapp.utils.District;
import govt.opendataapp.utils.State;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

@SuppressLint("NewApi")
public class SearchFormActivity extends Activity implements View.OnClickListener{
	
	 
	private Button btnSearchForm;
	private Button btnBack;
	private EditText txtKeyword;
	private RadioButton radioNationalSearch;
	private RadioButton radioDistrictSearch;
	private Button txtCountry;
	private Button txtState;
	private Button txtDistrict;
	private Button txtCategory;
	private String strLevel = "1";
	private String strCategoryID="";
	private String strCountryID = "";
	private String strStateID = "";
	private String strDistrictID = "";
	private AlertDialog.Builder dialogAlert;
	private ApplicationDatabase dbDataBaseObject;
	private ArrayList<Country> countriesList;
	private ArrayList<State> statesList;
	private ArrayList<District> districtsList;
	private ArrayList<Category> categoriesList;
	private String strKeyword;
	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.FROYO)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.searchform);
		dbDataBaseObject = new ApplicationDatabase(this);
		dbDataBaseObject.openDataBase();
		btnBack =(Button) findViewById(R.id.backbtn);
		btnSearchForm = (Button)findViewById(R.id.search_form);
		txtKeyword = (EditText)findViewById(R.id.txt_keyword);
		radioNationalSearch = (RadioButton)findViewById(R.id.radio_national_search);
		radioDistrictSearch = (RadioButton)findViewById(R.id.radio_district_search);
		txtCountry = (Button)findViewById(R.id.spn_country);
		txtState = (Button)findViewById(R.id.spn_state);
		txtDistrict = (Button)findViewById(R.id.spn_district);
		txtCategory = (Button)findViewById(R.id.spn_category);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		radioDistrictSearch.setChecked(false);
		txtCountry.setEnabled(false);
		txtState.setEnabled(false);
		txtDistrict.setEnabled(false);
		btnSearchForm.setOnClickListener(this);
		txtCountry.setOnClickListener(this);
		txtState.setOnClickListener(this);
		txtDistrict.setOnClickListener(this);
		txtCategory.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		radioNationalSearch.setOnClickListener(this);
		radioDistrictSearch.setOnClickListener(this);
	}

	@Override
	public void onClick(View selectedView) {
		if(selectedView == btnSearchForm){
			strKeyword = txtKeyword.getText().toString();
			Intent intent = new Intent(SearchFormActivity.this, SearchResultActivity.class);
			intent.putExtra(Constants.URL_PARAMETERS_CATEGORY_ID, strCategoryID);
			intent.putExtra(Constants.URL_PARAMETERS_KEYWORD, strKeyword.replace(" ", "%20"));
			intent.putExtra(Constants.URL_PARAMETERS_LEVEL, strLevel);
			intent.putExtra(Constants.COUNTRY_ID, strCountryID);
			intent.putExtra(Constants.STATE_ID, strStateID);
			intent.putExtra(Constants.DISTRICT_ID, strDistrictID);
			startActivity(intent);
		}
		else if(selectedView == radioNationalSearch){
			radioNationalSearch.setChecked(true);
			radioDistrictSearch.setChecked(false);
			txtCountry.setEnabled(false);
			txtState.setEnabled(false);
			txtDistrict.setEnabled(false);
			strLevel = Constants.NATIONAL_LEVEL;
		}
		else if(selectedView == radioDistrictSearch){
			radioDistrictSearch.setChecked(true);
			radioNationalSearch.setChecked(false);
			txtCountry.setEnabled(true);
			txtState.setEnabled(true);
			txtDistrict.setEnabled(true);
			strLevel = Constants.DISTRICT_LEVEL;
		}
		else if(selectedView == txtCountry){
			countriesList = new ArrayList<Country>();
			countriesList =  dbDataBaseObject.getCountryList();
			String countryArray[] = new String[countriesList.size()];
			if(countriesList.size() > 0){
				for(int index = 0; index < countriesList.size();index++){
					countryArray[index] = countriesList.get(index).getCountryName();
				}
				dialogAlert = new AlertDialog.Builder(this);
				dialogAlert.setTitle(getResources().getString(R.string.hint_select_country));
				dialogAlert.setSingleChoiceItems(countryArray, 0, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						txtCountry.setText(countriesList.get(which).getCountryName());
						strCountryID = String.valueOf(countriesList.get(which).getCountryID());
						if(strCountryID.equals("0")){
							strStateID = "0";
							strDistrictID = "0";
							txtCountry.setText("");
							txtState.setText("");
							txtDistrict.setText("");
						}
						dialog.dismiss();
					}
				});
				AlertDialog alert = dialogAlert.create();
				alert.show();
			}
			else{
				dialogAlert = new AlertDialog.Builder(this);
				dialogAlert.setMessage("Unable to fetch countries. Please try again later.");
				dialogAlert.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				});
				AlertDialog alert = dialogAlert.create();
				alert.show();
			}
		}
		else if(selectedView == txtState){
			statesList = new ArrayList<State>();
			if(!strCountryID.equals("") && !strCountryID.equals("0")){
				statesList = dbDataBaseObject.getStatesList(strCountryID);
				String stateArray[] = new String[statesList.size()];
				for(int index = 0; index < statesList.size();index++){
					stateArray[index] = statesList.get(index).getStateName();
				}
				dialogAlert = new AlertDialog.Builder(this);
				dialogAlert.setTitle(getResources().getString(R.string.hint_select_state));
				dialogAlert.setSingleChoiceItems(stateArray, 0, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						txtState.setText(statesList.get(which).getStateName());
						if(!strStateID.equals(String.valueOf(statesList.get(which).getStateID()))){
							strDistrictID = "0";
							txtDistrict.setText("");
						}
						strStateID = String.valueOf(statesList.get(which).getStateID());
						if(strStateID.equals("0")){
							strDistrictID = "0";
							txtState.setText("");
							txtDistrict.setText("");
						}
						dialog.dismiss();
					}
				});
				AlertDialog alert = dialogAlert.create();
				alert.show();
			}
			else{
				dialogAlert = new AlertDialog.Builder(this);
				dialogAlert.setTitle(getResources().getString(R.string.login_alert_error_heading));
				dialogAlert.setMessage("Please select country.");
				dialogAlert.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				AlertDialog alert = dialogAlert.create();
				alert.show();
			}
		}
		else if(selectedView == txtDistrict){
			districtsList = new ArrayList<District>();
			if(!strStateID.equals("") && !strStateID.equals("0")){
				districtsList = dbDataBaseObject.getDistrictList(strStateID);
				String districtArray[] = new String[districtsList.size()];
				for(int index = 0; index < districtsList.size();index++){
					districtArray[index] = districtsList.get(index).getDistrictName();
				}
				dialogAlert = new AlertDialog.Builder(this);
				dialogAlert.setTitle(getResources().getString(R.string.hint_select_district));
				dialogAlert.setSingleChoiceItems(districtArray, 0, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						txtDistrict.setText(districtsList.get(which).getDistrictName());
						strDistrictID = String.valueOf(districtsList.get(which).getDistrictID());
						if(strDistrictID.equals("0")){
							txtDistrict.setText("");
						}
						dialog.dismiss();
					}
				});
				AlertDialog alert = dialogAlert.create();
				alert.show();
			}
			else{
				dialogAlert = new AlertDialog.Builder(this);
				dialogAlert.setTitle(getResources().getString(R.string.login_alert_error_heading));
				dialogAlert.setMessage("Please select state.");
				dialogAlert.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				AlertDialog alert = dialogAlert.create();
				alert.show();
			}
		}
		else if(selectedView == txtCategory){
			categoriesList = new ArrayList<Category>();
			categoriesList = dbDataBaseObject.getCategoryList();
			if(categoriesList.size() > 0){
				String categoryArray[] = new String[categoriesList.size()];
				for(int index = 0; index < categoriesList.size();index++){
					categoryArray[index] = categoriesList.get(index).getCategoryName();
				}
				dialogAlert = new AlertDialog.Builder(this);
				dialogAlert.setTitle(getResources().getString(R.string.hint_select_category));
				dialogAlert.setSingleChoiceItems(categoryArray, 0, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						txtCategory.setText(categoriesList.get(which).getCategoryName());
						strCategoryID = String.valueOf(categoriesList.get(which).getCategoryID());
						dialog.dismiss();
					}
				});
				AlertDialog alert = dialogAlert.create();
				alert.show();
			}
			else{
				dialogAlert = new AlertDialog.Builder(this);
				dialogAlert.setTitle(getResources().getString(R.string.login_alert_error_heading));
				dialogAlert.setMessage("Unable to sync categories. Please try again later.");
				dialogAlert.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				AlertDialog alert = dialogAlert.create();
				alert.show();
			}
		}
		if(selectedView == btnBack){
			finish();
		}
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) { //Back key pressed
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	

	
}
