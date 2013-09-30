package govt.opendataapp;

import govt.opendataapp.parsing.WebParsing;
import govt.opendataapp.services.UpdateTables;
import govt.opendataapp.utils.Constants;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class SplashScreenActivity extends Activity {
	private  int SPLASH_TIME_OUT = 8000;
	private WebParsing jsonParser;
	private SharedPreferences applicationPreferences;
	private long lngUserID;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.splashscreenlayout);
	applicationPreferences = (SharedPreferences)getSharedPreferences(Constants.LOGGED_IN_USER_PREFERENCES_NAME, Context.MODE_PRIVATE);
	lngUserID = applicationPreferences.getLong(Constants.PREF_USER_ID, 0);
	jsonParser = new WebParsing();
	startService(new Intent(getApplicationContext(), UpdateTables.class));
	 new Handler().postDelayed(new Runnable() {
		  @Override
         public void run() {
             // This method will be executed once the timer is over
             // Start your app main activity
        		
			  if(lngUserID == 0){
				  Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
				  startActivity(i);
				  finish();
			  }
			  else{
				  Intent i = new Intent(SplashScreenActivity.this, HomeScreenActivity.class);
				  startActivity(i);
				  finish();
			  }

             // close this activity
             finish();
         }
     }, SPLASH_TIME_OUT);
}

}
