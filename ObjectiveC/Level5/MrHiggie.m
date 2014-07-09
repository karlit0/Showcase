#import "MrHiggie.h"

@implementation MrHiggie


-(MrHiggie *) init;
{
	_batteryLife = [[NSNumber alloc] initWithInt:100];
	return [super init];
}
-(MrHiggie *) initWithBatteryLife:(NSNumber *) batteryLife;
{
	_batteryLife = batteryLife;
	return [super init];
}
-(id) copyWithZone:(NSZone *) zone;
{
	id copy = [[[self class] allocWithZone:zone] initWithBatteryLife:_batteryLife];
	[copy setPhoneName:[NSString stringWithFormat:@"Copy of %@", _phoneName]];
	return copy;
}
-(NSString *)phoneName;
{
	return _phoneName;
}
-(void)setPhoneName:(NSString *) name;
{
	_phoneName = name;
}
-(void) reportBatteryLife;
{
	if (_phoneName)
		NSLog(@"%@'s battery life: %@", _phoneName, _batteryLife);
	else
		NSLog(@"%@'s battery life: %@", self, _batteryLife);
}
-(void) decreaseBatteryLife:(NSNumber *) amount;
{
	_batteryLife = [[NSNumber alloc] initWithInt:([_batteryLife intValue] - [amount intValue])];
}

@end