#import "MrHiggie.h"

@implementation MrHiggie


-(NSString *)phoneName;
{
	return _phoneName;
}
-(void)setPhoneName:(NSString *) name;
{
	_phoneName = name;
}
-(NSString *)modelNumber;
{
	return _modelNumber;
}
-(void)setModelNumber:(NSString *) modelNumber;
{
	_modelNumber = modelNumber;
}
-(void) speak;
{
	NSLog(@"%@ says Hello There!", _phoneName);
}
-(NSString *) speak2;
{
	NSString *message = [NSString stringWithFormat:@"%@ says Hello Thar!", _phoneName];
	return message;
}
-(NSString *) speak3:(NSString *) greeting;
{
	NSString *message = [NSString stringWithFormat:@"%@ says %@", _phoneName, greeting];
	return message;
}
-(void) setBatteryLife:(NSNumber *) batteryLife;
{
	_batteryLife = batteryLife;
}
-(void) reportBatteryLife;
{
	NSLog(@"Battery life: %@", _batteryLife);
}
-(void) decreaseBatteryLife:(NSNumber *) amount;
{
	_batteryLife = [[NSNumber alloc] initWithInt:([_batteryLife intValue] - [amount intValue])];
}

@end