pkill -9 java
nohup java -Xms1024m -Xmx2048m -Djava.ext.dirs=. -jar voyage.jar > log 2>&1 &
