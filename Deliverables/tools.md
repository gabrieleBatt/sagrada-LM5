#Tool json syntax
- "name": tool name

- "conditions": Sublist of:
    - "CANCEL_DRAFTING": using the tool cancels the draft action(includes BEFORE_DRAFTING);
    
    - "BEFORE_DRAFTING": the tool must be used before the draft action
    
    - "FIRST_TURN": the tool must be used on the first turn of the round
    
    - "SECOND_TURN": the tool must be used on the second turn of the round
                        
  
- "toolColor": Color of the die on the tool card
  
- "actions": numbers of actions to perform when the tool is used

- "actionN": action number n to perform; one of  the following
    - "rollPool": roll all dice in the pool
    - "move":
        - "marker": marker of the die to move(valid in this tool context only)
        - "color"*: color or marker a die
        - "restriction": Sublist of:
            - "COLOR": Respecting color restriction
            - "NUMBER": Respecting number restriction
            - "CLOSE" Respecting adjacency restriction
    - "draft": 
        - "marker": marker of the die given to the one drafted(valid in this tool context only)
    - "place":
        - "marker": marker of the die to place(valid in this tool context only)
            - "restriction": Sublist of:
                - "COLOR": Respecting color restriction
                - "NUMBER": Respecting number restriction
                - "CLOSE" Respecting adjacency restriction
    - "random"/"set":
        - "marker": marker of the die to set(valid in this tool context only)
        - "setN": Sublist of \[1,2,3,4,5,6], if the dice has value N, it is
        changed in one in the list, randomly or accordingly to the player choice  
    - "swap":
          "markerInContainer": marker of the die given to the one removed from where it currently is(valid in this tool context only)
          "markerInMap": marker of the die to place in its place(valid in this tool context only)                
          "with": where the first die is contained
    - "select",
        "from": where the die is contained
        "marker": marker of the die given to the one selected(valid in this tool context only)
    - "skipNextTurn"           
    
\*not used but available in others actions

######Markers

The markers are used in a single tool card context, 
so that the same dice can be 
referenced across multiple actions           








    