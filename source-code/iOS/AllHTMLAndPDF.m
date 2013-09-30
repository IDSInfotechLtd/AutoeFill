//
//  AllHTMLAndPDF.m
//  Users_Profile
//
//  Created by Eliza on 13/09/13.
//  Copyright (c) 2013 Eliza. All rights reserved.
//

#import "AllHTMLAndPDF.h"
#import "FormViewController.h"


@interface AllHTMLAndPDF ()

@end

@implementation AllHTMLAndPDF

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
     if([self.formType isEqualToString:HTML_EXT])
     {
          self.title = TITLE_DOWNLOADED_FORMS;
          self.getDownloadedFormBtn.selected = YES;
          self.getPDFBtn.selected = NO;
          [self getDownloadedFromBtnPressed:nil];
     }
     else if([self.formType isEqualToString:PDF_EXT]){
          self.title = TITLE_SAVED_PDF;
          self.getPDFBtn.selected = YES;
          self.getDownloadedFormBtn.selected = NO;
          [self getPDFBtnPressed:nil];
     }
     else
     {
          self.title = FORMS_TO_BE_DELETED;
          self.getDownloadedFormBtn.selected = YES;
          self.getPDFBtn.selected = NO;
          [self getDownloadedFromBtnPressed:nil];
     }
    if([self.formType isEqualToString:FORMS_TO_BE_DELETED])
    {
        UIBarButtonItem *deleteButton=[[UIBarButtonItem alloc]initWithTitle:@"Edit" style:UIBarButtonItemStyleBordered target:self action:@selector(deleteRows)];
        self.navigationItem.rightBarButtonItem=deleteButton;
    }
}
-(void)deleteRows
{
    if (self.editing)
    {
        [super setEditing:NO animated:NO];
        [self.tableView setEditing:NO animated:NO];
        [self.tableView reloadData];
        [self.navigationItem.rightBarButtonItem setTitle:@"Edit"];
        [self.navigationItem.rightBarButtonItem setStyle:UIBarButtonItemStylePlain];
    }
    else{
        [super setEditing:YES animated:YES];
        [self.tableView setEditing:YES animated:YES];
        [self.tableView reloadData];
        [self.navigationItem.rightBarButtonItem setTitle:@"Done"];
        [self.navigationItem.rightBarButtonItem setStyle:UIBarButtonItemStyleDone];

    }
}
- (IBAction)getDownloadedFromBtnPressed:(id)sender {
     self.title = @"Downloaded Forms";
     [self getAllDownloadedFormsAndPDF:HTML_EXT];
     self.getPDFBtn.selected = NO;
     self.getDownloadedFormBtn.selected = YES;
     [self.tableView reloadData];
}

- (IBAction)getPDFBtnPressed:(id)sender {
     self.title = @"PDF";
     [self getAllDownloadedFormsAndPDF:PDF_EXT];
     self.getPDFBtn.selected = YES;
     self.getDownloadedFormBtn.selected = NO;
     [self.tableView reloadData];
     }

/*
 Method: getAllDownloadedFormsAndPDF:
 param: NSString
 return type: void
 This method will find either all html or pdf files from bundle 
 */

-(void) getAllDownloadedFormsAndPDF:(NSString*)type
{
     NSError *error = nil;
     NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
     NSString *documentsPath = [paths objectAtIndex:0];
     NSArray * directoryContents = [[NSFileManager defaultManager] contentsOfDirectoryAtPath:documentsPath error:nil];
     self.arrOfPathForDownloadedFilesInDomain = [[NSMutableArray alloc]init];
     self.arr = [[NSMutableArray alloc]init];
     if (!error)
     {
          for (NSString * filename in directoryContents)
          {
               NSString *extension = [filename pathExtension];
               if ([extension isEqualToString:type])
               {
                    [self.arr addObject:filename];
                    NSLog(@"%@", filename);
                    
                    [self.arrOfPathForDownloadedFilesInDomain addObject:[documentsPath stringByAppendingString:[NSString stringWithFormat:@"/%@",filename]]];
                    NSLog(@"%@",self.arrOfPathForDownloadedFilesInDomain);
               }
          }
     }
}

/*
 delegate and datasource methods for table view
 */
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView; {
     return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
     return  [self.arr count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
     static NSString *cellIdentifier=@"cellIdentifier";
     UITableViewCell *cell=[tableView dequeueReusableCellWithIdentifier:cellIdentifier];
     if(cell==nil)
     {
          cell=[[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
          NSLog(@"%d", indexPath.row);
     }
     cell.textLabel.text=  [ self.arr  objectAtIndex:indexPath.row ];
     return cell;

}

/*
 on click any specific row, navigate to another screen where selected selected file will open in webview
 */

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
     if(([self.formType isEqualToString: HTML_EXT]) || ([self.formType isEqualToString: PDF_EXT]))
     {
          NSString *str1 = [self.arrOfPathForDownloadedFilesInDomain objectAtIndex:indexPath.row];
          FormViewController *htmlForms=[[FormViewController alloc]init];
          htmlForms.urlToBeOpened = str1;
          htmlForms.formName = [str1 lastPathComponent];
          [self.navigationController pushViewController:htmlForms animated:YES];
          
}
}

- (UITableViewCellEditingStyle)tableView:(UITableView *)aTableView editingStyleForRowAtIndexPath:(NSIndexPath *)indexPath
{
     if(([self.formType isEqualToString: HTML_EXT]) || ([self.formType isEqualToString: PDF_EXT])){
          return UITableViewCellEditingStyleNone;
     }
     else
          return UITableViewCellEditingStyleDelete;
}

- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
{
     if([self.formType isEqualToString:FORMS_TO_BE_DELETED])
     {
          if (editingStyle == UITableViewCellEditingStyleDelete)
          {
               NSLog(@"DELETED");
               [self.arr removeObjectAtIndex:indexPath.row];
               [tableView deleteRowsAtIndexPaths:[NSArray arrayWithObject:indexPath] withRowAnimation:YES];
               
               NSFileManager *fileManager = [NSFileManager defaultManager];
               [fileManager removeItemAtPath:[self.arrOfPathForDownloadedFilesInDomain objectAtIndex:indexPath.row] error:NULL];
              [self.arrOfPathForDownloadedFilesInDomain removeObjectAtIndex:indexPath.row];
               [tableView reloadData];
          }
     }
}

- (void)didReceiveMemoryWarning
{
     [super didReceiveMemoryWarning];
     // Dispose of any resources that can be recreated.
}
@end
