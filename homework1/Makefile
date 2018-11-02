
default:

server: build-server run-server
client: build-client run-client

build-server:
	javac -sourcepath src src/org/kth/id1212/Server.java

run-server:
	java -cp src org.kth.id1212.Server

build-client:
	javac -sourcepath src src/org/kth/id1212/Client.java

run-client:
	java -cp src org.kth.id1212.Client