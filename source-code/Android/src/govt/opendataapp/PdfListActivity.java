package govt.opendataapp;

import govt.opendataapp.adapter.ViewFormListAdapter;
import govt.opendataapp.utils.Constants;
import govt.opendataapp.utils.FormList;

import java.io.File;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class PdfListActivity extends ListActivity implements View.OnClickListener{

	// declare class variables
		private ArrayList<FormList> formsList = new ArrayList<FormList>();
		private ViewFormListAdapter adpPDFList;
		private ArrayList<String> fileName;
		private ArrayList<Long> fileCreatedTime;
		private TextView listTitle;
		private int i = 0;
		private Character ch;
		private AlertDialog.Builder dialogAlert;
		private Button btnBack;
		
		/** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.forms_list);
	        
	        btnBack = (Button)findViewById(R.id.backbtn);
	        btnBack.setOnClickListener(this);
	        listTitle = (TextView) findViewById(R.id.listTitle);

	        listTitle.setText(getResources().getString(R.string.form_list_screen_heading));
	        dialogAlert =new AlertDialog.Builder(PdfListActivity.this);

	        listTitle.setText(getResources().getString(R.string.form_list_screen_heading));
	        listTitle.setTextColor(getResources().getColor(R.color.White));
	        
	        formsList = getDownloadedFormsList();
	        // instantiate our ViewFormListAdapter class
	        if(formsList.size() > 0){
		        adpPDFList = new ViewFormListAdapter(this, R.layout.list_item, formsList);
		        setListAdapter(adpPDFList);
	        }
	        else{
	        	dialogAlert.setTitle(getResources().getString(R.string.login_screen_heading));
				dialogAlert.setMessage(getResources().getString(R.string.no_forms));
				dialogAlert.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dailog, int arg1) {
							// TODO Auto-generated method stub
							dailog.cancel();
						}
					});
				dialogAlert.show();
	        }
	    }
	    
	    private ArrayList<FormList> getDownloadedFormsList() {
	    	ArrayList<FormList> getFormsList = new ArrayList<FormList>(); 
	       	File dir = new File(Environment.getExternalStorageDirectory().getPath() +Constants.PDF_PATH);
	       	Log.e("PDF",""+dir.getAbsolutePath());
	    	File[] files = dir.listFiles();
	    	if(files!=null){
		    	while (i < files.length){
		    		FormList currentForm = new FormList();
		    	  	String name = files[i].getName();
		    	  	ch = name.charAt(0);
		    	    if (ch == Character.toUpperCase(ch)) {
		    	    	currentForm.setFormName(name);
			        	currentForm.setFormDownloadTime(files[i].lastModified());
		    	    }else{
		    	       	ch = Character.toUpperCase(ch);  
		    	        name = name.replace(name.charAt(0),ch);
		    	        currentForm.setFormName(name);
		    	        currentForm.setFormDownloadTime(files[i].lastModified());
		    	    }
		    	    i++;
			        getFormsList.add(currentForm);
		    	}  
	    	}
			return getFormsList;	
		}

		@Override
		public void onClick(View view) {
			finish();			
		}		
}
