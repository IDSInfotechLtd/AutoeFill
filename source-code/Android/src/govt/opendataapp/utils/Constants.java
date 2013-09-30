package govt.opendataapp.utils;

import govt.opendataapp.database.DatabaseContractClass;

public class Constants {
	public static final String COUNTRY_ID="CountryId";
	public static final String COUNTRY_NAME="CountryName";

	public static final String STATE_ID="StateId";
	public static final String STATE_NAME="StateName";

	public static final String DISTRICT_ID="DistrictId";
	public static final String DISTRICT_NAME="DistrictName";
	
	public static final String CATEGORY_ID="CategoryId";
	public static final String CATEGORY_NAME="CategoryName";
	
	public static final String FORM_ID="FormId";
	public static final String FORM_NAME="FormName";
	public static final String FORM_LOCATION="FormLocation";
	
	public static final String SERVER="http://st3.idsil.com/";
	public static final String APPNAME="open_data_app/";
	public static final String REPOSITORY="listings/";
	public static final String JSON_DATA_OBJECT = "data";
	public static final String JSON_GFORMS_OBJECT = "tbl_gforms";
	public static final String JSON_ERRORMESSAGE = "message";
	public static final String ERROR_MESSAGE = "No matches found";
	
	public static final String JSON_COUNTRY_OBJECT = "Country";
	public static final String JSON_STATE_OBJECT = "State";
	public static final String JSON_DISTRICT_OBJECT = "District";
	public static final String JSON_CATEGORY_OBJECT = "Category";
	
	public static final String METHOD_COUNTRY="getcountrylist";
	public static final String METHOD_STATE="getstates";
	public static final String METHOD_DISTRICTS="getdistricts";
	public static final String METHOD_CATEGORY="getcategorylist";
	public static final String METHOD_SELECTED_STATES="getstatelist";
	public static final String METHOD_SELECTED_DISTRICTS="getdistrictlist";
	public static final String METHOD_SELECTED_FORMS="searchform";
	
	public static final String URL_PARAMETERS_CATEGORY_ID="cat";
	public static final String URL_PARAMETERS_KEYWORD="keyword";
	public static final String URL_PARAMETERS_LEVEL="level";
	public static final String DATABASE_NAME = "opendataapp.sqlite";
	
	
	public static final String CHECK_USER_NAME = "Please check your user name.";
	public static final String USER_NAME_EXIST = "User name already exists.";
	public static final String NO_ERROR = "no-error";
	
	public static final String LOGGED_IN_USER_PREFERENCES_NAME = "UserSession";
	public static final String PREF_USER_ID = "user_id";
	public static final String PREF_PROFILE_ID = "profile_id";
	public static final String PREF_PROFILE_NAME = "profile_name";
	public static final String HTMLPAGE_PREFERENCES_NAME = "HtmlPageIds";
	public static final String PREF_URL_OR_DOWNLOADED = "urlordownloaded";
	public static final String PREF_DOWNLOADED = "downloaded";
	public static final String PREF_URL = "url";
	
	public static final String DELETE_ACCOUNT_TITLE = "Confirm Account Deletion";
	public static final String DELETE_ACCOUNT_ERRORMESSAGE = "Are you sure you want to delete your account?";
	
	public static final String TEMP_HTML_FILE = "temp";
	public static final String EDIT_NEW = "edit_new";
	public static final String NEW = "new";
	public static final String EDIT = "edit";
	public static final long SEARCH_FORM = 0;
	
	
	
	public static final String NATIONAL_LEVEL = "1";
	public static final String DISTRICT_LEVEL = "0";
	
	// Variable used to save the Database name
	public static final String DB_NAME = "OpenData.sqlite";

