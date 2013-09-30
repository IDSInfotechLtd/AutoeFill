//
//  DatabaseUtilities.h
//  OpenDataApp
//
//  Created by Taranjit on 17/09/13.
//  Copyright (c) 2013 Taranjit. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <sqlite3.h>

@interface DatabaseUtilities : NSObject

+(id)sharedManager;

-(BOOL)registerUser:(NSString*)userName password:(NSString*)password;
-(BOOL)verifyCredential:(NSString*)userName password:(NSString*)password;

-(BOOL)isProfileExist:(NSString*)userName;
-(BOOL)isUserExist:(NSString*)userName;
-(BOOL)copyDatabaseIfNeeded;

- (int)saveUserProfile:(NSDictionary*) profile;

-(BOOL)alterTable:(NSDictionary*)dictionary;
-(BOOL)updateUserProfile:(NSDictionary*)userProfile profileId:(NSString*)profileId;

-(NSMutableArray*)getAllColumnsName;

-(NSMutableArray*)getUserProfileWithID:(NSString*)profileID;
-(NSMutableArray*)getAllProfileIdForUserId:(NSString*)userId;
-(void)deleteUserProfile:(NSString*)userId;
-(void)deleteMyAccount:(NSString*)userId;
-(void)changePassword:(NSString*)pswd;
-(void)changeProfileAndLanguage:(NSString*)profileName language:(NSString*)profileLanguage :(NSString*)profileId;


-(void)SaveAllStateListInDB:(NSMutableArray*)StateList;
-(void)SaveAllDistrictListInDB:(NSMutableArray*)DistrictList;
-(void)SaveAllCategoryListInDB:(NSMutableArray*)CategoryList;


-(NSMutableArray*)getAllStateListFromDB:(NSString*)countryID;
-(NSMutableArray*)getAllDistrictListFromDB:(NSString*)stateID;
-(NSMutableArray*)getAllCategoryListFromDB;

@end
