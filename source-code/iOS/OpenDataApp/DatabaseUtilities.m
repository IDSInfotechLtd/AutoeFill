//
//  DatabaseUtilities.m
//  OpenDataApp
//
//  Created by Taranjit on 17/09/13.
//  Copyright (c) 2013 Taranjit. All rights reserved.
//

#import "DatabaseUtilities.h"
#include <math.h>

static DatabaseUtilities* sharedManager=nil;

@implementation DatabaseUtilities
{
    sqlite3 *database;
    
}
+(DatabaseUtilities*)sharedManager{
    static dispatch_once_t pred;
    dispatch_once(&pred,
                  ^{
                      sharedManager=[[super allocWithZone:nil]init];
                  });
    return sharedManager;
}

+(id)allocWithZone:(NSZone *)zone
{
    return [self sharedManager];
}

/*
 Method: getDBPath
 param: NULl
 return type: NSString
 methos will setpath of sqlite file in directory in domain and return the same path
 */
-(NSString*)getDBPath
{
    NSArray *dirPaths =NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentDir= [dirPaths objectAtIndex:0];
    NSString* dbPath=[[NSString alloc]initWithString:[documentDir stringByAppendingPathComponent:@"OpenDataAppDB.sqlite"]];
    NSLog(@"Database Path %@",dbPath);
    return dbPath;
}
/*
 Method: copyDatabaseIfNeeded
 return type: BOOl
 methos will copy the database */

-(BOOL)copyDatabaseIfNeeded
{
    NSFileManager *fileManager=[NSFileManager defaultManager];
    NSError *error;
    NSString* dbPath=[self getDBPath];
    BOOL success=[fileManager fileExistsAtPath:dbPath];
    if (!success)
    {
        NSString *defaultDBpath=[[[NSBundle mainBundle]resourcePath]stringByAppendingPathComponent:@"OpenDataAppDB.sqlite"];
        success=[fileManager copyItemAtPath:defaultDBpath toPath:dbPath error:&error];
    }
    
    if (sqlite3_open([[self getDBPath] UTF8String], &database)==SQLITE_OK){
        NSLog(@"Connected");
        return YES;
    }
    else{
        NSLog(@"Not Connected");
        return NO;
    }    
}
/*
 Method: isUserExist
 param: NSString
 return type: BOOl
 methos will use at the time new user register and check that user already exist or not */

-(BOOL)isUserExist:(NSString*)userName
{
    NSString *insertSQL=[NSString stringWithFormat:@"select * from Login where username like ('%@');",userName];
        
        const char *fetch_stmt=[insertSQL UTF8String];
        sqlite3_stmt* statement;
        if (sqlite3_prepare_v2(database, fetch_stmt, -1, &statement, NULL)==SQLITE_OK)
        {
            if(sqlite3_step(statement)==SQLITE_ROW)
            {
                NSString* userId=[NSString stringWithUTF8String:(const char*)sqlite3_column_text(statement, 0)];
                NSLog(@"User Exists with user id = %@ -- isUserExist",userId);
                return YES;
            }
    }
    return NO;
}

-(BOOL)isProfileExist:(NSString*)userName
{
    NSString *fetchQuery=[NSString stringWithFormat:@"select * from User_Profile where Full_Name like ('%@');",userName];
    
    const char *fetch_stmt=[fetchQuery UTF8String];
    sqlite3_stmt* statement;
    if (sqlite3_prepare_v2(database, fetch_stmt, -1, &statement, NULL)==SQLITE_OK)
    {
        if(sqlite3_step(statement)==SQLITE_ROW)
            return YES;
    }
    return NO;
}

/*
 Method: registerUser
 param: NSString
 return type: BOOl
 methos will register the new user with userName and password */
-(BOOL)registerUser:(NSString*)userName password:(NSString*)password
{
    NSString *insertSQL=[NSString stringWithFormat:@"insert into Login(username,password,preferred_language,default_profile_id,default_profile_name) values ('%@','%@','ENGLISH','','')",userName,password];
    
    if (sqlite3_exec(database, (const char*)[insertSQL UTF8String], NULL, NULL, nil)==SQLITE_OK)
    {
        NSLog(@"User Register added");
    }
    else {
        NSLog(@"unable to Register");
        return NO;
    }
    return YES;
}
/*
 Method: verifyCredential
 param: NSString
 return type: BOOl
 methos will verify userName and Password if exists then login to application
 */

