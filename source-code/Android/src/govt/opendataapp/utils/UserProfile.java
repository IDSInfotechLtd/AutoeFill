package govt.opendataapp.utils;

public class UserProfile {
    
    //private variables
    int _id, _size;
    String _fname, _lname, _mname;
    String _control;
     
    // Empty constructor
    public UserProfile(){
         
    }
    // constructor
    public UserProfile(int id, String fname, String mname,String lname, String control, int size){
        this._id = id;
        this._fname = fname;
        this._lname = lname;
        this._mname = mname;

    }

    // getting ID
    public int getID(){
        return this._id;
    }
     
    // setting id
    public void setID(int id){
        this._id = id;
    }
     
    // getting name
    public String getFirstName(){
        return this._fname;
    }
     
    // setting name
    public void setFirstName(String fname){
        this._fname = fname;
    }
    
 // getting name
    public String getMName(){
        return this._mname;
    }
     
    // setting name
    public void setMName(String mname){
        this._mname = mname;
    }
    
 // getting name
    public String getLastName(){
        return this._lname;
    }
     
    // setting name
    public void setLastName(String lname){
        this._lname = lname;
    }
     
    

}
