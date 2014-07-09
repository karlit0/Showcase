#import <Foundation/Foundation.h>

@interface MrHiggie	: NSObject <NSCopying> {
	NSString *_phoneName;
	NSNumber *_batteryLife;
}

-(MrHiggie *) initWithBatteryLife:(NSNumber *) batteryLife;
-(void) setPhoneName:(NSString *) name;
-(NSString *) phoneName;
-(void) reportBatteryLife;
-(void) decreaseBatteryLife:(NSNumber *) amount;

@end