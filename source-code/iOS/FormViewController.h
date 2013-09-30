//
//  FormViewController.h
//  OpenDataApp
//
//  Created by Preetinder Kaur on 17/09/13.
//  Copyright (c) 2013 Taranjit. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <QuartzCore/QuartzCore.h>
#import "AllHTMLAndPDF.h"
#import <MessageUI/MessageUI.h>
#import "SelectProfileViewController.h"

@interface FormViewController : UIViewController<UIWebViewDelegate,MFMailComposeViewControllerDelegate, UIPrintInteractionControllerDelegate,UIScrollViewDelegate,SelectProfileViewControllerDelegate>

@property (strong, nonatomic) UIScrollView *scrollView;
@property (weak, nonatomic) IBOutlet UIWebView *webView;
@property (weak, nonatomic) IBOutlet UIButton *btnSaveAsPDF;
@property (weak, nonatomic) IBOutlet UIView *loadingView;

@property (strong, nonatomic) NSString *urlToBeOpened;
@property(nonatomic, strong) NSString *formName;
@property(nonatomic, strong) NSString *profileID;
@property(nonatomic,strong)NSString *userName;

@property(nonatomic, strong) NSMutableDictionary *dict;


- (IBAction)saveAsPDF:(id)sender;
- (IBAction)sendEmail:(id)sender;
- (IBAction)printPDF:(id)sender;


@end
