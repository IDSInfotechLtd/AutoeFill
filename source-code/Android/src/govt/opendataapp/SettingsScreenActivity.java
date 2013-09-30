package govt.opendataapp;

import govt.opendataapp.database.ApplicationDatabase;
import govt.opendataapp.database.DatabaseContractClass;
import govt.opendataapp.utils.Constants;
import govt.opendataapp.utils.FormList;
import govt.opendataapp.utils.Profile;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsScreenActivity extends Activity implements OnClickListener{

	private ArrayList<String> profileName, profileId, savedNames, savedIds;
	private String[] pNames={"Kavita","Eliza","Preetinder","Swati"};
	private String[] pIds={"1","2","3","4"};
	private String[] name, id;
	private Character ch;
    private ArrayList<Profile> profileList = new ArrayList<Profile>();
    private ApplicationDatabase open_db = new ApplicationDatabase(SettingsScreenActivity.this);
    private EditText language;
    private TextView profile;
    private Button btnDeleteAccount;
    private Button btnDeleteProfile;
    private Button btnDeleteForm;
    private ArrayList<FormList> formList = new ArrayList<FormList>();
    private SharedPreferences applicationPreferences;
    private String strProfileName;
    private String strApplicationPassword;
    private String strUserID;
    private EditText txtChgPwd;
    private Button btnBack;
    private long userID; 
    private int intProfileID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.settings);
        
		profile = (TextView) findViewById(R.id.profileSpinner);
		language = (EditText) findViewById(R.id.langSpinner);
		txtChgPwd = (EditText) findViewById(R.id.pwdText);
		btnBack = (Button) findViewById(R.id.backbtn);
		btnDeleteAccount = (Button) findViewById(R.id.delAccount);
		btnDeleteForm = (Button) findViewById(R.id.delForms);
		btnDeleteProfile = (Button) findViewById(R.id.delProfiles);		
		applicationPreferences = (SharedPreferences)getSharedPreferences(Constants.LOGGED_IN_USER_PREFERENCES_NAME, Context.MODE_PRIVATE);
		strProfileName = applicationPreferences.getString(Constants.PREF_PROFILE_NAME, "Select profile");
		strApplicationPassword = applicationPreferences.getString(Constants.PREF_USER_PASSWORD, "none");
		strUserID = String.valueOf(applicationPreferences.getLong(Constants.PREF_USER_ID, 0));
		intProfileID = Integer.valueOf(applicationPreferences.getString(Constants.PREF_PROFILE_ID, "0"));
		txtChgPwd.setText(strApplicationPassword);
		profile.setText(strProfileName);
		profileName = new ArrayList<String>();   
		profileId = new ArrayList<String>();

		for(int i = 0; i<pNames.length; i++){
			profileName.add(""+pNames[i]); 
			profileId.add(""+pIds[i]);
		}
		
		// add button listeners
		profile.setOnClickListener(this);
		language.setOnClickListener(this);
		btnDeleteAccount.setOnClickListener(this);
		btnDeleteProfile.setOnClickListener(this);
		btnDeleteForm.setOnClickListener(this);
		btnBack.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View view) {
		if(view.getId()==R.id.profileSpinner){
			SharedPreferences prefs = getSharedPreferences(Constants.LOGGED_IN_USER_PREFERENCES_NAME, Context.MODE_PRIVATE);
			long userID = prefs.getLong(Constants.PREF_USER_ID, 0);
			final ApplicationDatabase open_db = new ApplicationDatabase(SettingsScreenActivity.this);
			open_db.openDataBase();
			profileList = open_db.getProfileDetails(userID);
			int selectedIndex = 0;
			if(profileList!= null && profileList.size() != 0){
				savedNames = new ArrayList<String>();
				savedIds = new ArrayList<String>();
				for(int i = 0; i < profileList.size(); i++){
					if(profileList.get(i).getProfileName() != null || profileList.get(i).getProfileID() != 0) {
						savedNames.add(profileList.get(i).getProfileName());
						if(intProfileID == profileList.get(i).getProfileID()){
							selectedIndex = i;
						}
						savedIds.add(String.valueOf(profileList.get(i).getProfileID()));					
					}else{
						Toast.makeText(SettingsScreenActivity.this, "No data is present", Toast.LENGTH_LONG).show();
					}		
				}
				
				name = new String[savedNames.size()];
				name = savedNames.toArray(name);
				
				id = new String[savedNames.size()];
				id = savedIds.toArray(id);
				
				
				// custom dialog
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Select Profile");
				builder.setSingleChoiceItems(name, selectedIndex, new DialogInterface.OnClickListener() {
	
					@Override
					public void onClick(DialogInterface dialog, int which) {
						profile.setText(name[which]);
						Editor prefEditor = applicationPreferences.edit();
						prefEditor.putString(Constants.PREF_PROFILE_ID, id[which]);
						prefEditor.putString(Constants.PREF_PROFILE_NAME, name[which]);
						intProfileID = Integer.valueOf(id[which]);
						prefEditor.commit();
						open_db.openDataBase();
						ContentValues cv = new ContentValues();
						cv.put(DatabaseContractClass.User.PROFILEID, id[which]);
						cv.put(DatabaseContractClass.User.PROFILE_NAME, name[which]);
						open_db.setProfileId(Long.valueOf(strUserID), cv);
						dialog.dismiss();
					}
				});
				AlertDialog alert = builder.create();
				alert.show();	
			}else {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(Constants.PROFILE_ERRORMESSAGE);
				builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						return;
					}					
				});
				AlertDialog alert = builder.create();
				alert.show();	
			}
		} else if(view.getId()==R.id.delAccount){
			SharedPreferences prefs = getSharedPreferences(Constants.LOGGED_IN_USER_PREFERENCES_NAME, Context.MODE_PRIVATE);
			userID = prefs.getLong(Constants.PREF_USER_ID, 0);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(Constants.DELETE_ACCOUNT_TITLE);
			builder.setMessage(Constants.DELETE_ACCOUNT_ERRORMESSAGE);
			builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					open_db.openDataBase();
					if(open_db.deleteUser(userID)){
						SharedPreferences prefs = getApplicationContext().getSharedPreferences(Constants.LOGGED_IN_USER_PREFERENCES_NAME, Context.MODE_PRIVATE);
						Editor prefEdit = prefs.edit();
						prefEdit.putLong(Constants.PREF_USER_ID, 0);
						prefEdit.putString(Constants.PREF_PROFILE_ID, "0");
						prefEdit.commit();
						startActivity(new Intent(SettingsScreenActivity.this,LoginActivity.class));
						finish();
					} else{
						Toast.makeText(getApplicationContext(), Constants.SKIP_ERRORMESSAGE, Toast.LENGTH_SHORT).show();
					}
				}					
			});
			builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dailog, int arg1) {
				dailog.cancel();
				}
			});
			AlertDialog alert = builder.create();
			alert.show();	
			
		} else if(view.getId()==R.id.delProfiles){
			
			SharedPreferences prefs = getSharedPreferences(Constants.LOGGED_IN_USER_PREFERENCES_NAME, Context.MODE_PRIVATE);
			long userID = prefs.getLong(Constants.PREF_USER_ID, 0);
			
			if(userID != 0){				
				open_db.openDataBase();
				profileList = open_db.getProfileDetails(userID);
				Log.i("PROFILELIST LENGTH", profileList.size()+"");
				
				if(profileList!=null && profileList.size() != 0){
					// custom dialog
					final Dialog dialog = new Dialog(SettingsScreenActivity.this);
					dialog.setContentView(R.layout.custom_view);
					dialog.setTitle(getResources().getString(R.string.custom_view_heading));
					
					// set the custom dialog components - radiogroup with radiobuttons
					final RadioGroup rdGroup = (RadioGroup) dialog.findViewById(R.id.selectItem);
//					RadioButton profileName;
					for(int i=0; i<profileList.size(); i++){
						String name = profileList.get(i).getProfileName();
						final RadioButton profileName = new RadioButton(this);
						profileName.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
						profileName.setText(name);
						profileName.setTextColor(getResources().getColor(R.color.White));
						profileName.setId(i);
						if(profileList.get(i).getProfileID() == intProfileID){
							profileName.setChecked(true);
						}
						rdGroup.addView(profileName, i);
						profileName.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								int profileId = profileList.get(profileName.getId()).getProfileID();	
								if(profileList.get(profileName.getId()).getProfileName().equals(profile.getText().toString())){
									AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
									builder.setMessage("Profile can not be deleted. Please change your default profile first.");
									builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog, int which) {
											return;
										}					
									});
									AlertDialog alert = builder.create();
									alert.show();
								}
								else if(open_db.deleteProfileRecord(profileId)){
							    	Toast.makeText(getApplicationContext(), "Profile is deleted", Toast.LENGTH_SHORT).show();
							    	dialog.dismiss();
							    }
								
							}
						});
					} 
					
					
					Button removeProfile = (Button) dialog.findViewById(R.id.removeSelectedItem);
					removeProfile.setVisibility(View.GONE);