	public static final String SCRIPT_GET_HTML = "javascript:window.HtmlViewer.showHTML"+"(\"<head>\"+document.getElementsByTagName(\"html\")[0].innerHTML+\"</head>\");";
	public static final String JAVASCRIPTGETVALUE= "javascript: function GetVal(){var idKey = new Array();var idValue = new Array();var input = document.getElementsByTagName(\"input\");var select = document.getElementsByTagName(\"select\");for(i = 0; i < select.length; i++){idKey.push(select[i].id);idValue.push(select[i].options.item(select[i].selectedIndex).text);}for (i = 0; i < input.length; i++){if (input[i].type == \"text\" || input[i].type == \"password\"){idKey.push(input[i].id);idValue.push(input[i].value);}if (input[i].type == \"checkbox\"){if(document.getElementById(input[i].id).checked){idKey.push(input[i].id);idValue.push(input[i].value);}}if (input[i].type == \"radio\"){if(document.getElementById(input[i].id).checked){idKey.push(input[i].id);idValue.push(input[i].value);}}}autofill.print(idKey,idValue);}";
	public static final String JAVASCRIPTSETVALUE="javascript: function SetVal(toast,toast1) {var x;x = document.getElementById(toast).type;if(x==\"text\"){document.getElementById(toast).value=toast1;}else if(x==\"radio\"){document.getElementById(toast).checked= true;}else if(x==\"checkbox\"){document.getElementById(toast).checked= true;}else  if(x==\"select-one\"){document.getElementById(toast).options[0].text=toast1;document.getElementById(toast).selected=\"selected\";}}";
	public static final String JAVASCRIPTTEMPVALUE= "javascript: function getValue(profileID){var val= \"abc\"; var idKey = new Array(); var idValue = new Array(); var input = document.getElementsByTagName(\"input\"); var select = document.getElementsByTagName(\"select\"); for(i = 0; i < select.length; i++){ idKey.push(select[i].id); idValue.push(select[i].options.item(select[i].selectedIndex).text); } for(i = 0;i < input.length; i++){ if (input[i].type == \'text\' || input[i].type == \'password\'){	idKey.push(input[i].id);idValue.push(input[i].value);} if (input[i].type == \'radio\'){ if(document.getElementById(input[i].id).checked){ idKey.push(input[i].id); idValue.push(input[i].value);} } try{ if (input[i].type == \"checkbox\"){ if(document.getElementById(input[i].id)!=null && document.getElementById(input[i].id).checked){ idKey.push(input[i].id);idValue.push(input[i].value);} } } catch(err){ val = \" \" + err.message; } } autofill.print(idKey,idValue,profileID);}";
	// Variable used to save the Database name
	
	
	
	
	
	
	// Variable used to set integer type to table column
	private static final String INTEGER_TYPE = " INTEGER";

	//	Variable used to set text type to table column
	private static final String TEXT_TYPE = " TEXT";
	private static final String BOOLEAN_TYPE = " BOOLEAN";
	
	// Variable used to set format of file
	public static final String FILE_FORMAT = "application/pdf";

	// Variable used to set format of date
	public static final String DATE_FORMAT = "dd-MM-yyyy";

	// Variable used to set path of pdf files
	public static final String PDF_PATH = "/PDF/";

	public static final String PROFILENOID = "0";
	public static final String PROFILENOIDFROMSHAREDPREFRENCES = "";
	public static final String NULLCHECK = "null";
	public static final String SKIP_KEY = "skip";
	public static final String SKIP_ERRORMESSAGE = "No user is logged in";
	public static final String PROFILE_ERRORMESSAGE = "No profile found";
	public static final String FORMS_ERRORMESSAGE = "No forms found";
	public static final String PREF_USER_PASSWORD = "user_password";
	//	Variable used to set comma
	private static final String COMMA_SEP = ",";
//	public static final String CREATE_PROFILE_TABLE="Create table " + DatabaseContractClass.UserInformation.TABLE_NAME + " ("
//			+ DatabaseContractClass.UserInformation.PROFILE_ID + INTEGER_TYPE+ " PRIMARY KEY AUTOINCREMENT,"
//			+ DatabaseContractClass.User.USERID + INTEGER_TYPE+COMMA_SEP+ DatabaseContractClass.UserInformation.PROFILE_NAME + TEXT_TYPE +  " )";
	
//	public static final String CREATE_USER_TABLE="Create table " + DatabaseContractClass.User.TABLE_NAME + " ("
//			+ DatabaseContractClass.User.USERID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT,"
//			+ DatabaseContractClass.User.USERNAME + TEXT_TYPE + COMMA_SEP + DatabaseContractClass.User.PASSWORD+TEXT_TYPE+  " )";
//	
	public static final String CREATE_COUNTRY_TABLE = "Create table " + DatabaseContractClass.Countries.TABLE_NAME + " ("
			+ DatabaseContractClass.Countries.ID + INTEGER_TYPE + " PRIMARY KEY,"
			+ DatabaseContractClass.Countries.NAME + TEXT_TYPE +  " )";
	
