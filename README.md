# Game_AI_Universitaet_Wien


The aim of the project is to offer central concepts of software development like:
• Requirements analysis,
• Verification, validation of software,
• Software projects,
• Software development processes,
• Security in software development,
• Maintenance, further development, rollout
• Etc.


These goals are achieved by developing a software project in the form of a interactive game. The developer is provided with documentation in the form of 
a game idea and the network protocol which is to be used for communication between server and client.

The implementation of the project is carried out in three subtasks / deadlines:

Sub-task 1 - Analysis & Design: The aim of this sub-task is to define functional and non-functional requirements 
from the game idea (documentation) provided. As well as recognizing and documenting relevant use cases. In addition, 
the first drafts of our future implementations (client and server) and use cases must be modeled as class diagrams and sequence diagrams.


Sub-task 2 - Implementation of the client: Based on the feedback received for subtask 1, the client model and its use cases are revised again. 
The client is programmed using these models and best practices.

The client is developed as follows:

		• Network communication: The client must be able to communicate with the network interface specified by the course management. 
		    			 This creates a common basis so that two different clients, e.g. in a tournament, can play against each 
					 other. The server serves as a middleman and ensures compliance with the rules of the game. REST and the 
					 WebClient integrated in Spring are used for this.

		• User interface: Visualize the course of the game, i.e. all information available about the game card, which, for example, has been 
				  transmitted by the server (e.g. terrain types and treasure or castle positions) using the MVC pattern.It must also 
				  be clear where on the map the characters of both AIs are currently located in order to visualize their movements.

		• Map generation: Implementing an algorithm that generates the half of the map required during the start of the game for the given card 
				   size while paying attention to the rules and restrictions defined in the game idea. 

		• Artificial intelligence: Develop an artificial intelligence that, based on the current state of the game, calculates the next 
					   suitable actions (like movements of the game character).

During the development following quality features are taken into consideration: 

		• Logging: Log the behavior of the implementation to a sufficient extent. This includes the occurrence of errors, important states such as 
			   the start of a new game, the background to why a player's behavior was recognized as a rule violation, etc.

		• Error handling: Identifyint places where self-defined exceptions can be used. Apply best practices and create individual exception classes.

		• Unit Tests: Create an appropriate number of unit tests (data-driven tests, negative tests and tests that implements mocking via Mockito). 
			      A test coverage of at least 60% must be achieved



Game description:

The basic idea of the game is that two AIs on the same map have to perform a similar task. The AI that fulfills this faster wins the game which
is turn-based (therefore each AI takes turns taking a game action). The map on which the game is played is not fixed, but is created by both AIs
independently and automatically at the start of the game (one half of the map each).


Programming languages used: Java
Framework: Spring Boot
Securing the source code: GIT 
Build tool:Gradle 
Modeling Platform: Visual Paradigm
Programming interface: RESTful API