//					// if button is clicked, close the custom dialog
//					removeProfile.setOnClickListener(new OnClickListener() {
//						@Override
//						public void onClick(View view) {
//							
//							if(rdGroup.getCheckedRadioButtonId()!=-1){
//							    int selectedRadioButtonId= rdGroup.getCheckedRadioButtonId();
//							    int profileId = profileList.get(selectedRadioButtonId).getProfileID();					    
//							    Log.i("RadioButton Id", profileId+"");
//							    if(open_db.deleteProfileRecord(profileId)){
//							    	Toast.makeText(getApplicationContext(), "Profile is deleted", Toast.LENGTH_SHORT).show();
//							    }
//							}
//							dialog.dismiss();
//						}
//					});			
					dialog.show();	
				}else{
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setMessage(Constants.PROFILE_ERRORMESSAGE);
					builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							return;
						}					
					});
					AlertDialog alert = builder.create();
					alert.show();
				}
			}else
				Toast.makeText(getApplicationContext(), Constants.SKIP_ERRORMESSAGE, Toast.LENGTH_SHORT).show();
		} else if(view.getId()==R.id.delForms){
			SharedPreferences prefs = getSharedPreferences(Constants.LOGGED_IN_USER_PREFERENCES_NAME, Context.MODE_PRIVATE);
			long userID = prefs.getLong(Constants.PREF_USER_ID, 0);
			if(userID != 0){
				formList = getDownloadedFormsList();
				if(formList!=null && formList.size() > 0){
					ArrayList<String> fileName = new ArrayList<String>();
					for(int position = 0; position<formList.size(); position++){
						FormList currentForm = formList.get(position);
			
						if (currentForm != null) {
							String name = currentForm.getFormName();
							if(name.contains(".")){
								String substr = name.substring(0, name.indexOf("."));
								fileName.add(substr);
							}else{
								fileName.add(name);
							}
						}
					}
					
					// custom dialog
					Context context=SettingsScreenActivity.this;
					final Dialog dialog = new Dialog(context);
					dialog.setContentView(R.layout.custom_view);
					dialog.setTitle("Select Form");
	
					// set the custom dialog components - radiogroup with radiobuttons
					final RadioGroup rdGroup = (RadioGroup) dialog.findViewById(R.id.selectItem);
					RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT,RadioGroup.LayoutParams.WRAP_CONTENT);
					for(int index = 0; index<formList.size(); index++){
						String name = formList.get(index).getFormName();
						if(name.contains("."))
							name = name.substring(0, name.indexOf("."));
						final RadioButton formNameButton = new RadioButton(this);
						formNameButton.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
						formNameButton.setText(name);
						formNameButton.setTextColor(getResources().getColor(R.color.White));
						formNameButton.setId(index);
						formNameButton.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								String formName = formList.get(formNameButton.getId()).getFormName();
							    ch = formName.charAt(0);
							    if (ch == Character.toUpperCase(ch)) {
							    	ch = Character.toLowerCase(ch);  
							    	formName = formName.replace(formName.charAt(0),ch);
							    }
							    Log.i("RadioButton Id", formName+"");
							    File file = new File(Environment.getExternalStorageDirectory().toString()+Constants.HTML_PATH+formName);
							    if (file.exists()){
						            boolean isDelete = file.delete(); 
						            if(isDelete == true)
						            	Toast.makeText(getApplicationContext(), formName+" deleted.", Toast.LENGTH_LONG).show();
						        }
							    dialog.dismiss();
								
							}
						});
						rdGroup.addView(formNameButton, layoutParams);
						
					} 	
					
					Button removeProfile = (Button) dialog.findViewById(R.id.removeSelectedItem);
					removeProfile.setVisibility(View.GONE);
					
	//				// if button is clicked, close the custom dialog
	//				removeProfile.setOnClickListener(new OnClickListener() {
	//					@Override
	//					public void onClick(View view) {						
	//						if(rdGroup.getCheckedRadioButtonId()!=-1){
	//						    int selectedRadioButtonId= rdGroup.getCheckedRadioButtonId();	
	//						    String formName = formList.get(selectedRadioButtonId).getFormName();
	//						    ch = formName.charAt(0);
	//						    if (ch == Character.toUpperCase(ch)) {
	//						    	ch = Character.toLowerCase(ch);  
	//						    	formName = formName.replace(formName.charAt(0),ch);
	//						    }
	//						    Log.i("RadioButton Id", formName+"");
	//						    File file = new File(Environment.getExternalStorageDirectory().toString()+Constants.HTML_PATH+formName);
	//						    if (file.exists()){
	//					            boolean isDelete = file.delete(); 
	//					            if(isDelete == true)
	//					            	Toast.makeText(getApplicationContext(), formName+" deleted.", Toast.LENGTH_LONG).show();
	//					        }
	//						}
	//						dialog.dismiss();
	//					}				
	//				});	
	//				dialog.setContentView(inflateView);
					dialog.show();
				}
				else{
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setMessage(Constants.FORMS_ERRORMESSAGE);
					builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							return;
						}					
					});
					AlertDialog alert = builder.create();
					alert.show();
				}
			} else
				Toast.makeText(getApplicationContext(), Constants.SKIP_ERRORMESSAGE, Toast.LENGTH_SHORT).show();
		}	
		else if(view == btnBack){
			open_db = new ApplicationDatabase(SettingsScreenActivity.this);
			open_db.openDataBase();
			if(!txtChgPwd.getText().toString().trim().equals("")){
				open_db.updatePassword(txtChgPwd.getText().toString().trim());
				startActivity(new Intent(SettingsScreenActivity.this, HomeScreenActivity.class));
				finish();
			}
			else if(txtChgPwd.getText().toString().trim().length() <= 4){
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("Password length should be greater then four characters.");
				builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						return;
					}					
				});
				AlertDialog alert = builder.create();
				alert.show();
			}
			else{
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("Please enter password");
				builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						return;
					}					
				});
				AlertDialog alert = builder.create();
				alert.show();
			}
			
		}
	}
	
	public ArrayList<FormList> getDownloadedFormsList()	{
		ArrayList<FormList> getFormsList = new ArrayList<FormList>(); 
       	File dir = new File(Environment.getExternalStorageDirectory().getPath()+Constants.HTML_PATH);
    	File[] files = dir.listFiles();
    	if(files.length > 0){
    		for(int index = 0;index < files.length;index++){
	    		FormList currentForm = new FormList();
	    	  	String name = files[index].getName();
	    	  	ch = name.charAt(0);
	    	    if (ch == Character.toUpperCase(ch)) 
	    	    	currentForm.setFormName(name);
	    	    else{
	    	       	ch = Character.toUpperCase(ch);  
	    	        name = name.replace(name.charAt(0),ch);
	    	        currentForm.setFormName(name);
	    	    }
	    	    getFormsList.add(currentForm);
    		}
    	}  
		return getFormsList;	
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) { //Back key pressed
			startActivity(new Intent(SettingsScreenActivity.this, HomeScreenActivity.class));
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
//	public void createCustomDialog(ArrayList<Profile> profileList){
//		
//	
//	}

//	private ArrayList<Profile> getDetails(ArrayList<String> profileName,ArrayList<String> profileId) {
//		// TODO Auto-generated method stub
//		ArrayList<Profile> list = new ArrayList<Profile>();
//		for(int i=0; i<profileId.size(); i++){
//			Profile info = new Profile();
//			Log.i("Size",profileId.get(i).toString());
//	        info.setID(Integer.parseInt(profileId.get(i).toString()));
//	        info.setName(profileName.get(i).toString());
//	        list.add(info);
//		}
//		return list;
//	}
}
