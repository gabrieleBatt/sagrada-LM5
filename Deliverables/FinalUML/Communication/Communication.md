######The game makes four request to the users:
- The first three requests are to the same socket channel:
    - the first one is successful;
    
    - the second one the timer ends before an input 
    is received so the communication
    channel sets the user offline and produces
    a neutral response; 

    - the third time the channel is still offline so it
    produces directly a neutral response

- The last request is via a rmi channel which
has access to the remote screen so it calls
directly its methods.


######In all circumstances the Game:
- ignores whether the user is offline
- ignores how the request is forwarded 
- will treat the return value of the method 
called as the user choice

######In all circumstances the CommunicationChannel:
- will return a value after the timer has ended
- will return a valid choice
- ignores how the response will be acquired from the user
