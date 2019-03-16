FROM tomcat:9-jre8

ADD app.war /usr/local/tomcat/webapps/

ADD https://repo1.maven.org/maven2/org/hsqldb/hsqldb/2.4.1/hsqldb-2.4.1.jar /usr/local/tomcat/lib/hsqldb-2.4.1.jar

ENV CATALINA_OPTS -Dapp.home=/akk-home
