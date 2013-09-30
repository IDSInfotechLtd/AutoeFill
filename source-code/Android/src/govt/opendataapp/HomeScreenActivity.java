package govt.opendataapp;

import govt.opendataapp.utils.Constants;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class HomeScreenActivity extends Activity implements View.OnTouchListener{
	
	private Button btnSearch;
	private Button btnEditProfile;
	private Button btnSettings;
	private Button btnViewFilledPDFForm;
	private Button btnCreateNewProfile;
	private AlertDialog.Builder dialogAlert;
	private Button btnViewDownloadedForm;
	private Button btnLogout;
	private SharedPreferences appPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home_activity);
		btnLogout=(Button)findViewById(R.id.logout);
		btnSearch = (Button)findViewById(R.id.btnSearch);
		btnEditProfile = (Button)findViewById(R.id.btn_edit_profile);
		btnSettings = (Button)findViewById(R.id.btn_settings);
		btnViewFilledPDFForm = (Button)findViewById(R.id.btn_viewpdf);
		btnCreateNewProfile = (Button)findViewById(R.id.btn_createNewProfile);
		btnViewDownloadedForm = (Button)findViewById(R.id.btn_viewdownloadedform);
		dialogAlert =new AlertDialog.Builder(HomeScreenActivity.this);
		appPreferences = (SharedPreferences)getSharedPreferences(Constants.LOGGED_IN_USER_PREFERENCES_NAME, Context.MODE_PRIVATE);
		
