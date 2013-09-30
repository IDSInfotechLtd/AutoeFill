//
//  UserProfileViewController.m
//  OpenDataApp
//
//  Created by Taranjit on 17/09/13.
//  Copyright (c) 2013 Taranjit. All rights reserved.
//

#import "UserProfileViewController.h"
#import "DatabaseUtilities.h"
#import "Constants.h"
#import "AllHTMLAndPDF.h"

@interface UserProfileViewController ()

@end

@implementation UserProfileViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    UIBarButtonItem *submitButton = [[UIBarButtonItem alloc] initWithTitle:@"Submit" style:UIBarButtonItemStylePlain target:self action:@selector(getAllValues)];
    self.navigationItem.rightBarButtonItem = submitButton;
    
    UIBarButtonItem *nextButton = [[UIBarButtonItem alloc] initWithTitle:@"NEXT" style:UIBarButtonItemStylePlain target:self action:@selector(moveToAllHTMLAndPDFScreen)];
    self.navigationItem.leftBarButtonItem = nextButton;
    self.title = PROFILE_TITLE;
    self.view.backgroundColor=[UIColor whiteColor];
    
    self.scroll=[[UIScrollView alloc]initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height)];
    
    CGFloat yOrigin;
    for(int i=0; i<[MY_ARRAY count]; i++)
    {
        yOrigin=DISTANCE_BETWEEN_LABEL_AND_TEXTFIELD+(SPACE_FOR_LABEL_TEXTFIELD*i);
        UILabel* myProfileLabel=[[UILabel alloc]initWithFrame : CGRectMake (XORIGIN_FOR_LABEL,yOrigin,WIDTH_FOR_LABEL_AND_TEXTFIELD,HEIGHT_FOR_LABEL_AND_TEXTFIELD)];
        [self.scroll addSubview:myProfileLabel];
        myProfileLabel.textColor = [UIColor blackColor];
        myProfileLabel.text =MY_ARRAY[i];
        NSLog(@"%@",myProfileLabel.text);
    }
    for(int i=0; i<[MY_ARRAY count]; i++)
    {
        yOrigin=DISTANCE_BETWEEN_LABEL_AND_TEXTFIELD+(SPACE_FOR_LABEL_TEXTFIELD*i);
        UITextField* profileTextField=[[UITextField alloc]initWithFrame : CGRectMake (XORIGIN_FOR_TEXTFIELD,yOrigin,WIDTH_FOR_LABEL_AND_TEXTFIELD,HEIGHT_FOR_LABEL_AND_TEXTFIELD)];
        [self.scroll addSubview:profileTextField];
        profileTextField.borderStyle = UITextBorderStyleRoundedRect;
        profileTextField.tag = i+1;
        profileTextField.delegate = self;
    }
    self.scroll.contentSize=CGSizeMake(self.view.frame.size.width,yOrigin+SPACE_FOR_SCROLL_CONTENT);
    self.scrollHeight = self.scroll.contentSize.height;
    [self.view addSubview:self.scroll];
    
}

-(void)moveToAllHTMLAndPDFScreen
{
    AllHTMLAndPDF *allHtmlAndPdf  =[[AllHTMLAndPDF alloc]init];
    [self.navigationController pushViewController:allHtmlAndPdf animated:YES];
}

-(void)getAllValues{
    self.arrForTextFieldValues = [[NSMutableArray alloc]init];
    for (int i=0; i<[MY_ARRAY count]; i++)
    {
        NSString *textFieldText = [(UITextField *)[self.view viewWithTag:i+1] text];
        @try {
            [self.arrForTextFieldValues addObject:textFieldText];
        }
        @catch (NSException *exception) {
            [self.arrForTextFieldValues addObject:@""];
        }
        @finally {
            NSLog(@"");
        }
    }
    NSDictionary *profileDictionary = [NSDictionary dictionaryWithObjects:self.arrForTextFieldValues forKeys:MY_ARRAY];
    for(NSString *key in profileDictionary)
    {
        NSLog(@"key : %@",key);
        NSLog(@"value : %@",[profileDictionary valueForKey:key]);
    }
    [[DatabaseUtilities sharedManager]saveUserProfile:profileDictionary];
    
}

- (BOOL)validateEmail:(NSString *)emailStr
{
    NSString *emailRegex = @"[A-Z0-9a-z._%+]+@[A-Za-z0-9.]+\\.[A-Za-z]{2,4}";
    
    NSPredicate *emailTest = [NSPredicate predicateWithFormat:@"SELF MATCHES %@",                                 emailRegex];
    return [emailTest evaluateWithObject:emailStr];
}

- (BOOL)validatePhoneAndFax:(NSString *)phoneStr
{
    NSString *numberValidation = @"[0-9]{10}";
    NSPredicate *check = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", numberValidation];
    return [check evaluateWithObject:phoneStr];
}

-(BOOL)textFieldShouldBeginEditing:(UITextField *)textField{
    self.scroll.contentSize=CGSizeMake(self.scroll.contentSize.width,self.scrollHeight+KEYBOARD_HEIGHT);
    return YES;
}

- (BOOL)textFieldShouldEndEditing:(UITextField *)textField{
    
    if(textField.tag == [MY_ARRAY indexOfObject: EMAIL_ID]+1)
    {
        BOOL ifValid = [self validateEmail:textField.text];
        if(ifValid ==NO)
        {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Invalid"
                                                            message:@"Please enter valid Email-id"
                                                           delegate:nil
                                                  cancelButtonTitle:@"OK"
                                                  otherButtonTitles:nil];
            textField.text =@"";
            [alert show];
        }
    }
    else if ((textField.tag == [MY_ARRAY indexOfObject: PHONE]+1)||(textField.tag == [MY_ARRAY indexOfObject: FAX]+1))
    {
        BOOL isValidPhone = [self validatePhoneAndFax:textField.text];
        if(isValidPhone ==NO)
        {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Invalid"
                                                            message:@"Please enter valid 10 digit Number"
                                                           delegate:nil
                                                  cancelButtonTitle:@"OK"
                                                  otherButtonTitles:nil];
            textField.text =@"";
            [alert show];
        }
    }
    return YES;
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