	public static final String CREATE_STATE_TABLE = "Create table " + DatabaseContractClass.States.TABLE_NAME + " ("
			+ DatabaseContractClass.States.ID + INTEGER_TYPE + " PRIMARY KEY,"
			+ DatabaseContractClass.States.NAME + TEXT_TYPE + COMMA_SEP  
			+ DatabaseContractClass.States.COUNTRY_ID + TEXT_TYPE + " )";
	
	public static final String CREATE_DISTRICT_TABLE = "Create table " + DatabaseContractClass.District.TABLE_NAME + " ("
			+ DatabaseContractClass.District.ID + INTEGER_TYPE + " PRIMARY KEY,"
			+ DatabaseContractClass.District.NAME + TEXT_TYPE + COMMA_SEP  
			+ DatabaseContractClass.District.STATE_ID + TEXT_TYPE +  " )";
	
	public static final String CREATE_CATEGORY_TABLE = "Create table " + DatabaseContractClass.Categories.TABLE_NAME + " ("
			+ DatabaseContractClass.Categories.ID + INTEGER_TYPE + " PRIMARY KEY,"
			+ DatabaseContractClass.Categories.NAME + TEXT_TYPE +  " )";
	// Variable used to set path of html files
	public static final String HTML_PATH = "/HTML/";

	public static final String TAG = "KEY";
		
	public static final String HTML_KEY = "html";
		
	public static final String PDF_KEY = "pdf";

	public static final String CREATE_PROFILE_TABLE= "Create table " + DatabaseContractClass.UserInformation.TABLE_NAME + " ("
			+ DatabaseContractClass.UserInformation.PROFILE_ID + INTEGER_TYPE+ " PRIMARY KEY AUTOINCREMENT,"+DatabaseContractClass.UserInformation.USER_ID+INTEGER_TYPE+","+DatabaseContractClass.UserInformation.PROFILE_NAME+TEXT_TYPE+","+DatabaseContractClass.UserInformation.FIRSTNAME+TEXT_TYPE+","+DatabaseContractClass.UserInformation.LASTNAME+TEXT_TYPE+","+DatabaseContractClass.UserInformation.MIDDLENAME+TEXT_TYPE+","+DatabaseContractClass.UserInformation.ADDRESS+TEXT_TYPE+","+DatabaseContractClass.UserInformation.CITY+TEXT_TYPE+","+DatabaseContractClass.UserInformation.COUNTRY+TEXT_TYPE+","+DatabaseContractClass.UserInformation.STATE+TEXT_TYPE+","+DatabaseContractClass.UserInformation.ZIPCODE+TEXT_TYPE+","+DatabaseContractClass.UserInformation.EMAIL_ADDRESS+TEXT_TYPE+","+DatabaseContractClass.UserInformation.NATIONALITY+TEXT_TYPE+" )";
	public static final String CREATE_USER_TABLE = "Create table " + DatabaseContractClass.User.TABLE_NAME + " ("
			+ DatabaseContractClass.User.USERID + INTEGER_TYPE+ " PRIMARY KEY AUTOINCREMENT,"+DatabaseContractClass.User.PROFILEID+INTEGER_TYPE+","+DatabaseContractClass.User.PROFILE_NAME+TEXT_TYPE+","+DatabaseContractClass.User.USERNAME+TEXT_TYPE+","+DatabaseContractClass.User.PASSWORD+TEXT_TYPE+" )";
	
	
	
	


}
