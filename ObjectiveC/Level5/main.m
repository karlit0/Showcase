#import <Foundation/Foundation.h>
#import "MrHiggie.h"
#import "ChargeableMrHiggie.h"
 
int main (int argc, const char * argv[]) {
 
	NSAutoreleasePool * pool = [[NSAutoreleasePool alloc] init];
		
	MrHiggie *talkingiPhone = [[MrHiggie alloc] init];
	[talkingiPhone setPhoneName:@"Mr. Higgie"];
	
	[talkingiPhone decreaseBatteryLife:[[NSNumber alloc] initWithInt:5]];
	
	MrHiggie *copy = [talkingiPhone copy];
	[copy reportBatteryLife];

	NSLog(@"\n");
	ChargeableMrHiggie *phone = [[ChargeableMrHiggie alloc] initWithBatteryLife:[[NSNumber alloc] initWithInt:50]];
	[phone setPhoneName:@"Chargeable Mr. Higgie"];
	[phone reportBatteryLife];
	
	ChargeableMrHiggie *copyPhone = [phone copy];
	[copyPhone increaseBatteryLife:[[NSNumber alloc] initWithInt:5]];
	[copyPhone reportBatteryLife];
	
	NSLog(@"The memory address for the original object:\t %p", phone);
	NSLog(@"The memory address for the copied object:\t\t %p", copyPhone);
	
    [pool release];
	return 0;
}