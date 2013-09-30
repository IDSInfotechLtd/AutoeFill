//
//  ViewController.m
//  WebServicesConnection
//
//  Created by Eliza on 16/09/13.
//  Copyright (c) 2013 Eliza. All rights reserved.
//

#import "SearchViewController.h"
#import "ServerUtility.h"
#import "SearchResultViewController.h"
#import "DatabaseUtilities.h"
#import "Reachability.h"
#define ALERT_NOT_REACHABLE 100


@interface SearchViewController ()
@end

@implementation SearchViewController
static int counter =0;
- (void)viewDidLoad
{
    [super viewDidLoad];
    [self checkForInternetAvailability];
    [self searchScreenView];
    self.levelOfForm = @"1";
    self.stateID = @"";
    self.categoryID = @"";
    self.districtID = @"";
    self.countryID = @"1";
    self.formSearchBar.text = @"";
    self.scrollView.scrollEnabled = NO;
    counter=0;
}

-(void)checkForInternetAvailability{
    // Allocate a reachability object
    Reachability* reach = [Reachability reachabilityWithHostname:@"www.google.com"];
    
    // Set the blocks
    reach.reachableBlock = ^(Reachability*reach)
    {
        NSLog(@"REACHABLE!");
    };
    
    reach.unreachableBlock = ^(Reachability*reach)
    {
        NSLog(@"UNREACHABLE!");
       [[NSNotificationCenter defaultCenter] addObserver:self
                                                 selector:@selector(showAlertForNoInternetAvailable)
                                                     name:kReachabilityChangedNotification
                                                   object:nil];
    };
    
    // Start the notifier, which will cause the reachability object to retain itself!
    [reach startNotifier];
    
}

