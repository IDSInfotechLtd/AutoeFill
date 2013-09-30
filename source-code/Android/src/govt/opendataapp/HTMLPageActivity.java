package govt.opendataapp;


import govt.opendataapp.database.ApplicationDatabase;
import govt.opendataapp.database.DatabaseContractClass;
import govt.opendataapp.pdf.WebPageToPDF;
import govt.opendataapp.progressdialog.ProgressDialog;
import govt.opendataapp.utils.Constants;
import govt.opendataapp.utils.Profile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfWriter;


@SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
public class HTMLPageActivity extends Activity implements View.OnClickListener{



	private TextView txtFormTitle;
	private WebView webPage;
	private String strFormTitle;
	private String strFormLocation;
	private String DESKTOP_USERAGENT;
	private Button btnSavePage, btnSaveAsPdf, btnPrint, btnEmailPdf, btnBack, btnChgProfile;
	private MyJavaScriptInterface myJavaScriptInterface;
	private Document itextDocument;
	private WebPageToPDF pdfConvertor;
	private Bitmap mBitmap;
	private SharedPreferences applicationPreferences;
	private File htmlFile;
	private ProgressDialog customProgressDialog;
	private URI uri = null;
	private Picture pictureWebPage; 
	private String formName;
	private ApplicationDatabase databaseObject;
	private String isFormDownloaded, strProfileID, strProfileName;
	private String strSelectedProfileID;
	private long lngUserID;
	private ArrayList<Profile> getAllProfile;
	private String strSelectedProfile;

