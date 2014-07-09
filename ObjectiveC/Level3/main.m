#import <Foundation/Foundation.h>
#import "MrHiggie.h"
#import "DayOfWeek.h" 
 
int main (int argc, const char * argv[]) {
 
	NSAutoreleasePool * pool = [[NSAutoreleasePool alloc] init];
		
	BOOL mrHiggieIsMean = YES;
	if (mrHiggieIsMean)
		NSLog(@"Confirmed: he is super mean");
	
	MrHiggie *mrHiggie = [[MrHiggie alloc] initWithMean: YES];
	mrHiggieIsMean = [mrHiggie areYouMean];
	if (mrHiggieIsMean)
		NSLog(@"Confirmed: he is super mean");

	mrHiggie = [[MrHiggie alloc] initWithMeannessScale: 9];
	NSUInteger meannessScale = [mrHiggie meannessScale];
	if (meannessScale < 4)
		NSLog(@"Mr. Higgie is on the nice side");
	else if (meannessScale < 8)
		NSLog(@"Mr. Higgie is sorta nice but not really");
	else
		NSLog(@"Mr. Higgie is definitely mean");
		
		
	[mrHiggie setCurrentHat:@"Fedora"];
	NSString *hat = [mrHiggie currentHat];
	if ([hat isEqualToString:@"Sombrero"])
		NSLog(@"Ese es un muy buen sombrero");
	else if ([hat isEqualToString:@"Fedora"])
		NSLog(@"Mr. Higgie was an iPhone before there was iPhone");
	else if ([hat isEqualToString:@"AstrounatHelmet"])
		NSLog(@"Mr. Higgie believes himself to be an astronaut");
	else
		NSLog(@"Mr. Higgie is currently hatless");
	
	
	NSInteger day = 6;
	switch (day) {
		case 1:
		case 2:
		case 3:
		case 4: {
			[mrHiggie setCurrentHat:@"Fedora"];
			break;
		}
		case 5: {
			[mrHiggie setCurrentHat:@"Sombrero"];
			break;
		}
		case 6:			
		case 7: {
			[mrHiggie setCurrentHat:@"AstronautHelmet"];
			break;
		}
	}
	NSLog(@"Mr. Higgie is wearing: %@", [mrHiggie currentHat]);
		
	DayOfWeek enumDay = DayOfWeekTuesday;
	switch (enumDay) {
		case DayOfWeekMonday:
		case DayOfWeekTuesday:
		case DayOfWeekWednesday:
		case DayOfWeekThursday: {
			[mrHiggie setCurrentHat:@"Fedora"];
			break;
		}
		case DayOfWeekFriday: {
			[mrHiggie setCurrentHat:@"Sombrero"];
			break;
		}
		case DayOfWeekSaturday:
		case DayOfWeekSunday: {
			[mrHiggie setCurrentHat:@"AstronautHelmet"];
			break;
		}
	}
	NSLog(@"Mr. Higgie is wearing: %@", [mrHiggie currentHat]);
	
	NSArray *newHats = [[NSArray alloc] initWithObjects: @"Cowboy", @"Conductor", @"Baseball", nil];
	for (NSString *hat in newHats) {
		NSLog(@"Trying on new %@ hat", hat);
		
		if ([mrHiggie tryOnHat:hat])
			NSLog(@"Mr. Higgie loves it");
		else
			NSLog(@"Mr. Higgie hates it");
	}
	
	NSDictionary *funnyWords = [NSDictionary dictionaryWithObjectsAndKeys: 
									@"pleasure derived by someone from another person's misfortune.", @"Schadenfreude",
									@"consisting of or combining two or more separable aspects or qualities", @"Portmanteau",
									@"second to the last", @"Penultimate", nil];
	for (NSString *word in funnyWords) {
		NSString *definition = [funnyWords objectForKey:word];
		NSLog(@"%@ is defined as %@", word, definition);
	}
	
	NSDictionary *newHatsDict = [NSDictionary dictionaryWithObjectsAndKeys:
									@"White", @"Cowboy",
									@"Brown", @"Conductor",
									@"Red", @"Baseball", nil];
	for (NSString *hat in newHatsDict) {
		
		NSString *color = [newHatsDict objectForKey:hat];
		
		NSLog(@"Trying on new %@ %@ hat", color, hat);
		
		if ([mrHiggie tryOnHat:hat withColor:color])
			NSLog(@"Mr. Higgie loves it");
		else
			NSLog(@"Mr. Higgie hates it");
	}
	
	/*
	void (^myFirstBlock)(void) = ^{
		NSLog(@"Hello from inside the block");
	};	
	myFirstBlock();
	*/
	
	/*
	void (^mySecondBlock)(NSString *str) = ^{
		NSLog(@"%@", str);
	};
	mySecondBlock(@"Hello");
	mySecondBlock(@"World");
	*/
	
	/*
	newHats = [[NSArray alloc] initWithObjects: @"Cowboy", @"Conductor", @"Baseball",
												@"Beanie", @"Beret", @"Fez", nil];
	void (^enumeratingBlock)(NSString *, NSUInteger, BOOL *) =
		^(NSString *word, NSUInteger index, BOOL *stop) {
			NSLog(@"%@ is a funny word", word);
		}
	[newHats enumerateObjectsUsingBlock:enumeratingBlock];
	*/
	
    [pool release];
	return 0;
}