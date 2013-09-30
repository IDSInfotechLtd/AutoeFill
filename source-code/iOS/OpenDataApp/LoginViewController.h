//
//  LoginViewController.h
//  OpenDataApp
//
//  Created by Taranjit on 17/09/13.
//  Copyright (c) 2013 Taranjit. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ServerUtility.h"

@interface LoginViewController : UIViewController<UITextFieldDelegate,ServerUtilityDelegate>

@property (strong, nonatomic) IBOutlet UITextField *txtUserName;
@property (strong, nonatomic) IBOutlet UITextField *txtPassword;
@property (strong,nonatomic) NSString *userNameStr;
@property (strong,nonatomic) NSString *userPassword;
@property (weak, nonatomic) IBOutlet UIScrollView *scrollView;
@property (weak, nonatomic) IBOutlet UISwitch* rememberMe;
/*
 Method Name: loginButtonPressed
 Action will generate on click of loginBtn
 result in go to Home screen for registered user
 */
- (IBAction)loginButtonPressed:(id)sender;
/*
 Method Name: gotoRegisterScreen:
 Action will generate on click of RegisterBtn,
 page will be open for new user to register themself
 */
- (IBAction)gotoRegisterScreen:(id)sender;
/*
 Method Name: skipLogin:
 Action will generate on click of skipBtn,
 go to Home screen directly for guest user
 */
- (IBAction)skipLogin:(id)sender;

-(void)getAllIndiaFromServer;

@end
