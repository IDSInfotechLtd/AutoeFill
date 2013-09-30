    //
//  UserProfileViewController.m
//  OpenDataApp
//
//  Created by Taranjit on 17/09/13.
//  Copyright (c) 2013 Taranjit. All rights reserved.
//

#import "UserProfileViewController.h"
#import "DatabaseUtilities.h"
#import "AllHTMLAndPDF.h"

@interface UserProfileViewController ()
@property(nonatomic,strong)NSMutableArray* arrAllFields;
@property(nonatomic,strong)NSMutableArray* arrUserProfileData;


@end

@implementation UserProfileViewController

- (void)viewDidLoad
{
     [super viewDidLoad];
  
    self.arrAllFields  = [[NSMutableArray alloc]init];
    self.arrUserProfileData = [[NSMutableArray alloc]init];
     self.arrAllFields = [[DatabaseUtilities sharedManager] getAllColumnsName];

    
    UIBarButtonItem *submitButton = [[UIBarButtonItem alloc] initWithTitle:@"Create" style:UIBarButtonItemStylePlain target:self action:@selector(submitAllValues)];
     self.navigationItem.rightBarButtonItem = submitButton;
    
    [self createScrollView];
    
    if([self.editOrCreateProfile isEqualToString:EDIT_PROFILE])
    {
        self.title = EDIT_PROFILE_TITLE;
        [self insertValuesIntoTextFields];
        UIBarButtonItem *submitButton = [[UIBarButtonItem alloc] initWithTitle:@"Update" style:UIBarButtonItemStylePlain target:self action:@selector(submitAllValues)];
        self.navigationItem.rightBarButtonItem = submitButton;
    }
    else{
        self.title = CREATE_PROFILE_TITLE;
    }
}

-(void)insertValuesIntoTextFields{
   self.arrUserProfileData =[[DatabaseUtilities sharedManager] getUserProfileWithID:self.userProfileID];
    for(int i=2;i<self.arrUserProfileData.count;i++){
        UITextField* textfield = (UITextField*)[self.view viewWithTag:i];
        textfield.text =  [self.arrUserProfileData objectAtIndex:i];
    }
}

-(void)createScrollView{
    
    self.scroll=[[UIScrollView alloc]initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height)];
    [self displayAllLabels];
    [self displayAllTextFields];
    self.scroll.contentSize=CGSizeMake(self.view.frame.size.width,self.yOrigin+SPACE_FOR_SCROLL_CONTENT);
    self.scrollHeight = self.scroll.contentSize.height;
    [self.view addSubview:self.scroll];

}
/*
 Method: displayAllLabels
 param: NULL
 return type: void
 methos will create labels for all items in MY_ARRAY and add to scroll view
 */
-(void)displayAllLabels{
     for(int i=2; i<self.arrAllFields.count; i++)
     {
          self.yOrigin=DISTANCE_BETWEEN_LABEL_AND_TEXTFIELD+(SPACE_FOR_LABEL_TEXTFIELD*(i-2));
          UILabel* myProfileLabel=[[UILabel alloc]initWithFrame : CGRectMake (XORIGIN_FOR_LABEL,self.yOrigin,WIDTH_FOR_LABEL,HEIGHT_FOR_LABEL_AND_TEXTFIELD)];
          myProfileLabel.backgroundColor = [UIColor clearColor];
          [self.scroll addSubview:myProfileLabel];
          myProfileLabel.textColor = [UIColor blackColor];
         NSString* text =  [[self.arrAllFields objectAtIndex:i] uppercaseString];
         text = [text stringByReplacingOccurrencesOfString:@"_" withString:@" "];
          myProfileLabel.text = text;
         myProfileLabel.font = [UIFont systemFontOfSize:14];
          NSLog(@"%@",myProfileLabel.text);
     }
}

/*
 Method: displayAllTextFields
 param: NULL
 return type: void
 methos will create TextFields for  items in arrAllFields and add to scroll view
 */
