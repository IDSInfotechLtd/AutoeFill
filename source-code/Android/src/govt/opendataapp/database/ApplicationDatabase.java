package govt.opendataapp.database;

import govt.opendataapp.HTMLPageActivity.MyJavaScriptInterface;
import govt.opendataapp.utils.Category;
import govt.opendataapp.utils.Constants;
import govt.opendataapp.utils.Country;
import govt.opendataapp.utils.District;
import govt.opendataapp.utils.Profile;
import govt.opendataapp.utils.State;
import govt.opendataapp.utils.UserProfile;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ApplicationDatabase extends SQLiteOpenHelper {

	public ApplicationDatabase(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	private SQLiteDatabase openDataDatabase;
	private String newFieldToadd;
	private Context mContext;
	public static int dataBaseVersion =1;

	/**
	 * 
	 * Constructor takes and keeps a reference of the passed context in order to access to the application assets and
	 * resources.
	 * 
	 * @param context
	 * 
	 */

	public ApplicationDatabase(Context context) {
		super(context, Constants.DATABASE_NAME, null,1);
		mContext=context;

	}

	public void openDataBase() throws SQLException {
		this.openDataDatabase = this.getWritableDatabase();

	}

	@Override
	public synchronized void close() {
		try {
			if (openDataDatabase != null)
				openDataDatabase.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.close();
	}


	@Override
	public void onCreate(SQLiteDatabase sqlitedatabase) {
		Log.e("database creartion start", "");
		sqlitedatabase.execSQL(Constants.CREATE_PROFILE_TABLE);
		sqlitedatabase.execSQL(Constants.CREATE_USER_TABLE);
		sqlitedatabase.execSQL(Constants.CREATE_COUNTRY_TABLE);
		sqlitedatabase.execSQL(Constants.CREATE_STATE_TABLE);
		sqlitedatabase.execSQL(Constants.CREATE_DISTRICT_TABLE);
		sqlitedatabase.execSQL(Constants.CREATE_CATEGORY_TABLE);
		SharedPreferences pref=	mContext.getSharedPreferences(Constants.HTMLPAGE_PREFERENCES_NAME, 0);
		Editor edit = pref.edit();
		edit.putString(DatabaseContractClass.UserInformation.FIRSTNAME, null);
		edit.putString(DatabaseContractClass.UserInformation.LASTNAME, null);
		edit.putString(DatabaseContractClass.UserInformation.MIDDLENAME, null);
		edit.putString(DatabaseContractClass.UserInformation.ADDRESS, null);
		edit.putString(DatabaseContractClass.UserInformation.CITY, null);
		edit.putString(DatabaseContractClass.UserInformation.STATE, null);
		edit.putString(DatabaseContractClass.UserInformation.ZIPCODE, null);
		edit.putString(DatabaseContractClass.UserInformation.EMAIL_ADDRESS, null);
		edit.putString(DatabaseContractClass.UserInformation.NATIONALITY, null);
		edit.putString(DatabaseContractClass.UserInformation.NATIONALITY, null);
		edit.commit();
	
		Log.e("database crearted", "");
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		if(oldVersion >=newVersion){

		}
	}
	public long insertDataInTable(String pTableName, ContentValues pContentValues) {
		long isInserted = 0;
		openDataBase();
		isInserted  = openDataDatabase.insertOrThrow(pTableName, null, pContentValues);


		return isInserted;
	}
	public void setDataBaseVersionAndColumn(int dataBaseVersion,String newFieldToadd){
		try{
			openDataDatabase.execSQL("ALTER TABLE "+DatabaseContractClass.UserInformation.TABLE_NAME+" ADD "+newFieldToadd+" TEXT(128)");}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	public boolean checkusername(String username){
		Cursor findEntry1 = openDataDatabase.query(DatabaseContractClass.User.TABLE_NAME,null, DatabaseContractClass.User.USERNAME+ "='"+username+"'" ,null, null, null, null);
		findEntry1.getCount();
		if(findEntry1.getCount()>=1){

			return false;
		}
		else{
			return true;
		}
	}

	public boolean verifyCrediantials(String username,String password){
		String[] columns = new String[]{DatabaseContractClass.User.USERID, DatabaseContractClass.User.PROFILEID, DatabaseContractClass.User.PROFILE_NAME};
		Cursor userData = openDataDatabase.query(DatabaseContractClass.User.TABLE_NAME,columns, DatabaseContractClass.User.USERNAME+ "='"+username+"' AND "+DatabaseContractClass.User.PASSWORD+"='"+password+"'" ,null, null, null, null);
		userData.getCount();
		if(userData.getCount()>=1){
			userData.moveToFirst();
			long UserId = 	userData.getInt(userData.getColumnIndex(DatabaseContractClass.User.USERID));
			int ProfileId =	userData.getInt(userData.getColumnIndex(DatabaseContractClass.User.PROFILEID));
			String ProfileName	= userData.getString(userData.getColumnIndex(DatabaseContractClass.User.PROFILE_NAME));
			SharedPreferences prefs = mContext.getSharedPreferences(Constants.LOGGED_IN_USER_PREFERENCES_NAME, Context.MODE_PRIVATE);
			Editor edit = prefs.edit();
			edit.putLong(Constants.PREF_USER_ID, UserId);
			edit.putString(Constants.PREF_PROFILE_ID, String.valueOf(ProfileId));
			edit.putString(Constants.PREF_PROFILE_NAME, ProfileName);
			edit.putString(Constants.PREF_USER_PASSWORD, password);
			edit.commit();
			return true;
		}
		else{

			return false;
		}
	}

	public ArrayList<Country> getCountryList(){
		ArrayList<Country> countryList = new ArrayList<Country>();
		String[] columns = new String[]{DatabaseContractClass.Countries.ID,DatabaseContractClass.Countries.NAME}; 
		String orderBy =DatabaseContractClass.Countries.NAME+" ASC";
		Cursor getCountry = openDataDatabase.query(DatabaseContractClass.Countries.TABLE_NAME, columns, null ,null, null, null, orderBy);
		getCountry.moveToFirst();
		if(getCountry.getCount() > 0){
			Country country = new Country();
			country.setCountryID(0);
			country.setCountryName("Select Country");
			countryList.add(country);
			do{
				country = new Country();
				country.setCountryID(getCountry.getInt(getCountry.getColumnIndex(DatabaseContractClass.Countries.ID)));
				country.setCountryName(getCountry.getString(getCountry.getColumnIndex(DatabaseContractClass.Countries.NAME)));
				countryList.add(country);
			}while (getCountry.moveToNext());
		}
		return countryList;
	}

	public ArrayList<State> getStatesList(String countryID) {
		ArrayList<State> stateList = new ArrayList<State>();
		String[] columns = new String[]{DatabaseContractClass.States.ID,DatabaseContractClass.States.NAME}; 
		String whereString = DatabaseContractClass.States.COUNTRY_ID + "=" + countryID;
		String orderBy =DatabaseContractClass.States.NAME+" ASC";
		Cursor getState = openDataDatabase.query(DatabaseContractClass.States.TABLE_NAME, columns, whereString ,null, null, null, orderBy);
		getState.moveToFirst();
		if(getState.getCount() > 0){
			State state = new State();
			state.setStateID(0);
			state.setStateName("Select State");
			stateList.add(state);
			do{
				state = new State();
				state.setStateID(getState.getInt(getState.getColumnIndex(DatabaseContractClass.States.ID)));
				state.setStateName(getState.getString(getState.getColumnIndex(DatabaseContractClass.States.NAME)));
				stateList.add(state);
			}while (getState.moveToNext());
		}
		return stateList;
	}

	public ArrayList<District> getDistrictList(String stateID) {
		ArrayList<District> districtList = new ArrayList<District>();
		String[] columns = new String[]{DatabaseContractClass.District.ID,DatabaseContractClass.District.NAME}; 
		String whereString = DatabaseContractClass.District.STATE_ID + "=" + stateID;
       String orderBy =DatabaseContractClass.District.NAME+" ASC";
		Cursor getDistrict = openDataDatabase.query(DatabaseContractClass.District.TABLE_NAME, columns, whereString ,null, null, null, orderBy);
		getDistrict.moveToFirst();
		if(getDistrict.getCount() > 0){
			District district = new District();
			district.setDistrictID(0);
			district.setDistrictName("Select District");
			districtList.add(district);
			do{
				district = new District();
				district.setDistrictID(getDistrict.getInt(getDistrict.getColumnIndex(DatabaseContractClass.District.ID)));
				district.setDistrictName(getDistrict.getString(getDistrict.getColumnIndex(DatabaseContractClass.District.NAME)));
				districtList.add(district);
			}while (getDistrict.moveToNext());
		}
		return districtList;
	}

	public ArrayList<Category> getCategoryList() {
		ArrayList<Category> categoryList = new ArrayList<Category>();
		String[] columns = new String[]{DatabaseContractClass.Categories.ID,DatabaseContractClass.Categories.NAME}; 

		Cursor getCategory = openDataDatabase.query(DatabaseContractClass.Categories.TABLE_NAME, columns, null ,null, null, null, null);
		getCategory.moveToFirst();
		if(getCategory.getCount() > 0){
			do{
				Category category = new Category();
				category.setCategoryID(getCategory.getInt(getCategory.getColumnIndex(DatabaseContractClass.Categories.ID)));
				category.setCategoryName(getCategory.getString(getCategory.getColumnIndex(DatabaseContractClass.Categories.NAME)));
				categoryList.add(category);
			}while (getCategory.moveToNext());
		}
		return categoryList;
	}


	// Fetch all information of user from UserInformation table
	public ArrayList<UserProfile> getAllUserInfo(){
		ArrayList<UserProfile> infoList = new ArrayList<UserProfile>();
		String selectQuery = "SELECT  * FROM " + DatabaseContractClass.UserInformation.TABLE_NAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				UserProfile info = new UserProfile();
				info.setID(Integer.parseInt(cursor.getString(0)));
				info.setFirstName(cursor.getString(1));
				info.setMName(cursor.getString(2));
				info.setLastName(cursor.getString(3));
				infoList.add(info);
			} while (cursor.moveToNext());
		}

		return infoList;
	}

	public boolean deleteUser(long userID){			
		return openDataDatabase.delete(DatabaseContractClass.User.TABLE_NAME, DatabaseContractClass.User.USERID + "=" + userID, null)>0;
	}



	public String[] getTotalColumns(){
		Cursor dbCursor = openDataDatabase.query(DatabaseContractClass.UserInformation.TABLE_NAME, null, null, null, null, null, null);
		String[] columnNames = dbCursor.getColumnNames();
		return columnNames;
	}

	public String[] getProfileData(){

		Cursor dbcursor= openDataDatabase.query(DatabaseContractClass.UserInformation.TABLE_NAME, null, null, null, null, null, null);
		String[] columnNames= 	dbcursor.getColumnNames();
		String columnName = null;
		ArrayList<String> updatedColumnNames = new ArrayList<String>();
		for(int Index=0;Index<=columnNames.length-1;Index++){
			if(columnNames[Index].equalsIgnoreCase("userid")||columnNames[Index].equalsIgnoreCase("ProfileId")){

			}
			else{
				columnName=columnNames[Index];
				Log.e("columname profile", ""+columnName);
				updatedColumnNames.add(columnName);
			}

		}
		String[] ColumnNamesArray= new String[updatedColumnNames.size()];
		ColumnNamesArray = updatedColumnNames.toArray(ColumnNamesArray);
		SharedPreferences prefs = mContext.getSharedPreferences(Constants.LOGGED_IN_USER_PREFERENCES_NAME, Context.MODE_PRIVATE);
		String profileid = prefs.getString(Constants.PREF_PROFILE_ID, null);
		String[] profileValues = new String[ColumnNamesArray.length];
		Cursor data= openDataDatabase.query(DatabaseContractClass.UserInformation.TABLE_NAME,ColumnNamesArray, DatabaseContractClass.UserInformation.PROFILE_ID+"="+profileid, null, null, null, null);
		int counter =0;
		data.moveToFirst();
		if(data!=null && data.getCount() > 0){
			do{
				for(int index=0;index<=profileValues.length-1;index++)
					profileValues[index]= data.getString(index);
					counter++;
			}
			while(data.moveToNext());
		}
		return profileValues;
	
		 }
public void setProfileId(long userid,ContentValues values){
	 openDataDatabase.update(DatabaseContractClass.User.TABLE_NAME, values, DatabaseContractClass.User.USERID+"="+userid,null);
	 
}
public void updateprofile(ContentValues values,String profileid){
	try{
		int row = openDataDatabase.update(DatabaseContractClass.UserInformation.TABLE_NAME, values, DatabaseContractClass.UserInformation
			 .PROFILE_ID + "=" + profileid , null);
		Log.e("", ""+row);
	}
	catch(Exception ex){
		Log.e("update profile error", ex.toString());
	}
	 
}


public boolean hasProfile(String profileName, String UserId){
	Cursor getProfile = openDataDatabase.query(DatabaseContractClass.UserInformation.TABLE_NAME, null, DatabaseContractClass.UserInformation.PROFILE_NAME + "= '" + profileName + "' AND " + DatabaseContractClass.UserInformation.USER_ID + "="+UserId , null, null, null, null);
	if (getProfile.getCount() > 0)
		return true;
	else
		return false;
		
}
	public void  selectValues(String pTableName,MyJavaScriptInterface m1, String profileID)  {
		openDataBase();
		Cursor findEntry = openDataDatabase.query(pTableName,null, null, null, null, null, null);
		String[] columnnames=	findEntry.getColumnNames();
		for(int i=0;i<=columnnames.length-1;i++){
			Log.e("column names", ""+columnnames[i]);
		}
//		SharedPreferences pref = mContext.getSharedPreferences(Constants.LOGGED_IN_USER_PREFERENCES_NAME, 0); // 0 - for private mode
//		String profileid	= pref.getString(Constants.PREF_PROFILE_ID, null);
		Cursor findEntry1 = openDataDatabase.query(pTableName,columnnames, DatabaseContractClass.UserInformation.PROFILE_ID+"="+profileID,null, null, null, null);
		Log.e("NO of rows",""+ findEntry1.getCount()) ;
		try{

			String values[] = new String[columnnames.length];
			findEntry1.moveToFirst();

			values[0] = String.valueOf(findEntry1.getInt(0));

			Log.e(columnnames[0], values[0]);
			for(int g=1;g<=columnnames.length-1;g++){
				values[g]=String.valueOf(findEntry1.getString(g)); 
				Log.e(columnnames[g], values[g]);
			}



			m1.setfromdatabase(columnnames, values);
		} 
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
	
	public ArrayList<Profile> getProfileDetails(long userID){
		ArrayList<Profile> profileList = new ArrayList<Profile>();
		String userId = String.valueOf(userID);
		String[] columns = new String[]{DatabaseContractClass.UserInformation.PROFILE_ID,DatabaseContractClass.UserInformation.PROFILE_NAME}; 

		Cursor getProfiles = openDataDatabase.query(DatabaseContractClass.UserInformation.TABLE_NAME, columns, DatabaseContractClass.UserInformation.USER_ID + "=" + userId ,null, null, null, null);
		getProfiles.moveToFirst();
		if(getProfiles.getCount()!=0){
			do{
				Profile profile = new Profile();
				profile.setProfileID(getProfiles.getInt(getProfiles.getColumnIndex(DatabaseContractClass.UserInformation.PROFILE_ID)));
				profile.setProfileName(getProfiles.getString(getProfiles.getColumnIndex(DatabaseContractClass.UserInformation.PROFILE_NAME)));
				profileList.add(profile);
			}
			while (getProfiles.moveToNext());
		}
		
		return profileList;
	}
	
	public boolean deleteProfileRecord(int profileId) {
		return openDataDatabase.delete(DatabaseContractClass.UserInformation.TABLE_NAME, DatabaseContractClass.UserInformation.PROFILE_ID + "=" + profileId, null)>0;			
	}
	
	public boolean isDeletedCountries(){
		return openDataDatabase.delete(DatabaseContractClass.Countries.TABLE_NAME, "1", null) > 0;
	}
	public boolean isDeletedStates(){
		return openDataDatabase.delete(DatabaseContractClass.States.TABLE_NAME, "1", null) > 0;
	}
	public boolean isDeletedDistricts(){
		return openDataDatabase.delete(DatabaseContractClass.District.TABLE_NAME, "1", null) > 0;
	}
	public boolean isDeletedCategories(){
		return openDataDatabase.delete(DatabaseContractClass.Categories.TABLE_NAME, "1", null) > 0;
	}
	public void updatePassword(String password){
		SharedPreferences prefs = mContext.getSharedPreferences(Constants.LOGGED_IN_USER_PREFERENCES_NAME, Context.MODE_PRIVATE);
		long Userid = prefs.getLong(Constants.PREF_USER_ID, 0);
		ContentValues cv = new ContentValues();
		cv.put(DatabaseContractClass.User.PASSWORD, password);
		int row = openDataDatabase.update(DatabaseContractClass.User.TABLE_NAME, cv, DatabaseContractClass.User.USERID+ "=" + Userid , null);
		 
	}
	
}	
