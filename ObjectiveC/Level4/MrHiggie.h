#import <Foundation/Foundation.h>

@interface MrHiggie	: NSObject {
	NSString *_phoneName;
	NSString *_modelNumber;
	NSNumber *_batteryLife;
}

-(void) setPhoneName:(NSString *) name;
-(NSString *) phoneName;
-(void) setModelNumber:(NSString *) modelNumber;
-(NSString *) modelNumber;
-(void) speak;
-(NSString *) speak2;
-(NSString *) speak3:(NSString *) greeting;
-(void) setBatteryLife:(NSNumber *) batteryLife;
-(void) reportBatteryLife;
-(void) decreaseBatteryLife:(NSNumber *) amount;

@end