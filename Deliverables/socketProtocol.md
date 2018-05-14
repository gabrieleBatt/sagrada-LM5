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

s: update [-p \<diceList>] [-rt \<diceList>] [-t \<toolList>] [-pub \<pubObjList>] 
	[-prv \<prvObjList>] [-wl \<windowList>] [-tk \<tokenList>] 
	[-w [\<nickname>] \<diceList> ] [-pl \<nicknameList>]


