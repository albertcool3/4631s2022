# Applying UML CS Concepts to Android Programming
Traditional GUI based programs (like those from COMP4) features an event loops; Mobile apps behave similiarly even though the main loop might be hidden in the framework/library. To maintain a responsive UI, any work that is "lengthy" has to be done outside the event loop, which implies that multi-threaded programming is a must. In Android programming environemnt, many different methods/APIs/libraries could be challenging to understand and best tools could be difficult to choose. In this paper, I will relate concepts I learned  in UML (from Arch, OPL, OS classes) to ones in Android programming such as coroutine, flows  etc..