
# General design
* Discrete-event simulator with exclusive-state state machine engine.
* Generic game context and game states.
* The specific parts of the Geometry game programmed using modular parts of the above engine.
* Each screen and its functionality is modelled as a State, and these States can respond to entry and to received string events from outside the engine.
* States are not symbolically coupled together, only by State object references set by the game init code.
* Random shape generation is based on first choosing a point count, then checking in these random points constitute a convex shape.
* If one keeps re-choosing the point-count every time a combination fails, I think this has a bias towards simpler shapes (like triangles and 4-point shapes). I observed this whilst playing with the random shape generator, until I changed the code to decide on a random point count first.
* Note the use of tailrec to instruct compiler to use Tail Call Optimisation for this recursive function.
* Game engine IO is disconnected from actual console IO, which allows for unit-testing the game without requiring dealing with actual console IO or even with the console streams at all. Input string events and output text can be routed from anywhere, like from a network peer.
* I made a little program (FormatPointListForExcelCells) which transforms the point list output of the game into lines of tab-separated XY values, perfect for plotting in Google Sheets. This allows me to visualise the random shapes.

# Assumptions
* I've filled in a few missing rules not in your md file about handling bad input.
* I added some rules around convex shapes, like:
  * No straight junctions/angles.
  * No points on top of existing points.
* I've favoured recalculating data over accumulating calculated state. For this size of application the cost of recalculation doesn't matter, and makes for more functional and concise code. But this assumption doesn't stand at Internet-scale data volumes.

# Build
* This is a Kotlin program built using Gradle.
* Uses the Shadow plugin to create a minimised shaded jar.
* Kotlin 1.7 on Java 14, developed and tested in IntelliJ IDEA on Mac.
* I've been careful to use platform-independent code wherever I can and should, but this is not tested on Windows. Especially string handling for LF vs CR-LF.

# Hard parts
* The geometry. Creating a convex shape is quite subtle, there's quite a few rules to make (and find as you realise what more you need to check for).
* I initially tried to find whether a shape is convex by looking at the angles being all in the same direction. I was happy with this until I inspected some of my randomly generated shapes, which showed compliant lines that exhibited line crossing. I thought by detecting line crossing I could detect non-convexness. But line crossing detection is hard to implement without solving linear algebra  ... something I didn't want to take on.
* Then I progressed to checking all the angles that a shape has, to see that they add up to 360 (within minor tolerance due to Double approximation). This, plus checking that no junction is a straight junction (either 0 or 180 degrees) can be used to confirm a shape as convex.

# Test coverage
* Reasonable amount of geometry tests are covered.
* Some basic game use cases are tested.
* Not testing most of the pieces of the game engine, for they are tested when the game runs in a unit test.

# My favourite parts / parts that i'm proud of
* The game engine and the statechart execution nature to model the UX. My undergraduate degree dissertation featured modelling with statecharts, so i'm very comfortable modelling in this way.
* This also gives a very maintainable / changeable / extensible design to the program. Can easily add more behavior later.
* The engine modular design wasn't just for extensibility, it was actually the easiest way for me to model your requirements.
* I'm happy with my geometry functions, now that i've got my approach correct.
* Usage of functional programming and minimal use of variable state. I'm an FP fan and apply this ideology everywhere that I can (within reason).
* SOLID principles, especially keeping classes having one responsibility.

# To build and run
1. Expand the zip file.
2. Change to the directory with the `gradlew` file.
3. Execute in bash, to build a shaded jar file:

`./gradlew clean build`

4. Execute in bash to run the game:

`java -jar ./build/libs/gic-geometry-1.0-SNAPSHOT-all.jar`

# How could be better?
* Use BigDecimal - would make everything more accurate, but at expense of space and time.
* More tests, but unfortunately i'm out of time. I've written tests for the geometry functions and hand-tested the application a lot.