-(BOOL)verifyCredential:(NSString*)userName password:(NSString*)password
{
        NSString *fetchSQL=[NSString stringWithFormat:@"select * from Login where username='%@' and password='%@'", userName,password];
        const char *fetch_stmt=[fetchSQL UTF8String];
        sqlite3_stmt* statement;
        if (sqlite3_prepare_v2(database, fetch_stmt, -1, &statement, NULL)==SQLITE_OK)
        {
            if (sqlite3_step(statement)==SQLITE_ROW)
            {
                 NSString* userId = [NSString stringWithUTF8String:(const char*)sqlite3_column_text(statement, 0)];
                 NSString* userName = [NSString stringWithUTF8String:(const char*)sqlite3_column_text(statement, 1)];
                 NSString* userPreferredLanguage = [NSString stringWithUTF8String:(const char*)sqlite3_column_text(statement, 3)];
                 NSString* defaultProfileID = [NSString stringWithUTF8String:(const char*)sqlite3_column_text(statement, 4)];
                 NSString* defaultProfileName = [NSString stringWithUTF8String:(const char*)sqlite3_column_text(statement, 5)];
                 
                 [[NSUserDefaults standardUserDefaults] setObject:userId forKey:USER_ID];
                 [[NSUserDefaults standardUserDefaults] setObject:userName forKey:USER_NAME];
                 [[NSUserDefaults standardUserDefaults] setObject:userPreferredLanguage forKey:PREFERRED_LANGUAGE];
                 [[NSUserDefaults standardUserDefaults] setObject:defaultProfileID forKey:DEFAULT_PROFILE_ID];
                 [[NSUserDefaults standardUserDefaults] setObject:defaultProfileName forKey:DEFAULT_PROFILE_NAME];
                 [[NSUserDefaults standardUserDefaults] synchronize];
                 NSLog(@"user found -- verifyCredential ");
                 return YES;
            }
            else
            {
                NSLog(@"User not found -- verifyCredential");
            }
        }
    return NO;
}


/*
 Method: saveUserProfile
 param: NSDictionary*
 return type: BOOl
 methos will recieve a dictionary with all keys for user profile detail, and their values, and save them to database
*/
- (int)saveUserProfile:(NSDictionary*) profile
{
    
    NSString* query=[NSString stringWithFormat:@"insert into User_Profile("];
    for (NSString *key in profile)
        query=[NSString  stringWithFormat:@"%@%@,",query,key];
    
    query = [query substringToIndex:query.length-1];
    query = [NSString stringWithFormat:@"%@) values (",query];
    
    for (NSString *key in profile)
        query=[NSString  stringWithFormat:@"%@'%@',",query,[profile valueForKey:key]];
    
    query = [query substringToIndex:query.length-1];
    query = [NSString stringWithFormat:@"%@)",query];
    
    if (sqlite3_exec(database, (const char*)[query UTF8String], NULL, NULL,nil)==SQLITE_OK)
    {
       int primaryKey = sqlite3_last_insert_rowid(database);
        NSLog(@"primary Key = %d",primaryKey);
        return primaryKey;
    }
    else{
        NSLog(@"unable to insert");
        return NO;
    }
}

/*
 Method: alterTable
 param: NSDictionary
 return type: BOOl
 methos will check the column name exist or not, if not exist then alter the table */
-(BOOL)alterTable:(NSDictionary*)dictionary
{
    NSMutableArray* arrayForColumn = [self getAllColumnsName];
    for (NSString *key in dictionary)
    {
        if(![arrayForColumn containsObject:key])
        {
            NSString *updateSQL=[NSString stringWithFormat:@"ALTER TABLE User_Profile ADD COLUMN %@ VARCHAR",key];
            if (sqlite3_exec(database, (const char*)[updateSQL UTF8String], NULL, NULL, nil)==SQLITE_OK)
            {
                NSLog(@"Column added %@",key);
            }
        }
    }
return YES;
}

/*
 Method: getAllColumnsName
 param: NULL
 return type: void
 methods will get name of all columns which are already added in table
 */
-(NSMutableArray*)getAllColumnsName
{
    const char *sql="PRAGMA table_info(User_Profile)";
    sqlite3_stmt *stmt;
     NSMutableArray* arrColumnList=[[NSMutableArray alloc]init];
    
    if (sqlite3_prepare_v2(database, sql, -1, &stmt, NULL)!=SQLITE_OK)
    {
        NSLog(@"Fail");
    }
    while (sqlite3_step(stmt)==SQLITE_ROW)
    {
        NSString *fieldName=[NSString stringWithUTF8String:(char*)sqlite3_column_text(stmt, 1)];
        [arrColumnList addObject:fieldName];
        NSLog(@"fieldNames:%@",fieldName);
    }
    return arrColumnList;
}


