Socket Protocol
--
Italics lines are either optional or used for possible additional features. \
All messages are encoded using standard JSON format (key/value). \
Except for the login phase, whether server sends a list of possible choice/message 
to client, it expects client to send a message/choice among those provided right before. \
Whether server receives a not expected message from client, server suddenly disconnects that client. \
Therefore the message flow between client and server is the following:


 ```mermaid
 +----------+                 +----------+
 |  CLIENT  |                 |  SERVER  |
 +----------+                 +----------+
      |                             |
      |                             |     
      |       option1, option2, ... |    
      |<----------------------------|
      |                             |  /--------------------------\
      |                             |--| client choses an option  |
      |    option 1                 |  | among those given        |       
      |---------------------------->|  \--------------------------/ 
      |                             |
      
                                  
      |    option1, option2, option3|    
      |<----------------------------|
      |                             |  /--------------------------\
      |   option4                   |--| client sends a message   |
      |---------------------------->|  | server does not expect   |       
      |              disconnection  |  \--------------------------/ 
      |<----------------------------|      
      |--------------X--------------|    
```
The login phase expects the client to send a message containing
the login header, nickname, password and the game mode: GUI or CLI.
Server has to answer with the result of the connection attempt.

- Login messages:                           
    - client:{       
        "header":"login",                   \
        "nickname":"\<nickname>",           \
        "password":"\<password>",           \
        <i>"gameMode":"\<gameMode>"</i> 
        }
    - server:{                              \
        "header":"login",                   \
        "result":"\<result>"
        }
        
```mermaid
 +----------+                 +----------+
 |  CLIENT  |                 |  SERVER  |
 +----------+                 +----------+
      |                             |
      |                             |     
      |       login message         |    
      |---------------------------->|
      |                             |  /-----------------------------\
      |                             |--| Server receives client login| 
      |                             |  | data, checks login-password |
      |    login result             |  | match, allow/denie login    |
      |<----------------------------|  \-----------------------------/ 
      |                             |
      
```
As login is complete, client receives a collection of windows 
to choose from and answers with the window chosen.

- Window choice messages:                   \
    - server:{                              \
        "header":"chooseWindow",            \
        "glassWindow":\[\<windowIdList>] 
        }

    - client:{                              \
        "header":"chooseWindow",            \
        "glassWindow": \<windowId>
        }

```mermaid
 +----------+                 +----------+
 |  CLIENT  |                 |  SERVER  |
 +----------+                 +----------+
      |                             |   
      |             windows list    |    
      |<----------------------------|
      |                             |  /--------------------------\
      |                             |--| Client choses among two  |
      |    window chosen            |  | pairs of windows         |
      |---------------------------->|  \--------------------------/ 
      |                             |
```
Many time, during game, client will be asked by server to choose among 
a list of ids of selectable objects on table. A -container- is also 
provided: it represents the actual container of the object sent.

- Select object messages:
    - server:{ \
        "header":"selectObject",            \
        "container": \<container>,          \
        "option": \[\<objectIdList>]
        }

    - client:{
        "header": "selectObject",           \ 
        "option": \<objectChosen>
        }
 ```mermaid
 +----------+                 +----------+
 |  CLIENT  |                 |  SERVER  |
 +----------+                 +----------+
      |                             |   
      |             Object ids list |    
      |<----------------------------|
      |                             |  /-----------------------------\
      |                             |--|Client chose among a list of |
      | Object id chosen            |  |object ids                   |
      |---------------------------->|  \-----------------------------/
      |                             |
```

Many times, during game, client will be asked by server to choose 
among a list of options (not representing real objects on table).

- Select from messages:
    - server:{                              \
        "header": "selectFrom",             \
        "message": "\<message>",            \
        "option": \[\<optionsList>]           \
    }
    - client:{                              \
        "header": "selectFrom",
        "option": "\<optionChosen>"         \
    }
 ```mermaid
 +----------+                 +----------+
 |  CLIENT  |                 |  SERVER  |
 +----------+                 +----------+
      |                             |   
      |                Options list |    
      |<----------------------------|
      |                             |  /-----------------------------\
      |                             |--|Client chose among a list of |
      | Option chosen               |  |options                      |
      |---------------------------->|  \-----------------------------/
      |                             |
```

During game, every time server needs to update view, an update table
message is sent. It contains the object to update. When game ends, server 
sends in broadcast an update table message containing the leader board 
(pair of nickname-score).

- Update table messages:
    - server:{                              \
        "header":"update",                  \
        <i>"pool": \[\<diceIdList>],            \
        "roundTrack": \[\<round:diceId List>],    (r1:1R1, r2:3C4 ,...)       \
        "tool": \[\<tool-used List>],    \
        "pubObj": \[\<pubObj List>],    \
        "player": \[\<nicknameList>]</i>
    }
    
    - server:{                                
        "header": "endGame",                    \
        "leaderBoard: \[\<nickname:score List>] 
        }
```mermaid
 +----------+                 +----------+
 |  CLIENT  |                 |  SERVER  |
 +----------+                 +----------+
      |                             |   
      |        Update table message |    
      |<----------------------------|  /-----------------------------\
      |                             |--|Object that has to be changed|
      |                             |  |on table                     |
                                       \-----------------------------/
      |            End game message |    
      |<----------------------------|  /------------------------------\
      |                             |--|Message containing game result|
      |                             |  \------------------------------/
                                                                             
```
Message sent by server when a change occurs in player's number of token 
or glass window.
  
- Update player messages:
    - server:{                              \
        "header":"updatePlayer",           \
        "player": \"\<player>",            \
        "connection": \"\<connectionStatus>", \
        <i>"prvObj": \[\<prvObjList>],    \
        "token": \<token>,                \
        "glassWindow": \[\<glassWindow, diceList>]</i>
        }
```mermaid
 +----------+                 +----------+
 |  CLIENT  |                 |  SERVER  |
 +----------+                 +----------+
      |                             |   
      |       Update player message |    
      |<----------------------------|  /-----------------------------\
      |                             |--|Player's attribute that has  |
      |                             |  |be updated                   |
                                       \-----------------------------/
```

Message sent by server to communicate at client any other information 
(game starting, error messages, opponent disconnection...).   
- Message to user:
    - server:{                  \
        "header": "send",           \
        "message": "\<message>"
        }
```mermaid
 +----------+                 +----------+
 |  CLIENT  |                 |  SERVER  |
 +----------+                 +----------+
      |                             |   
      |                message      |    
      |<----------------------------|  /-----------------------------\
      |                             |--|message about changes client |
      |                             |  |has to be notified           |
                                       \-----------------------------/
```