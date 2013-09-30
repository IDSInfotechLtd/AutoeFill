//
//  Connectivity.m
//  WebServicesConnection
//
//  Created by Eliza on 16/09/13.
//  Copyright (c) 2013 Eliza. All rights reserved.
//

#import "ServerUtility.h"
#import "AFJSONRequestOperation.h"
#import "AFHTTPClient.h"
#import "AFHTTPRequestOperation.h"

@implementation ServerUtility

+(ServerUtility*)sharedManager{
     
     static ServerUtility *sharedManager=nil;
     static dispatch_once_t pred;
     dispatch_once(&pred, ^{
          sharedManager = [[super allocWithZone:nil] init];
     });
     return sharedManager;
}

+ (id)allocWithZone:(NSZone *)zone
{
     return [self sharedManager];
}

-(void)getAllValues:(NSString*)urlString Region:(NSString*)type
{
     NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:urlString]];
     [AFJSONRequestOperation addAcceptableContentTypes:[NSSet setWithObject:@"text/html"]];
     AFJSONRequestOperation *operation = [AFJSONRequestOperation JSONRequestOperationWithRequest:request success:^(NSURLRequest *request, NSHTTPURLResponse *response, id JSON) {
         
          [self gotJSONSuccess:JSON region:type];
     } failure: nil ];
     
     [operation start];
}

/*
 Method: GotJSONSuccess
 param: id & NSString
 return type: void
 This method will fetch details from JSON 
 */
- (void)gotJSONSuccess:(id)JSON region:(NSString*)region
{
    if(!self.arrAllIndiaList)
        self.arrAllIndiaList = [[NSMutableArray alloc] init];
    else
        [self.arrAllIndiaList removeAllObjects];
    
     NSDictionary* dataDict =  [JSON objectForKey:@"data"];
     
     if([region isEqualToString:@"State"])
     {
         [self getStateListFromServer:dataDict];
     }
     else if ([region isEqualToString:@"Country"])
     {
          [self getCountryListFromServer:dataDict];
     }
     else if ([region isEqualToString:@"District"])
     {
          [self getDistrictListFromServer:dataDict];
     }
     else if ([region isEqualToString:@"Category"])
     {
          [self getCategoryListFromServer:dataDict];
     }
     else if ([region isEqualToString:@"Form Search"])
     {
          [self getFormListFromServer:dataDict];
     }
     [self.objDelegate reloadPickerViewData];
}

/*
 Method: getCountryListFromServer
 param: null
 return type: void
  method will fetch all country IDs and country names 
 */
- (void)getCountryListFromServer:(NSDictionary*)dataDict{
    
     NSDictionary *countryDict = [dataDict valueForKey:@"Country"];
     NSMutableArray *countryIDs = [countryDict valueForKey:@"CountryId"];
     NSMutableArray *countryNames = [countryDict valueForKey:@"CountryName"];
    [self.arrAllIndiaList addObject:countryIDs];
    [self.arrAllIndiaList addObject:countryNames];
}

/*
 Method: getStateListFromServer
 param: null
 return type: void
 method will fetch all stateIDs IDs and stateIDs names
 */

- (void) getStateListFromServer:(NSDictionary*)dataDict{
    
     NSDictionary *stateDict = [dataDict valueForKey:@"State"];
     NSMutableArray *stateIDs = [stateDict valueForKey:@"StateId"];
     NSMutableArray *stateNames = [stateDict valueForKey:@"StateName"];
     NSMutableArray *CountryID = [stateDict valueForKey:@"CountryId"];
    [self.arrAllIndiaList addObject:stateIDs];
    [self.arrAllIndiaList addObject:stateNames];
    [self.arrAllIndiaList addObject:CountryID];
}

/*
 Method: getDistrictListFromServe
 param: null
 return type: void
 method will fetch all District IDs and District names

 */
- (void) getDistrictListFromServer:(NSDictionary*)dataDict{
    
     NSDictionary *districtDict = [dataDict valueForKey:@"District"];
     NSMutableArray *districtIDs = [districtDict valueForKey:@"DistrictId"];
     NSMutableArray *districtNames = [districtDict valueForKey:@"DistrictName"];
     NSMutableArray *StateIDs = [districtDict valueForKey:@"StateId"];
    [self.arrAllIndiaList addObject:districtIDs];
    [self.arrAllIndiaList addObject:districtNames];
    [self.arrAllIndiaList addObject:StateIDs];
}

/*
 Method: getCategoryListFromServer
 param: null
 return type: void
 method will fetch all categoryIDs and category names
 */
- (void) getCategoryListFromServer:(NSDictionary*)dataDict{
    
     NSDictionary *categoryDict = [dataDict valueForKey:@"Category"];
     NSMutableArray *categoryIDs = [categoryDict valueForKey:@"CategoryId"];
     NSMutableArray *categoryNames = [categoryDict valueForKey:@"CategoryName"];
    [self.arrAllIndiaList addObject:categoryIDs];
    [self.arrAllIndiaList addObject:categoryNames];
}

/*
 Method: getFormListFromServer
 param: null
 return type: void
 method will fetch all form IDs, form location and form names
 */
-(void)getFormListFromServer:(NSDictionary*)dataDict
{
    if(!self.arrSearchedFormResult)
        self.arrSearchedFormResult =[[NSMutableArray alloc] init];
    else
        [self.arrSearchedFormResult removeAllObjects];
    
     NSDictionary *formDict = [dataDict valueForKey:@"tbl_gforms"];
     NSMutableArray *formIDs = [formDict valueForKey:@"FormId"];
     NSMutableArray *formNames = [formDict valueForKey:@"FormName"];
     NSMutableArray *formLocation = [formDict valueForKey:@"FormLocation"];
    
    [self.arrSearchedFormResult addObject:formNames];
     [self.arrSearchedFormResult addObject:formLocation];
   
    NSLog(@"Available Form Details %@ %@ %@",formNames,formIDs, formLocation);
     [self.objDelegate reloadTableViewData];
}

@end