	@SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.html_view);
		Bundle extras = getIntent().getExtras();
		strFormTitle = extras.getString(Constants.FORM_NAME);
		strFormLocation = extras.getString(Constants.FORM_LOCATION);
		txtFormTitle = (TextView)findViewById(R.id.form_title);
		webPage = (WebView)findViewById(R.id.web_html_form);
		btnSavePage = (Button)findViewById(R.id.btnSavePage);
		btnSaveAsPdf =(Button)findViewById(R.id.btnSaveaspdf);
		btnEmailPdf=(Button)findViewById(R.id.btnemailpdf);
		btnPrint =(Button)findViewById(R.id.btnprint);
		btnBack =(Button)findViewById(R.id.backbtn);
		btnChgProfile = (Button)findViewById(R.id.btn_Change_Profile);
		txtFormTitle.setTextColor(getResources().getColor(R.color.White));
		customProgressDialog = new ProgressDialog(this, R.drawable.spinner);
		databaseObject = new ApplicationDatabase(HTMLPageActivity.this);
		databaseObject.openDataBase();
		btnSaveAsPdf.setOnClickListener(this);
		btnEmailPdf.setOnClickListener(this);
		btnPrint.setOnClickListener(this);
		btnSavePage.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		btnChgProfile.setOnClickListener(this);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		applicationPreferences = (SharedPreferences)getSharedPreferences(Constants.LOGGED_IN_USER_PREFERENCES_NAME, Context.MODE_PRIVATE);
		isFormDownloaded = applicationPreferences.getString(Constants.PREF_URL_OR_DOWNLOADED, "");
		lngUserID = applicationPreferences.getLong(Constants.PREF_USER_ID, 0);
		strProfileID = applicationPreferences.getString(Constants.PREF_PROFILE_ID, "0");
		strSelectedProfileID = strProfileID;
		strProfileName = applicationPreferences.getString(Constants.PREF_PROFILE_NAME, "");
		strSelectedProfile = strProfileName;
		itextDocument = new Document();
		pdfConvertor = new WebPageToPDF();
		txtFormTitle.setText(strFormTitle);
		
		SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.HTMLPAGE_PREFERENCES_NAME, 0); // 0 - for private mode
		Editor editor = pref.edit();
		editor.putString("ids", null);
		editor.commit();
		
		DESKTOP_USERAGENT = webPage.getSettings().getUserAgentString ();
		DESKTOP_USERAGENT = DESKTOP_USERAGENT.replace("Android 4.2.2;","");
		DESKTOP_USERAGENT = DESKTOP_USERAGENT.replace("Mobile",""); 
		//        Log.e("", DESKTOP_USERAGENT);
		webPage.getSettings().setUserAgentString(DESKTOP_USERAGENT); 
		webPage.getSettings().setJavaScriptEnabled(true);
		webPage.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		myJavaScriptInterface= new MyJavaScriptInterface(this);
		webPage.addJavascriptInterface(myJavaScriptInterface, "autofill");

		//        web.getSettings().setUserAgentString(newUA);
		webPage.setHorizontalScrollBarEnabled(true);
		webPage.getSettings().setBuiltInZoomControls(true);
		webPage.getSettings().setDefaultZoom(ZoomDensity.MEDIUM);
		webPage.getSettings().setUseWideViewPort(true);
		webPage.getSettings().setLoadWithOverviewMode(true);
		webPage.setDrawingCacheEnabled(true);
		webPage.getSettings().setUseWideViewPort(true); 

		if(isFormDownloaded.equals(Constants.PREF_URL)){
			URI uri = null;
			try {
				URL url = new URL(strFormLocation);
				uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			saveHTMLToFile(uri, Constants.TEMP_HTML_FILE);
			htmlFile = new File(Environment.getExternalStorageDirectory()+"/HTML/", Constants.TEMP_HTML_FILE+".html");
		}
		else if(isFormDownloaded.equals(Constants.PREF_DOWNLOADED)){
			htmlFile = new File(Environment.getExternalStorageDirectory()+"/HTML/", strFormTitle+".html");
			
		}
		
		Log.e("", "file:/"+htmlFile.getAbsolutePath());
			
		webPage.loadUrl("file:/"+htmlFile.getAbsolutePath());
		
		webPage.setWebViewClient(new WebViewClient(){
		
			
			@Override
			public void onPageFinished(WebView view, String url)
			{  
				super.onPageFinished(view, url);
				view.loadUrl(Constants.JAVASCRIPTTEMPVALUE);
//				Toast.makeText(HTMLPageActivity.this, "on pagefinished", Toast.LENGTH_SHORT).show();
				injectJavascript(view);
				
			}
		});


		
	}
	public void injectJavascript(WebView view){
		view.loadUrl(Constants.JAVASCRIPTSETVALUE);
		SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.LOGGED_IN_USER_PREFERENCES_NAME, 0); // 0 - for private mode
		String profileid = pref.getString(Constants.PREF_PROFILE_ID, null);
		if(profileid=="0"){
		}
		else{
			myJavaScriptInterface.setinhtmlpage(strProfileID);
		}
 }


	public String saveHTMLToFile(URI URL, String formTitle){
		Log.e("URL", ""+URL);
		String fileName = "";
		String pageHTML = "";
		HttpClient vClient = new DefaultHttpClient();
		HttpGet vGet = new HttpGet(URL);
		try {
			ResponseHandler<String> vHandler = new BasicResponseHandler();
			pageHTML = vClient.execute(vGet, vHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
		File file = new File(Environment.getExternalStorageDirectory()+"/HTML");
		if(!file.mkdirs());
		File newfile = new File(Environment.getExternalStorageDirectory()+"/HTML", formTitle.replace(" ", "_") + ".html");
		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(newfile);
			outputStream.write(pageHTML.getBytes());
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileName;
	}



	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}
	
	public class MyJavaScriptInterface {
		String[] ids1;
		String[] values1;
		Context mContext;

		MyJavaScriptInterface(){

		}

		MyJavaScriptInterface(Context c) {
			mContext = c;
		}

		
		@JavascriptInterface
        public void receiveValueFromJs(String str) {
             //do something useful with str
//             Toast.makeText(mContext, "Received Value from JS: " + str,Toast.LENGTH_LONG).show();
        }
		public void setinhtmlpage(String profileID){

			ApplicationDatabase openDataBaseObject =new ApplicationDatabase(getApplicationContext());
			openDataBaseObject.openDataBase();
			openDataBaseObject.selectValues(DatabaseContractClass.UserInformation.TABLE_NAME,myJavaScriptInterface, profileID);
			getFormValues();
		}
		
		@JavascriptInterface
		public void print(String[] ids,String[] values,String profileID) {
//			Toast.makeText(HTMLPageActivity.this, "javascript callles", Toast.LENGTH_LONG).show();
			ApplicationDatabase openDataBaseObject =new ApplicationDatabase(getApplicationContext());
			openDataBaseObject.openDataBase();
			ContentValues contentValues = new ContentValues();
			HashMap<String, String> map = new HashMap<String, String>();
			try{
				for(int i=0;i<=ids.length-1;i++){
					Log.e(ids[i], values[i]);
					map.put(ids[i], values[i]);
					SharedPreferences pref=	getApplicationContext().getSharedPreferences(Constants.HTMLPAGE_PREFERENCES_NAME, 0);
					if(pref.contains(ids[i])){

					}
					else{
						Editor edit = pref.edit();
						edit.putString(ids[i], null);
						edit.commit();
						openDataBaseObject.setDataBaseVersionAndColumn(ApplicationDatabase.dataBaseVersion+1, ids[i]);
					}
				}
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			for(int j=0;j<=ids.length-1;j++){
				if(ids[j].equals("")){
				}
				else{
					
				contentValues.put(ids[j], values[j]);
			}
				}
//			SharedPreferences pref=	getApplicationContext().getSharedPreferences(Constants.LOGGED_IN_USER_PREFERENCES_NAME, 0);
//			String profileid =pref.getString(Constants.PREF_PROFILE_ID, "");
			if(!profileID.equals("0")){
				openDataBaseObject.updateprofile(contentValues, profileID);
			}
			
		}

		public void getFormValues(){
			if(!strProfileID.equals("0")){
				if(ids1!=null){
					for(int k=0;k<=ids1.length-1;k++)
					{	
						if(values1[k].equals("null"))
						{
							values1[k]="";
							webPage.loadUrl("javascript:SetVal('"+ids1[k]+"','"+values1[k]+"')");
						}
						else
						{
							webPage.loadUrl("javascript:SetVal('"+ids1[k]+"','"+values1[k]+"')");
						}
						
					}
				}
			}
			else{
				AlertDialog.Builder alertbox = new AlertDialog.Builder(HTMLPageActivity.this);
            	alertbox.setMessage("No Profile found so Auto fill can not be implemented.");
            	alertbox.setPositiveButton("OK", new DialogInterface.OnClickListener() 
            	{
            		@Override
            		public void onClick(DialogInterface dialog, int which) 
            		{
            			return;
            		}
            	});
            	alertbox.show();
			}
		}
		public void setfromdatabase(String[] ids,String[] values){
			ids1=ids;
			values1=values;
		}
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.equals(btnSavePage)){
			try{
				Log.e("PROFILEID", ""+strProfileID);
//				webPage.loadUrl("javascript:getValue('"+strProfileID+"')");
				URL url = new URL(strFormLocation);
				uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
				saveHTMLToFile(uri, strSelectedProfile+strFormTitle);
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
//			AlertDialog.Builder alertbox = new AlertDialog.Builder(HTMLPageActivity.this);
//            alertbox.setMessage("HTML form has been successfully saved in SDCard.");
//            alertbox.setPositiveButton("OK", new DialogInterface.OnClickListener() 
//            {
//            	@Override
//                public void onClick(DialogInterface dialog, int which) 
//                {		  	    				  
//            		return;
//                }
//            });
//            alertbox.show();
		}
		if(v.equals(btnSaveAsPdf))
		{	
//			webPage.loadUrl("javascript:getValue('"+strProfileID+"')"); 
			pictureWebPage = webPage.capturePicture();
			customProgressDialog.show();
			formName = strSelectedProfile.replace(" ", "_") + strFormTitle;
			Thread background = new Thread(new Runnable() {
                
                public void run() {
                    try {
                    	try{
                			File file = new File(Environment.getExternalStorageDirectory()+"/PDF");
                			if(!file.mkdirs());
                			File newfile = new File(Environment.getExternalStorageDirectory()+"/PDF", formName.replace(" ", "_") + ".pdf");
                			if(newfile.exists()){
                				newfile.delete();
                			}
                			Log.e("PDF URL", newfile.getAbsolutePath());
                			itextDocument = new Document();
                			PdfWriter writerPDf = PdfWriter.getInstance(itextDocument, new FileOutputStream(newfile));
                			pdfConvertor.convertToPDF(webPage, itextDocument, writerPDf, pictureWebPage, mBitmap, formName);
    						Message msgObj = handlerDialog.obtainMessage();
                            Bundle b = new Bundle();
                            b.putString("message", "ok");
                            msgObj.setData(b);
    						handlerDialog.sendMessage(msgObj);
                        }
                        catch(Exception ex){
                        	ex.printStackTrace();
                        }
                    	
                    	
                    } catch (Throwable t) {
                        // just end the background thread
                    	Message msgObj = handlerDialog.obtainMessage();
                        Bundle b = new Bundle();
                        b.putString("message", "not ok");
                        msgObj.setData(b);
						handlerDialog.sendMessage(msgObj);
                        Log.i("Animation", "Thread  exception " + t);
                    }
                }
               
                private Handler handlerDialog = new Handler() {
                	 
                    public void handleMessage(Message msg) {
                         
                        String aResponse = msg.getData().getString("message");

                        if (aResponse.equals("ok")) {

                        	AlertDialog.Builder alertbox = new AlertDialog.Builder(HTMLPageActivity.this);
                        	alertbox.setMessage("PDF has been successfully saved in SDCard.");
                        	alertbox.setPositiveButton("OK", new DialogInterface.OnClickListener() 
                        	{
                        		@Override
                        		public void onClick(DialogInterface dialog, int which) 
                        		{		  	    				  
      		    				 			
                        		}
                        	});
                        	alertbox.show();
                            customProgressDialog.dismiss();
                        }
                        else
                        {
                        	AlertDialog.Builder alertbox = new AlertDialog.Builder(HTMLPageActivity.this);
                        	alertbox.setMessage("Unable to create pdf. Please try again later");
                        	alertbox.setPositiveButton("OK", new DialogInterface.OnClickListener() 
                        	{
                        		@Override
                        		public void onClick(DialogInterface dialog, int which) 
                        		{		  	    				  
                        			return;
                        		}
                        	});
                        	alertbox.show();
                            customProgressDialog.dismiss();
                        }   

                    }
                };
			});
			background.start();
			
		}
		
		if(v.equals(btnPrint)){
//			webPage.loadUrl("javascript:GetVal()"); 
			
			
			 pictureWebPage = webPage.capturePicture();
				formName = strSelectedProfile.replace(" ", "_") + strFormTitle;
				File file = new File(Environment.getExternalStorageDirectory()+"/PDF");
				if(!file.mkdirs());
				File newfile = new File(Environment.getExternalStorageDirectory()+"/PDF", formName.replace(" ", "_") + ".pdf");
				if(newfile.exists()){
					newfile.delete();
				}
				Log.e("PDF URL", newfile.getAbsolutePath());
				itextDocument = new Document();
				PdfWriter writerPDf;
	  try {
					writerPDf = PdfWriter.getInstance(itextDocument, new FileOutputStream(newfile));
					pdfConvertor.convertToPDF(webPage, itextDocument, writerPDf, pictureWebPage, mBitmap, formName);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	  Intent printIntent = new Intent(this, PrintDialogActivity.class);
				Uri imageUri = Uri.fromFile(newfile);
				printIntent.setDataAndType(imageUri, "application/pdf");
				printIntent.putExtra("title", "test print");
				startActivity(printIntent);

		}
		if(v.equals(btnEmailPdf)){
//			webPage.loadUrl("javascript:getValue('"+strProfileID+"')");
			pictureWebPage = webPage.capturePicture();
			customProgressDialog.show();
			Thread background = new Thread(new Runnable() {
                
                public void run() {
                    try {
                    	formName = strSelectedProfile.replace(" ", "_") + strFormTitle;
                    	File file = new File(Environment.getExternalStorageDirectory()+"/PDF");
            			if(!file.mkdirs());
            			itextDocument = new Document();
            			File newfile = new File(Environment.getExternalStorageDirectory()+"/PDF", formName.replace(" ", "_") + ".pdf");
            			if(newfile.exists()){
            				newfile.delete();
            			}
            			PdfWriter writerPDf = PdfWriter.getInstance(itextDocument, new FileOutputStream(newfile));
            			if(writerPDf.isCloseStream()){
            				writerPDf.open();
            				writerPDf = PdfWriter.getInstance(itextDocument, new FileOutputStream(newfile));
            			}
                    	pdfConvertor.convertToPDF(webPage, itextDocument, writerPDf, pictureWebPage, mBitmap, formName);
						Message msgObj = handlerDialog.obtainMessage();
                        Bundle b = new Bundle();
                        b.putString("message", "ok");
                        msgObj.setData(b);
						handlerDialog.sendMessage(msgObj);
                    } catch (Throwable t) {
                        // just end the background thread
                    	Message msgObj = handlerDialog.obtainMessage();
                        Bundle b = new Bundle();
                        b.putString("message", "not ok");
                        msgObj.setData(b);
						handlerDialog.sendMessage(msgObj);
                        Log.i("Animation", "Thread  exception " + t);
                    }
                }
               
                private Handler handlerDialog = new Handler() {
                	 
                    public void handleMessage(Message msg) {
                         
                        String aResponse = msg.getData().getString("message");

                        if (aResponse.equals("ok")) {
                        	String formName = strSelectedProfile.replace(" ", "_") + strFormTitle;
                        	String accounts[] = getGoogleAccounts();
            		        if (accounts.length == 0) {
            		        	customProgressDialog.dismiss();
            		        	AlertDialog.Builder alertbox = new AlertDialog.Builder(HTMLPageActivity.this);
            		    		  alertbox.setMessage("There are no accounts setup on this phone. Please create account in Settings");
            		    		  alertbox.setPositiveButton("OK", new DialogInterface.OnClickListener() 
            		    		  {
            		    			  @Override
            		    			  public void onClick(DialogInterface dialog, int which) 
            		    			  {		  	    				  
            		    				 			
            		    			  }
            		    		  });
            		    		  alertbox.show();
            		        }
            		        else{
            		        	customProgressDialog.dismiss();
            		        	Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            		        	sharingIntent.setType("vnd.android.cursor.dir/email");
            		        	SharedPreferences smacnaPrefs = getSharedPreferences("inputPreferences", Context.MODE_PRIVATE);
            		        	String[] to = {smacnaPrefs.getString("email", "").toString()};
            		        	sharingIntent.putExtra(Intent.EXTRA_EMAIL, to);
            		        	sharingIntent.putExtra(Intent.EXTRA_CC, "");
            		        	sharingIntent.putExtra(Intent.EXTRA_BCC, "");
            		        	sharingIntent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(new File(Environment.getExternalStorageDirectory()+"/PDF", formName.replace(" ", "_") + ".pdf")));
            		        	sharingIntent.putExtra(Intent.EXTRA_SUBJECT,formName);
            		        	startActivity(Intent.createChooser(sharingIntent, "Send fabrication report"));
            		        }                            
                        }
                        else
                        {
                        	AlertDialog.Builder alertbox = new AlertDialog.Builder(HTMLPageActivity.this);
      		    		  	alertbox.setMessage("There are no accounts setup on this phone. Please create account in Settings");
      		    		  	alertbox.setPositiveButton("OK", new DialogInterface.OnClickListener() 
      		    		  	{
      		    		  		@Override
      		    		  		public void onClick(DialogInterface dialog, int which) 
      		    		  		{		  	    				  
      		    		  			return;
      		    		  		}
      		    		  	});
      		    		  	alertbox.show();
                        }   

                    }
                };
			});
			background.start();
		}
		if(v.equals(btnBack)){
			
			File newfile = new File(Environment.getExternalStorageDirectory()+"/PDF/", "temp.html");
			newfile.delete();
			
			AlertDialog.Builder dialogAlert = new AlertDialog.Builder(v.getContext());
			dialogAlert.setTitle(getResources().getString(R.string.alert_title));
			dialogAlert.setMessage(getResources().getString(R.string.save_data_option));
			dialogAlert.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dailog, int arg1) {
					if(!strProfileID.equals("0")){
						webPage.loadUrl("javascript:getValue('"+strSelectedProfileID+"')");	
						finish();
					}
					else{
						AlertDialog.Builder dialogAlert = new AlertDialog.Builder(HTMLPageActivity.this);
						dialogAlert.setTitle(getResources().getString(R.string.alert_title));
						dialogAlert.setMessage(getResources().getString(R.string.unable_to_save));
						dialogAlert.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dailog, int arg1) {
								dailog.cancel();
							}
						});
						dialogAlert.show();
					}
					
				}
			});
			dialogAlert.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dailog, int arg1) {
					dailog.cancel();
					finish();
				}
			});
			dialogAlert.show();			
		}
		if(v.equals(btnChgProfile)){
			if(lngUserID != 0){
				getAllProfile = databaseObject.getProfileDetails(lngUserID);
				Log.i("PROFILELIST LENGTH", getAllProfile.size()+"");
				
				if(getAllProfile.size() != 0){
					// custom dialog
					final Dialog dialog = new Dialog(HTMLPageActivity.this);
					dialog.setContentView(R.layout.custom_view);
					dialog.setTitle(getResources().getString(R.string.custom_view_heading));
					
					// set the custom dialog components - radiogroup with radiobuttons
					final RadioGroup rdGroup = (RadioGroup) dialog.findViewById(R.id.selectItem);
					
					for(int i=0; i<getAllProfile.size(); i++){
						String name = getAllProfile.get(i).getProfileName();
						final RadioButton profileName = new RadioButton(this);
						profileName.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
						profileName.setText(name);
						profileName.setTextColor(getResources().getColor(R.color.White));
						profileName.setId(i);
						rdGroup.addView(profileName, i);
						profileName.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								int profileId = getAllProfile.get(profileName.getId()).getProfileID();
								myJavaScriptInterface.setinhtmlpage(String.valueOf(profileId));
								strSelectedProfile = getAllProfile.get(profileName.getId()).getProfileName();
								strSelectedProfileID = String.valueOf(profileId);
								dialog.dismiss();
								
							}
						});
					} 
					
