Socket Protocol
--
Italics lines are either optional or used 
for possible additional features

- Login messages:                           \
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

- Window choice messages:                   \
    - server:{                              \
        "header":"chooseWindow",            \
        "glassWindow":\[\<windowIdList>] 
        }

    - client:{                              \
        "header":"chooseWindow",            \
        "glassWindow": \<windowId>
        }

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

- Update table messages:
    - server:{                              \
        "header":"update",                  \
        <i>"pool": \[\<diceIdList>],            \
        "roundTrack": \[\<round:diceId List>],    (r1:1R1, r2:3C4 ,...)       \
        "tool": \[\<tool-used List>],    \
        "pubObj": \[\<pubObj List>],    \
        "player": \[\<nickname-connected List>]</i>
    }
    
    - server:{                                
        "header": "endGame",                    \
        "leaderBoard: \[\<nickname-score List>] 
        }


- Update player messages:
    - server:{                              \
        "header":"updatePlayer",                  \
        "player": \"\<player>",            \
        <i>"prvObj": \[\<prvObjList>],    \
        "token": \<token>,                   \
        "glassWindow": \[\<glassWindow, diceList>]</i>
        }

- Message to user:
    - server:{                  \
        "header": "send",           \
        "message": "\<message>"
        }
