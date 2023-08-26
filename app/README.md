## Student Info

Victor Astinov, 20851407 vastinov

## Build Info

Windows 10 Home, Version 10.0.19045 Build 19045

Kotlin Version: 1.8.20, targeting JVM

java version "1.8.0_261"
Java(TM) SE Runtime Environment (build 1.8.0_261-b12)
Java HotSpot(TM) Client VM (build 25.261-b12, mixed mode, sharing)


## Additional Application Details
- App icon is custom as a for fun item not in the specification
- Movement is done as such. Move left by pressing A or left arrow, move right by pressing D or right arrow. Movement is stored in a state, so the movement keys do not need to be held down
- When an alien hits the user's ship, the game will end
- Bullet spawn rate and bullet limit increase with the levels, along with alien move speed and all bullet speed (including player bullets)
- High score is stored for the duration of the app
- Colliding bullets will destroy each other
- When switching levels, current score and lives stay the same. This is to see how long can a player last and how high of a score they can get. There was some ambiguity on whether this should be possible during a level on Piazza, so I enabled switching during levels
- Player fire rate is limited to 2 shots per second as per spec, this means that there is a 0.5-second delay after firing a shot before the next shot will be fired
- There is sound when the player fires or dies, aliens fire or die, and bullets colliding. There is no background noise or soundtrack as this can get distracting to the user, especially if they want to play music with another app
- There is an ending screen which displays score and highscore, pressing enter on this screen returns the user to the main screen where they can restart or quit. Jeff said this was a fine approach


## Resources

All images and sounds are sourced from the official SpaceInvaders website given in the assignment outline page

Sounds: http://www.classicgaming.cc/classics/space-invaders/sounds

Images: http://www.classicgaming.cc/classics/space-invaders/graphics

App Icon: http://www.classicgaming.cc/classics/space-invaders/icons-and-fonts


