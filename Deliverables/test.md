### Test
- server
    - model
        - objective
            - AreaPublicObjectiveTest
                - Scoring column color objective :heavy_check_mark:
                
                    *Checks if the score (achieved every time a column is filled with 4 different colours) is set correctly and at the right time*
                - Scoring diagonal color objective :heavy_check_mark:
                    
                    *Checks if the score (due to the count of diagonally adjacent dice with the same colour) is set correctly and at the right time*
                - Illegal creation of objective :heavy_check_mark:   
                
                    *Checks if the exception (to throw every time the multiplicity doesn't match with the area condition in class Target) is thrown only under those conditions and at the right time*
            - ColorPrivateObjectiveTest
                - Scoring cyan private objective :heavy_check_mark:
                
                    *Checks if the score due to the sum of dice value having the color of the private objective is right anytime*
            - SetPublicObjectiveTest
                - Scoring set of 3 and 5 :heavy_check_mark:
                
                    *Checks if the sum of couples (made of 3 and 5) is right any time*
                - Scoring color set objective :heavy_check_mark:
                    
                    *Check if the sum of points due to the color set is right any time*
                    
        - table
            - dice
                - DiceBagTest 
                    - Create and empty the bag :heavy_check_mark:
                    
                        *Checks if the diceBag size is 90 when created, checkDice remove 90 elements and checks if it's empty*
                    - Draw multiple times multiple dice :heavy_check_mark:
                        *Draws n dice, checks if diceBag doesn't contain those dice anymore, checks if diceBag size has been updated.
                        Iterates n times. Checks if BagEmptyException is thrown when asked to draw a number of dice superior to actual diceBag size*
                    - Place one extra die in the bag :heavy_check_mark:
                    
                        *Create a diceBag and check if contains 90 elements. Add one element, checks again the dimension, checks if diceBag actually contains the new insertion.
                         Checks if BagEmptyException is thrown if is the number of dice to create is superior to the actual size of the diceBag*
                    - Testing memento :heavy_check_mark:
                    
                        *Check if the "memory saver" has been updated after every step*
                - DieTest
                    - Set die numeric value :heavy_check_mark:
                        
                        *Checks if every admissible die value (1 to 6)is set correctly, checks if IllegalArgumentExceptions is thrown if the value requested is not admissible*
                    
                    - Get die id :heavy_check_mark:
                    
                        *Checks if the die identifier is correctly return by the method*
                    - Confront dice :heavy_check_mark:
                       
            - glassWindow
                - CellTest 
                    - Place die in cell, various restrictions :heavy_check_mark:
                    
                        *Checks every condition to place a die on a cell: no restriction, restriction ignored, restriction respected (both value and colour)*
                    - Check empty and full cell :heavy_check_mark:
                    
                        *Check if a cell just created is empty, fill it, checks if it's occupied*
                    - Place and get die :heavy_check_mark:
                        
                        *Checks if EmpyCellException is thrown every time getDie() is called on an empty cell.
                        Place a die in the cell, checks if getDie() returns the right die*
                    - Check allowed dice considering several restrictions :heavy_check_mark:
                    
                        *Creates 2 cells with colour and value restriction and 2 dice (matching the restriction), checks all placement combination*
                - GlassWindowTest
                    - Find cell by die inside :heavy_check_mark:
                    
                        *Checks if getCellByDie() (given a die Id) returns the cell where the die is actually placed.
                        Checks if NoSuchElementException is thrown every time the glassWindow doesn't contain the requested die.*
                    - Get row in which a cell is :heavy_check_mark:
                    
                        *Checks if the returned row contains the requested cell*
                    - Get column in which a cell is :heavy_check_mark:
                    
                        *Checks if the returned column contains the requested cell*
                    - Check for surrounding dice :heavy_check_mark:
                    
                        *Given a cell with no dice around, checks if hasSurroundingDice() answer correctly, repeats with a cell having dice around*
                    - Check if a cell has dice adjacent and similar to a die :heavy_check_mark:
                    
                        *Checks if hasAdjacentSimilar responds correctly every condition*
                    - Available cells for a die in glassWindow :heavy_check_mark:
                    
                        *Creates a dashboard and checks if the list returned by availableCell has the expected size and contains elements expected only. Repeats ignoring restrictions*
            - PlayerTest
                - Player nickname :heavy_check_mark:
                
                    *Checks if method getNickname returns the nickname of the expected player*
                - Player tokens :heavy_check_mark:
                
                    *Sets, for each player, a number of tokens using setTokens(). Checks if getTokens() returns the number of tokens set, for each player*
                - Testing memento :heavy_check_mark:
                
                    *Checks if memento is able to restore the right glassWindow state*
               
            - PoolTest 
                - Add dice to pool :heavy_check_mark:
                
                    *Creates a Set of dice to add to the pool through setDice(), checks if pool contains the right elements.
                    Add another die through addDie(), checks if pool still contains all the right dice.*
                - Remove die from pool :heavy_check_mark:
                    
                    *Adds a set of dice to the pool, removes a die through takeDie().
                     Checks if pool doesn't contain the die anymore, checks if pool still contains all other dice*
                - Roll all dice in pool :heavy_check_mark:
                
                     *Checks if pool contains all alone dice after rolling them*
                - Testing memento :heavy_check_mark:
                
                    *Checks if memento is able to restore the right pool state*
            - RoundTrackTest 
                - End round adding dice :heavy_check_mark:
                
                    *Checks if dice left in the pool at the end of the round are correctly stored in the roundTrack when endRound() is called.
                    Checks if EndGameException is thrown every time endRound is invoked on a round superior to 10*
                - Remove a die from roundTrack placing another in its place :heavy_check_mark:
                
                    *Checks if method switchDie() called on the roundTrack doesn't contain anymore the die to change and contains the one it has been changed with.
                    Checks if roundTrack still contains all alone other dice*
                - Testing memento :heavy_check_mark:  
                
                    *Checks if memento is able to restore the right roundTrack state*     
            - TableTest
                - Get players in table :heavy_check_mark:
                
                    *Checks if all alone players of a specific table are returned by get.Players()*
                - Iterate players :heavy_check_mark:
                
                    *Checks if the iterator moves correctly through players' order, both in reverse and directly*
                - Get players by name :heavy_check_mark:
                
                    *Checks if getPlayer(playerName) returns the expected player of the table.
                    Checks if NoSuchElementException is thrown if the player cannot be found on that table*
        - tool
            - ToolTest
                - Get tool name :heavy_check_mark:
                
                    *Creates a tool and checks if getName() returns the expected name*    
                - Get tool action :heavy_check_mark:
                
                    *Creates a tool and checks if getActionCommand returns a list containing all alone actionCommand of that tool*
                - Use tool :heavy_check_mark:
                
                    *Check if a tool has been used, through setUsed() method, in different conditions*
    - controller
        - rules
            - DefaultRulesTest 
                - Give window and token test :heavy_check_mark:
                
                    *Checks if NoSuchElementException is thrown if method giveTokens() is called when players don't have a glassWindow yet.
                    Distribute a pair of glassWindows to each player, the fake player choose between the 2 glassWindow,
                    checks if everyPlayer has a glassWindow*
                - Give private objective :heavy_check_mark:
                        
                    *Distribute a private objective to every player, checks if every player has a private objective*
                - Give public objectives :heavy_check_mark:
                    
                    *Distribute 3 public objectives to every game, checks if every game has 3 public objective*
                - Testing round actions :heavy_check_mark:
                    
                    *Checks if pool size is 0 before calling getSetRoundAction, checks if pool size is 5 after getSetRoundAction.
                    Calls getEndRoundAction and checks if pool size is back to zero, check if all alone those 5 dice have been moved to the roundTrack*
                - Testing end game action :heavy_check_mark:
                
                    *Plays a game with two players, calls getEndGameActionScreen, checks if ranking is set right for each player,
                    set a communication channel offline, now checks if the ranking is related to the online player only.*
                    
            - TurnActionCommandTest
                 - Testing Turn :heavy_check_mark:
                    
                     *Checks after every turn played if the number of dice in the pool is in the expected range.
                     Checks after every turn played if the number of cell occupied in players glassWindow is in the expevvted range*
            - ToolActionsTest
                - Test set :heavy_check_mark:
                
                    *Checks if the tool that allows to increase or decrease die value is working correctly: when tool is used checks if actual value of the die is different from the previous one;
                    checks if the new value is among those expected*
                - Test random :heavy_check_mark:
                
                    *Checks if the tool that allows to re-roll a die is working correctly: given a List of identifiable to choose among, checks if the die re-rolled is among those identifiable in the list*
                - Test select :heavy_check_mark:
                
                    *Check if the selection of a die in the pool is correct: checks if that die, while selected, is included in game.getMap(). It's the equivalent of holding a die in the hand when chosen.*
                - Test move :heavy_check_mark:
                
                    *Checks after move action if the cell where die was is not occupied. Checks if the size of the list that contains all dice on the glassWindow is the same as before*
                - Testing swap from DiceBag :heavy_check_mark:
                
                    *Checks if game.getMaps() contains both dice to switch. Checks if BagEmptyExceptions is thrown when there is only one die left in the diceBag*
                - Testing swap from Pool :heavy_check_mark:
                
                    *Checks if game.getMaps() contains both dice to switch. Checks if pool contains the new die*
                - Testing swap from RoundTrack :heavy_check_mark:
                
                    *Checks if game.getMaps() contains both dice to switch. Checks if roundTrack contains the new die*
        - deck 
            - PrivateObjectiveDeckTest 
                - Draw 4 PrivateObjective :heavy_check_mark:
                    
                    *Checks if privateObjectiveDeck draws all alone 4 private objective*
                - Draw too many PrivateObjective :heavy_check_mark:
                
                    *Checks if DeckTooSmallException is thrown when privateObjectiveDeck is asked to draw an unexpected number of private objectives*
            - PublicObjectiveDeckTest 
                - Draw 4 PublicObjective :heavy_check_mark:
                
                    *Checks if privateObjectiveDeck draws all alone 4 public objective*
                - Draw too many PublicObjective :heavy_check_mark:
                
                    *Checks if DeckTooSmallException is thrown when publicObjectiveDeck is asked to draw an unexpected number of public objectives*
                - Target parsing control :heavy_check_mark:
                
                    *Checks if publicObjectiveDeck can actually create the card of an area public objective. Checks if the score is correct*
                - Set parsing control :heavy_check_mark:
                
                    *Checks if publicObjectiveDeck can actually create the card of a set public objective. Checks if the score is correct*
            - GlassWindowDeckTest
                - Draw 4 glassWindows :heavy_check_mark:
                    
                    *Checks if glassWindowDeck draw all alone 2 glassWindow per player*
                - Draw too many glassWindows :heavy_check_mark:
                
                    *Checks if DeckTooSmallException is thrown when glassWindowDeck is asked to draw an unexpected number of glassWindows*
                - Parsing control :heavy_check_mark:
                
                    *Checks if the glassWindow Industria is created correctly with all alone the expected restrictions*
            - ToolDeckTest
                - Draw tool card :heavy_check_mark:
                    
                    *Checks for each tool if the size of the action command list is correct*
                                        
        - UserDatabaseTest
            - New user and authentication :heavy_check_mark:
            
                *Checks if the database stores correctly player's username and password.
                Checks if IllegalArgumentException if a player tries to register himself with a username already taken.*
        - LobbyTest
            - Add commChannel to lobby and make game start :heavy_check_mark:
            
                *Adds 4 communication channel to the lobby, game starts, checks if the lobby is now empty.
                Adds only 1 communication channel, checks if the lobby contains it*
        - channels
            - SocketCommunicationChannelTest
                - Update test :heavy_check_mark:
                    
                    *Checks if strings sent through socketCommunicationChannel are equals to the expected*
 

- shared
    - identifiables
        - StdIdTest
            - Finding StdId :heavy_check_mark:
            
                *Checks if NoSuchElementException is thrown when a not valid id is asked to sdtID*
    - MessageTest
        - Convert message
            
            *Check if convertMessage(), through decodeMessage(), converts a message from client to the one server knows*
        - Convert name
        
            *Checks if convertName() converts a string arriving from client to the format expected by server*
  
- client
    - view
        - EndGameInfoTest
            - End game info creation :heavy_check_mark:
            
                *Checks if server and client have the same ranking*
        - LoginInfoTest
            - Login info creation :heavy_check_mark:  
                
                *Checks if server and client have the same login information* 
            