//
//  SearchResultViewController.m
//  OpenDataApp
//
//  Created by Eliza on 20/09/13.
//  Copyright (c) 2013 Taranjit. All rights reserved.
//

#import "SearchResultViewController.h"
#import "ServerUtility.h"
#import "FormViewController.h"

@interface SearchResultViewController ()

@end

@implementation SearchResultViewController

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
    self.title = @"Search Result";
    ((ServerUtility*)[ServerUtility sharedManager]).objDelegate = self;
    [[ServerUtility sharedManager]getAllValues:self.searchURL Region:@"Form Search"];
    self.formName = [[NSMutableArray alloc]init];
    [self.view addSubview:self.loadingView];
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView; {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    
    return  self.formName.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    static NSString *cellIdentifier=@"cellIdentifier";
    UITableViewCell *cell=[tableView dequeueReusableCellWithIdentifier:cellIdentifier];
    if(cell==nil)
    {
        cell=[[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
        NSLog(@"%d", indexPath.row);
    }
    cell.textLabel.text = [self.formName objectAtIndex:indexPath.row];
    return cell;
    
}
/*
 on click any specific row, navigate to another screen where selected selected file will open in webview
 */
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    NSMutableArray *tempArr = [[[ServerUtility sharedManager] arrSearchedFormResult] objectAtIndex:1];
    FormViewController *htmlForms=[[FormViewController alloc]init];
    htmlForms.urlToBeOpened = [tempArr objectAtIndex:indexPath.row];
    htmlForms.formName = [self.formName objectAtIndex:indexPath.row];
    [self.navigationController pushViewController:htmlForms animated:YES];
}


-(void)reloadTableViewData{
    [self.loadingView removeFromSuperview];
    
    @try {
        self.formName = [[[ServerUtility sharedManager]arrSearchedFormResult]objectAtIndex:0];
        if(self.formName.count > 0)
            [self.searchResultTableView reloadData];
    }
    @catch (NSException *exception) {
        UIAlertView* alert = [[UIAlertView alloc] initWithTitle:@"Alert" message:@"No Data Found" delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
        [alert show];
    }
    @finally {
        NSLog(@"Done");
    }
}

-(void)reloadPickerViewData{
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
