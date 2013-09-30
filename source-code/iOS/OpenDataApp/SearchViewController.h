//
//  ViewController.h
//  WebServicesConnection
//
//  Created by Eliza on 16/09/13.
//  Copyright (c) 2013 Eliza. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ServerUtility.h"

@interface SearchViewController : UIViewController<UIPickerViewDataSource,UIPickerViewDelegate, UITextFieldDelegate,ServerUtilityDelegate, UISearchBarDelegate,UIAlertViewDelegate>

@property(nonatomic, strong) UIPickerView *pickerview;
@property(nonatomic, strong) UIToolbar *customizedToolBar;
@property(nonatomic, strong) NSArray *arrayForPickerView;
@property (nonatomic)NSString *stateID;
@property (nonatomic)NSString *countryID;
@property (nonatomic)NSString *districtID;
@property (nonatomic)NSString *categoryID;
@property (nonatomic)NSString *levelOfForm;

@property (weak, nonatomic) IBOutlet UISearchBar *formSearchBar;
@property (weak, nonatomic) IBOutlet UITextField *txtSelectedCountry;
@property (weak, nonatomic) IBOutlet UITextField *txtSelectedState;
@property (weak, nonatomic) IBOutlet UITextField *txtSelectedDistrict;
@property (weak, nonatomic) IBOutlet UITextField *txtSelectedCategory;
@property (weak, nonatomic) UITextField *selectedTextfield;
@property (weak, nonatomic) IBOutlet UIScrollView *scrollView;

@property (weak, nonatomic) IBOutlet UIButton *nationalSearchBtn;
@property (weak, nonatomic) IBOutlet UIButton *specificSearchBtn;

/*
 Method Name: nationalSearchBtnPressed:
 Action will generate on click of nationalSearchBtn,
 result in all national level forms of selected category
 */
- (IBAction)nationalSearchBtnPressed:(id)sender;

/*
 Method Name: specificSearchBtnPressed:
 Action will generate on click of specificSearchBtn,
 result in either state level or district level forms of selected category
 */
- (IBAction)specificSearchBtnPressed:(id)sender;


@end
