Socket Protocol
--
c: login \<nickname> [-sp] \
s: login \<result>

s: send \<message>

s: chooseWindow \<windowId> [\<additionalWindowId>]\
c: windowChosen \<windowId>

s: selectObject \<container> \<idObject> [\<additionalIdObject>] [-s] [-u]\
c: optionSelected \<optionChosen>

s: selectFrom \<message> \<option> [\<additionalOption>] [-s] [-u]\
c: selected \<option>

s: update [-p \<diceList>] [-rt \<diceList> (round1:1R1, round2:3C4 ,...)] [-t \<toolList> (toolName-used)]
    [-pub \<pubObjList>] 
	[-prv \<prvObjList>] [-tk [\<nickname>] \<token>] 
	[-w [\<nickname>] \<diceList> \<window>] [-pl \<nicknameList>]

s: endGame \<nickname-score> \<nickname-score> [\<nickname-score>] (nickname1-57, nickname2-48) 