-(NSMutableArray*)getUserProfileWithID:(NSString*)profileID
{
    int columnLength = [self getAllColumnsName].count;
    NSMutableArray* arrFieldData= [[NSMutableArray alloc]init];
    NSString *fetchSQL=[NSString stringWithFormat:@"select * from User_Profile where id='%@'", profileID];
    const char *fetch_stmt=[fetchSQL UTF8String];
    sqlite3_stmt* statement;
    if (sqlite3_prepare_v2(database, fetch_stmt, -1, &statement, NULL)==SQLITE_OK)
    {
        if (sqlite3_step(statement)==SQLITE_ROW)
        {
            
            NSString* fieldData;
            NSString* data;
            for(int i=0;i<columnLength;i++){
                data = [NSString stringWithFormat:@""];
                @try {
                    fieldData=[NSString stringWithUTF8String:(const char*)sqlite3_column_text(statement, i)];
                    if(fieldData.length > 0){
                        data = [NSString stringWithFormat:@"%@",fieldData];
                    }
                    else
                        data = [NSString stringWithFormat:@""];
                }
                @catch (NSException *exception) {
                    if(fieldData.length > 0){
                        data = [NSString stringWithFormat:@"%@",fieldData];
                    }
                    else
                        data = [NSString stringWithFormat:@""];
                }
                @finally {
                    [arrFieldData addObject:data];
                }
            }
        }
        else{
            NSLog(@"User Profile not found");
        }
    }
    return arrFieldData;
}

-(BOOL)updateUserProfile:(NSDictionary*)userProfile profileId:(NSString*)profileId
{
    NSString* query=[NSString stringWithFormat:@"update User_Profile set"];
    for (NSString *key in userProfile)
        query=[NSString  stringWithFormat:@"%@ %@='%@',",query,key,[userProfile valueForKey:key]];
    
    query = [query substringToIndex:query.length-1];
    
    query=[NSString  stringWithFormat:@"%@ where id = %@",query,profileId];
    
    NSLog(@"Final Querry  = %@", query);
    
    if (sqlite3_exec(database, (const char*)[query UTF8String], NULL, NULL,nil)==SQLITE_OK)
    {
        NSLog(@"Inserted");
        return YES;
    }
    else{
        NSLog(@"unable to insert");
        return NO;
    }
}

-(NSMutableArray*)getAllProfileIdForUserId:(NSString*)userId{
    NSMutableArray* arrUserId_Name = [[NSMutableArray alloc] init];
    NSMutableArray* arrID= [[NSMutableArray alloc]init];
    NSMutableArray* arrUsername = [[NSMutableArray alloc]init];
    
    NSString *fetchSQL=[NSString stringWithFormat:@"select id,Full_Name from User_Profile where user_id='%@'", userId];
    const char *fetch_stmt=[fetchSQL UTF8String];
    sqlite3_stmt* statement;
    if (sqlite3_prepare_v2(database, fetch_stmt, -1, &statement, NULL)==SQLITE_OK)
    {
        while(sqlite3_step(statement)==SQLITE_ROW)
        {
            NSString* profileId=[NSString stringWithUTF8String:(const char*)sqlite3_column_text(statement, 0)];
            NSString* userName=[NSString stringWithUTF8String:(const char*)sqlite3_column_text(statement, 1)];
                    
            [arrID addObject:profileId];
            [arrUsername addObject:userName];
        }
    }
    [arrUserId_Name addObject:arrID];
    [arrUserId_Name addObject:arrUsername];
       return arrUserId_Name;
}

-(void)deleteUserProfile:(NSString*)profileId{
     NSString *query = [NSString stringWithFormat:@"Delete from User_Profile where id=%d",[profileId intValue]];
     sqlite3_exec(database, [query UTF8String], NULL, NULL, NULL);
}

-(void)deleteMyAccount:(NSString*)userId{
     NSString *query = [NSString stringWithFormat:@"Delete from Login where id=%d",[userId intValue]];
     sqlite3_exec(database, [query UTF8String], NULL, NULL, NULL);
     NSString *query1 = [NSString stringWithFormat:@"Delete from User_Profile where user_id=%d",[userId intValue]];
     sqlite3_exec(database,[query1 UTF8String],NULL,NULL,NULL);
}

