//
//  Connectivity.m
//  WebServicesConnection
//
//  Created by Eliza on 16/09/13.
//  Copyright (c) 2013 Eliza. All rights reserved.
//

#import "SercerUtility.h"
#import "AFJSONRequestOperation.h"
#import "AFHTTPClient.h"
#import "AFHTTPRequestOperation.h"

@implementation SercerUtility

+(SercerUtility*)sharedManager{
     
     static SercerUtility *sharedManager=nil;
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
     self.dataDict =  [JSON objectForKey:@"data"];
     
     if([region isEqualToString:@"State"])
     {
          [self getStateListFromServer];
     }
     else if ([region isEqualToString:@"Country"])
     {
          [self getCountryListFromServer];
     }
     else if ([region isEqualToString:@"District"])
     {
          [self getDistrictListFromServer];
     }
     else if ([region isEqualToString:@"Category"])
     {
          [self getCategoryListFromServer];
     }
     else if ([region isEqualToString:@"Form Search"])
     {
          [self getFormListFromServer];
     }
     [self.objDelegate reloadPickerViewData];
}

/*
 Method: getCountryListFromServer
 param: null
 return type: void
  method will fetch all country IDs and country names 
 */
- (void)getCountryListFromServer{
     NSDictionary *countryDict = [self.dataDict valueForKey:@"Country"];
     NSMutableArray *countryIDs = [countryDict valueForKey:@"CountryId"];
     NSMutableArray *countryNames = [countryDict valueForKey:@"CountryName"];
     self.arrNeedToReturnWithNames = countryNames;
     self.arrNeedToReturnWithIDs = countryIDs;
}

/*
 Method: getStateListFromServer
 param: null
 return type: void
 method will fetch all stateIDs IDs and stateIDs names

 */
- (void) getStateListFromServer{
     NSDictionary *stateDict = [self.dataDict valueForKey:@"State"];
     NSMutableArray *stateIDs = [stateDict valueForKey:@"StateId"];
     NSMutableArray *stateNames = [stateDict valueForKey:@"StateName"];
     self.arrNeedToReturnWithNames = stateNames;
     self.arrNeedToReturnWithIDs = stateIDs;
     
}

/*
 Method: getDistrictListFromServe
 param: null
 return type: void
 method will fetch all District IDs and District names

 */
- (void) getDistrictListFromServer
{
     NSDictionary *districtDict = [self.dataDict valueForKey:@"District"];
     NSMutableArray *districtIDs = [districtDict valueForKey:@"DistrictId"];
     NSMutableArray *districtNames = [districtDict valueForKey:@"DistrictName"];
     self.arrNeedToReturnWithNames = districtNames;
     self.arrNeedToReturnWithIDs = districtIDs;
}

/*
 Method: getCategoryListFromServer
 param: null
 return type: void
 method will fetch all categoryIDs and category names
 */
- (void) getCategoryListFromServer
{
     NSDictionary *categoryDict = [self.dataDict valueForKey:@"Category"];
     NSMutableArray *categoryIDs = [categoryDict valueForKey:@"CategoryId"];
     NSMutableArray *categoryNames = [categoryDict valueForKey:@"CategoryName"];
     self.arrNeedToReturnWithNames = categoryNames;
     self.arrNeedToReturnWithIDs = categoryIDs;
}

/*
 Method: getFormListFromServer
 param: null
 return type: void
 method will fetch all form IDs, form location and form names
 */
-(void)getFormListFromServer
{
     NSDictionary *formDict = [self.dataDict valueForKey:@"tbl_gforms"];
     NSMutableArray *formIDs = [formDict valueForKey:@"FormId"];
     NSMutableArray *formNames = [formDict valueForKey:@"FormName"];
     self.arrSearchedFormResult = formNames;
     NSMutableArray *formLocation = [formDict valueForKey:@"FormLocation"];
     NSLog(@"Available Form Details %@ %@ %@",formNames,formIDs, formLocation);
     [self.objDelegate reloadTableViewData];
}

@end
