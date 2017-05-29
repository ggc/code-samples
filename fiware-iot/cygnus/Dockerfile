FROM centos:6
# Environment variables
ENV MVN_VER "3.3.9"
ENV MVN_HOME "/opt/apache-maven"

ENV FLUME_VER "1.4.0"
ENV FLUME_HOME "/opt/apache-flume"

ENV JAVA_VERSION "1.7.0"
ENV CYGNUS_USER "cygnus"
ENV CYGNUS_HOME "/opt/fiware-cygnus"
ENV CYGNUS_VERSION "1.7.0_SNAPSHOT"
ENV CYGNUS_CONF_PATH "/opt/apache-flume/conf"
ENV CYGNUS_CONF_FILE "/opt/apache-flume/conf/agent.conf"
ENV CYGNUS_AGENT_NAME "cygnus-ngsi"
ENV CYGNUS_LOG_LEVEL "INFO"
ENV CYGNUS_LOG_APPENDER "console"
ENV CYGNUS_SERVICE_PORT "5050"
ENV CYGNUS_API_PORT "8081"
ENV CYGNUS_MYSQL_HOST "172.18.0.2"
ENV CYGNUS_MYSQL_PORT "3306"
ENV CYGNUS_MYSQL_USER "root"
ENV CYGNUS_MYSQL_PASS "r00t"
ENV CYGNUS_MONGO_HOSTS "172.18.0.3:27017"
ENV CYGNUS_MONGO_USER ""
ENV CYGNUS_MONGO_PASS ""

ENV FLUME_VER "1.4.0"
ENV FLUME_HOME "/usr/cygnus"

ENV JAVA_VERSION "1.7.0"

# Copy repo file
COPY ./conf/fiware.repo /etc/yum.repos.d/

# Install cygnus
RUN yum install -y cygnus-ngsi

# Install java
RUN yum -y install nc java-${JAVA_VERSION}-openjdk-devel

# Copy config files (instance, agent, grouping or naming, etc.)
RUN ls /usr/cygnus/conf/
COPY ./conf/*.conf /usr/cygnus/conf/
RUN ls /usr/cygnus/conf/
COPY ./conf/cygnus-entrypoint.sh /usr/cygnus/conf/
RUN ls /usr/cygnus/conf/


RUN \
    # Add Cygnus user
    # adduser ${CYGNUS_USER} && \
    chown -R cygnus:cygnus ${FLUME_HOME} && \
    mkdir -p /var/log/cygnus && \
    touch /var/log/cygnus/cygnus.log && \
    chmod 777 /var/log/cygnus/cygnus.log


# Define the entry point
ENTRYPOINT ["tail", "-f", "/var/log/cygnus/cygnus.log"]

# Ports used by cygnus-ngsi
EXPOSE ${CYGNUS_SERVICE_PORT} ${CYGNUS_API_PORT}