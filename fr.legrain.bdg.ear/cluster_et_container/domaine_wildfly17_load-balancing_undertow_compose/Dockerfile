#FROM jboss/wildfly
 
# Environment variable with default value
#ARG APP_FILE=appfile.war
 
# Add your application to the deployment folder
#ADD ${APP_FILE} /opt/jboss/wildfly/standalone/deployments/${APP_FILE}
 
# Add standalone-ha.xml - set your own network settings
#ADD standalone-ha-1.xml /opt/jboss/wildfly/standalone/configuration/standalone-ha-1.xml
#ADD standalone-ha-2.xml /opt/jboss/wildfly/standalone/configuration/standalone-ha-2.xml
#ADD standalone-ha-3.xml /opt/jboss/wildfly/standalone/configuration/standalone-ha-3.xml
 
# Add user for adminstration purpose
#RUN /opt/jboss/wildfly/bin/add-user.sh admin admin123 --silent


#**********************************


# Use latest jboss/base-jdk:11 image as the base
FROM jboss/base-jdk:11

# Set the WILDFLY_VERSION env variable
ENV WILDFLY_VERSION 17.0.1.Final
ENV WILDFLY_SHA1 eaef7a87062837c215e54511c4ada8951f0bd8d5
ENV JBOSS_HOME /opt/wildfly

USER root

# Add the WildFly distribution to /opt, and make wildfly the owner of the extracted tar content
# Make sure the distribution is available from a well-known place
#RUN cd $HOME \
#    && curl -O https://download.jboss.org/wildfly/$WILDFLY_VERSION/wildfly-$WILDFLY_VERSION.tar.gz \
#    && sha1sum wildfly-$WILDFLY_VERSION.tar.gz | grep $WILDFLY_SHA1 \
#    && tar xf wildfly-$WILDFLY_VERSION.tar.gz \
#    && mv $HOME/wildfly-$WILDFLY_VERSION $JBOSS_HOME \
#    && rm wildfly-$WILDFLY_VERSION.tar.gz \
#    && chown -R jboss:0 ${JBOSS_HOME} \
#    && chmod -R g+rw ${JBOSS_HOME}

RUN mkdir /var/lgr && mkdir /var/lgr/java
ADD openjdk-12.0.2_linux-x64_bin.tar.gz /var/lgr/java/
#RUN cd /var/lgr/java \
#    && chmod -R 777 openjdk-12.0.2_linux-x64_bin.tar.gz \
#    && tar xvzf openjdk-12.0.2_linux-x64_bin.tar.gz

ADD fr.legrain.autorisations.ear.ear /var/lgr/
ADD fr.legrain.bdg.ear.ear /var/lgr/
#ADD fr.legrain.espaceclient.ear.ear /var/lgr/
ADD fr.legrain.moncompte.ear.ear /var/lgr/

ADD wildfly-17.0.1.Final.lgr/ ${JBOSS_HOME}
#ADD wildfly-24.0.1.Final ${JBOSS_HOME}

RUN chmod -R 777 ${JBOSS_HOME}

####################################################################################################
RUN /bin/bash -c "echo '#!/bin/bash' > /tmp/jboss.sh" && chmod 777 /tmp/jboss.sh
# IPADDRESS
RUN /bin/bash -c "echo 'IPADDRESS=\$(hostname -i)' >> /tmp/jboss.sh"
RUN /bin/bash -c "echo 'echo \"IPADDRESS=\$IPADDRESS\"' >> /tmp/jboss.sh"
##########################
###/subsystem=jgroups/channel=ee:write-attribute(name=stack,value=tcp)  \n\

###########################

RUN /bin/bash -c "echo $'/opt/wildfly/bin/jboss-cli.sh <<EOF \n\
embed-host-controller --std-out=echo --host-config=host-master.xml \n\
deploy /var/lgr/fr.legrain.autorisations.ear.ear --server-groups=main-server-group  \n\
quit \n\
EOF\n'\
>> /tmp/jboss.sh"

