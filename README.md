# Tic-Tac-Toe Client-Server Desktop Application

###A. Instructions to run the files:

1. Starting the server: Java Server < port number >

2. Starting a client: Java Client < user nickname > < port number > < machine name >



3. Sending a message to another user: On the first line write the nickname of the recipient, and on the next line write the message.

   For example:

	> John

	> Hello

   This would print the message "Hello" to the user John.



4. Inviting another user to a game of Noughts and Crosses: On the first line write the nickname of the opponent, and on the next line write "play".

   For example:

	> John

	> play

   This would send an invitation to play to the user John.



5. To accept an invitation from another user, simply write "yes" or "y" and send the message.



6. To decline an invitation from another user, simply write "no" or "n" and send the message.



7. To see different information from the server, first write "show" on the first line and then, on the second line, write:

	- "all" to see all the users connected to the server

	- "available" to see a list of users that are not playing a game of tic-tac-toe at the moment

	- "record" to see your personal record

	- "scoreboard" to see the scoreboard consisting of the personal records of all the connected users


    For example:

	> show

	> all

    This would show a list of all the users online.







###B. What I implemented:



1. Quit function.

2. List all users.

3. List all users available to play.

4. Send requests to users.

5. Accept/decline requests from users.

6. Tic-Tac-Toe GUI.

7. Scoreboard function.

8. If the nickname chosen by the user is already taken, the server will ask the user to choose a different one.
