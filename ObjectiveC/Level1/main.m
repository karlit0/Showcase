#import <Foundation/Foundation.h>
 
int main (int argc, const char * argv[]) {
 
	NSAutoreleasePool * pool = [[NSAutoreleasePool alloc] init];
	   
	NSLog(@"Antonio");
	NSString *firstName = @"Antonio";
	NSLog(firstName);
	NSLog(@"%@ %@", firstName, @"Stipanovic" );
	NSString *lastName = @"Stipanovic";
	NSLog(@"%@ %@", firstName, lastName);
	
	NSNumber *age = [[NSNumber alloc] initWithInt:23];
	NSLog(@"%@ is %@ years old", firstName, age);

	NSArray *apps = [[NSArray alloc] initWithObjects: @"AngryFowl", @"Lettertouch", @"Tweetrobot", nil];
	NSLog(@"%@", [apps objectAtIndex:1]);

	apps = [NSArray arrayWithObjects: @"AngryFowl", @"Lettertouch", @"Tweetrobot", @"Instacanvas", nil];
	NSLog(@"%@", [apps objectAtIndex:3]);
	

	NSDictionary *appRatings = [NSDictionary dictionaryWithObjectsAndKeys:
									[NSNumber numberWithInt:3], @"AngryFowl",
									[NSNumber numberWithInt:5], @"Lettertouch", nil];
	NSLog(@"Lettertouch has a rating of %@.", [appRatings objectForKey: @"AngryFowl"]);

    [pool release];
	return 0;
}