//					rdGroup.setOnClickListener(new View.OnClickListener() {
//						
//						@Override
//						public void onClick(View v) {
//							if(rdGroup.getCheckedRadioButtonId()!=-1){
//							    int selectedRadioButtonId= rdGroup.getCheckedRadioButtonId();
//							    int profileId = getAllProfile.get(selectedRadioButtonId).getProfileID();
//								myJavaScriptInterface.setinhtmlpage(String.valueOf(profileId));
//								dialog.dismiss();
//							}
//						}
//					});
					
					
					Button removeProfile = (Button) dialog.findViewById(R.id.removeSelectedItem);
					removeProfile.setVisibility(View.GONE);
//					// if button is clicked, close the custom dialog
//					removeProfile.setText("Ok");
//					removeProfile.setOnClickListener(new OnClickListener() {
//						@Override
//						public void onClick(View view) {
//							
//							if(rdGroup.getCheckedRadioButtonId()!=-1){
//							    int selectedRadioButtonId= rdGroup.getCheckedRadioButtonId();
//							    int profileId = getAllProfile.get(selectedRadioButtonId).getProfileID();
//								myJavaScriptInterface.setinhtmlpage(String.valueOf(profileId));
//							}
//							dialog.dismiss();
//						}
//					});			
					dialog.show();	
				}else 
					Toast.makeText(getApplicationContext(), Constants.PROFILE_ERRORMESSAGE, Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(getApplicationContext(), Constants.SKIP_ERRORMESSAGE, Toast.LENGTH_SHORT).show();
			}
		}
	
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) { //Back key pressed
			File newfile = new File(Environment.getExternalStorageDirectory()+"/PDF/", "temp.html");
			newfile.delete();
			
			AlertDialog.Builder dialogAlert = new AlertDialog.Builder(HTMLPageActivity.this);
			dialogAlert.setTitle(getResources().getString(R.string.alert_title));
			dialogAlert.setMessage(getResources().getString(R.string.save_data_option));
			dialogAlert.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dailog, int arg1) {
					if(!strProfileID.equals("0")){
						webPage.loadUrl("javascript:getValue('"+strSelectedProfileID+"')");	
						finish();
					}
					else{
						AlertDialog.Builder dialogAlert = new AlertDialog.Builder(HTMLPageActivity.this);
						dialogAlert.setTitle(getResources().getString(R.string.alert_title));
						dialogAlert.setMessage(getResources().getString(R.string.unable_to_save));
						dialogAlert.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dailog, int arg1) {
								dailog.cancel();
							}
						});
						dialogAlert.show();
					}
					
				}
			});
			dialogAlert.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dailog, int arg1) {
					dailog.cancel();
					finish();
				}
			});
			dialogAlert.show();	
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		File newfile = new File(Environment.getExternalStorageDirectory()+"/HTML/", "temp.html");
		newfile.delete();
	}
	
	
	private String[] getGoogleAccounts() {
        ArrayList<String> accountNames = new ArrayList<String>();
        Account[] accounts = AccountManager.get(this).getAccounts();
        for (Account account : accounts) {
            if (account.type.equals("com.google")) {
                accountNames.add(account.name);
            }
        }

        String[] result = new String[accountNames.size()];
        accountNames.toArray(result);
        return result;
	}
}
