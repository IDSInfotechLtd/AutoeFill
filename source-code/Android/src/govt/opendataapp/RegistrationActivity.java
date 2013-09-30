package govt.opendataapp;

import govt.opendataapp.database.ApplicationDatabase;
import govt.opendataapp.database.DatabaseContractClass;
import govt.opendataapp.utils.Constants;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class RegistrationActivity extends Activity implements View.OnClickListener{
	private EditText txtUserName ;
	private EditText txtPassword ;
	private EditText txtConfirmPassword;
	private Button btnRegister;
	private ApplicationDatabase dbDataBaseObject;
	private AlertDialog.Builder dialogAlert;
	private String strUserName;
	private String strPassword;
	private String strConfirmPassword;
	private long userID;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.registration);
        dbDataBaseObject = new ApplicationDatabase(this);
        dbDataBaseObject.openDataBase();
       txtUserName = (EditText) findViewById(R.id.username);
       txtPassword = (EditText) findViewById(R.id.password);
       txtConfirmPassword = (EditText) findViewById(R.id.confirmpassword);
	   btnRegister= (Button) findViewById(R.id.registerbutton);
	   dialogAlert =new AlertDialog.Builder(RegistrationActivity.this);
	   btnRegister.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v==btnRegister){
			strUserName =	txtUserName.getText().toString().trim();
			strPassword = txtPassword.getText().toString().trim();
			strConfirmPassword =txtConfirmPassword.getText().toString().trim();
			if(strUserName.equals("")){
				dialogAlert.setTitle(getResources().getString(R.string.login_alert_error_heading));
				dialogAlert.setMessage(getResources().getString(R.string.no_user));
				dialogAlert.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dailog, int arg1) {
						// TODO Auto-generated method stub
						dailog.cancel();
					}
				});
				dialogAlert.show();
			}
			else if(strPassword.equals("")){
				dialogAlert.setTitle(getResources().getString(R.string.login_alert_error_heading));
				dialogAlert.setMessage(getResources().getString(R.string.no_password));
				dialogAlert.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dailog, int arg1) {
						// TODO Auto-generated method stub
						dailog.cancel();
					}
				});
				dialogAlert.show();
			}
			else if(strConfirmPassword.equals("")){
				dialogAlert.setTitle(getResources().getString(R.string.login_alert_error_heading));
				dialogAlert.setMessage(getResources().getString(R.string.no_confirm_password));
				dialogAlert.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dailog, int arg1) {
						// TODO Auto-generated method stub
						dailog.cancel();
					}
				});
				dialogAlert.show();
			}
			else if(strPassword.length()<=4){
				dialogAlert.setTitle(getResources().getString(R.string.login_alert_error_heading));
				dialogAlert.setMessage(getResources().getString(R.string.password_not_valid));
				dialogAlert.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dailog, int arg1) {
						dailog.cancel();
					}
				});
				dialogAlert.show();
			}
			else{
				boolean passwordMatches = passwordValidator(strPassword, strConfirmPassword);
				if(passwordMatches){
					String  strUserNameExist = usernameExists(strUserName);
					if(strUserNameExist.equals(Constants.CHECK_USER_NAME)){
						dialogAlert.setTitle(getResources().getString(R.string.login_alert_error_heading));
						dialogAlert.setMessage(getResources().getString(R.string.user_name_not_valid));
						dialogAlert.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dailog, int arg1) {
								dailog.cancel();
							}
						});
						dialogAlert.show();
					}
					else if(strUserNameExist.equals(Constants.USER_NAME_EXIST)){
						dialogAlert.setTitle(getResources().getString(R.string.login_alert_error_heading));
						dialogAlert.setMessage(getResources().getString(R.string.user_name_exist));
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
						 cv.put(DatabaseContractClass.User.USERNAME, strUserName);
						 cv.put(DatabaseContractClass.User.PASSWORD, strPassword);
						 userID = dbDataBaseObject.insertDataInTable(DatabaseContractClass.User.TABLE_NAME, cv);
						 dialogAlert.setTitle(getResources().getString(R.string.alert_title));
						 dialogAlert.setMessage(getResources().getString(R.string.registered_successfully));
						 dialogAlert.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
							 @Override
							 public void onClick(DialogInterface dailog, int arg1) {
								 SharedPreferences prefs = getApplicationContext().getSharedPreferences(Constants.LOGGED_IN_USER_PREFERENCES_NAME, Context.MODE_PRIVATE);
								 Editor edit = prefs.edit();
								 edit.putLong(Constants.PREF_USER_ID, userID);
								 edit.putString(Constants.PREF_PROFILE_ID, "0");
								 edit.putString(Constants.PREF_PROFILE_NAME, "");
								 edit.putString(Constants.PREF_USER_PASSWORD, strPassword);
								 edit.commit();
								 startActivity(new Intent(RegistrationActivity.this,HomeScreenActivity.class));
								 finish();
							 }
						 });
						 dialogAlert.show();
					 }
				}
				else{
					dialogAlert.setTitle(getResources().getString(R.string.login_alert_error_heading));
					dialogAlert.setMessage(getResources().getString(R.string.password_mismatch));
					dialogAlert.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dailog, int arg1) {
							dailog.cancel();
						}
					});
						
					dialogAlert.show();
				}
			}
		}
	}
	
	public boolean passwordValidator(String password , String confirmpassword) {
		if(password.equals(confirmpassword)){
			return true;
		}
		else{
			return false;	
		}
	}
	
	public String usernameExists(String username) {
		if(username.trim().length()<4){
			return Constants.CHECK_USER_NAME;
		}
	    else if(!dbDataBaseObject.checkusername(username))
	    {
	    	return Constants.USER_NAME_EXIST;
	    }
	    else{
	    	return Constants.NO_ERROR;
	    }	
	}
}