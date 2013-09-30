//
//  SettingsViewController.m
//  OpenDataApp
//
//  Created by Eliza on 20/09/13.
//  Copyright (c) 2013 Taranjit. All rights reserved.
//

#import "SettingsViewController.h"
#import "AllHTMLAndPDF.h"
#import "SelectProfileViewController.h"
#import "DatabaseUtilities.h"
#define TAG_DELETE_PROFILE 100
#define TAG_DELETE_ACCOUNT 200
#define TAG_CHANGE_PASSWORD 300


@interface SettingsViewController ()
{
    BOOL passwordChanged;
}
@end

@implementation SettingsViewController

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
    self.title  = @"Settings";
    passwordChanged=NO;
    self.txtSelectProfile.tag = 1;
    self.txtDefaultLanguage.tag = 2;
    self.txtAppPassword.tag  = 3;
    self.txtAppPassword.text = @"";
    
    UIBarButtonItem *backButton = [[UIBarButtonItem alloc]
                                   initWithTitle: @"Back"
                                   style:UIBarButtonItemStylePlain
                                   target:self
                                   action:@selector(changeDefaultProfileAndLanguage)];
    self.navigationItem.leftBarButtonItem = backButton;
    
    self.arrForProfiles = [[NSMutableArray alloc]init];
    [self loadArrForProfilesAndId];
    
    [self displaySettingsScreen];
}

-(void)loadArrForProfilesAndId{
    NSMutableArray* tempArr =[[DatabaseUtilities sharedManager]getAllProfileIdForUserId:[[NSUserDefaults standardUserDefaults]objectForKey:USER_ID]];
    self.arrForProfiles = [tempArr objectAtIndex:1];
    self.arrForProfileID = [tempArr objectAtIndex:0];
    self.txtDefaultLanguage.text = [[NSUserDefaults standardUserDefaults]objectForKey:PREFERRED_LANGUAGE];
    self.txtSelectProfile.text = [[NSUserDefaults standardUserDefaults]objectForKey:DEFAULT_PROFILE_NAME];
    
}

-(void)displaySettingsScreen
{
    [self setCustomizedToolBar];
    self.pickerview = [[UIPickerView alloc]init];
    self.pickerview.delegate = self;
    self.pickerview.dataSource = self;
    self.pickerview.frame = CGRectMake(0, 500, self.pickerview.frame.size.width,self.pickerview.frame.size.height);
    self.pickerview.showsSelectionIndicator = YES;
    self.pickerview.hidden = YES;
    self.customizedToolBar.hidden = YES;
}

-(void)setCustomizedToolBar{
    self.customizedToolBar = [[UIToolbar alloc]initWithFrame:CGRectMake(0, 470, self.view.frame.size.width, 30)];
    self.customizedToolBar.barStyle = UIBarStyleBlackTranslucent;
    UIBarButtonItem *doneButton = [[UIBarButtonItem alloc] initWithTitle:@"DONE" style:UIBarButtonItemStyleDone target:self action:@selector(hidePickerViewAndToolBarWithAnimation)];
    NSArray *barButtonArray = [[NSArray alloc]initWithObjects:doneButton, nil];
    [self.customizedToolBar setItems:barButtonArray];
}

- (IBAction)deleteFormsPressed:(id)sender {
    [self showFormListToDelete];
}

- (IBAction)deleteProfilePressed:(id)sender {
    SelectProfileViewController *selectProfileVC =[[SelectProfileViewController alloc]init];
    selectProfileVC.delegate = self;
    [self presentViewController:selectProfileVC animated:YES completion:NULL];
}

- (IBAction)deleteMyAccountPressed:(id)sender {
    [self displayAlertView:@"Delete Account" message:@"Are You Sure ?" tag:TAG_DELETE_ACCOUNT];
}

