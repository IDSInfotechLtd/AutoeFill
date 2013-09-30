package govt.opendataapp;

import govt.opendataapp.adapter.ViewFormListAdapter;
import govt.opendataapp.utils.Constants;
import govt.opendataapp.utils.FormList;

import java.io.File;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class DownloadedFormListActivity extends ListActivity {

	// declare class variables
	private ArrayList<FormList> formsList = new ArrayList<FormList>();
	private ViewFormListAdapter adpPDFList;
	private ArrayList<String> fileName;
	private ArrayList<Long> fileCreatedTime;
	int i = 0;
	Character ch;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.forms_list);
           
        Button btnBack = (Button)findViewById(R.id.backbtn);        
        TextView listTitle = (TextView) findViewById(R.id.listTitle);

        listTitle.setText(R.string.form_list_screen_heading);
        listTitle.setText(getResources().getString(R.string.form_list_screen_heading));
        listTitle.setTextColor(getResources().getColor(R.color.White));
        formsList = getDownloadedFormsList();
        // instantiate our ViewFormListAdapter class
        if(formsList!=null && formsList.size() > 0){
        	adpPDFList = new ViewFormListAdapter(this, R.layout.list_item, formsList);
        	setListAdapter(adpPDFList);   
        }
        else{
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(getResources().getString(R.string.no_forms));
			builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					return;
				}					
			});
			AlertDialog alert = builder.create();
			alert.show();
        }
        btnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
    }
    
    private ArrayList<FormList> getDownloadedFormsList() {
    	ArrayList<FormList> getFormsList = new ArrayList<FormList>(); 
       	File dir = new File(Environment.getExternalStorageDirectory().getPath()+Constants.HTML_PATH);
    	File[] files = dir.listFiles();
    	

    	while (files!= null && i < files.length){
    		FormList currentForm = new FormList();
    	  	String name = files[i].getName();
    	  	ch = name.charAt(0);
    	    if (ch == Character.toUpperCase(ch)) {
    	    	if(name.contains(".")){
					String substr = name.substring(0, name.indexOf("."));
					currentForm.setFormName(substr);
				}
				else{
					currentForm.setFormName(name);
				}
//    	    	currentForm.setFormName(name);
	        	currentForm.setFormDownloadTime(files[i].lastModified());
    	    }else{
    	       	ch = Character.toUpperCase(ch);  
    	        name = name.replace(name.charAt(0),ch);
    	        if(name.contains(".")){
					String substr = name.substring(0, name.indexOf("."));
					currentForm.setFormName(substr);
				}
				else{
					currentForm.setFormName(name);
				}
//    	    	currentForm.setFormName(name);
    	        currentForm.setFormDownloadTime(files[i].lastModified());
    	    }
    	    i++;
	        getFormsList.add(currentForm);
    	}  
		return getFormsList;	
	}		
}
