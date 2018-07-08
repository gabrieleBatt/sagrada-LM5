######Game
Game holds the info
on the main structure of the 
game and of the single
rounds. 
Upon creations it gets the 
single actions from the 
Rules it contains and builds 
a list of them.
When the game is running 
it executes all the actions on
the list and, if only one player
remains online, it stops and
immediately ends the game.

######Rules:
holds the info on the single actions.

Since the single player mode
is different only in the actions
and not in the structure, 
to implement the mode is 
only required to make another
rules extension(once the 
Server and Client are modified 
to accept multiple game 
modes)