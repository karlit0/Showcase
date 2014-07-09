#import "ChargeableMrHiggie.h"
#import <Foundation/Foundation.h>

@implementation ChargeableMrHiggie
-(void) increaseBatteryLife:(NSNumber *) amount;
{
	_batteryLife = [[NSNumber alloc] initWithInt:([_batteryLife intValue] + [amount intValue])];
}
@end