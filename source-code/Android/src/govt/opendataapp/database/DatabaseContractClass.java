package govt.opendataapp.database;

import android.provider.BaseColumns;

public class DatabaseContractClass {

	private DatabaseContractClass() {
		// Default constructor
	}

	public class UserInformation implements BaseColumns {
		public static final String TABLE_NAME = "Profile";
		public static final String PROFILE_ID = "ProfileId";
		public static final String PROFILE_NAME = "profilename";
		public static final String USER_ID = "UserId";
		public static final String FIRSTNAME = "First_Name";
		public static final String MIDDLENAME = "Middle_Name";
		public static final String LASTNAME = "Surname";
		public static final String ADDRESS = "address";
		public static final String COUNTRY = "country";
		public static final String STATE = "state";
		public static final String CITY = "city";
		public static final String EMAIL_ADDRESS = "Email_Address";
		public static final String NATIONALITY = "nationality";
		public static final String ZIPCODE = "zipcode";
	}

	public class User implements BaseColumns {
		public static final String TABLE_NAME = "User";
		public static final String USERID = "UserId";
		public static final String PASSWORD = "password";
		public static final String USERNAME = "username";
		public static final String PROFILEID = "ProfileId";
		public static final String PROFILE_NAME = "profilename";
		
	}
	
	public class Countries implements BaseColumns {
		public static final String TABLE_NAME = "Countries";
		public static final String ID = "id";
		public static final String NAME = "country_name";
	}
	
	public class States implements BaseColumns {
		public static final String TABLE_NAME = "States";
		public static final String ID = "id";
		public static final String NAME = "state_name";
		public static final String COUNTRY_ID = "country_id";
	}
	
	public class District implements BaseColumns {
		public static final String TABLE_NAME = "District";
		public static final String ID = "id";
		public static final String NAME = "district_name";
		public static final String STATE_ID = "state_id";
	}
	
	public class Categories implements BaseColumns {
		public static final String TABLE_NAME = "Categories";
		public static final String ID = "id";
		public static final String NAME = "category_name";
	}
}