RUN /bin/bash -c "echo $'/opt/wildfly/bin/jboss-cli.sh <<EOF \n\
embed-host-controller --std-out=echo --host-config=host-master.xml \n\
deploy /var/lgr/fr.legrain.moncompte.ear.ear --server-groups=main-server-group  \n\
quit \n\
EOF\n'\
>> /tmp/jboss.sh"

RUN /bin/bash -c "echo $'/opt/wildfly/bin/jboss-cli.sh <<EOF \n\
embed-host-controller --std-out=echo --host-config=host-master.xml \n\
deploy /var/lgr/fr.legrain.bdg.ear.ear --server-groups=main-server-group  \n\
quit \n\
EOF\n'\
>> /tmp/jboss.sh"

#RUN cd ${JBOSS_HOME}/standalone/deployments \
#&& chmod -R 777 ${JBOSS_HOME}/standalone \
#&& curl -O http://192.168.1.12:8088/job/bdg_test_prod/lastSuccessfulBuild/artifact/svn/fr.legrain.autorisations.ear/target/fr.legrain.autorisations.ear.ear \
#&& curl -O http://192.168.1.12:8088/job/bdg_test_prod/lastSuccessfulBuild/artifact/svn/fr.legrain.bdg.ear/target/fr.legrain.bdg.ear.ear \
#&& curl -O http://192.168.1.12:8088/job/bdg_test_prod/lastSuccessfulBuild/artifact/svn/fr.legrain.espaceclient.ear/target/fr.legrain.espaceclient.ear.ear \
#&& curl -O http://192.168.1.12:8088/job/bdg_test_prod/lastSuccessfulBuild/artifact/svn/fr.legrain.moncompte.ear/target/fr.legrain.moncompte.ear.ear

#&& curl -O http://192.168.1.12:8088/job/bdg%20production/lastSuccessfulBuild/artifact/fr.legrain.autorisations.ear.ear \
#&& curl -O http://192.168.1.12:8088/job/bdg%20production/lastSuccessfulBuild/artifact/fr.legrain.bdg.ear.ear \
#&& curl -O http://192.168.1.12:8088/job/bdg%20production/lastSuccessfulBuild/artifact/fr.legrain.espaceclient.ear.ear \
#&& curl -O http://192.168.1.12:8088/job/bdg%20production/lastSuccessfulBuild/artifact/fr.legrain.moncompte.ear.ear
#RUN curl -O http://192.168.1.12:8088/job/bdg%20production/lastSuccessfulBuild/artifact/setup_bdg.tar.gz

# Ensure signals are forwarded to the JVM process correctly for graceful shutdown
ENV LAUNCH_JBOSS_IN_BACKGROUND true

USER jboss

# Expose the ports we're interested in
EXPOSE 8080 8443 9990 9999 8090 23364 8009

#RUN sed -i s#"jdbc:postgresql:"#"jdbc:postgresql://"${DB_HOST_IP}#g ${JBOSS_HOME}/standalone/configuration/standalone.xml
# Set the default command to run on boot
# This will boot WildFly in the standalone mode and bind to all interface
#CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0","-DdbHostIp=${DB_HOST_IP}"]
#CMD /opt/jboss/wildfly/bin/standalone.sh -b 0.0.0.0 -DdbHostIp=${DB_HOST_IP} -c standalone-ha.xml

#ENTRYPOINT ["/opt/wildfly/bin/domain.sh"]
#CMD ["--host-config", "host-master.xml", "-b", "0.0.0.0","-bmanagement", "0.0.0.0"]

#RUN echo '/opt/wildfly/bin/domain.sh -b 0.0.0.0 -bmanagement 0.0.0.0 -Djboss.domain.master.address=dc -host-config=host-master.xml ${PARAM}' >> /tmp/jboss.sh

RUN echo '/opt/wildfly/bin/domain.sh -b 0.0.0.0 -bprivate $IPADDRESS -bmanagement $IPADDRESS -bprivate $IPADDRESS -Djboss.domain.master.address=dc -Djboss.domain.master.port=9999 -host-config=host-master.xml  ${PARAM}' >> /tmp/jboss.sh

CMD "/tmp/jboss.sh"

