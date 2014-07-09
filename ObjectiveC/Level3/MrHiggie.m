#import "MrHiggie.h"

@implementation MrHiggie

- (id) initWithMean:(BOOL) mean;
{
	_mean = mean;
	return [super init];
}
- (id) initWithMeannessScale:(NSUInteger) meannessScale;
{
	_meannessScale = meannessScale;
	return [super init];
}


- (BOOL) areYouMean;
{
	return _mean;
}
- (NSUInteger) meannessScale;
{
	return _meannessScale;
}
- (void) setCurrentHat:(NSString *) hat;
{
	_hat = hat;
}
- (NSString *) currentHat;
{
	return _hat;
}
- (BOOL) tryOnHat:(NSString *) hat;
{
	if ([hat isEqualToString:@"Cowboy"] || [hat isEqualToString:@"Baseball"])
		return YES;
	else
		return NO;
}
- (BOOL) tryOnHat:(NSString *) hat withColor:(NSString *) color;
{
	if ([hat isEqualToString:@"Cowboy"] && [color isEqualToString:@"White"])
		return YES;
	else
		return NO;
}

@end