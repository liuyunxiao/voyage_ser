pkill -9 java
nohup java -Xms1024m -Xmx2048m -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8888 -Djava.ext.dirs=. -jar voyage.jar > log 2>&1 &
