//
//  FormViewController.m
//  OpenDataApp
//
//  Created by Preetinder Kaur on 17/09/13.
//  Copyright (c) 2013 Taranjit. All rights reserved.
//

#import "FormViewController.h"
#import "DatabaseUtilities.h"
#import "AllHTMLAndPDF.h"

#define kPaperSizeA4 CGSizeMake(595,842)
#define kPaperSizeLetter CGSizeMake(612,792)

@interface UIPrintPageRenderer (PDF)

- (NSData*) printToPDF;

@end

@interface FormViewController ()

@end

@implementation FormViewController

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
	// Do any additional setup after loading the view, typically from a nib.
    //[self.webView setDelegate:self];
    self.title=self.formName;
    self.webView.delegate=self;
    self.userName=[[NSUserDefaults standardUserDefaults] objectForKey:DEFAULT_PROFILE_NAME];
    self.profileID =[[NSUserDefaults standardUserDefaults] objectForKey:DEFAULT_PROFILE_ID];
    
    [self creatingNavBarWithButtons];
    
    if([[self.formName substringFromIndex:self.formName.length-3] isEqualToString:@"pdf"])
        self.btnSaveAsPDF.enabled=NO;
}

-(void)viewWillAppear:(BOOL)animated{
    [self.view addSubview:self.loadingView];
}


- (void)saveWebPage
{
    NSURL* url = [NSURL URLWithString:self.urlToBeOpened];
    NSData* data = [NSData dataWithContentsOfURL:url];
    if(data)
    {
        NSArray *paths=NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
        NSString *filePath=[NSString stringWithFormat:@"%@/%@.html",[paths objectAtIndex:0],self.formName];
        if(![[NSFileManager defaultManager] fileExistsAtPath:filePath])
            [data writeToFile:filePath atomically:YES];
    }
}

-(void)creatingNavBarWithButtons
{
    NSString* userId = [[NSUserDefaults standardUserDefaults] objectForKey:USER_ID];
    NSString* fileExt= [self.formName substringFromIndex:self.formName.length-3];
    if(![userId isEqualToString:@""] && ![fileExt isEqualToString:@"pdf"]){
        UIBarButtonItem *changeProfile=[[UIBarButtonItem alloc]initWithTitle:@"Change Profile" style:UIBarButtonItemStyleBordered target:self action:@selector(changeUsersProfile)];
        self.navigationItem.rightBarButtonItem=changeProfile;
    }
    
    NSURL *url;
    if([[NSFileManager defaultManager]fileExistsAtPath:self.urlToBeOpened]){
        url =[NSURL fileURLWithPath:self.urlToBeOpened];
    }
    else{
        self.urlToBeOpened = [self.urlToBeOpened stringByReplacingOccurrencesOfString:@" " withString:@"%20"];
        url = [NSURL URLWithString:self.urlToBeOpened];
        [self saveWebPage];
    }
    NSURLRequest *request=[NSURLRequest requestWithURL:url];
    [self.webView loadRequest:request];
    
}

-(void)addingValuesInWebView
{
    for(NSString *key in self.dict)
    {
        NSString* itemsValueStr=[NSString stringWithFormat:@"document.getElementById('%@').value='%@'",key,[self.dict valueForKey:key]];
        
        [self.webView stringByEvaluatingJavaScriptFromString:itemsValueStr];
    }
    
}

-(void)loadingUserProfileInDictionaryWithUserId:(NSString*)userId
{
    NSMutableArray* tempProfiledata = [[DatabaseUtilities sharedManager]getUserProfileWithID:userId];
    NSMutableArray* tempProfileColumnName = [[DatabaseUtilities sharedManager]getAllColumnsName];
    if(tempProfiledata.count==tempProfileColumnName.count)
        self.dict = [NSMutableDictionary dictionaryWithObjects:tempProfiledata forKeys:tempProfileColumnName];
    //After Loading
    [self addingValuesInWebView];
}

-(void)changeUsersProfile
{
    SelectProfileViewController *selectProfile=[[SelectProfileViewController alloc]init];
    selectProfile.modalTransitionStyle=UIModalTransitionStyleCrossDissolve;
    selectProfile.delegate= self;
    [self presentViewController:selectProfile animated:YES completion:NULL];
}

