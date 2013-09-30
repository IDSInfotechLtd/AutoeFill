package govt.opendataapp.utils;

import java.io.Serializable;

public class Profile implements Serializable{

	//private variables
    int profileId, userId;
    String profileName;
     
    // Empty constructor
    public Profile(){
         
    }
    
    // getting profile ID
    public int getProfileID(){
        return this.profileId;
    }
     
    // setting id
    public void setProfileID(int profileId){
        this.profileId = profileId;
    }
    
    // getting user ID
    public int getUserID(){
        return this.profileId;
    }
     
    // setting user id
    public void setUserID(int userId){
        this.userId = userId;
    }
     
    // getting name
    public String getProfileName(){
        return this.profileName;
    }
     
    // setting name
    public void setProfileName(String profileName){
        this.profileName = profileName;
    }
}
