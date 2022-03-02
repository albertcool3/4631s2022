#  Connecting UML CS Classes Topics with new Mobile Programming Techniques

# Introduction

Making programmers’ lives easier on Android platform has always been the intention of Google. Constantly, it brings out new tools and technologies to empower the programmers and ultimately empower the end users. A side effect is that there are so many different methods(programming styles, libraries, APIs etc)  to solve a particular task. As a newcomer, I was overwhelmed with all kinds of new and old information, therefore became frustrated initially. Once I decided to focus on one particular style and supporting libraries, the stress alleviated.  It appears that Google indeed made its tools so approachable that most people can become a productive developer by just following a few sets of online tutorials. Nonetheless, there is still uneasiness growing behind the newly gained “productivity” - I rarely know anything behind the scenes and in many cases, why things are designed to work in certain ways. The Covid-19 paper[3] demonstrated that even a team of developers can come up a novel tracking solutions quickly, their app may not work as expected in certain situations. The paper's authors can only make such discoveries with sound theory foundations and system internal knowledges.

As a student, one has a long road ahead to become an expert in any field if ever. To get closer to that elusive goal, I felt tying a new concept to what I already knew, making connections or comparisons would give me a better chance than studying the new concept standalone. Coroutine is such an example. It was an old concept but becomes a new buzz word. It can appear in even the most trivial Android project source code(most likely, as part of an import file for the so called MutableStateFlow). Related topics in Organization of Programming Lanuages[4], Operating Systems[6], and Computer Archecture class[1] can guide one on Android mobile programming. 


# Coroutine in OPL

In OPL(Organization of Programming Languages) class[4], iterator is studied as a programming language feature. Iterators (in Python, it is called generators) and coroutines are related concepts. Iterator is considered as a special case of coroutine. Coroutine is discussed in Chapter 6, 9 and 13 [4]. The definition of the coroutine, even is not contradict to what is explained in Android Coroutine 101 Tutorials[8] directly, caused more confusion for me. The video [8] explains that coroutine is implemented using “continuation”, while the textbook emphasizes that coroutine and continuation has similarity (both are closures, a closure is a code address and a referencing environment) and difference (continuation is a constant while coroutine is not). 

But the some concrete examples offered some clarity.

## Coroutine Example in Simula 
The screen saver[4, page 451-453] with file system checking function described in Simula language demonstrates coroutine with a transfer() call. Coroutine looks really simple at least in the era of Simula.

## Web Browser Example
Chapter 13[4] revisits coroutine concept in the context of concurrency and parallelism. Internals of web browser are interesting topics on their own. Two different implementation methods, one using threads, and the alternative, using dispatch event loops, are compared. With preemptive multithreaded programming, the UI will be updated within human perception threshold, less than a tenth of a second. Any lengthy processing must be done in a separate thread so the web browser will maintain responsiveness even on a single-core machine. 

Without preemptive thread support, a browser must divide any task longer than a tenth of a second into smaller pieces, between which the programmer saves states and let the code return to the dispatch loop to update UI in order to maintain a good interactive response. The dispatch loop making the management of tasks explicit and the control flow within task more complex, “like trying to enumerate a recursive set with iterator objects, only worse.”[4, page 631] For a programmer, life without thread is miserable. Thread means liberation: is “Like true iterators, a preemptive thread package turns the program “right side out”, making the management of tasks(threads) implicit and the control flow within threads explicit.”[4, page 631]

## Implementing Thread Using Coroutine
Later in chapter 13 [4], the topic of how to implement thread using coroutine is discussed. All seems well reasoned, but that might just add more confusion for myself. In particular, I know Android uses Linux kernel underneath, therefore already has native thread support, why “coroutine” is introduced as a new technology? The answer only becomes clear once I made a connection from Python's coroutine and the “High Performance” Webserver.

# Coroutine in a 1979 Thesis Paper[2]

## Coroutine vs Subroutine
Coroutines are “mutual subroutines”. They are “subroutines at the same level, each acting as if it were the master program when in fact there is no master program”. Parser calling the scanner to get the next token, and scanner also calling the parser to dispose a token is a classic example.

## Coroutine as Producer-Consumer Problem
The classic producer/consumer problem is also described as an coroutine example [2]. In particular, if the queue shared by producer and consumer (with length greater than 1), the producer and consumer may be able to run in parallel as it was discussed in OS class (the focus was synchronization). If the queue length is limited to 1 or if the coroutines call each other with explicit calls (for example, the transfer() call in the Simula example), then the coroutines runs in locked steps, one after another, instead of a true parallel fashion. 

# Concurrency vs Parallelism
Quote from OS[6] "on a system with a single computing core, concurrency merely means that the execution of the threads will be interleaved over time, because the processing core is capable of executing only one thread at a time."... "It is possible to have concurrency without parallelism."... "On such single core system, the CPU schedulers were designed to provide the illusion of parallelism by rapidly switching between processes, thereby allowing each process to make progress."

