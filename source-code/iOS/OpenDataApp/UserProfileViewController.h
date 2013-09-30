//
//  UserProfileViewController.h
//  OpenDataApp
//
//  Created by Eliza on 17/09/13.
//  Copyright (c) 2013 Eliza. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UserProfileViewController : UIViewController <UITextFieldDelegate,UIAlertViewDelegate>

@property (nonatomic, strong) UIScrollView *scroll;
@property (nonatomic, strong) NSMutableArray *arrForLabelValues;
@property (nonatomic, strong) NSMutableArray *arrForTextFieldValues;
@property (assign) CGFloat scrollHeight;
@property (assign) CGFloat yOrigin;
@property (assign) BOOL isValidEmail;
@property (assign) BOOL isValidPhoneAndFaxNo;
@property(nonatomic,weak)NSString* editOrCreateProfile;
@property(nonatomic,strong)NSString* userProfileID;
@end
