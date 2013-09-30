//
//  SelectProfileViewController.h
//  OpenDataApp
//
//  Created by Preetinder Kaur on 20/09/13.
//  Copyright (c) 2013 Taranjit. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol SelectProfileViewControllerDelegate <NSObject>

-(void)changedProfileID:(NSString*)profileId profileName:(NSString*)profileName;

@end

@interface SelectProfileViewController : UIViewController<UITableViewDataSource,UITableViewDelegate,UINavigationControllerDelegate,UIAlertViewDelegate>

@property(nonatomic,weak) id <SelectProfileViewControllerDelegate> delegate;



@property (weak, nonatomic) IBOutlet UIImageView *imgTableBckgrnd;

@property (strong, nonatomic) NSString *userID;
@property (strong, nonatomic) NSMutableArray *userProfileDic;
@property (weak, nonatomic) IBOutlet UITableView *selectProfiletableView;

- (IBAction)okButtonPressed:(id)sender;
- (IBAction)cancelButtonPressed:(id)sender;

@end
