Socket Protocol
--
c: login \<nickname> \[-sp] \
s: login \<result>

s: send \<message>

s: chooseWindow \[-w] \<windowId> \[\<additionalWindowId>]\
c: windowChosen \<windowId>

s: selectObject \<container> \[-o] \<idObject> \[\<additionalIdObject>] \[skip] \[undo]\
c: objectSelected \<optionChosen>

s: selectFrom \<message> \[o] \<option> \[\<additionalOption>] \[skip] \[undo]\
c: selected \<option>

s: update \[-p \<diceList>] \[-rt \<diceList> (round1:1R1, round2:3C4 ,...)] 
    \[-t \<toolList> (toolName-used)]
    \[-pub \<pubObjList>] \[-pl \<nicknameList>]
 
s: update <player> \[-prv \<prvObjList>] \[-tk \[\<nickname>] \<token>] 
 	\[-w \[\<nickname>] \<diceList> \<window>]

s: endGame \[-l] \<nickname-score> \<nickname-score> \[\<nickname-score>] (nickname1-57, nickname2-48) 
