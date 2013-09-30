package govt.opendataapp.parsing;

import govt.opendataapp.utils.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class WebParsing {
	
	public WebParsing(){
		
	}
	
	/**
	 * Method to get countries list
	 * @param methodName <Method name to get countries list>
	 * @return ArrayList<HashMap<String, String>> it includes the country id and countryName
	 */
	public ArrayList<HashMap<String, String>> getCountry(String methodName){
		ArrayList<HashMap<String, String>> countryList = new ArrayList<HashMap<String,String>>();
		String jsonString = "";
		try{
			String URL = Constants.SERVER + Constants.APPNAME + Constants.REPOSITORY + methodName;
			jsonString = getJSONFromURL(URL);
	        JSONObject parentObject = new JSONObject(jsonString);
			JSONArray countryArray = parentObject.getJSONArray(Constants.JSON_DATA_OBJECT);
			for(int index = 0;index<countryArray.length();index++){
				HashMap<String, String> countryDetails = new HashMap<String, String>();
				JSONObject indexObject = countryArray.getJSONObject(index);
				JSONObject countryObject = indexObject.getJSONObject(Constants.JSON_COUNTRY_OBJECT);
				countryDetails.put(Constants.COUNTRY_ID, countryObject.getString(Constants.COUNTRY_ID));
				countryDetails.put(Constants.COUNTRY_NAME, countryObject.getString(Constants.COUNTRY_NAME));
				countryList.add(countryDetails);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return countryList;
	}
	
	/**
	 * Method to get states list
	 * @param methodName <Method name to get countries list>
	 * @return ArrayList<HashMap<String, String>> it includes the country id and countryName
	 */
	public ArrayList<HashMap<String, String>> getStates(String methodName, HashMap<String, String> parameters){
		ArrayList<HashMap<String, String>> stateList = new ArrayList<HashMap<String,String>>();
		String jsonString = "";
		String URL = "";
		try{
			if(parameters==null)
				URL = Constants.SERVER + Constants.APPNAME + Constants.REPOSITORY + methodName;
			else
				URL = Constants.SERVER + Constants.APPNAME + Constants.REPOSITORY + methodName + "?" + Constants.COUNTRY_ID + "=" + parameters.get(Constants.COUNTRY_ID);
			Log.e("URL", "" + URL);
			jsonString = getJSONFromURL(URL);
	        JSONObject parentObject = new JSONObject(jsonString);
			JSONArray stateArray = parentObject.getJSONArray(Constants.JSON_DATA_OBJECT);
			for(int index = 0;index<stateArray.length();index++){
				HashMap<String, String> stateDetails = new HashMap<String, String>();
				JSONObject indexObject = stateArray.getJSONObject(index);
				JSONObject stateObject = indexObject.getJSONObject(Constants.JSON_STATE_OBJECT);
				stateDetails.put(Constants.STATE_ID, stateObject.getString(Constants.STATE_ID));
				stateDetails.put(Constants.STATE_NAME, stateObject.getString(Constants.STATE_NAME));
				stateDetails.put(Constants.COUNTRY_ID, stateObject.getString(Constants.COUNTRY_ID));
				stateList.add(stateDetails);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return stateList;
	}
	
	/**
	 * Method to get districts list
	 * @param methodName <Method name to get districts list>
	 * @return ArrayList<HashMap<String, String>> it includes the district id and district name
	 */
	public ArrayList<HashMap<String, String>> getDistricts(String methodName, HashMap<String, String> parameters){
		ArrayList<HashMap<String, String>> districtsList = new ArrayList<HashMap<String,String>>();
		String jsonString = "";
		String URL = "";
		try{
			if(parameters==null)
				URL = Constants.SERVER + Constants.APPNAME + Constants.REPOSITORY + methodName;
			else
				URL = Constants.SERVER + Constants.APPNAME + Constants.REPOSITORY + methodName + "?" + Constants.STATE_ID + "=" + parameters.get(Constants.STATE_ID);
			Log.e("URL", "" + URL);
			jsonString = getJSONFromURL(URL);
	        JSONObject parentObject = new JSONObject(jsonString);
			JSONArray dataArray = parentObject.getJSONArray(Constants.JSON_DATA_OBJECT);
			for(int index = 0;index<dataArray.length();index++){
				HashMap<String, String> districtsDetails = new HashMap<String, String>();
				JSONObject indexObject = dataArray.getJSONObject(index);
				JSONObject countryObject = indexObject.getJSONObject(Constants.JSON_DISTRICT_OBJECT);
				districtsDetails.put(Constants.DISTRICT_ID, countryObject.getString(Constants.DISTRICT_ID));
				districtsDetails.put(Constants.DISTRICT_NAME, countryObject.getString(Constants.DISTRICT_NAME));
				districtsDetails.put(Constants.STATE_ID, countryObject.getString(Constants.STATE_ID));
				districtsList.add(districtsDetails);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return districtsList;
	}
	
	/**
	 * Method to get categories list
	 * @param methodName <Method name to get category list>
	 * @return ArrayList<HashMap<String, String>> it includes the category id and category name
	 */
	public ArrayList<HashMap<String, String>> getCategories(String methodName){
		ArrayList<HashMap<String, String>> categoryList = new ArrayList<HashMap<String,String>>();
		String jsonString = "";
		String URL = "";
		try{
			URL = Constants.SERVER + Constants.APPNAME + Constants.REPOSITORY + methodName;
			Log.e("URL", "" + URL);
			jsonString = getJSONFromURL(URL);
	        JSONObject parentObject = new JSONObject(jsonString);
			JSONArray dataArray = parentObject.getJSONArray(Constants.JSON_DATA_OBJECT);
			for(int index = 0;index<dataArray.length();index++){
				HashMap<String, String> categoriesDetails = new HashMap<String, String>();
				JSONObject indexObject = dataArray.getJSONObject(index);
				JSONObject countryObject = indexObject.getJSONObject(Constants.JSON_CATEGORY_OBJECT);
				categoriesDetails.put(Constants.CATEGORY_ID, countryObject.getString(Constants.CATEGORY_ID));
				categoriesDetails.put(Constants.CATEGORY_NAME, countryObject.getString(Constants.CATEGORY_NAME));
				categoryList.add(categoriesDetails);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return categoryList;
	}
	
	/**
	 * Method to get search list
	 * @param methodName <Method name to get search list>
	 * @return ArrayList<HashMap<String, String>> it includes the form id, form name, form location
	 */
	public ArrayList<HashMap<String, String>> getSearchFormList(String methodName, HashMap<String, String> parameters){
		ArrayList<HashMap<String, String>> searchList = new ArrayList<HashMap<String,String>>();
		String jsonString = "";
		String URL = "";
		String urlParameters = "?";
		try{
			URL = Constants.SERVER + Constants.APPNAME + Constants.REPOSITORY + methodName;
			if(parameters != null){
				urlParameters += Constants.URL_PARAMETERS_CATEGORY_ID + "=" + parameters.get(Constants.URL_PARAMETERS_CATEGORY_ID) + "&";
				urlParameters += Constants.URL_PARAMETERS_KEYWORD + "=" + parameters.get(Constants.URL_PARAMETERS_KEYWORD) + "&";
				urlParameters += Constants.URL_PARAMETERS_LEVEL + "=" + parameters.get(Constants.URL_PARAMETERS_LEVEL) + "&";
				urlParameters += Constants.COUNTRY_ID + "=" + parameters.get(Constants.COUNTRY_ID) + "&";
				urlParameters += Constants.STATE_ID + "=" + parameters.get(Constants.STATE_ID) + "&";
				urlParameters += Constants.DISTRICT_ID + "=" + parameters.get(Constants.DISTRICT_ID);
				URL +=urlParameters;
			}
			
			Log.e("URL", "" + URL);
			jsonString = getJSONFromURL(URL);
	        JSONObject parentObject = new JSONObject(jsonString);
	        
	        if(parentObject.getString(Constants.JSON_ERRORMESSAGE).equals(Constants.ERROR_MESSAGE)){
	        	HashMap<String, String> formsDetails = new HashMap<String, String>();
	        	formsDetails.put(Constants.JSON_ERRORMESSAGE, parentObject.getString(Constants.JSON_ERRORMESSAGE));
	        	searchList.add(formsDetails);
	        }
	        else{
				JSONArray dataArray = parentObject.getJSONArray(Constants.JSON_DATA_OBJECT);
				for(int index = 0;index<dataArray.length();index++){
					HashMap<String, String> formsDetails = new HashMap<String, String>();
					JSONObject indexObject = dataArray.getJSONObject(index);
					JSONObject gformsObject = indexObject.getJSONObject(Constants.JSON_GFORMS_OBJECT);
					formsDetails.put(Constants.JSON_ERRORMESSAGE, parentObject.getString(Constants.JSON_ERRORMESSAGE));
		        	formsDetails.put(Constants.FORM_ID, gformsObject.getString(Constants.FORM_ID));
					formsDetails.put(Constants.FORM_NAME, gformsObject.getString(Constants.FORM_NAME));
					formsDetails.put(Constants.FORM_LOCATION, gformsObject.getString(Constants.FORM_LOCATION));
					searchList.add(formsDetails);
				}
	        }
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return searchList;
	}
	
	
	public String getJSONFromURL(String url){
		String response = "";
		try{
			DefaultHttpClient client = new DefaultHttpClient();
	        HttpGet httpGet = new HttpGet(url);
	        HttpResponse execute = client.execute(httpGet);
	        InputStream content = execute.getEntity().getContent();
	        BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
	        String s = "";
	        while ((s = buffer.readLine()) != null) {
	        	response += s;
	        }
	        return response;
		}
		catch(IOException inputException){
			inputException.printStackTrace();
			return inputException.toString();
		}
	}

}
