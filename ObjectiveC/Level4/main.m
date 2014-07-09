#import <Foundation/Foundation.h>
#import "MrHiggie.h"
 
int main (int argc, const char * argv[]) {
 
	NSAutoreleasePool * pool = [[NSAutoreleasePool alloc] init];
		
	MrHiggie *talkingiPhone = [[MrHiggie alloc] init];
	[talkingiPhone setPhoneName:@"Higgster"];
	[talkingiPhone setModelNumber:@"C3P0"];
	NSLog(@"%@", [talkingiPhone phoneName]);
	NSLog(@"%@", [talkingiPhone modelNumber]);
		
	[talkingiPhone speak];	
	
	NSLog(@"\n");
	NSLog(@"%@", [talkingiPhone speak2]);
	
	NSLog(@"\n");
	NSLog(@"%@", [talkingiPhone speak3:@"Hello"]);
	NSLog(@"%@", [talkingiPhone speak3:@"Nice weather we're having"]);
	NSLog(@"%@", [talkingiPhone speak3:@"Hello?"]);
	
	[talkingiPhone setBatteryLife:[[NSNumber alloc] initWithInt:100]];
	[talkingiPhone reportBatteryLife];
	
	[talkingiPhone decreaseBatteryLife:[[NSNumber alloc] initWithInt:5]];
	[talkingiPhone reportBatteryLife];
	
    [pool release];
	return 0;
}