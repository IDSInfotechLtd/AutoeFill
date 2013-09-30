//
//  LoginViewController.m
//  OpenDataApp
//
//  Created by Preetinder Kaur on 17/09/13.
//  Copyright (c) 2013 Taranjit. All rights reserved.
//

#import "LoginViewController.h"
#import "RegistrationViewController.h"
#import "HomeViewController.h"
#import "DatabaseUtilities.h"
#import "UserProfileViewController.h"


@interface LoginViewController ()

@end

@implementation LoginViewController
int static counter=0;

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    self.title=LOGIN_SCREEN_TITLE;
    self.txtUserName.delegate = self;
    self.txtPassword.delegate = self;
    [ServerUtility sharedManager].objDelegate = self;
    [self checkIfUserRemembered];
//    [self getAllIndiaFromServer];
}

-(void)getAllIndiaFromServer{
    NSLog(@"counter = %d",counter);
    switch (counter) {
        case 0:
            [[ServerUtility sharedManager] getAllValues:GET_ALL_INDIA_STATES Region:@"State"];
            break;
        case 1:
            [[ServerUtility sharedManager] getAllValues:GET_ALL_INDIA_DISTRICTS Region:@"District"];
            break;
        case 2:
            [[ServerUtility sharedManager] getAllValues:GET_ALL_INDIA_CATEGORIES Region:@"Category"];
            break;
        default:
            break;
    }
}

-(void)reloadPickerViewData{
    NSMutableArray *tempArr = [ServerUtility sharedManager].arrAllIndiaList;
    switch (counter) {
        case 0:
            [[DatabaseUtilities sharedManager] SaveAllStateListInDB:tempArr];
            break;
        case 1:
            [[DatabaseUtilities sharedManager] SaveAllDistrictListInDB:tempArr];
            break;
        case 2:
            [[DatabaseUtilities sharedManager] SaveAllCategoryListInDB:tempArr];
            break;
        default:
            break;
    }
    counter++;
    if(counter<=2)
        [self getAllIndiaFromServer];
    
}
-(void)reloadTableViewData{
    
}


-(void)checkIfUserRemembered{
    NSString* user= [[NSUserDefaults standardUserDefaults]objectForKey:USER_ID];
    if(user.length>0){
        [self resettingScrollView];
        HomeViewController *homeVC=[[HomeViewController alloc]init];
        [self.navigationController pushViewController:homeVC animated:YES];
    }
    NSString* rememberMe =[[NSUserDefaults standardUserDefaults]objectForKey:REMEMBER_ME];
    if ([rememberMe isEqualToString:@"YES"])
        [self.rememberMe setOn:YES];
    else
        [self.rememberMe setOn:NO];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)gotoRegisterScreen:(id)sender
{
    [self resettingScrollView];
    RegistrationViewController *registerScreen=[[RegistrationViewController alloc]init];
        [self.navigationController pushViewController:registerScreen animated:YES];
}

- (IBAction)loginButtonPressed:(id)sender
{
    [self.view.window endEditing:YES];
    self.userNameStr=[[NSString alloc]initWithString:self.txtUserName.text];
    self.userPassword=[[NSString alloc]initWithString:self.txtPassword.text];
    
    if ([[DatabaseUtilities sharedManager]verifyCredential:self.userNameStr password:self.userPassword])
    {
        [self resettingScrollView];
        HomeViewController *homeScreen=[[HomeViewController alloc]init];
        [self.navigationController pushViewController:homeScreen animated:YES];
    }
    else{
        UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"" message:LOGIN_ERROR_MESSAGE delegate:self cancelButtonTitle:CANCEL_BUTTON_TITLE otherButtonTitles:nil, nil];
        [alert show];
    }
    self.txtUserName.text = @"";
    self.txtPassword.text = @"";
}
- (IBAction)skipLogin:(id)sender
{
    [[NSUserDefaults standardUserDefaults] setObject:@"" forKey:USER_ID];
    [[NSUserDefaults standardUserDefaults] setObject:@"" forKey:USER_NAME];
    [[NSUserDefaults standardUserDefaults] setObject:@"" forKey:PREFERRED_LANGUAGE];
    [[NSUserDefaults standardUserDefaults] setObject:@"" forKey:DEFAULT_PROFILE_ID];
    [[NSUserDefaults standardUserDefaults] setObject:GUEST_USER forKey:DEFAULT_PROFILE_NAME];
    [[NSUserDefaults standardUserDefaults] synchronize];
    
    [self resettingScrollView];
    
    HomeViewController *homeVC=[[HomeViewController alloc]init];
    [self.navigationController pushViewController:homeVC animated:YES];
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
- (IBAction)btnRememberMe:(id)sender
{
    if (self.rememberMe.isOn)
    {
        
        [[NSUserDefaults standardUserDefaults]setObject:@"YES" forKey:REMEMBER_ME];
        [[NSUserDefaults standardUserDefaults] synchronize];
    }
    else{
        [[NSUserDefaults standardUserDefaults]setObject:@"NO" forKey:REMEMBER_ME];
        [[NSUserDefaults standardUserDefaults] synchronize];
    }
}

-(void)resettingScrollView{
    [self.view.window endEditing:YES];
    self.scrollView.contentSize=CGSizeMake(self.view.frame.size.width,self.view.frame.size.height);
    self.scrollView.scrollEnabled = NO;
    self.txtUserName.text = @"";
    self.txtPassword.text = @"";

}
@end
