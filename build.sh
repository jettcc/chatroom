mvn -B clean install package -Dmaven.test.skip=true -Dautoconfig.skip
mkdir -p build
mkdir -p build/oauth build/gateway build/file build/chat

echo build/oauth build/gateway build/file build/chat | xargs -n 1 cp -v log4j2-spring.xml

mv im_oauth2server/target/im_oauth2server-0.0.1-SNAPSHOT.jar build/oauth/oauth-server.jar
cp im_oauth2server/Dockerfile build/oauth/Dockerfile

mv im_gateway/target/im_gateway-0.0.1-SNAPSHOT.jar build/gateway/gateway.jar
cp im_gateway/Dockerfile build/gateway/Dockerfile
cp im_gateway/src/main/resources/application-docker.yml build/gateway/application-docker.yml

mv im_fileServer/target/im_fileServer-0.0.1-SNAPSHOT.jar build/file/fileServer.jar
cp im_fileServer/Dockerfile build/file/Dockerfile

mv im_chatServer/target/im_chatServer-0.0.1-SNAPSHOT.jar build/chat/chat-server.jar
cp im_chatServer/Dockerfile build/chat/Dockerfile