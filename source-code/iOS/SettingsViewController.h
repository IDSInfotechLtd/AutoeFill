//
//  SettingsViewController.h
//  OpenDataApp
//
//  Created by Eliza on 20/09/13.
//  Copyright (c) 2013 Taranjit. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SelectProfileViewController.h"

@interface SettingsViewController : UIViewController<UITextFieldDelegate, UIAlertViewDelegate, UIPickerViewDataSource, UIPickerViewDelegate, SelectProfileViewControllerDelegate>
@property (weak, nonatomic) IBOutlet UITextField *txtSelectProfile;
@property (weak, nonatomic) IBOutlet UITextField *txtDefaultLanguage;
@property (weak, nonatomic) IBOutlet UITextField *txtAppPassword;
@property (weak, nonatomic) UITextField *selectedTextField;

@property(nonatomic, strong) UIPickerView *pickerview;
@property(nonatomic, strong) UIToolbar *customizedToolBar;
@property(nonatomic)NSString* profileID;
@property(nonatomic)NSMutableArray *arrForProfiles;
@property(nonatomic)NSMutableArray *arrForProfileID;
@property(nonatomic)BOOL isChange;
- (IBAction)deleteFormsPressed:(id)sender;
- (IBAction)deleteProfilePressed:(id)sender;
- (IBAction)deleteMyAccountPressed:(id)sender;


@end
