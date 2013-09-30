//
//  HomeScreenClass.h
//  LoginClass
//
//  Created by Preetinder Kaur on 11/09/13.
//  Copyright (c) 2013 Preetinder Kaur. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SelectProfileViewController.h"
#import "OBShapedButton.h"

@interface HomeViewController : UIViewController <SelectProfileViewControllerDelegate,UIAlertViewDelegate>
@property (weak, nonatomic) IBOutlet OBShapedButton *btnEditProfile;
@property (weak, nonatomic) IBOutlet OBShapedButton *btnCreateProfile;

@property (weak, nonatomic) IBOutlet OBShapedButton *btnSettings;

/*
 Method Name: gotoSearchForm:
 Action will generate on click of SearchFormBtn
 result in go to SearchViewController
 */
- (IBAction)gotoSearchForm:(id)sender;
/*
 Method Name: gotoViewDownloadedForm:
 Action will generate on click of DownloadedFormBtn
 result in go to ALLHTMLAndPDF page
 */
- (IBAction)gotoViewDownloadedForm:(id)sender;
/*
 Method Name: gotoCreateNewProfile:
 Action will generate on click of CreateNewProfileBtn
 result in go to UserProfileViewController
 */
- (IBAction)gotoCreateNewProfile:(id)sender;
/*
 Method Name: gotoViewFilledPDF:
 Action will generate on click of FilledPDFBtn
 result in go to FilledViewController
 */
- (IBAction)gotoViewFilledPDF:(id)sender;
/*
 Method Name: gotoSettings:
 Action will generate on click of settingBtn
 result in go to settingsViewController
 */
- (IBAction)gotoSettings:(id)sender;
/*
 Method Name: gotoEditProfile:
 Action will generate on click of editProfileBtn
 result in go to editProfile screen
 */
- (IBAction)gotoEditProfile:(id)sender;

@end
