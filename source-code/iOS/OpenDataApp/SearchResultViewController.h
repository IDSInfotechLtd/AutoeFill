//
//  SearchResultViewController.h
//  OpenDataApp
//
//  Created by Eliza on 20/09/13.
//  Copyright (c) 2013 Taranjit. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ServerUtility.h"
@interface SearchResultViewController : UIViewController<UITableViewDataSource,UITableViewDelegate,ServerUtilityDelegate>

@property (weak, nonatomic) IBOutlet UITableView *searchResultTableView;
@property (strong, nonatomic) NSMutableArray* formName;
@property(strong, nonatomic) NSString* searchURL;
@property(strong, nonatomic)IBOutlet UIView* loadingView;
@end