-(void)changePassword:(NSString*)pswd
{
     NSString *query = [NSString stringWithFormat:@"Update Login SET password='%@' where id=%@",pswd,[[NSUserDefaults standardUserDefaults]objectForKey:USER_ID]];
     sqlite3_exec(database, [query UTF8String], NULL, NULL, NULL);
}
-(void)changeProfileAndLanguage:(NSString*)profileName language:(NSString*)profileLanguage :(NSString*)profileId
{
     NSString *query = [NSString stringWithFormat:@"Update Login SET  default_profile_id='%@', default_profile_name='%@',preferred_language='%@' where id=%@",profileId,profileName,profileLanguage,[[NSUserDefaults standardUserDefaults]objectForKey:USER_ID]];
    if(sqlite3_exec(database, [query UTF8String], NULL, NULL, NULL)==SQLITE_OK){
        NSLog(@"saved successfully");
    }
    else
        NSLog(@"Not saved");
}



#pragma mark - SAVE ALL LIST (COUNTRY, STATE, DISTRICT AND CATEGORY)


-(void)SaveAllStateListInDB:(NSMutableArray*)StateList{
    [self removeAllValuesFromTable:@"State"];
    NSArray* arrTempID=[StateList objectAtIndex:0];
    NSArray* arrTempName=[StateList objectAtIndex:1];
    NSArray* arrTempCID=[StateList objectAtIndex:2];
    
    NSString *query = [NSString stringWithFormat:@"insert into State (StateId, StateName, CountryId) values "];
    for(int i=0;i < arrTempID.count;i++){
        query =[NSString stringWithFormat:@"%@('%@','%@','%@'),",query, [arrTempID objectAtIndex:i],[arrTempName objectAtIndex:i],[arrTempCID objectAtIndex:i]];
    }
    query = [query substringToIndex:query.length-1];
    if(sqlite3_exec(database, [query UTF8String], NULL, NULL, NULL)==SQLITE_OK){
        NSLog(@"saved successfully");
    }
    else
        NSLog(@"Not saved");
    
}

-(void)SaveAllDistrictListInDB:(NSMutableArray*)DistrictList{
   [self removeAllValuesFromTable:@"District"];
    NSArray* arrTempID=[DistrictList objectAtIndex:0];
    NSArray* arrTempName=[DistrictList objectAtIndex:1];
    NSArray* arrTempSID=[DistrictList objectAtIndex:2];

    int batchSize= 15;
    int length = ceil(arrTempID.count/batchSize)+1;
    NSString *mainQuery = [NSString stringWithFormat:@"insert into District (DistrictId, DistrictName, StateId) values "];
    NSString* tempQuery;
    int counter=0;
    
    for(int x = 0; x < length; x++){
        tempQuery = mainQuery;
        for(int y=0;y < batchSize; y++){
            NSLog(@"counter = %d",counter);
            if(counter >=arrTempID.count){
                tempQuery = [tempQuery substringToIndex:tempQuery.length-1];
                if(sqlite3_exec(database, [tempQuery UTF8String], NULL, NULL, NULL)==SQLITE_OK){
                    NSLog(@"saved successfully");
                }
                else
                    NSLog(@"Not saved");
                return;
            }
            tempQuery =[NSString stringWithFormat:@"%@('%@','%@','%@'),",tempQuery, [arrTempID objectAtIndex:counter],[arrTempName objectAtIndex:counter],[arrTempSID objectAtIndex:counter]];
            counter++;
        }
        tempQuery = [tempQuery substringToIndex:tempQuery.length-1];
        if(sqlite3_exec(database, [tempQuery UTF8String], NULL, NULL, NULL)==SQLITE_OK){
            NSLog(@"saved successfully");
        }
        else
            NSLog(@"Not saved");
        
    }
        
}

-(void)SaveAllCategoryListInDB:(NSMutableArray*)CategoryList{
    [self removeAllValuesFromTable:@"Category"];
    NSArray* arrTempID=[CategoryList objectAtIndex:0];
    NSArray* arrTempName=[CategoryList objectAtIndex:1];
    
    NSString *query = [NSString stringWithFormat:@"insert into Category (CategoryId, CategoryName) values "];
    
    for(int i=0;i < arrTempID.count;i++){
        query =[NSString stringWithFormat:@"%@('%@','%@'),",query, [arrTempID objectAtIndex:i],[arrTempName objectAtIndex:i]];
    }
    query = [query substringToIndex:query.length-1];
    if(sqlite3_exec(database, [query UTF8String], NULL, NULL, NULL)==SQLITE_OK){
        NSLog(@"saved successfully");
    }
    else
    NSLog(@"Not saved");
}


