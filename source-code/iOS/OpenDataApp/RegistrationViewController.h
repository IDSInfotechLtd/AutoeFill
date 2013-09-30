//
//  RegistrationViewController.h
//  Users_Profile
//
//  Created by Preetinder Kaur on 11/09/13.
//  Copyright (c) 2013 Preetinder Kaur. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface RegistrationViewController : UIViewController<UITextFieldDelegate>

@property (weak, nonatomic) IBOutlet UITextField *txtUserName;
@property (weak, nonatomic) IBOutlet UITextField *txtPassword;
@property (weak, nonatomic) IBOutlet UITextField *txtConfirmPassword;
@property (weak, nonatomic) IBOutlet UIScrollView *scrollView;

/*
 Method Name: registerBtnPressed:
 Action will generate on click of registerBtn
 result in userName and password will be saved in database 
 */
- (IBAction)registerBtnPressed:(id)sender;

@end
