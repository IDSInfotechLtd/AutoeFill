//
//  HomeScreenClass.m
//  LoginClass
//
//  Created by Preetinder Kaur on 11/09/13.
//  Copyright (c) 2013 Preetinder Kaur. All rights reserved.
//

#import "HomeViewController.h"
#import "FormViewController.h"
#import "DatabaseUtilities.h"
#import "SearchViewController.h"
#import "UserProfileViewController.h"
#import "AllHTMLAndPDF.h"
#import "SettingsViewController.h"


@interface HomeViewController ()

@end

@implementation HomeViewController

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
    self.title=HOME_SCREEN_TITLE;
    NSString* btnTitle;
    NSString* userId = [[NSUserDefaults standardUserDefaults] objectForKey:USER_ID];
    if([userId isEqualToString:@""])
        btnTitle = @"Back";
    else
        btnTitle = LOGOUT_BAR_BUTTON_TITLE;
    
    self.navigationItem.hidesBackButton=YES;
    UIBarButtonItem *logout=[[UIBarButtonItem alloc]initWithTitle:btnTitle style:UIBarButtonItemStyleBordered target:self action:@selector(logoutBtn:)];
    self.navigationItem.leftBarButtonItem=logout;
    [self checkIFValidUserOrGuest];
   
}

-(void)checkIFValidUserOrGuest
{
    NSString* userId = [[NSUserDefaults standardUserDefaults] objectForKey:USER_ID];
    if([userId isEqualToString:@""]){
        self.btnCreateProfile.enabled = NO;
        self.btnEditProfile.enabled = NO;
        self.btnSettings.enabled = NO;
    }
    else{
        self.btnCreateProfile.enabled = YES;
        self.btnEditProfile.enabled = YES;
        self.btnSettings.enabled = YES;
    }
}

-(IBAction)logoutBtn:(id)sender
{
    UIAlertView* logoutAlert = [[UIAlertView alloc] initWithTitle:@"Log out" message:@"Are You Sure" delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"YES", nil];
    [logoutAlert show];
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    if (buttonIndex == alertView.firstOtherButtonIndex){
        
        [[NSUserDefaults standardUserDefaults] setObject:@"" forKey:USER_ID];
        [[NSUserDefaults standardUserDefaults] setObject:@"" forKey:USER_NAME];
        [[NSUserDefaults standardUserDefaults] setObject:@"" forKey:PREFERRED_LANGUAGE];
        [[NSUserDefaults standardUserDefaults] setObject:@"" forKey:DEFAULT_PROFILE_ID];
        [[NSUserDefaults standardUserDefaults] setObject:@"" forKey:DEFAULT_PROFILE_NAME];
        [[NSUserDefaults standardUserDefaults] synchronize];
        
        [self.navigationController popToRootViewControllerAnimated:YES];
        
    }
}
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)gotoSearchForm:(id)sender{
     SearchViewController* searchVC = [[SearchViewController alloc] init];
     [self.navigationController pushViewController:searchVC animated:YES];

}

- (IBAction)gotoViewDownloadedForm:(id)sender
{
     AllHTMLAndPDF *getAllDownloadedForms = [[AllHTMLAndPDF alloc]init];
     getAllDownloadedForms.self.formType = HTML_EXT;
     [self.navigationController pushViewController:getAllDownloadedForms animated:YES];
}

- (IBAction)gotoCreateNewProfile:(id)sender{
     UserProfileViewController *createProfileVC = [[UserProfileViewController alloc]init];
    createProfileVC.editOrCreateProfile = CREATE_PROFILE;
     [self.navigationController pushViewController:createProfileVC animated:YES];
}

- (IBAction)gotoViewFilledPDF:(id)sender{
     AllHTMLAndPDF *getAllDownloadedForms = [[AllHTMLAndPDF alloc]init];
     getAllDownloadedForms.self.formType = PDF_EXT;
     [self.navigationController pushViewController:getAllDownloadedForms animated:YES];
}
- (IBAction)gotoSettings:(id)sender{
     SettingsViewController *settingVC = [[SettingsViewController alloc]init];
     [self.navigationController pushViewController:settingVC animated:YES];
}

-(void)changedProfileID:(NSString*)profileId profileName:(NSString*)profileName{
    UserProfileViewController *editProfileVC = [[UserProfileViewController alloc]init];
    editProfileVC.editOrCreateProfile = EDIT_PROFILE;
    editProfileVC.userProfileID = [NSString stringWithFormat:@"%@",profileId];
    [self.navigationController pushViewController:editProfileVC animated:YES];
    
    
}
- (IBAction)gotoEditProfile:(id)sender{
    SelectProfileViewController* selectProfile =[[SelectProfileViewController alloc]init];
    selectProfile.delegate=self;
   // [self.navigationController pushViewController:selectProfile animated:YES];
    [self presentViewController:selectProfile animated:YES completion:NULL];

}

@end
