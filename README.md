# Chat client à la Janne
## Preparing
User needs connection to a chat server that can be found from https://github.com/anttijuu/O4-server. Server has it's own read me file.
If you start the server on your own computer, chat client already has right configuration. If you want to do changes to servers connection or port, you can do it in `config.txt` file found from project's root.
For building and running the chat client, you are going to need JDK and Maven installed.
## Starting chat client
Chat client is Maven project, so it installs all necessary dependencies when build. Building is done by using
```
mvn package assembly:single
```
and running by using
```
java -jar target/chatclient-1.0-jar-with-dependencies.jar
```