-(void)changedProfileID:(NSString*)profileId profileName:(NSString*)profileName{
    [self clearAllTextFieldValues];
    NSLog(@"selected profile is %@",profileId);
    
    self.profileID =[NSString stringWithFormat:@"%@",profileId];
    if(self.profileID){
        [self loadingUserProfileInDictionaryWithUserId:self.profileID];
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)saveUserProfileInDatabase
{
    NSString* userId = [[NSUserDefaults standardUserDefaults] objectForKey:USER_ID];
    if([userId isEqualToString:@""] && [self.profileID isEqualToString:@""])
        return;
    
    self.dict = [[NSMutableDictionary alloc]init];
    
    [self.webView stringByEvaluatingJavaScriptFromString:@"document.forms.length"];
    
    NSString* form=[self.webView stringByEvaluatingJavaScriptFromString:@"document.forms[0].id"];
    NSString *elementsStr=[NSString stringWithFormat:@"document.getElementById('%@').elements.length",form];
    NSString* allElements=[self.webView stringByEvaluatingJavaScriptFromString:elementsStr];
    
    for (int i=0; i<[allElements intValue]; i++)
    {
        NSString *str= [NSString stringWithFormat:@"document.getElementById('%@').elements[%d].type=='checkbox'",form,i];
        NSString *strField=[self.webView stringByEvaluatingJavaScriptFromString:str];
        NSString *radioStr=[NSString stringWithFormat:@"document.getElementById('%@').elements[%d].type=='radio'",form,i];
        NSString *strRadioField=[self.webView stringByEvaluatingJavaScriptFromString:radioStr];
        if ([strField isEqualToString:@"true"] || [strRadioField isEqualToString:@"true"])
        {
            NSString *elemnt=[NSString stringWithFormat:@"document.forms[0].elements[%i].checked",i];
            NSString *items=[self.webView stringByEvaluatingJavaScriptFromString:elemnt];
            if([items isEqualToString:@"true"])
            {
                NSString* itemsValueStr=[NSString stringWithFormat:@"document.getElementById('%@').elements[%d].value",form,i];
                NSString* itemsValue=[self.webView stringByEvaluatingJavaScriptFromString:itemsValueStr];
                NSString* tagNameValue = [NSString stringWithFormat:@"document.getElementById('%@').elements[%d].id",form,i];
                NSString* tagName=[self.webView stringByEvaluatingJavaScriptFromString:tagNameValue];
                if([tagName isEqualToString:@""])
                    continue;
                [self.dict setObject:itemsValue forKey:tagName];
            }
        }
        else
        {
            NSString* itemsValueStr=[NSString stringWithFormat:@"document.getElementById('%@').elements[%d].value",form,i];
            NSString* itemsValue=[self.webView stringByEvaluatingJavaScriptFromString:itemsValueStr];
            NSString* tagNameValue = [NSString stringWithFormat:@"document.getElementById('%@').elements[%d].id",form,i];
            NSString* tagName=[self.webView stringByEvaluatingJavaScriptFromString:tagNameValue];
            if([tagName isEqualToString:@""])
                continue;
            
            if(itemsValue.length>0){
            }
            else
                itemsValue=@"";
            [self.dict setObject:itemsValue forKey:tagName];
        }
    }
    NSLog(@"count value : %d",[self.dict count]);
    NSLog(@"dictionary is:%@",self.dict);
    [[DatabaseUtilities sharedManager]alterTable:self.dict];
    [[DatabaseUtilities sharedManager]updateUserProfile:self.dict profileId:self.profileID];
}

-(NSData*)convertHtmlToData{
    CGSize pageSize=kPaperSizeA4;
    UIEdgeInsets pageMargins=UIEdgeInsetsMake(10, 5, 10, 5);
    
    
    UIPrintPageRenderer *render = [[UIPrintPageRenderer alloc] init];
    
    [render addPrintFormatter:self.webView.viewPrintFormatter startingAtPageAtIndex:0];
    
    CGRect printableRect = CGRectMake(pageMargins.left,
                                      pageMargins.top,
                                      pageSize.width - pageMargins.left - pageMargins.right,
                                      pageSize.height - pageMargins.top - pageMargins.bottom);
    
    CGRect paperRect = CGRectMake(0, 0, pageSize.width, pageSize.height);
    
    [render setValue:[NSValue valueWithCGRect:paperRect] forKey:@"paperRect"];
    [render setValue:[NSValue valueWithCGRect:printableRect] forKey:@"printableRect"];
    
    NSData *pdfData = [render printToPDF];
    return pdfData;

}

-(void)saveFormAsPdf
{
    NSArray *paths=NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *PDFpath=[NSString stringWithFormat:@"%@/%@_%@.pdf",[paths objectAtIndex:0],self.userName,[self.formName stringByDeletingPathExtension]];
    NSLog(@"PDF path = %@",PDFpath);
    
    NSData* pdfData = [self convertHtmlToData];
    [pdfData writeToFile: PDFpath  atomically: YES];
}

- (IBAction)saveAsPDF:(UIButton *)sender
{
    [self performSelector:@selector(saveUserProfileInDatabase) withObject:nil afterDelay:0.1];
    [self saveFormAsPdf];
    UIAlertView *alert=[[UIAlertView alloc]initWithTitle:@"Save As PDF" message:@"Saved Successfully" delegate:self cancelButtonTitle:@"OK" otherButtonTitles: nil];
    [alert show];
}

- (IBAction)sendEmail:(id)sender
{
     [self performSelector:@selector(saveUserProfileInDatabase) withObject:nil afterDelay:0.1];
    NSString* fileName = [NSString stringWithFormat:@"%@.pdf",[self.formName stringByDeletingPathExtension]];
    NSData *data=[self convertHtmlToData];
    if ([MFMailComposeViewController canSendMail])
    {
        MFMailComposeViewController *mail=[[MFMailComposeViewController alloc]init];
        mail.mailComposeDelegate=self;
        [mail addAttachmentData:data mimeType:@"application/pdf" fileName:fileName];
        [self presentViewController:mail animated:YES completion:NULL];
    }
    else
    {
        UIAlertView *alert=[[UIAlertView alloc]initWithTitle:@"Failure" message:@"Your device does not support the composer sheet" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles: nil];
        [alert show];
    }
}
-(void)mailComposeController:(MFMailComposeViewController *)controller didFinishWithResult:(MFMailComposeResult)result error:(NSError *)error
{
    switch (result)
    {
        case MFMailComposeResultCancelled:
            NSLog(@"Mail cancelled");
            break;
        case MFMailComposeResultSaved:
            NSLog(@"Mail saved");
            break;
        case MFMailComposeResultSent:
            NSLog(@"Mail send");
            break;
        case MFMailComposeResultFailed:
            NSLog(@"Mail failed");
            break;
        default:
            NSLog(@"Mail not sent.");
            break;
    }
    [self dismissViewControllerAnimated:YES completion:NULL];
}

- (IBAction)printPDF:(id)sender
{
    [self saveFormAsPdf];
    NSData *data=[self convertHtmlToData];
    
    UIPrintInteractionController *print=[UIPrintInteractionController sharedPrintController];
    if (print && [UIPrintInteractionController canPrintData:data]) {
        print.delegate=self;
        
        UIPrintInfo *printInfo=[UIPrintInfo printInfo];
        printInfo.outputType=UIPrintInfoOutputGeneral;
        printInfo.jobName=[self.formName lastPathComponent];

        printInfo.duplex=UIPrintInfoDuplexLongEdge;
        print.printInfo=printInfo;
        print.showsPageRange=YES;
        print.printingItem=data;
        
        void(^completionHandler)(UIPrintInteractionController *, BOOL,NSError*)=^(UIPrintInteractionController *print,BOOL completed,NSError *error)
        {
            if (!completed && error)
                NSLog(@"failed!due to error in domain %@ with error code %u",error.domain,error.code);
        };
        [print presentAnimated:YES completionHandler:completionHandler];
    }
}
-(void)clearAllTextFieldValues
{
    NSString* form=[self.webView stringByEvaluatingJavaScriptFromString:@"document.forms[0].id"];
    NSString *elementsStr=[NSString stringWithFormat:@"document.getElementById('%@').elements.length",form];
    NSString* allElements=[self.webView stringByEvaluatingJavaScriptFromString:elementsStr];
    
    for (int i=0; i<[allElements intValue]; i++)
    {
        NSString *str= [NSString stringWithFormat:@"document.getElementById('%@').elements[%d].value=''",form,i];
       [self.webView stringByEvaluatingJavaScriptFromString:str];
    }
}

#pragma mark - WEBVIEW DELEGATE

- (void)webViewDidFinishLoad:(UIWebView *)webView
{
    [self.loadingView removeFromSuperview];
    if ([[NSUserDefaults standardUserDefaults]objectForKey:DEFAULT_PROFILE_ID])
    {
        [self loadingUserProfileInDictionaryWithUserId:[[NSUserDefaults standardUserDefaults]objectForKey:DEFAULT_PROFILE_ID]];
    }
    [self addingValuesInWebView];
}

@end

@implementation UIPrintPageRenderer (PDF)

- (NSData*) printToPDF
{
    NSMutableData *pdfData = [NSMutableData data];
    
    UIGraphicsBeginPDFContextToData( pdfData, self.paperRect, nil );
    
    [self prepareForDrawingPages: NSMakeRange(0, self.numberOfPages)];
    
    CGRect bounds = UIGraphicsGetPDFContextBounds();
    
    for ( int i = 0 ; i < self.numberOfPages ; i++ )
    {
        UIGraphicsBeginPDFPage();
        
        [self drawPageAtIndex:i inRect: bounds];
    }
    
    UIGraphicsEndPDFContext();
    
    return pdfData;
}

@end

