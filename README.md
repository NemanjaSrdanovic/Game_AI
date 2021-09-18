# Game_AI_Universitaet_Wien


## Project description


The aim of the project is to offer central concepts of software development like:<br/>
• Requirements analysis,<br/>
• Verification, validation of software,<br/>
• Software projects,<br/>
• Software development processes,<br/>
• Security in software development,<br/>
• Maintenance, further development, rollout<br/>
• Etc.<br/>


These goals are achieved by developing a software project in the form of a interactive game. The developer is provided with documentation in the form of 
a game idea and the network protocol which is to be used for communication between server and client.


#### Game description:

The basic idea of the game is that two AIs on the same map have to perform a similar task. The AI that fulfills this faster wins the game which
is turn-based (therefore each AI takes turns taking a game action). The map on which the game is played is not fixed, but is created by both AIs
independently and automatically at the start of the game (one half of the map each).



## Project implemetation


The implementation of the project is carried out in three subtasks / deadlines:



### Sub-task 1 - Analysis & Design 


The aim of this sub-task is to define functional and non-functional requirements 
from the game idea (documentation) provided. As well as recognizing and documenting relevant use cases. In addition, 
the first drafts of our future implementations (client and server) and use cases must be modeled as class diagrams and sequence diagrams.



### Sub-task 2 - Implementation of the client 


Based on the feedback received for subtask 1, the client model and its use cases are revised again. 
The client is programmed using these models the documentation  and best practices.

#### The client is developed as follows:

<details>
<summary>Network communication</summary>
<p> 
The client must be able to communicate with the network interface specified by the
course management. This creates a common basis so that two different clients, e.g.
in a tournament, can play against each other. The server serves as a middleman and
ensures compliance with the rules of the game. REST and the WebClient integrated
in Spring are used for this.
</p>
</details>


<details>
<summary>User interface</summary>
<p>
Visualize the course of the game, i.e. all information available about the game card, 
which, for example, has been transmitted by the server (e.g. terrain types and treasure
or castle positions) using the MVC pattern.It must also be clear where on the map the 
characters of both AIs are currently located in order to visualize their movements.
</p>
</details>


<details>
<summary>Map generation</summary>
<p>
Implementing an algorithm that generates the half of the map required during the start of
the game for the given card size while paying attention to the rules and restrictions
defined in the game idea.
</p>
</details>
	

<details>
<summary>Artificial intelligence</summary>
<p>
Develop an artificial intelligence that, based on the current state of the game,
calculates the next suitable actions (like movements of the game character).
</p>
</details>



#### During the development following quality features are taken into consideration:


<details>
<summary>Logging</summary>
<p>
Log the behavior of the implementation to a sufficient extent. This includes the occurrence of
errors, important states such as the start of a new game, the background to why a player's
behavior was recognized as a rule violation, etc.
</p>
</details>


<details>
<summary>Error handling</summary>
<p>
Identifyint places where self-defined exceptions can be used. Apply best practices and
create individual exception classes.
</p>
</details>


<details>
<summary>Unit Tests</summary>
<p>
Create an appropriate number of unit tests (data-driven tests, negative tests and tests that
implements mocking via Mockito). A test coverage of at least 60% must be achieved
</p>
</details>


### Sub-task 3 - Implementation of the server 


Based on the feedback received for subtask 1, the server model and its use cases are revised again. 
The server is programmed using these models the documentation and best practices.

#### The server is developed as follows:



<details>
<summary>Network communication</summary>
<p>
The server must be able to communicate with the client implemented in 
subtask 2 and the test client provided by the course via the specified network
interfaces(see network protocol). This means that the messages from the clients 
should be accepted, processed and answered by the server. 
</p>
</details>
	

<details>
<summary>Review business rules</summary>
<p>
Implement the necessary logic to check at least eight of the business rules 
described in the game idea and relevant for the first four endpoints.
</p>
</details>

	
<details>
<summary>Move & rules</summary>
<p>
This includes accepting, processing and answering the incoming movement requests of the
clients. It should also be possible to fulfill the goals described in the game idea in 
order to win a game in the correct way (moving, collecting treasure, visibility, etc.).
</p>
</details>


## Project technology


**Programming languages used**: Java <br/>
**Framework**: Spring Boot <br/>
**Securing the source code**: GIT  <br/>
**Build tool**: Gradle  <br/>
**Modeling Platform**: Visual Paradigm <br/>
**Programming interface**: RESTful API <br/>
