package govt.opendataapp.services;

import govt.opendataapp.database.ApplicationDatabase;
import govt.opendataapp.database.DatabaseContractClass;
import govt.opendataapp.parsing.WebParsing;
import govt.opendataapp.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class UpdateTables extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
    public void onCreate()
    {   
        // Start up the thread running the service. Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.
        Thread notifyingThread = new Thread(null, mTask, "DownloadingService");
        notifyingThread.start();
    }
	
	private Runnable mTask = new Runnable()
    {
        public void run()
        {
        	WebParsing jsonParser = new WebParsing();
    		ApplicationDatabase dbDataBaseObject = new ApplicationDatabase(getApplicationContext());
    		dbDataBaseObject.openDataBase();
    		Log.e("Services", "running");
    		ArrayList<HashMap<String, String>> countriesList = jsonParser.getCountry(Constants.METHOD_COUNTRY);
    	    ArrayList<HashMap<String, String>> statesList = jsonParser.getStates(Constants.METHOD_STATE, null);
    	    ArrayList<HashMap<String, String>> districtList = jsonParser.getDistricts(Constants.METHOD_DISTRICTS, null);
    	    ArrayList<HashMap<String, String>> categoriesList = jsonParser.getCategories(Constants.METHOD_CATEGORY);
    	    if(countriesList.size() > 0){
        		dbDataBaseObject.isDeletedCountries();
    	    }
    	    if(statesList.size() > 0){
        		dbDataBaseObject.isDeletedStates();
    	    }
    	    if(districtList.size() > 0){
        		dbDataBaseObject.isDeletedDistricts();
    	    }
    	    if(categoriesList.size() > 0){
        		dbDataBaseObject.isDeletedCategories();
    	    }
    	    for(int index = 0;index < countriesList.size();index++){
    	    	ContentValues values = new ContentValues();
    	    	values.put(DatabaseContractClass.Countries.ID, Integer.valueOf(countriesList.get(index).get(Constants.COUNTRY_ID)));
    	    	values.put(DatabaseContractClass.Countries.NAME, countriesList.get(index).get(Constants.COUNTRY_NAME));
    	    	dbDataBaseObject.insertDataInTable(DatabaseContractClass.Countries.TABLE_NAME, values);
    	    }
    	    for(int index = 0;index < statesList.size();index++){
    	    	ContentValues values = new ContentValues();
    	    	values.put(DatabaseContractClass.States.ID, Integer.valueOf(statesList.get(index).get(Constants.STATE_ID)));
    	    	values.put(DatabaseContractClass.States.NAME, statesList.get(index).get(Constants.STATE_NAME));
    	    	values.put(DatabaseContractClass.States.COUNTRY_ID, statesList.get(index).get(Constants.COUNTRY_ID));
    	    	dbDataBaseObject.insertDataInTable(DatabaseContractClass.States.TABLE_NAME, values);
    	    }
    	    for(int index = 0;index < districtList.size();index++){
    	    	ContentValues values = new ContentValues();
    	    	values.put(DatabaseContractClass.District.ID, Integer.valueOf(districtList.get(index).get(Constants.DISTRICT_ID)));
    	    	values.put(DatabaseContractClass.District.NAME, districtList.get(index).get(Constants.DISTRICT_NAME));
    	    	values.put(DatabaseContractClass.District.STATE_ID, districtList.get(index).get(Constants.STATE_ID));
    	    	dbDataBaseObject.insertDataInTable(DatabaseContractClass.District.TABLE_NAME, values);
    	    }
    	    for(int index = 0;index < categoriesList.size();index++){
    	    	ContentValues values = new ContentValues();
    	    	values.put(DatabaseContractClass.Categories.ID, Integer.valueOf(categoriesList.get(index).get(Constants.CATEGORY_ID)));
    	    	values.put(DatabaseContractClass.Categories.NAME, categoriesList.get(index).get(Constants.CATEGORY_NAME));
    	    	dbDataBaseObject.insertDataInTable(DatabaseContractClass.Categories.TABLE_NAME, values);
    	    }
        }
    };
    
    
	
	

}
