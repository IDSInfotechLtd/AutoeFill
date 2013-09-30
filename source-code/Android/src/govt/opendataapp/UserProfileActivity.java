package govt.opendataapp;

import govt.opendataapp.database.ApplicationDatabase;
import govt.opendataapp.database.DatabaseContractClass;
import govt.opendataapp.utils.Constants;
import govt.opendataapp.utils.UserProfile;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class UserProfileActivity extends Activity  implements View.OnClickListener{
//	private ArrayList<String> colNames;
//	private ArrayList<UserProfile> info;
	private String[] colArr;
	private static int lblId = 100, starttexid = 2000, textId=2000;
	private String[] columnNames;
	private ArrayList<String> updatedColumns;
	private String col;
	private String activity;
	private static int counter=0;
	private AlertDialog.Builder dialogAlert;
	private String[] profileValues;
	private String 	profilename;
	private Display display;
	private int screenWidth;
	private Button btnSubmit;
	private ApplicationDatabase databaseObject;
	private SharedPreferences applicationPreferences;
	private String strUserID;
	private String strProfileID;
	private String strCurrentProfileName;
	private TextView txtScreenTitle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.userprofilelayout);
		Bundle extras = getIntent().getExtras();
		activity =extras.getString(Constants.EDIT_NEW);
		dialogAlert =new AlertDialog.Builder(UserProfileActivity.this);
		btnSubmit =(Button)findViewById(R.id.createprofilebutton);
		txtScreenTitle = (TextView)findViewById(R.id.textView1);
		btnSubmit.setOnClickListener(this);
		databaseObject = new ApplicationDatabase(this);
		databaseObject.openDataBase();
		applicationPreferences = getApplicationContext().getSharedPreferences(Constants.LOGGED_IN_USER_PREFERENCES_NAME, Context.MODE_PRIVATE);
		strUserID = String.valueOf(applicationPreferences.getLong(Constants.PREF_USER_ID, 0));
		strProfileID = applicationPreferences.getString(Constants.PREF_PROFILE_ID, "0");
		display= getWindowManager().getDefaultDisplay();
		 
		screenWidth = display.getWidth(); 
//		screenHeight = display.getHeight(); 
		
		updatedColumns = new ArrayList<String>();
		columnNames = databaseObject.getTotalColumns();
		
		// Reading all contacts
        Log.d("Reading: ", "Reading all contacts.."); 
//        info = databaseObject.getAllUserInfo();       
     	Log.d("Checking: ", "Checking Fields..");
		for(int i=0; i<columnNames.length; i++){
			if(columnNames[i].equalsIgnoreCase("size")||columnNames[i].equalsIgnoreCase("userid")||columnNames[i].equalsIgnoreCase("control")||columnNames[i].equalsIgnoreCase("ProfileId")){
			}
			else{
                col = columnNames[i];
				updatedColumns.add(col);						
			}					
		}
		colArr = new String[updatedColumns.size()];
		colArr = updatedColumns.toArray(colArr);
		
		// Find Tablelayout defined in main.xml 
		TableLayout tl = (TableLayout) findViewById(R.id.tLayout);
		
		if(activity.equals(Constants.NEW)){
		}
		else
		{
			txtScreenTitle.setText("Edit Profile");
			profileValues=	databaseObject.getProfileData();
		}
		 counter=0;
		 textId=2000;
		for(String name: colArr){
			Log.i("REQUIRED COLUMNS ", name+"");
			
			// Create a new row to be added. 
			TableRow tr = new TableRow(this);
			tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
			// Create a TextView to be the row-content. 
			TextView lblName = new TextView(this);
			lblName.setText(name.toUpperCase()+":");
			lblName.setPadding(15, 0, 0, 0);
			lblName.setId(lblId);
			lblName.setSingleLine();
			lblName.setScrollContainer(true);
			lblName.setMovementMethod(new ScrollingMovementMethod());
			lblName.setLayoutParams(new TableRow.LayoutParams(screenWidth/2 - 10, TableRow.LayoutParams.WRAP_CONTENT));
			lblId++;
			EditText lblText = new EditText(this);
			if(activity.equals(Constants.NEW)){
				// Create a EditText to be the row-content. 
				lblText.setId(textId);
				lblText.setTag(name);	
				TableRow.LayoutParams edittext = new TableRow.LayoutParams(screenWidth/2, TableRow.LayoutParams.WRAP_CONTENT);
				lblText.setLayoutParams(edittext);
				lblText.setBackgroundDrawable(getResources().getDrawable(R.drawable.small_textbox));
				lblText.setSingleLine();
				lblText.setHint(name.toUpperCase());
			}
			else {
				if(name.equals(DatabaseContractClass.UserInformation.PROFILE_NAME))
					strCurrentProfileName = profileValues[counter];
				
				lblText.setTag(name);	
				lblText.setId(textId);
				TableRow.LayoutParams edittext = new TableRow.LayoutParams(screenWidth/2, TableRow.LayoutParams.WRAP_CONTENT);
				lblText.setLayoutParams(edittext);
				lblText.setBackgroundDrawable(getResources().getDrawable(R.drawable.small_textbox));
				lblText.setSingleLine();
				if(profileValues[counter]!=null){
					if(profileValues[counter].equals(Constants.NULLCHECK)){
						lblText.setText("");
					}else{
						lblText.setText(profileValues[counter]);
					}
				}
				
				
			}
		    textId++;
			// Add Button to row. */
			tr.addView(lblName);
			tr.addView(lblText);
			// Add row to TableLayout. 
			tl.addView(tr);
			counter++;
				}
		
		
