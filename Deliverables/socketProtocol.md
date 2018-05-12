Socket Protocol
--
c: login \<nickname>   [-gm \<gameMode>]

s: login \<result>

s: send \<message>

s: chooseWindow \<windowId> [-a \<additionalWindowId>]

c: windowChosen \<windowId>

s: selectObject \<idObject> [-a \<additionalIdObject>]

c: optionSelected \<idObject>

s: selectFrom \<message> \<option> [-a \<additionalOption>]

c: selected \<option>

s: update [-p \<diceList>] [-rt \<diceList>] [-t \<toolList>] [-pub \<pubObjList>] 
	[-prv \<prvObjList>] [-wl \<windowList>] [-tk \<tokenList>] 
	[-w [\<nickname>] \<diceList> ] [-pl \<nicknameList>]