#pragma mark - GET ALL LIST (COUNTRY, STATE, DISTRICT AND CATEGORY)

-(NSMutableArray*)getAllStateListFromDB:(NSString*)countryID
{
    NSMutableArray* arrState_Id_Name = [[NSMutableArray alloc] init];
    NSMutableArray* arrStateIDs= [[NSMutableArray alloc]init];
    NSMutableArray* arrStateNames = [[NSMutableArray alloc]init];
    NSString *query = [NSString stringWithFormat:@"select * from State where CountryId like ('%@');",countryID];
    const char *fetch_stmt=[query UTF8String];
    sqlite3_stmt* statement;
    if (sqlite3_prepare_v2(database, fetch_stmt, -1, &statement, NULL)==SQLITE_OK)
    {
        while(sqlite3_step(statement)==SQLITE_ROW)
        {
            NSString* stateId=[NSString stringWithUTF8String:(const char*)sqlite3_column_text(statement, 0)];
            NSString* stateName=[NSString stringWithUTF8String:(const char*)sqlite3_column_text(statement, 1)];
            
            [arrStateIDs addObject:stateId];
            [arrStateNames addObject:stateName];
        }
    }
    [arrState_Id_Name addObject:arrStateIDs];
    [arrState_Id_Name addObject:arrStateNames];
    return arrState_Id_Name;
}

-(NSMutableArray*)getAllDistrictListFromDB:(NSString*)stateID
{
    NSMutableArray* arrDistrict_Id_Name = [[NSMutableArray alloc] init];
    NSMutableArray* arrDistrictIDs= [[NSMutableArray alloc]init];
    NSMutableArray* arrDistrictNames = [[NSMutableArray alloc]init];
    NSMutableArray* arrStateId = [[NSMutableArray alloc]init];
    
    NSString *query = [NSString stringWithFormat:@"select * from District where StateId like ('%@');",stateID];
    const char *fetch_stmt=[query UTF8String];
    sqlite3_stmt* statement;
    if (sqlite3_prepare_v2(database, fetch_stmt, -1, &statement, NULL)==SQLITE_OK)
    {
        while(sqlite3_step(statement)==SQLITE_ROW)
        {
            NSString* districtId=[NSString stringWithUTF8String:(const char*)sqlite3_column_text(statement, 0)];
            NSString* districtName=[NSString stringWithUTF8String:(const char*)sqlite3_column_text(statement, 1)];
            NSString* stateId=[NSString stringWithUTF8String:(const char*)sqlite3_column_text(statement, 2)];
            
            [arrDistrictIDs addObject:districtId];
            [arrDistrictNames addObject:districtName];
            [arrStateId addObject:stateId];
        }
    }
    [arrDistrict_Id_Name addObject:arrDistrictIDs];
    [arrDistrict_Id_Name addObject:arrDistrictNames];
    [arrDistrict_Id_Name addObject:arrStateId];
    
    return arrDistrict_Id_Name;
}

-(NSMutableArray*)getAllCategoryListFromDB
{
    NSMutableArray* arrCategory_Id_Name = [[NSMutableArray alloc] init];
    NSMutableArray* arrCategoryIDs= [[NSMutableArray alloc]init];
    NSMutableArray* arrCategoryNames = [[NSMutableArray alloc]init];
    
    NSString *query = [NSString stringWithFormat:@"select * from Category"];
    const char *fetch_stmt=[query UTF8String];
    sqlite3_stmt* statement;
    if (sqlite3_prepare_v2(database, fetch_stmt, -1, &statement, NULL)==SQLITE_OK)
    {
        while(sqlite3_step(statement)==SQLITE_ROW)
        {
            NSString* categoryId=[NSString stringWithUTF8String:(const char*)sqlite3_column_text(statement, 0)];
            NSString* categoryName=[NSString stringWithUTF8String:(const char*)sqlite3_column_text(statement, 1)];
            
            [arrCategoryIDs addObject:categoryId];
            [arrCategoryNames addObject:categoryName];
        }
    }
    [arrCategory_Id_Name addObject:arrCategoryIDs];
    [arrCategory_Id_Name addObject:arrCategoryNames];
    return arrCategory_Id_Name;
}

-(void)removeAllValuesFromTable:(NSString*)tableName{
     NSString *query = [NSString stringWithFormat:@"delete from %@",tableName];
    if(sqlite3_exec(database, [query UTF8String], NULL, NULL, NULL)==SQLITE_OK){
        NSLog(@"Removed Successfully");
    }
    else
        NSLog(@"Not Removed");
}
@end

