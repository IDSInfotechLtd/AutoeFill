//
//  AllHTMLAndPDF.h
//  Users_Profile
//
//  Created by Eliza on 13/09/13.
//  Copyright (c) 2013 Eliza. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface AllHTMLAndPDF : UIViewController<UITableViewDataSource, UITableViewDelegate, UITabBarDelegate>

@property (weak, nonatomic) IBOutlet UITableView *tableView;

@property (strong, nonatomic) NSMutableArray* arrAllDownloadedForms;
@property (strong, nonatomic) NSMutableArray* arrAllPDFForms;
@property (strong, nonatomic) NSMutableArray *arr ;
@property (strong, nonatomic) NSMutableArray *arrOfPathForDownloadedFilesInDomain;

@property (strong, nonatomic) NSString *formType;
@property (weak, nonatomic) IBOutlet UIButton *getDownloadedFormBtn;
@property (weak, nonatomic) IBOutlet UIButton *getPDFBtn;

/*
 Method Name: getDownloadedFromBtnPressed:
 Action will generate on click of getDownloadedFormBtn,
 display all downloaded forms from bundle into tableview
 */
- (IBAction)getDownloadedFromBtnPressed:(id)sender;

/*
 Method Name: getPDFBtnPressed:
 Action will generate on click of getPDFBtn,
 display all PDF forms from bundle into tableview
 */
- (IBAction)getPDFBtnPressed:(id)sender;


//-(NSMutableArray*) getAllDownloadedForms;
//- (NSMutableArray*) getAllPDFForms;

@end
