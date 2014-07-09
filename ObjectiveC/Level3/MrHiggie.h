#import <Foundation/Foundation.h>

@interface MrHiggie : NSObject {
	BOOL _mean;
	NSUInteger _meannessScale;
	NSString *_hat;
}

- (id) initWithMean:(BOOL) mean;
- (id) initWithMeannessScale:(NSUInteger) meannessScale;
- (BOOL) areYouMean;
- (NSUInteger) meannessScale;
- (void) setCurrentHat:(NSString *) hat;
- (NSString *) currentHat;
- (BOOL) tryOnHat:(NSString *) hat;
- (BOOL) tryOnHat:(NSString *) hat withColor:(NSString *) color;

@end