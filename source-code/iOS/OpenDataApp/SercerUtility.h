//
//  Connectivity.h
//  WebServicesConnection
//
//  Created by Eliza on 16/09/13.
//  Copyright (c) 2013 Eliza. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol ServerUtilityDelegate <NSObject>
-(void)reloadPickerViewData;
-(void)reloadTableViewData;
@end

@interface SercerUtility : NSObject

+(SercerUtility*)sharedManager;
+(id)allocWithZone:(NSZone *)zone;

@property (nonatomic, weak) id <ServerUtilityDelegate>objDelegate;

@property (nonatomic, strong) id jsonData;
@property (nonatomic, strong) NSMutableArray *arrNeedToReturnWithNames;
@property (nonatomic, strong) NSMutableArray *arrNeedToReturnWithIDs;
@property (nonatomic, strong) NSMutableArray *dataDict;
@property (nonatomic, strong)NSMutableArray *arrSearchedFormResult;
/*
 Method: getAllValues
 param: NSString
 return type: void
 This method will receive URL for webservices as argument and type country, state, district, category, if succeeded  result in JSON, else display error message
 */
-(void)getAllValues:(NSString*)urlString Region:(NSString*)type;
@end
