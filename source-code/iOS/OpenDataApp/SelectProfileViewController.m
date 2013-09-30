//
//  SelectProfileViewController.m
//  OpenDataApp
//
//  Created by Preetinder Kaur on 20/09/13.
//  Copyright (c) 2013 Taranjit. All rights reserved.
//

#import "SelectProfileViewController.h"
#import "FormViewController.h"
#import "DatabaseUtilities.h"
#import <QuartzCore/QuartzCore.h>

#define CELL_TAG 100

@interface SelectProfileViewController ()
{
    int rowValue;
}
@property(nonatomic,weak)NSString* profileId;

@end

@implementation SelectProfileViewController

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
    self.title=@"Select Profile";
    self.imgTableBckgrnd.layer.cornerRadius = 15.0f;
    self.userID = [[NSUserDefaults standardUserDefaults] objectForKey:USER_ID];
    self.userProfileDic = [[NSMutableArray alloc] init];
    
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    self.userProfileDic=[[DatabaseUtilities sharedManager] getAllProfileIdForUserId:self.userID];
    NSLog(@"dict = %@",self.userProfileDic);
    if ([[self.userProfileDic objectAtIndex:0] count] == 0)
    {
        UIAlertView *alert=[[UIAlertView alloc]initWithTitle:@"" message:@"There is no Profile" delegate:self cancelButtonTitle:@"OK" otherButtonTitles: nil];
        alert.delegate=self;
        [alert show];
    }

}
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    [self dismissViewControllerAnimated:YES completion:^{}];
}
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
/*
 delegate and datasource methods for table view
 */
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView; {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    NSMutableArray* tempArr = [self.userProfileDic objectAtIndex:0];
    return tempArr.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    static NSString *cellIdentifier=@"cellIdentifier";
    UITableViewCell *cell=[tableView dequeueReusableCellWithIdentifier:cellIdentifier];
    if(cell==nil)
    {
        cell=[[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
    }
    NSString* profileName =[[self.userProfileDic objectAtIndex:1] objectAtIndex:indexPath.row];
    cell.textLabel.text= profileName;
    if([profileName isEqualToString:[[NSUserDefaults standardUserDefaults] objectForKey:DEFAULT_PROFILE_NAME]])
        cell.imageView.image=[UIImage imageNamed:@"selected.png"];
    else
        cell.imageView.image=[UIImage imageNamed:@"Unselected.png"];
    cell.tag = indexPath.row+CELL_TAG;
    cell.selectionStyle=UITableViewCellSelectionStyleNone;
    return cell;
    
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSMutableArray* tempArr = [self.userProfileDic objectAtIndex:0];
    for (int i=0; i<tempArr.count; i++)
    {
        UITableViewCell* cell = (UITableViewCell*)[tableView viewWithTag:i+CELL_TAG];
        if (i==indexPath.row)
        {
            cell.imageView.image=[UIImage imageNamed:@"selected.png"];
            rowValue = indexPath.row;
        }
        else
            cell.imageView.image=[UIImage imageNamed:@"Unselected.png"];
    }

}

- (IBAction)okButtonPressed:(id)sender
{    
    NSLog(@" %@ profile is selected", [[self.userProfileDic objectAtIndex:1] objectAtIndex:rowValue]);
    self.profileId = [[self.userProfileDic objectAtIndex:0] objectAtIndex:rowValue];
    [self.delegate changedProfileID:self.profileId profileName:[[self.userProfileDic objectAtIndex:1]objectAtIndex:rowValue]];
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (IBAction)cancelButtonPressed:(id)sender
{
   [self dismissViewControllerAnimated:YES completion:nil];
}
@end
