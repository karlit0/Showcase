#import <Foundation/Foundation.h>
 
int main (int argc, const char * argv[]) {
 
	NSAutoreleasePool * pool = [[NSAutoreleasePool alloc] init];
		
	NSArray *foods = [[NSArray alloc] initWithObjects: @"tacos", @"burgers", nil];
	NSLog(@"%@", [foods description]);
	
	NSString *result = [foods description];
	NSLog(@"result = %@", result);
	
	NSString *city = @"Ice World";
	NSUInteger cityLength = [city length];
	NSLog(@"City has %lu characters", cityLength);
	
	NSNumber *higgiesAge = [[NSNumber alloc] initWithInt:6];
	NSNumber *phoneLives = [[NSNumber alloc] initWithInt:3];
	NSUInteger product = [higgiesAge unsignedIntegerValue] * [phoneLives unsignedIntegerValue];
	NSLog(@"Higgie is actually %lu years old.", product);
	
	NSString *firstName = @"Antonio";
	NSString *lastName = @"Stipanovic";
	NSString *fullName = [firstName stringByAppendingString: lastName];
	NSLog(@"%@", fullName);
	
	fullName = [[firstName stringByAppendingString: @" "] stringByAppendingString: lastName];
	NSLog(@"%@", fullName);
	
	NSString *replaced = [fullName stringByReplacingOccurrencesOfString:firstName
															withString: lastName];
	NSLog(@"%@", replaced);
	
	NSString *copy = [firstName copy];
	NSLog(@"%@ is a copy of %@", copy, firstName);
	
	copy = [[NSString alloc] initWithString:firstName];
	NSLog(@"%@ is a copy of %@", copy, firstName);
	
	fullName = [NSString stringWithFormat:@"%@ %@", firstName, lastName];
	NSLog(@"%@", fullName);
	
    [pool release];
	return 0;
}