-(void)showAlertForNoInternetAvailable{

    if(counter == 0){
        UIAlertView* alertNotReachable = [[UIAlertView alloc] initWithTitle:@"Internet" message:@"No Internet Available" delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
        alertNotReachable.delegate= self;
        alertNotReachable.tag = ALERT_NOT_REACHABLE;
          [alertNotReachable show];
    }
    counter ++;
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    if(alertView.tag==ALERT_NOT_REACHABLE){
        [self.navigationController popViewControllerAnimated:YES];
    }
}
-(void)viewWillAppear:(BOOL)animated{
    ((ServerUtility*)[ServerUtility sharedManager]).objDelegate = self;
    self.formSearchBar.text =@"";
    self.txtSelectedCategory.text = @"";
    self.txtSelectedDistrict.text = @"";
    self.txtSelectedState.text = @"";
    
    self.stateID = @"";
    self.categoryID = @"";
    self.districtID = @"";
    self.countryID = @"1";
    
    if(self.nationalSearchBtn.selected)
        self.levelOfForm = @"1";
    else
        self.levelOfForm = @"0";
}

/*
 Method: searchScreenView
 param: null
 return type: void
 This method set tags to textfields and add bar button(search) on navigation bar
 */
-(void)searchScreenView
{
    self.title = @"Search Form";
    UIBarButtonItem *searchBtn = [[UIBarButtonItem alloc]initWithTitle:@"Search" style:UIBarButtonItemStylePlain target:self action:@selector(searchButtonPressed)];
    self.navigationItem.rightBarButtonItem = searchBtn;
    
    [self setCustomizedToolBar];
    self.pickerview = [[UIPickerView alloc]init];
    self.pickerview.delegate = self;
    self.pickerview.dataSource = self;
    self.pickerview.frame = CGRectMake(0, 500, self.pickerview.frame.size.width,self.pickerview.frame.size.height);
    self.pickerview.showsSelectionIndicator = YES;
    self.pickerview.hidden = YES;
}

/*
 Method: checkTagforTextField:
 param: UITextField
 return type: void
 This method will call when user click on any textfield and check tags of selected textfield and call webservices corresponding to that field
 */
-(void)checkTagforTextField:(UITextField*)textField
{
    if(textField == self.txtSelectedCountry){
//        [[DatabaseUtilities sharedManager] getalls]
//        [[ServerUtility sharedManager]getAllValues:GET_ALL_COUNTRIES Region:@"Country"];
//        self.pickerview.hidden = NO;
//        self.customizedToolBar.hidden = NO;
    }
    else if (textField == self.txtSelectedState){
       self.arrayForPickerView = [[DatabaseUtilities sharedManager] getAllStateListFromDB:@"1"];
        textField.text= [[ self.arrayForPickerView objectAtIndex:1] objectAtIndex:0];
        self.stateID = [[ self.arrayForPickerView objectAtIndex:0] objectAtIndex:0];
        self.pickerview.hidden = NO;
        self.customizedToolBar.hidden = NO;
        self.txtSelectedDistrict.text=@"";
        self.districtID=@"";
       [self.pickerview reloadAllComponents];
    }
    else if (textField == self.txtSelectedDistrict)
    {
        if(![self.stateID isEqualToString:@""])
        {
            self.arrayForPickerView = [[DatabaseUtilities sharedManager] getAllDistrictListFromDB:self.stateID];
            textField.text= [[ self.arrayForPickerView objectAtIndex:1] objectAtIndex:0];
            self.districtID = [[ self.arrayForPickerView objectAtIndex:0] objectAtIndex:0];
            self.pickerview.hidden = NO;
            self.customizedToolBar.hidden = NO;
            [self.pickerview reloadAllComponents];
        }
        else
        {
            self.pickerview.hidden = YES;
            self.customizedToolBar.hidden = YES;
            [self displayAlertView:ALERT_TITLE message:ALERT_DISTRICT_SELECTION];
        }
    }
    else if (textField == self.txtSelectedCategory){
        self.arrayForPickerView = [[DatabaseUtilities sharedManager] getAllCategoryListFromDB];
        textField.text= [[ self.arrayForPickerView objectAtIndex:1] objectAtIndex:0];
        self.pickerview.hidden = NO;
        self.customizedToolBar.hidden = NO;
        [self.pickerview reloadAllComponents];
    }
}

/*
 Method: setCustomizedToolBar
 param: null
 return type: void
 This method will create customized toolbar and add Done Bar button on it
 */
-(void)setCustomizedToolBar{
    self.customizedToolBar = [[UIToolbar alloc]initWithFrame:CGRectMake(0, 470, self.view.frame.size.width, 30)];
    self.customizedToolBar.barStyle = UIBarStyleBlackTranslucent;
    UIBarButtonItem *doneButton = [[UIBarButtonItem alloc] initWithTitle:@"DONE" style:UIBarButtonItemStyleDone target:self action:@selector(hidePickerViewAndToolBarWithAnimation)];
    NSArray *barButtonArray = [[NSArray alloc]initWithObjects:doneButton, nil];
    [self.customizedToolBar setItems:barButtonArray];
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
 Method: fetchSearchedForm
 param: null
 return type: void
 This method fetch forms on particular selection of category , keyword or level of form(national, state, district)
 */
- (void)searchButtonPressed
{
    NSString* searchedFormText = [self.formSearchBar.text stringByReplacingOccurrencesOfString:@" " withString:@"_"];
    NSString *url1 = [NSString stringWithFormat:GET_SEARCHED_FORMS_NEW,self.categoryID,searchedFormText,self.levelOfForm,self.countryID,self.stateID,self.districtID];
    NSLog(@"url Query = %@", url1);

    [self.view.window endEditing:YES];
    self.scrollView.contentSize=CGSizeMake(self.view.frame.size.width,self.view.frame.size.height);
    self.scrollView.scrollEnabled = NO;
    self.pickerview.hidden = YES;
    self.customizedToolBar.hidden = YES;

    SearchResultViewController *searchResultVC = [[SearchResultViewController alloc]init];
    searchResultVC.searchURL = url1;
    [self.navigationController pushViewController:searchResultVC animated:YES];

}

- (IBAction)nationalSearchBtnPressed:(id)sender {
    
    //     self.txtSelectedState.text = @"";
    //     self.txtSelectedDistrict.text = @"";
    //     self.txtSelectedCategory.text = @"";
    //     self.stateID =@"";
    //     self.categoryID = @"";
    [self.formSearchBar resignFirstResponder];
    self.pickerview.hidden = YES;
    self.customizedToolBar.hidden = YES;
    self.nationalSearchBtn.selected= YES;
    self.specificSearchBtn.selected=NO;
    
    self.levelOfForm = @"1";
    self.txtSelectedCountry.enabled =NO;
    self.txtSelectedState.enabled = NO;
    self.txtSelectedDistrict.enabled = NO;
    self.txtSelectedCountry.alpha = 0.5;
    self.txtSelectedState.alpha = 0.5;
    self.txtSelectedDistrict.alpha = 0.5;
}

- (IBAction)specificSearchBtnPressed:(id)sender {
    [self.formSearchBar resignFirstResponder];
    self.pickerview.hidden = YES;
    self.nationalSearchBtn.selected= NO;
    self.customizedToolBar.hidden = YES;

    self.specificSearchBtn.selected=YES;
    //     self.txtSelectedCategory.text = @"";
    //     self.categoryID = @"";
    self.levelOfForm = @"0";
    self.txtSelectedCountry.enabled = YES;
    self.txtSelectedState.enabled = YES;
    self.txtSelectedDistrict.enabled = YES;
    self.txtSelectedCountry.alpha = 1.0;
    self.txtSelectedState.alpha = 1.0;
    self.txtSelectedDistrict.alpha = 1.0;
}

-(void)displayAlertView:(NSString*)title message:(NSString*)msg
{
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:title message:msg delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
    [alert show];
}

#pragma mark - PICKER VIEW DELEGATES
/*
 Delegate methods for Picker View
 */
- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView {
    return 1;
}

- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component
{
    NSArray* tempArrStateId = [self.arrayForPickerView objectAtIndex:0];
    return tempArrStateId.count;
}

- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component
{
   
    if (self.selectedTextfield == self.txtSelectedState)
    {
        NSArray* tempArrStateId = [self.arrayForPickerView objectAtIndex:0];
        NSArray* tempArrStateName = [self.arrayForPickerView objectAtIndex:1];
        
        self.txtSelectedState.text =[tempArrStateName objectAtIndex:row];
        
        self.stateID = [tempArrStateId objectAtIndex:row];
        NSLog(@"stateID %@",self.stateID);
    }
    else if (self.selectedTextfield == self.txtSelectedDistrict)
    {
        NSArray* tempArrDistId = [self.arrayForPickerView objectAtIndex:0];
        NSArray* tempArrDistName = [self.arrayForPickerView objectAtIndex:1];
        
        self.txtSelectedDistrict.text = [tempArrDistName objectAtIndex:row];
        self.districtID = [tempArrDistId objectAtIndex:row];
        NSLog(@"districtID %@",self.districtID);
    }
    else if (self.selectedTextfield == self.txtSelectedCategory)
    {
        NSArray* tempArrCatId = [self.arrayForPickerView objectAtIndex:0];
        NSArray* tempArrCatName = [self.arrayForPickerView objectAtIndex:1];
        
        self.txtSelectedCategory.text = [tempArrCatName objectAtIndex:row];
        self.categoryID = [tempArrCatId objectAtIndex:row];
        NSLog(@"categoryID %@",self.categoryID);
    }
}

- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component {
     NSArray* tempArrNames = [self.arrayForPickerView objectAtIndex:1];
    return [tempArrNames objectAtIndex:row];
}

#pragma mark - SEARCH BAR DELEGATES
/*
 Delegate methods for Search bar
 */
-(void)searchBarSearchButtonClicked:(UISearchBar *)searchBar {
    [self.formSearchBar resignFirstResponder];
}

- (BOOL)searchBarShouldBeginEditing:(UISearchBar *)searchBar{
    self.pickerview.hidden = YES;
    self.customizedToolBar.hidden = YES;
    self.scrollView.scrollEnabled = YES;
    return YES;
}

- (void)searchBarCancelButtonClicked:(UISearchBar *) searchBar
{
    [searchBar resignFirstResponder];
}

#pragma mark TEXTFIELD DELEGATE
/*
 Delegate method of UITextfield delegate
 */
- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField
{
    [self checkTagforTextField:textField];
    
    [self.formSearchBar resignFirstResponder];
    self.selectedTextfield = textField;
    self.scrollView.scrollEnabled = YES;
    [self.pickerview removeFromSuperview];
    [self.customizedToolBar removeFromSuperview];
    
    
    textField.inputView = self.pickerview;
    [self showPickerViewAndToolBarWithAnimation];
    
    return NO;
}

#pragma mark SERVER UTILITY DELEGATE
/*
 Delegate method of ServerUtility
 */
-(void)reloadPickerViewData{
    [self.pickerview reloadAllComponents];
}
-(void)reloadTableViewData
{
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