-(void)displayAllTextFields{
     for(int i=2; i<self.arrAllFields.count; i++)
     {
          self.yOrigin=DISTANCE_BETWEEN_LABEL_AND_TEXTFIELD+(SPACE_FOR_LABEL_TEXTFIELD*(i-2));
          UITextField* profileTextField=[[UITextField alloc]initWithFrame : CGRectMake (XORIGIN_FOR_TEXTFIELD,self.yOrigin,WIDTH_FOR_LABEL_AND_TEXTFIELD,HEIGHT_FOR_LABEL_AND_TEXTFIELD)];
          profileTextField.borderStyle = UITextBorderStyleRoundedRect;
          profileTextField.tag = i;
          profileTextField.delegate = self;
          profileTextField.text=@"";
          [self.scroll addSubview:profileTextField];
     }
}

/*
 Method: submitAllValues
 param: NULL
 return type: void
 methos will get all values from every textField and add to a dictionary with corresponding keys(label)
 */
-(void)submitAllValues
{
    UITextField* txtName = (UITextField*)[self.view viewWithTag:[self.arrAllFields indexOfObject:NAME]];
    if([txtName.text isEqualToString:@""]){
        [self displayAlertView:ALERT_TITLE message:@"Please enter Name" tag:5];
        txtName.text =@"";
        return;
    }
    
    if([self validateProfileFields])
    {
        self.arrForTextFieldValues = [[NSMutableArray alloc]init];
        [self.arrForTextFieldValues addObject:@""];
        [self.arrForTextFieldValues addObject:[[NSUserDefaults standardUserDefaults] objectForKey:USER_ID]];
        for(int i=2; i<self.arrAllFields.count; i++)
        {
            NSString *textFieldText = [(UITextField *)[self.view viewWithTag:i] text];
            @try
            {
                [self.arrForTextFieldValues addObject:textFieldText];
            }
            @catch (NSException *exception)
            {
                [self.arrForTextFieldValues addObject:@""];
            }
            @finally
            {
                NSLog(@"");
            }
        }
        
        NSMutableDictionary *profileDictionary = [NSMutableDictionary dictionaryWithObjects:self.arrForTextFieldValues forKeys:self.arrAllFields];
        [profileDictionary removeObjectForKey:ID];
        
        for(NSString *key in profileDictionary)
        {
            NSLog(@"%@   :   %@",key,[profileDictionary valueForKey:key]);
        }
        
        if([self.editOrCreateProfile isEqualToString:CREATE_PROFILE])
        {
            if([[DatabaseUtilities sharedManager] isProfileExist:txtName.text]){
                [self displayAlertView:ALERT_TITLE message:@"Profile Already exist with this User" tag:6];
                return;
            }
            
            NSString* prmaryKey = [NSString stringWithFormat:@"%d",[[DatabaseUtilities sharedManager]saveUserProfile:profileDictionary]];
            
            NSString* defProf = [[NSUserDefaults standardUserDefaults] objectForKey:DEFAULT_PROFILE_ID];
            NSString* defProfLang = [[NSUserDefaults standardUserDefaults] objectForKey:PREFERRED_LANGUAGE];
            if([defProf isEqualToString:@""]){
                [[NSUserDefaults standardUserDefaults] setObject:prmaryKey forKey:DEFAULT_PROFILE_ID];
                [[NSUserDefaults standardUserDefaults] setObject:txtName.text forKey:DEFAULT_PROFILE_NAME];
                [[NSUserDefaults standardUserDefaults] synchronize];
                
                [[DatabaseUtilities sharedManager] changeProfileAndLanguage:txtName.text language:defProfLang :prmaryKey];
                [self displayAlertView:@"Valid" message:@"Saved Successfully, and set this profile as Default" tag:4];
            }
            else
                [self displayAlertView:@"Valid" message:@"Saved Successfully" tag:4];
            
            return;
        }
        else
        {
            [[DatabaseUtilities sharedManager] updateUserProfile:profileDictionary profileId:self.userProfileID];
            [self displayAlertView:@"Valid" message:@"Profile Update" tag:6];
            return;
        }
    }    
}


