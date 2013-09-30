//
//  RegistrationViewController.m
//  Users_Profile
//
//  Created by Preetinder Kaur on 11/09/13.
//  Copyright (c) 2013 Preetinder Kaur. All rights reserved.
//

#import "RegistrationViewController.h"
#import "DatabaseUtilities.h"

@interface RegistrationViewController ()

@end

@implementation RegistrationViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
     self.txtUserName.delegate = self;
     self.txtPassword.delegate = self;
     self.txtConfirmPassword.delegate = self;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)registerBtnPressed:(id)sender
{
    NSString *userID = self.txtUserName.text;
    NSString *password = self.txtPassword.text;
    NSString *confirmPasswd = self.txtConfirmPassword.text;
     if([password isEqualToString:confirmPasswd])
     {
         if ([[DatabaseUtilities sharedManager]isUserExist:userID])
         {
             UIAlertView *alert=[[UIAlertView alloc] initWithTitle:ALERT_TITLE message:ALERT_USER_EXIST delegate:self cancelButtonTitle:CANCEL_BUTTON_TITLE otherButtonTitles:nil, nil];
             alert.tag=1;
             [alert show];
             
         }
         else if ([userID isEqualToString:@""])
         {
              UIAlertView *alert=[[UIAlertView alloc] initWithTitle:ALERT_TITLE message:ALERT_ENTER_USER_NAME delegate:self cancelButtonTitle:CANCEL_BUTTON_TITLE otherButtonTitles:nil, nil];
              alert.tag=2;
              [alert show];
         }
         else
         {
              NSString* msg;
              if([[DatabaseUtilities sharedManager]registerUser:userID password:password])
              {
                   msg = ALERT_REGISTRATION_SUCCESS;
                   UIAlertView *alert=[[UIAlertView alloc] initWithTitle:ALERT_TITLE message:msg delegate:self cancelButtonTitle:CANCEL_BUTTON_TITLE otherButtonTitles:nil, nil];
                   alert.tag=3;
                   [alert show];
              }
              else
              {
                   msg = ALERT_REGISTRATION_FAIL;
                   UIAlertView *alert=[[UIAlertView alloc] initWithTitle:ALERT_TITLE message:msg delegate:self cancelButtonTitle:CANCEL_BUTTON_TITLE otherButtonTitles:nil, nil];
                   alert.tag=4;
                   [alert show];
              }
         }
       
    }
    else
     {
         UIAlertView *alert=[[UIAlertView alloc] initWithTitle:ALERT_PASSWORD_TITLE message:ALERT_PASWWORD_MATCH_MESSAGE delegate:self cancelButtonTitle:CANCEL_BUTTON_TITLE otherButtonTitles:nil, nil];
         alert.tag=5;
         [alert show];
     }
}
/*
  method of UIAlertView
 return a result on OK btn click
 */
-(void)alertView :(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
     self.txtPassword.text = @"";
     self.txtConfirmPassword.text = @"";
     self.txtUserName.text = @"";
    if (alertView.tag==3)
    {
        [self.navigationController popToRootViewControllerAnimated:YES];
    }
   
}

/*
 Delegate method of UITextfield delegate
 */
- (BOOL)textFieldShouldReturn:(UITextField *)textField;
{
     [textField resignFirstResponder];
     self.scrollView.scrollEnabled = NO;
     return YES;
}
/*
 Delegate method of UITextfield delegate
 */
- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField
{
     self.scrollView.scrollEnabled = YES;
     return YES;
}
@end
