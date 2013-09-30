//
//  AppDelegate.m
//  OpenDataApp
//
//  Created by Taranjit on 17/09/13.
//  Copyright (c) 2013 Taranjit. All rights reserved.
//

#import "AppDelegate.h"
#import "LoginViewController.h"
#import "DatabaseUtilities.h"

@implementation AppDelegate


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
   [[DatabaseUtilities sharedManager] copyDatabaseIfNeeded];

    self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    LoginViewController* loginVC = [[LoginViewController alloc] init];
    [loginVC performSelectorInBackground:@selector(getAllIndiaFromServer) withObject:nil];
    // Override point for customization after application launch.
    self.navController = [[UINavigationController alloc] initWithRootViewController:loginVC];
    self.navController.navigationBar.tintColor=[UIColor colorWithRed:0         green:83.0/255.0   blue:141.0/255.0 alpha:1];
    self.window.rootViewController = self.navController;
    [self.window makeKeyAndVisible];
    return YES;
}



- (void)applicationWillResignActive:(UIApplication *)application
{
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
    
   NSString* rememberMe =[[NSUserDefaults standardUserDefaults]objectForKey:REMEMBER_ME];
    if ([rememberMe isEqualToString:@"YES"])
    {
        NSLog(@" return");
    }
    else
    {
        [[NSUserDefaults standardUserDefaults] setObject:@"" forKey:USER_ID];
        [[NSUserDefaults standardUserDefaults] setObject:@"" forKey:USER_NAME];
        [[NSUserDefaults standardUserDefaults] setObject:@"" forKey:PREFERRED_LANGUAGE];
        [[NSUserDefaults standardUserDefaults] setObject:@"" forKey:DEFAULT_PROFILE_ID];
        [[NSUserDefaults standardUserDefaults] setObject:@"" forKey:DEFAULT_PROFILE_NAME];
        [[NSUserDefaults standardUserDefaults] synchronize];
    }
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application
{
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
    NSString* rememberMe =[[NSUserDefaults standardUserDefaults]objectForKey:REMEMBER_ME];
    if ([rememberMe isEqualToString:@"YES"])
    {
        NSLog(@" return");
    }
    else
    {
        [[NSUserDefaults standardUserDefaults] setObject:@"" forKey:USER_ID];
        [[NSUserDefaults standardUserDefaults] setObject:@"" forKey:USER_NAME];
        [[NSUserDefaults standardUserDefaults] setObject:@"" forKey:PREFERRED_LANGUAGE];
        [[NSUserDefaults standardUserDefaults] setObject:@"" forKey:DEFAULT_PROFILE_ID];
        [[NSUserDefaults standardUserDefaults] setObject:@"" forKey:DEFAULT_PROFILE_NAME];
        [[NSUserDefaults standardUserDefaults] synchronize];
    }
}


@end
