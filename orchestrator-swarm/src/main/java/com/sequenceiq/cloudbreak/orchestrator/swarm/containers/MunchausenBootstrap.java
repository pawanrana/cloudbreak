package com.sequenceiq.cloudbreak.orchestrator.swarm.containers;

import static com.sequenceiq.cloudbreak.orchestrator.DockerContainer.MUNCHAUSEN;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.HostConfig;
import com.sequenceiq.cloudbreak.orchestrator.containers.ContainerBootstrap;
import com.sequenceiq.cloudbreak.orchestrator.swarm.DockerClientUtil;
import com.sequenceiq.cloudbreak.orchestrator.swarm.builder.BindsBuilder;
import com.sequenceiq.cloudbreak.orchestrator.swarm.builder.HostConfigBuilder;

public class MunchausenBootstrap implements ContainerBootstrap {

    private static final Logger LOGGER = LoggerFactory.getLogger(MunchausenBootstrap.class);

    private final DockerClient docker;
    private final String[] cmd;
    private final String containerName;
    private final DockerClientUtil dockerClientUtil;

    public MunchausenBootstrap(DockerClient docker, String containerName, String[] cmd, DockerClientUtil dockerClientUtil) {
        this.docker = docker;
        this.cmd = cmd;
        this.containerName = containerName;
        this.dockerClientUtil = dockerClientUtil;
    }

    @Override
    public Boolean call() throws Exception {

        Bind[] binds = new BindsBuilder()
                .addDockerSocket().build();

        HostConfig hostConfig = new HostConfigBuilder().privileged().binds(binds).build();
        String containerId = dockerClientUtil.createContainer(docker, docker.createContainerCmd(containerName)
                .withName(MUNCHAUSEN.getName() + new Date().getTime())
                .withHostConfig(hostConfig)
                .withCmd(cmd));

        dockerClientUtil.startContainer(docker, docker.startContainerCmd(containerId));
        LOGGER.info("Munchausen bootstrap container started.");
        return true;
    }

}