-(void)changedProfileID:(NSString*)profileId profileName:(NSString*)profileName
{
    NSLog(@"%@ %@",profileName,profileId);
    self.profileID = profileId;
    [self displayAlertView:@"Delete Profile" message:@"Are You Sure ?" tag:0];
}

-(void)displayAlertView:(NSString*)title message:(NSString*)msg tag:(int)tag
{
    UIAlertView *alert1 = [[UIAlertView alloc]initWithTitle:title message:msg delegate:nil cancelButtonTitle:@"NO" otherButtonTitles:@"YES", nil];
    alert1.tag=tag;
    alert1.delegate = self;
    [alert1 show];
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
   if(alertView.tag == TAG_DELETE_PROFILE)
    {
        if (buttonIndex == alertView.firstOtherButtonIndex){
            [[DatabaseUtilities sharedManager] deleteUserProfile:self.profileID];
            if ([self.profileID isEqualToString:[[NSUserDefaults standardUserDefaults] objectForKey:DEFAULT_PROFILE_ID]]){
                [[NSUserDefaults standardUserDefaults] setObject:@"" forKey:DEFAULT_PROFILE_ID];
                [[NSUserDefaults standardUserDefaults] setObject:@"" forKey:DEFAULT_PROFILE_NAME];
                [[NSUserDefaults standardUserDefaults] synchronize];
                UIAlertView *alert1 = [[UIAlertView alloc]initWithTitle:@"Alert" message:@"Default Profile Deleted, Please select a Default profile" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
                alert1.delegate = self;
                [alert1 show];
                [self loadArrForProfilesAndId];
            }
        }
    }
    else if(alertView.tag == TAG_DELETE_ACCOUNT)
    {
        if (buttonIndex == alertView.firstOtherButtonIndex){
            [[DatabaseUtilities sharedManager] deleteMyAccount:[[NSUserDefaults standardUserDefaults] objectForKey:USER_ID]];
            [self.navigationController popToRootViewControllerAnimated:YES];
        }
    }
    else if(alertView.tag == TAG_CHANGE_PASSWORD){
        if (buttonIndex == alertView.firstOtherButtonIndex){
            [[DatabaseUtilities sharedManager] changePassword:self.txtAppPassword.text];
            [self.navigationController popViewControllerAnimated:YES];
        }
        else{
            passwordChanged = NO;
            self.txtAppPassword.text=@"";
        }
    }
    
}

-(void) showFormListToDelete{
    AllHTMLAndPDF *allHtmlAndPdfVC = [[AllHTMLAndPDF alloc]init];
    allHtmlAndPdfVC.formType = FORMS_TO_BE_DELETED;
    [self.navigationController pushViewController:allHtmlAndPdfVC animated:YES];
}

-(void)changeDefaultProfileAndLanguage
{
    [[DatabaseUtilities sharedManager] changeProfileAndLanguage:self.txtSelectProfile.text language:self.txtDefaultLanguage.text :[[NSUserDefaults standardUserDefaults] objectForKey:DEFAULT_PROFILE_ID]];
    
    
    if(passwordChanged)
        [self displayAlertView:@"Password Change" message:@"Are You Sure ?" tag:TAG_CHANGE_PASSWORD];
    else
    {
        [self.navigationController popViewControllerAnimated:YES];
    }
    
}
/*
 Method: showPickerViewAndToolBarWithAnimation
 param: null
 return type: void
 This method set animation on display of picker view and customized tool bar with done button
 */
-(void)showPickerViewAndToolBarWithAnimation
{
    [UIView beginAnimations:nil context:NULL];
    [UIView setAnimationDuration:.5];
    [UIView setAnimationDelegate:self];
    CGAffineTransform transform = CGAffineTransformMakeTranslation(0, 0);
    self.pickerview.frame = CGRectMake(0, 200, self.pickerview.frame.size.width,self.pickerview.frame.size.height);
    self.pickerview.transform = transform;
    self.customizedToolBar.frame = CGRectMake(0, 170, self.view.frame.size.width, 30) ;
    self.customizedToolBar.transform = transform;
    [self.view addSubview:self.pickerview];
    [self.view addSubview:self.customizedToolBar];
    [UIView commitAnimations];
}

/*
 Method: hidePickerViewAndToolBarWithAnimation
 param: null
 return type: void
 This method set animation on hide of picker view and customized tool bar with done button
 */
-(void)hidePickerViewAndToolBarWithAnimation
{
    [UIView beginAnimations:nil context:NULL];
    [UIView setAnimationDuration:.5];
    [UIView setAnimationDelegate:self];
    CGAffineTransform transform = CGAffineTransformMakeTranslation(0, 0);
    self.pickerview.frame = CGRectMake(0, 500, self.pickerview.frame.size.width,self.pickerview.frame.size.height);
    self.pickerview.transform = transform;
    self.customizedToolBar.frame = CGRectMake(0, 470, self.view.frame.size.width, 30) ;
    self.customizedToolBar.transform = transform;
    [self.view addSubview:self.pickerview];
    [self.view addSubview:self.customizedToolBar];
    [UIView commitAnimations];
}

/*
 Delegate methods for Picker View
 */
- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView {
    return 1;
}

- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component
{
    if(self.selectedTextField.tag==1)
    {
        if ([self.arrForProfiles count]==0)
        {
            return 1;
        }
        else
            return [self.arrForProfiles count];
    }
    else if (self.selectedTextField.tag==2)
    {
        return [ARRAY_DEFAULT_LANGUAGE count];
    }
    else
        return 1;
}

- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component
{
    if(self.selectedTextField.tag==1)
    {
        if ([self.arrForProfiles count]==0)
        {
            NSLog(@"No Profile");
        }
        else
        {
            [self.txtSelectProfile setText:[self.arrForProfiles objectAtIndex:row]];
            [[NSUserDefaults standardUserDefaults]setObject:[self.arrForProfiles objectAtIndex:row] forKey:DEFAULT_PROFILE_NAME];
            [[NSUserDefaults standardUserDefaults]setObject:[self.arrForProfileID objectAtIndex:row] forKey:DEFAULT_PROFILE_ID];
        }
    }
    else{
        [self.txtDefaultLanguage setText:[ARRAY_DEFAULT_LANGUAGE objectAtIndex:row]];
        [[NSUserDefaults standardUserDefaults]setObject:[ARRAY_DEFAULT_LANGUAGE objectAtIndex:row] forKey:PREFERRED_LANGUAGE];
    }
    [[NSUserDefaults standardUserDefaults] synchronize];
}

- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component
{
    if(self.selectedTextField.tag==1)
    {
        if([self.arrForProfiles count]==0){
            return @"No Profile";
        }
        return [self.arrForProfiles objectAtIndex:row];
    }
    else if (self.selectedTextField.tag==2)
    {
        return [ARRAY_DEFAULT_LANGUAGE objectAtIndex:row];
    }
    else
        return @"Invalid";
}

/*
 Delegate method of UITextfield delegate
 */
- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField
{
    self.selectedTextField = textField;
    [self.pickerview removeFromSuperview];
    [self.customizedToolBar removeFromSuperview];
    
    if((self.selectedTextField.tag==1)||(self.selectedTextField.tag==2))
    {
        [(UITextField*)[self.view viewWithTag:3] resignFirstResponder];
        self.pickerview.hidden = NO;
        self.customizedToolBar.hidden = NO;
        [self.pickerview reloadAllComponents];
        self.selectedTextField.inputView = self.pickerview;
        [self showPickerViewAndToolBarWithAnimation];
        return  NO;
    }
    else{
        self.pickerview.hidden = YES;
        self.customizedToolBar.hidden = YES;
        passwordChanged = YES;
        return YES;
        
    }
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField{
    [textField resignFirstResponder];
    return YES;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


@end