#pragma mark - Validate Fields

/*
 Method: validateEmail:
 param: NSString
 return type: BOOl
 methos will validate entered email, check email is in proper format or not and return BOOl value
 */
- (BOOL)validateEmail:(NSString *)emailStr
{
    NSString *emailRegex = @"[A-Z0-9a-z._%+]+@[A-Za-z0-9.]+\\.[A-Za-z]{2,4}";
    
    NSPredicate *emailTest = [NSPredicate predicateWithFormat:@"SELF MATCHES %@",                                 emailRegex];
    return [emailTest evaluateWithObject:emailStr];
}

/*
 Method: validatePhoneAndFax:
 param: NSString
 return type: BOOl
 methos will validate entered phone and fax no, whether it is numeric 10 digit number or not
 */
- (BOOL)validatePhoneAndFax:(NSString *)phoneStr
{
    NSString *numberValidation = @"[0-9]{10}";
    NSPredicate *check = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", numberValidation];
    return [check evaluateWithObject:phoneStr];
}

-(void)displayAlertView:(NSString*)title message:(NSString*)msg tag:(NSInteger)tag
{
     UIAlertView *alert = [[UIAlertView alloc] initWithTitle:title message:msg delegate:nil cancelButtonTitle:@"OK"
     otherButtonTitles:nil];
    alert.delegate=self;
     alert.tag=tag;
     [alert show];
}

#pragma mark - Text Fields Delegates

/*
 delegate methods for text field
 */
-(BOOL)textFieldShouldBeginEditing:(UITextField *)textField{
    self.scroll.contentSize=CGSizeMake(self.scroll.contentSize.width,self.scrollHeight+KEYBOARD_HEIGHT);
    return YES;
}

- (BOOL)textFieldShouldEndEditing:(UITextField *)textField{
    NSLog(@"%d -- %d -- %d ",[self.arrAllFields indexOfObject: EMAIL_ID],[self.arrAllFields indexOfObject: MOBILE],[self.arrAllFields indexOfObject: NAME]);
    
     if((textField.tag == [self.arrAllFields indexOfObject: EMAIL_ID]) ||(textField.tag == [self.arrAllFields indexOfObject: MOBILE])){
          [self validateProfileFields];
     }
    return YES;
}

-(BOOL)validateProfileFields{
     UITextField* txtEmail = (UITextField*)[self.view viewWithTag:[self.arrAllFields indexOfObject: EMAIL_ID]];

     
     UITextField* txtMobile = (UITextField*)[self.view viewWithTag:[self.arrAllFields indexOfObject: MOBILE]];
     
         if(!([txtEmail.text isEqualToString: @""]))
          {
               self.isValidEmail = [self validateEmail:txtEmail.text];
               if(!self.isValidEmail)
               {
                    [self displayAlertView:ALERT_VALIDATION_TITLE message:ALERT_MSG_INVALID_EMAIL tag:1];
                    txtEmail.text =@"";
                    return NO;
               }
          }
          else if(!([txtMobile.text isEqualToString: @""]))
          {
               if(![self validatePhoneAndFax:txtMobile.text])
               {
                    [self displayAlertView:ALERT_VALIDATION_TITLE message:ALERT_MSG_INVALID_PHONE_FAX tag:3];
                    txtMobile.text =@"";
                    return NO;
               }                 
          }
     return YES;
        
}
-(void)alertView :(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (alertView.tag==4 || alertView.tag==6)
    {
        [self.navigationController popViewControllerAnimated:YES];
    }
    
       
}
- (BOOL)textFieldShouldReturn:(UITextField *)textField;
{
    [textField resignFirstResponder];
    self.scroll.contentSize=CGSizeMake(self.scroll.contentSize.width,self.scrollHeight);
    return YES;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}
@end