//		
//		String key="test" ;
//		key = getIntent().getExtras().getString(Constants.TAG);
//		if(key.equals("skip")){
//			btnCreateNewProfile.setEnabled(false);
//			btnEditProfile.setEnabled(false);
//		}
		String profileId= appPreferences.getString(Constants.PREF_PROFILE_ID, "");
		String profileName=appPreferences.getString(Constants.PREF_PROFILE_NAME, "");
		if(profileId.equals("0") && profileName.equals("guest")){
			btnEditProfile.setEnabled(false);
			btnCreateNewProfile.setEnabled(false);
			btnSettings.setEnabled(false);
			btnEditProfile.setBackgroundResource(R.drawable.edit_profile_hover);
			btnCreateNewProfile.setBackgroundResource(R.drawable.new_profile_hover);
			btnSettings.setBackgroundResource(R.drawable.setting_hover);
			btnLogout.setVisibility(View.GONE);
		}
		else {
			btnEditProfile.setEnabled(false);
			btnCreateNewProfile.setEnabled(true);
			btnSettings.setEnabled(true);
		}
		
		if(profileId.equals("0")){
			btnEditProfile.setEnabled(false);
		}
		else{
			btnEditProfile.setEnabled(true);
		}
		
		btnSearch.setOnTouchListener(this);
		btnEditProfile.setOnTouchListener(this);
		btnSettings.setOnTouchListener(this);
		btnViewFilledPDFForm.setOnTouchListener(this);
		btnCreateNewProfile.setOnTouchListener(this);
		btnViewDownloadedForm.setOnTouchListener(this);
		btnLogout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialogAlert.setTitle(getResources().getString(R.string.alert_title));
				dialogAlert.setMessage(getResources().getString(R.string.Logout_string));
				dialogAlert.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dailog, int arg1) {
						SharedPreferences prefs = getApplicationContext().getSharedPreferences(Constants.LOGGED_IN_USER_PREFERENCES_NAME, Context.MODE_PRIVATE);
						Editor prefEdit = prefs.edit();
						prefEdit.putLong(Constants.PREF_USER_ID, 0);
						prefEdit.putString(Constants.PREF_PROFILE_ID, "0");
						prefEdit.commit();
						startActivity(new Intent(HomeScreenActivity.this,LoginActivity.class));
						finish();
					}
				});
				dialogAlert.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dailog, int arg1) {
					dailog.cancel();
					}
				});
				dialogAlert.show();
				
			}
		});
	}
	

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int action = event.getAction();
		if(v==btnSearch && action == MotionEvent.ACTION_DOWN){
			Bitmap bitmap = ((BitmapDrawable)btnSearch.getBackground()).getBitmap();
			int pixel = bitmap.getPixel((int)event.getX(), (int)event.getY());
//			WebParsing webParser = new WebParsing();
//			HashMap<String, String> abc = new HashMap<String, String>();
//			abc.put(Constants.URL_PARAMETERS_CATEGORY_ID, "1");
//			abc.put(Constants.URL_PARAMETERS_KEYWORD, "");
//			abc.put(Constants.URL_PARAMETERS_LEVEL, "0");
//			abc.put(Constants.COUNTRY_ID, "1");
//			abc.put(Constants.STATE_ID, "1");
//			abc.put(Constants.DISTRICT_ID, "14");
			
//			ArrayList<HashMap<String, String>> countryList = webParser.getSearchFormList(Constants.METHOD_SELECTED_FORMS, abc);
//			if(countryList.get(0).get(Constants.JSON_ERRORMESSAGE).equals(Constants.ERROR_MESSAGE)){
//				Log.e(Constants.JSON_ERRORMESSAGE, countryList.get(0).get(Constants.JSON_ERRORMESSAGE));
//			}
//			else{
//				for(int index = 0;index < countryList.size();index++){
//					Log.e(Constants.FORM_ID, countryList.get(index).get(Constants.FORM_ID));
//					Log.e(Constants.FORM_NAME, countryList.get(index).get(Constants.FORM_NAME));
//					Log.e(Constants.FORM_LOCATION, countryList.get(index).get(Constants.FORM_LOCATION));
//				}
//			}
			
			if(event.getX() < bitmap.getWidth()/2 && event.getY() > bitmap.getHeight()/2 && pixel == 0){
				Intent intent = new Intent(HomeScreenActivity.this, UserProfileActivity.class);
				intent.putExtra(Constants.EDIT_NEW, Constants.EDIT);
				startActivity(intent);
			}
			else if(event.getX()>bitmap.getWidth()/2  && event.getY() > bitmap.getHeight()/2 && pixel == 0){				
				Editor htmlEditor = appPreferences.edit();
				htmlEditor.putString(Constants.PREF_URL_OR_DOWNLOADED, Constants.PREF_DOWNLOADED);
				htmlEditor.commit();
				startActivity(new Intent(HomeScreenActivity.this, DownloadedFormListActivity.class));
			}else if(pixel != 0){
				if(isInternetOn()){
					Editor htmlEditor = appPreferences.edit();
					htmlEditor.putString(Constants.PREF_URL_OR_DOWNLOADED, Constants.PREF_URL);
					htmlEditor.commit();
					startActivity(new Intent(HomeScreenActivity.this, SearchFormActivity.class));
				}
				else{
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setMessage("No internet connection. Please enable internet connection and restart the application.");
					builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}					
					});
					AlertDialog alert = builder.create();
					alert.show();
				}
			}
			return true;
		}
		else if(v==btnEditProfile && action == MotionEvent.ACTION_DOWN){
			Bitmap bitmap = ((BitmapDrawable)btnEditProfile.getBackground()).getBitmap();
			int pixel = bitmap.getPixel((int)event.getX(), (int)event.getY());
			if(event.getX() > bitmap.getWidth()/2  && event.getY() < bitmap.getHeight()/2 && pixel == 0){
				if(isInternetOn()){
					Editor htmlEditor = appPreferences.edit();
					htmlEditor.putString(Constants.PREF_URL_OR_DOWNLOADED, Constants.PREF_URL);
					htmlEditor.commit();
					startActivity(new Intent(HomeScreenActivity.this, SearchFormActivity.class));
				}
				else{
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setMessage("No internet connection. Please enable internet connection and restart the application.");
					builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}					
					});
					AlertDialog alert = builder.create();
					alert.show();
				}
			}else if(pixel != 0){
				Intent intent = new Intent(HomeScreenActivity.this, UserProfileActivity.class);
				intent.putExtra(Constants.EDIT_NEW, Constants.EDIT);
				startActivity(intent);
			}
			return true;
		}
		else if(v == btnSettings && action == MotionEvent.ACTION_DOWN){
			Bitmap bitmap = ((BitmapDrawable)btnSettings.getBackground()).getBitmap();
			int pixel = bitmap.getPixel((int)event.getX(), (int)event.getY());
			if(event.getX() > bitmap.getWidth()/2 && event.getY() > bitmap.getHeight()/2 && pixel == 0){
				startActivity(new Intent(HomeScreenActivity.this, PdfListActivity.class));
			}else if(pixel != 0){
				startActivity(new Intent(HomeScreenActivity.this, SettingsScreenActivity.class));
				finish();
			}
			return true;
		}
		else if(v == btnViewFilledPDFForm && action == MotionEvent.ACTION_DOWN){
			Bitmap bitmap = ((BitmapDrawable)btnViewFilledPDFForm.getBackground()).getBitmap();
			int pixel = bitmap.getPixel((int)event.getX(), (int)event.getY());
			if(event.getX() < bitmap.getWidth()/2 && event.getY() < bitmap.getHeight()/2 && pixel == 0){
				startActivity(new Intent(HomeScreenActivity.this, SettingsScreenActivity.class));
				finish();
			}else if(event.getX() > bitmap.getWidth()/2 && event.getY() < bitmap.getHeight()/2 && pixel == 0){
				Intent intent = new Intent(HomeScreenActivity.this, UserProfileActivity.class);
				intent.putExtra(Constants.EDIT_NEW, Constants.NEW);
				startActivity(intent);
			}else if(pixel != 0){
				startActivity(new Intent(HomeScreenActivity.this, PdfListActivity.class));
			}
			return true;
		}
		else if(v == btnCreateNewProfile && action == MotionEvent.ACTION_DOWN){
			Bitmap bitmap = ((BitmapDrawable)btnCreateNewProfile.getBackground()).getBitmap();
			int pixel = bitmap.getPixel((int)event.getX(), (int)event.getY());
			if(event.getX() < bitmap.getWidth()/2 && event.getY() > bitmap.getHeight()/2 && pixel == 0){
				startActivity(new Intent(HomeScreenActivity.this, PdfListActivity.class));
			}else if(pixel != 0){
				Intent intent = new Intent(HomeScreenActivity.this, UserProfileActivity.class);
				intent.putExtra(Constants.EDIT_NEW, Constants.NEW);
				startActivity(intent);
			}
			return true;
		}
		else if(v == btnViewDownloadedForm && action == MotionEvent.ACTION_DOWN){
			Bitmap bitmap = ((BitmapDrawable)btnViewDownloadedForm.getBackground()).getBitmap();
			int pixel = bitmap.getPixel((int)event.getX(), (int)event.getY());
			if(event.getX() < bitmap.getWidth()/2 && event.getY() < bitmap.getHeight()/2 && pixel == 0){
				if(isInternetOn()){
					startActivity(new Intent(HomeScreenActivity.this, SearchFormActivity.class));
				}
				else{
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setMessage("No internet connection. Please enable internet connection and restart the application.");
					builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}					
					});
					AlertDialog alert = builder.create();
					alert.show();
				}			}else if(pixel != 0){
				Editor htmlEditor = appPreferences.edit();
				htmlEditor.putString(Constants.PREF_URL_OR_DOWNLOADED, Constants.PREF_DOWNLOADED);
				htmlEditor.commit();
				startActivity(new Intent(HomeScreenActivity.this, DownloadedFormListActivity.class));
			}
			return true;
		}
		return false;
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		appPreferences = (SharedPreferences)getSharedPreferences(Constants.LOGGED_IN_USER_PREFERENCES_NAME, Context.MODE_PRIVATE);
		String profileId= appPreferences.getString(Constants.PREF_PROFILE_ID, "");
		String profileName=appPreferences.getString(Constants.PREF_PROFILE_NAME, "");
		if(profileId.equals("0") && profileName.equals("guest")){
			btnEditProfile.setEnabled(false);
			btnCreateNewProfile.setEnabled(false);
			btnSettings.setEnabled(false);
		}
		else {
			btnEditProfile.setEnabled(false);
			btnCreateNewProfile.setEnabled(true);
			btnSettings.setEnabled(true);
		}
		
		if(profileId.equals("0")){
			btnEditProfile.setEnabled(false);
		}
		else{
			btnEditProfile.setEnabled(true);
		}
	}
	
	
	/**
	 * @desc This method checks weather Internet is on or not.
	 * @return
	 */
	public boolean isInternetOn() {
		ConnectivityManager connec = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);		   
		if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED
				|| connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING
				|| connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING
				|| connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
			
			return true;
		} else if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED
				|| connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
			return false;
		}
		return false;
	}
	
	
		
}
