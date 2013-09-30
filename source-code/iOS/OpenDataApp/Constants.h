//
//  Constants.h
//  LoginClass
//
//  Created by Preetinder Kaur on 11/09/13.
//  Copyright (c) 2013 Preetinder Kaur. All rights reserved.
//

#ifndef LoginClass_Constants_h
#define LoginClass_Constants_h

#define LOGIN_SCREEN_TITLE @"Login Screen"
#define HOME_SCREEN_TITLE @"Home Screen"
#define LOGIN_ERROR_MESSAGE @"Not Registered Yet, Please Register"
#define CANCEL_BUTTON_TITLE @"OK"
#define LOGIN_DEFAULTS_USERNAME_KEY @"userName"
#define LOGIN_DEFAULTS_PASSWORD_KEY @"password"
#define LOGOUT_BAR_BUTTON_TITLE @"Log Out"
#define REMEMBER_ME @"RememberMe"

/*
 User Profile
 */
#define EDIT_PROFILE @"edit_profile"
#define CREATE_PROFILE @"create_profile"
 
/*
 TAGS
 */
#define EMAIL_ID @"Email_Address"
#define MOBILE @"Mobile_No"
#define NAME @"Full_Name"
#define ID @"id"
#define USER_ID @"user_id"
#define GUEST_USER @"GUEST"

//#define MY_ARRAY [NSMutableArray arrayWithObjects:@"Title", @"First_Name", @"Last_Name", EMAIL_ID, PHONE, FAX, @"Position", @"Company", @"Street_1", @"Street_2", @"City", @"State", @"Country", @"Postal_Code", nil]
#define ALERT_VALIDATION_TITLE @"Invalid"
#define ALERT_MSG_INVALID_PHONE_FAX @"Please Enter Valid 10 Digit Number"
#define ALERT_MSG_INVALID_EMAIL @"Please Enter Valid Email-id"
#define CREATE_PROFILE_TITLE @"Create Profile"
#define EDIT_PROFILE_TITLE @"Edit Profile"
#define WIDTH_FOR_LABEL 140
#define WIDTH_FOR_LABEL_AND_TEXTFIELD 130
#define HEIGHT_FOR_LABEL_AND_TEXTFIELD 30
#define DISTANCE_BETWEEN_LABEL_AND_TEXTFIELD 20
#define XORIGIN_FOR_LABEL 10
#define XORIGIN_FOR_TEXTFIELD 180
#define SPACE_FOR_SCROLL_CONTENT 160
#define KEYBOARD_HEIGHT 216
#define SPACE_FOR_LABEL_TEXTFIELD 40

/*
 Registration Screen
 */
#define ALERT_USER_EXIST @"User Already Exist"
#define ALERT_ENTER_USER_NAME @"Please Enter User Name"
#define ALERT_TITLE @""
#define ALERT_REGISTRATION_SUCCESS @"Registration Successfull";
#define ALERT_REGISTRATION_FAIL @"Registration Failed";
#define ALERT_PASSWORD_TITLE @"Please Re-Enter"
#define ALERT_PASWWORD_MATCH_MESSAGE @"Passwords Do Not Match"

/*
 Web Services
 */
#define GET_ALL_COUNTRIES @"http://st3.idsil.com/open_data_app/listings/getcountrylist"
#define GET_ALL_STATES @"http://st3.idsil.com/open_data_app/listings/getstatelist?CountryId=1"
#define GET_ALL_DISTRICT @"http://st3.idsil.com/open_data_app/listings/getdistrictlist/?StateId="
#define GET_ALL_CATEGORIES @"http://st3.idsil.com/open_data_app/listings/getcategorylist"
#define GET_SEARCHED_FORMS @"http://st3.idsil.com/open_data_app/listings/searchform?cat="
#define GET_SEARCHED_FORMS_NEW @"http://st3.idsil.com/open_data_app/listings/searchform?cat=%@&keyword=%@&level=%@&CountryId=%@&StateId=%@&DistrictId=%@"


/*
GET ALL INDIA
 */
#define GET_ALL_INDIA_STATES @"http://st3.idsil.com/open_data_app/listings/getstates"
#define GET_ALL_INDIA_DISTRICTS @"http://st3.idsil.com/open_data_app/listings/getdistricts"
#define GET_ALL_INDIA_CATEGORIES @"http://st3.idsil.com/open_data_app/listings/getcategorylist"


/*
 HTML And PDF
 */
#define HTML_EXT @"html"
#define PDF_EXT @"pdf"
#define TITLE_DOWNLOADED_FORMS @"Downloaded Forms"
#define TITLE_SAVED_PDF @"PDF"

/*
 Search Screen
 */
#define SEARCH_FORM_TITLE @"Search Form"
#define ALERT_DISTRICT_SELECTION @"Please Select State First"
#define ALERT_CATEGORY_SELECTION @"Please Select Category"

/*
 Queries
*/
#define VALIDATE_USERNAME_PASSWORD @"select * from Login where username='%@' and password='%@';"

/*
 Settings Screen
 */
#define PREFERRED_LANGUAGE @"preferred_language"
#define DEFAULT_PROFILE_ID @"default_profile_id"
#define DEFAULT_PROFILE_NAME @"default_profile_name"
#define USER_NAME @"user_name"
#define ARRAY_DEFAULT_LANGUAGE [NSMutableArray arrayWithObjects:@"ENGLISH", @"HINDI", nil]
#define FORMS_TO_BE_DELETED @"FormsToBeDeleted"

#endif