//			submit.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if(activity.equals(Constants.NEW)){
//				ContentValues values = new ContentValues();
//				SharedPreferences prefs = getApplicationContext().getSharedPreferences(Constants.LOGGED_IN_USER_PREFERENCES_NAME, Context.MODE_PRIVATE);
//				long userid = prefs.getLong(Constants.PREF_USER_ID, 0);
//				String profileId=prefs.getString(Constants.PREF_PROFILE_ID, "");
//				values.put(DatabaseContractClass.UserInformation.USER_ID, String.valueOf(userid));
//				for(int index = starttexid;index < textId;index++){	
//					EditText edt = (EditText)findViewById(index);
//					String tagname =edt.getTag().toString();
//					if(tagname.equals(DatabaseContractClass.UserInformation.PROFILE_NAME)){
//			       	profilename =edt.getText().toString().trim();
//			       
//			        Editor prefEdit = prefs.edit();
//					prefEdit.putString(Constants.PREF_PROFILE_NAME, profilename);
//					prefEdit.commit();
//					}
//					values.put(edt.getTag().toString(), edt.getText().toString());
//				}
//				ApplicationDatabase opendatabaseobject = new ApplicationDatabase(getApplicationContext());
//				opendatabaseobject.openDataBase();
//			long profileid=	opendatabaseobject.insertDataInTable(DatabaseContractClass.UserInformation.TABLE_NAME, values);
//				if(profileId.equals(Constants.PROFILENOID)){
//					SharedPreferences newprefs = getApplicationContext().getSharedPreferences(Constants.LOGGED_IN_USER_PREFERENCES_NAME, Context.MODE_PRIVATE);
//					Editor edit = newprefs.edit();
//					 edit.putString(Constants.PREF_PROFILE_ID, String.valueOf(profileid));
//					 edit.commit();
//				}
//				ContentValues cv = new ContentValues();
//				cv.put(DatabaseContractClass.User.PROFILEID, profileid);
//				
//				if(profilename.equals("")){
//					dialogAlert.setTitle(getResources().getString(R.string.alert_title));
//					 dialogAlert.setMessage(getResources().getString(R.string.enter_profile_name));
//					 dialogAlert.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
//						 @Override
//						 public void onClick(DialogInterface dailog, int arg1) {
//							
//							dailog.cancel();
//							
//						 }
//					 });
//					 dialogAlert.show();
//				}
//				else{
//					opendatabaseobject.setProfileId(userid, cv);
//				dialogAlert.setTitle(getResources().getString(R.string.alert_title));
//				 dialogAlert.setMessage(getResources().getString(R.string.created_profile_successfully));
//				 dialogAlert.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
//					 @Override
//					 public void onClick(DialogInterface dailog, int arg1) {
//						
//						
//						 finish();
//					 }
//				 });
//				 dialogAlert.show();
//				 }
//				}
//			 else {
//					ContentValues newvalues = new ContentValues();
//					SharedPreferences prefs = getApplicationContext().getSharedPreferences(Constants.LOGGED_IN_USER_PREFERENCES_NAME, Context.MODE_PRIVATE);
//					long userid = prefs.getLong(Constants.PREF_USER_ID, 0);
//					String profileId=prefs.getString(Constants.PREF_PROFILE_ID, "");
//					for(int index = starttexid;index < textId;index++){	
//						EditText edt = (EditText)findViewById(index);
//						newvalues.put(edt.getTag().toString(), edt.getText().toString());
//					}
//					ApplicationDatabase opendatabaseobject = new ApplicationDatabase(getApplicationContext());
//					opendatabaseobject.openDataBase();
//				opendatabaseobject.updateprofile(newvalues, profileId);
//				 dialogAlert.setTitle(getResources().getString(R.string.alert_title));
//				 dialogAlert.setMessage(getResources().getString(R.string.updated_profile_successfully));
//				 dialogAlert.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
//					 @Override
//					 public void onClick(DialogInterface dailog, int arg1) {
//						 finish();
//					 }
//				 });
//				 dialogAlert.show();
//				}
//			}
//			
//		});

	}
	@Override
	public void onClick(View v) {
		if(v==btnSubmit){
			if(activity.equals(Constants.NEW)){
				ContentValues values = new ContentValues();
				values.put(DatabaseContractClass.UserInformation.USER_ID, strUserID);
				for(int index = starttexid;index < textId;index++){	
					EditText edt = (EditText)findViewById(index);
					String tagname = edt.getTag().toString();
					if(tagname.equals(DatabaseContractClass.UserInformation.PROFILE_NAME)){
						profilename = edt.getText().toString().trim();
					}
					values.put(edt.getTag().toString(), edt.getText().toString());
				}
				
				
				if(profilename.equals("")){
					dialogAlert.setTitle(getResources().getString(R.string.alert_title));
					dialogAlert.setMessage(getResources().getString(R.string.enter_profile_name));
					dialogAlert.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dailog, int arg1) {

							dailog.cancel();

						}
					});
					dialogAlert.show();
				}
				else if(databaseObject.hasProfile(profilename, strUserID)){
					dialogAlert.setTitle(getResources().getString(R.string.alert_title));
					dialogAlert.setMessage("Profile name already exists.");
					dialogAlert.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dailog, int arg1) {
							dailog.cancel();
						}
					});
					dialogAlert.show();
				}
				else{
					ContentValues cv = new ContentValues();
					long profileid=	databaseObject.insertDataInTable(DatabaseContractClass.UserInformation.TABLE_NAME, values);
					if(strProfileID.equals(Constants.PROFILENOID)){
						Editor edit = applicationPreferences.edit();
						edit.putString(Constants.PREF_PROFILE_ID, String.valueOf(profileid));
						edit.putString(Constants.PREF_PROFILE_NAME, profilename);
						cv.put(DatabaseContractClass.User.PROFILEID, profileid);
						cv.put(DatabaseContractClass.User.PROFILE_NAME, profilename);
						databaseObject.setProfileId(Long.valueOf(strUserID), cv);
						edit.commit();
					}
					
					dialogAlert.setTitle(getResources().getString(R.string.alert_title));
					dialogAlert.setMessage(getResources().getString(R.string.created_profile_successfully));
					dialogAlert.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dailog, int arg1) {


							finish();
						}
					});
					dialogAlert.show();
				}
			}
			else {
				ContentValues values = new ContentValues();
				values.put(DatabaseContractClass.UserInformation.USER_ID, strUserID);
				for(int index = starttexid;index < textId;index++){	
					EditText edt = (EditText)findViewById(index);
					String tagname = edt.getTag().toString();
					if(tagname.equals(DatabaseContractClass.UserInformation.PROFILE_NAME)){
						profilename = edt.getText().toString().trim();
					}
					values.put(edt.getTag().toString(), edt.getText().toString());
				}
				if(profilename.equals("")){
					dialogAlert.setTitle(getResources().getString(R.string.alert_title));
					dialogAlert.setMessage(getResources().getString(R.string.enter_profile_name));
					dialogAlert.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dailog, int arg1) {

							dailog.cancel();

						}
					});
					dialogAlert.show();
				}
				else if(!profilename.equals(strCurrentProfileName) && databaseObject.hasProfile(profilename, strUserID)){
					dialogAlert.setTitle(getResources().getString(R.string.alert_title));
					dialogAlert.setMessage("Profile name already exists.");
					dialogAlert.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dailog, int arg1) {
							dailog.cancel();
						}
					});
					dialogAlert.show();
				}
				else {
					Editor edit = applicationPreferences.edit();
					edit.putString(Constants.PREF_PROFILE_NAME, profilename);
					edit.commit();
					databaseObject.updateprofile(values, strProfileID);
					dialogAlert.setTitle(getResources().getString(R.string.alert_title));
					dialogAlert.setMessage(getResources().getString(R.string.updated_profile_successfully));
					dialogAlert.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dailog, int arg1) {
							finish();
						}
					});
					dialogAlert.show();
				}
			}
		}
	}

}

