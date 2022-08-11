package com.linagora.tmail.deployment;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.apache.james.mpt.imapmailbox.external.james.host.external.ExternalJamesConfiguration;
import org.apache.james.util.Port;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.MountableFile;

public class TmailMemoryExtension implements BeforeEachCallback, AfterEachCallback {

    private final GenericContainer<?> container = new GenericContainer<>("linagora/tmail-backend-memory:latest")
        .withCopyFileToContainer(MountableFile.forClasspathResource("james-conf/imapserver.xml"), "/root/conf/")
        .withCopyFileToContainer(MountableFile.forClasspathResource("james-conf/jwt_privatekey"), "/root/conf/")
        .withCopyFileToContainer(MountableFile.forClasspathResource("james-conf/jwt_publickey"), "/root/conf/")
        .withCreateContainerCmdModifier(createContainerCmd -> createContainerCmd.withName("tmail-memory-testing" + UUID.randomUUID()))
        .waitingFor(TestContainerWaitStrategy.WAIT_STRATEGY)
        .withExposedPorts(25, 143, 80);

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws IOException {
        String dockerSaveFileUrl = new File("").getAbsolutePath().replace(Paths.get("tmail-backend", "deployment-tests", "memory").toString(),
            Paths.get("tmail-backend", "apps", "memory", "target", "jib-image.tar").toString());
        container.getDockerClient().loadImageCmd(Files.newInputStream(Paths.get(dockerSaveFileUrl))).exec();
        container.start();
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) {
        container.stop();
    }

    public GenericContainer<?> getContainer() {
        return container;
    }

    ExternalJamesConfiguration configuration() {
        return new ExternalJamesConfiguration() {
            @Override
            public String getAddress() {
                return container.getContainerIpAddress();
            }

            @Override
            public Port getImapPort() {
                return Port.of(container.getMappedPort(143));
            }

            @Override
            public Port getSmptPort() {
                return Port.of(container.getMappedPort(25));
            }
        };
    }
}
