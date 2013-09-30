//
//  UserProfileViewController.h
//  OpenDataApp
//
//  Created by Taranjit on 17/09/13.
//  Copyright (c) 2013 Taranjit. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UserProfileViewController : UIViewController <UITextFieldDelegate>

@property (nonatomic, strong) UIScrollView *scroll;

@property (nonatomic, strong) NSMutableArray *arrForLabelValues;

@property (nonatomic, strong) NSMutableArray *arrForTextFieldValues;
@property (nonatomic, strong) NSString *dbPath;

@property (assign) CGFloat scrollHeight;

@end
