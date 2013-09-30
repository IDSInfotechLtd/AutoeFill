package govt.opendataapp;

import govt.opendataapp.database.ApplicationDatabase;
import govt.opendataapp.parsing.WebParsing;
import govt.opendataapp.services.UpdateTables;
import govt.opendataapp.utils.Constants;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;


@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.FROYO)
public class LoginActivity extends Activity implements View.OnClickListener{
	private EditText txtUsername ;
	private EditText txtPassword ;
	private Button btnRegister;
	private Button btnSkip;
	private Button btnLogin;
	private AlertDialog.Builder dialogAlert;
	private ApplicationDatabase dbDataBaseObject;
	private String strUserName;
	private String strPassword;
	
	private SharedPreferences applicationPreferences;
	
	
	
	@TargetApi(Build.VERSION_CODES.FROYO)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_activity);
		
		dbDataBaseObject = new ApplicationDatabase(this);
//		dbDataBaseObject.openDataBase();
		txtUsername = (EditText) findViewById(R.id.username);
	    txtPassword = (EditText) findViewById(R.id.password);
	    btnRegister= (Button) findViewById(R.id.registrationbutton);
	    btnSkip =(Button) findViewById(R.id.skipbutton);
	    btnLogin=(Button) findViewById(R.id.loginbutton);
	    applicationPreferences = (SharedPreferences)getSharedPreferences(Constants.LOGGED_IN_USER_PREFERENCES_NAME, Context.MODE_PRIVATE);
	    dialogAlert =new AlertDialog.Builder(LoginActivity.this);
	    btnRegister.setOnClickListener(this);
	    btnSkip.setOnClickListener(this);
	    btnLogin.setOnClickListener(this);
	    if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
	
	}
	@Override
	public void onClick(View currentView) {
		if(currentView == btnRegister){
			Intent intent = new Intent(LoginActivity.this,RegistrationActivity.class);
			startActivity(intent);
			finish();
		}
		else if(currentView == btnLogin){
			strUserName = txtUsername.getText().toString().trim();
			strPassword = txtPassword.getText().toString().trim();
			
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
					
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				dialogAlert.show();
			}
			else {
				dbDataBaseObject.openDataBase();
				boolean loginsucessful = dbDataBaseObject.verifyCrediantials(strUserName, strPassword); 
				if(loginsucessful){
					Intent intent = new Intent(LoginActivity.this,HomeScreenActivity.class);
					startActivity(intent);
					finish();
				}
				else {
					dialogAlert.setTitle(getResources().getString(R.string.login_alert_error_heading));
					dialogAlert.setMessage(getResources().getString(R.string.not_valid_crediantials));
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
		else if(currentView == btnSkip){
			Intent intent = new Intent(LoginActivity.this,HomeScreenActivity.class);
			Editor prefEdit = applicationPreferences.edit();
			prefEdit.putLong(Constants.PREF_USER_ID, 0);
			prefEdit.putString(Constants.PREF_PROFILE_ID, "0");
			prefEdit.putString(Constants.PREF_PROFILE_NAME, "guest");
			prefEdit.commit();
			intent.putExtra(Constants.TAG,Constants.SKIP_KEY);
			startActivity(intent);
			finish();
		} 
		
	}
}