However, in OPL text[4], "In general, we use the word concurrent to characterize any system in which two or more tasks may be underway (at an unpredictable point in their execution) at the same time. Under this definition, coroutines are not concurrent, because at any given time, all but one of them is stopped at a well-know place."

The above paragraph conveys a different view from[7][5][6]

# Coroutines Are Everywhere

At this point, I realize that the word coroutine is only for conveying the essence of asynchronous or concurrent code. Its incarnations can be very different depending on eras and programming languages.

## The high performance web server
As the counter part of web browser, [1] and [6] describes that a popular/busy web server can draw web traffics/requests from many web browsers at the same time. Performance is at stake. The very first generation of web server fork a new process on demand whenever there is a http request comes in. The next generation improves performance by replacing processes with threads. Because the threads in a web server shares the same virtual memory mapping, context switches between the threads do not involve virtual memory page table, therefore cost less. The expenses related to creating and destroying thread can further be reduced by using a pool of threads on standby, preparing to handle incoming requests. The thread pools can be modelled as the multiple consumers of (work items), with a single producer of the work items by listening on the server socket interface.  But the ultimate high performance web servers remove the operating system threads all together and reduce context switch overhead to zero. They use the same technique as the dispatch loop version of the web browser (where the miserable programmer does that out of necessity when OS does not support thread). The dispatch loop has near zero context switch cost because there is no user-kernel mode context switch. A side effect of this approach is that, all the user mode threads (coroutines) only work on a single core concurrently. To take advantage of multicore hardware, the web server has to run multiple copies of itself.


## Python’s coroutine
Python's coroutine [5] (latest incarnation) sports the async and await keyword. The async keyword make the marked function returns a coroutine object. Coroutine objects runs in an event loop created by Python environment behind the scene. The loop will run the callbacks (await part of the function) when the async part of the function finishes. 

Python's coroutine is not to improve performance. Without coroutine, programmer has to use alternatives that involves callback functions. The async/await syntax makes the code look like sequential.

# Kotlin coroutine
Kotlin coroutine is similar to Python with the same goal to make the programmer's life easier. While, Python has one hidden event loop to poll the async function's status; Kotlin has a concept of thread pool(similiar to webserver). The coroutines (background work items) do not have to be confined to one particular thread. Therefore, Kotlin coroutine can be viewed as user mode, lighter weighted thread that co-operates by yielding control to other coroutines at certain “continuation”. The continuation (closure with reference envinroment) has lower cost than the thread context in the pre-emptive thread. Kotlin use both the lower cost version of user mode threads (coroutines) and the native pre-emptive os threads to make concurrent code look like sequential and can still perform. 

The Flow concept in Kotlin is exactly a producer-consumer pattern, and it is implemented in the coroutine library. The architecture guideline recommends separating the UI from data, which requires the observer pattern. Flow is the current recommended solution. A slightly older technology called "LiveData" is not as scalable as Flow.


# Summary
In the 1979 paper [2], "(coroutines) are often regarded as giving rise to poor program structure and inefficient program execution, and are not widely available in programming language." Now, coroutines are coming back as a new technology with sequential like program structure and better efficiency (comparing to create a working thread and callbacks).


# References

1. Randal E. Bryant, David R. O’Hallaron,  Computer Systems, A Programmer’s Perspective Second Edition, Prentice Hall, 2011
2. Christopher David Marlin, COROUTINES:A PROGRAMMING METHODOLOGY
A LANGUAGE DESIGN AND AN IMPLEMENTATION, 1979,University of Adelaide, https://digital.library.adelaide.edu.au/dspace/handle/2440/21027?mode=full
3. Philipp H. Kindt, Trinad Chakraborty, Samarjit Chakraborty, How Reliable Is Smartphone-Based Electronic Contact Tracing for COVID-19? Communications of the ACM, January 2022, Vol. 65 No. 1, Pages 56-67
4. Michael L. Scott, Programming Language Pragmatics, Fourth Edition, Morgan Kaufmann, 2016
5. Anthony Shaw, CPython Internals: Your Guide to the Python 3 Interpreter, Real Python (realpython.com), 2012–2020
6. ABRAHAM SILBERSCHATZ,PETER BAER GALVIN, GREG GAGNE, Operating System Concepts, Tenth Edition, Wiley, 2018
7. Venkat Subramaniam, Programming Kotlin, Creating Elegant, Expressive, and Performant JVM and Android Applications, The Pragmatic Bookshelf, 2019
8. Manuel VivoKotlin, Coroutines 101 - Android Conference Talks, (2020) https://www.youtube.com/watch?v=ZTDXo0-SKuU

