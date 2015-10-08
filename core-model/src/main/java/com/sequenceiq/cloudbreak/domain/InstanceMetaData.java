package com.sequenceiq.cloudbreak.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import com.sequenceiq.cloudbreak.common.type.InstanceStatus;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "InstanceMetaData.findHostInStack",
                query = "SELECT i FROM InstanceMetaData i "
                        + "WHERE i.instanceGroup.stack.id= :stackId "
                        + "AND i.discoveryFQDN= :hostName "
                        + "AND i.instanceStatus <> 'TERMINATED' "),
        @NamedQuery(
                name = "InstanceMetaData.findUnregisteredHostsInInstanceGroup",
                query = "SELECT i FROM InstanceMetaData i "
                        + "WHERE i.instanceGroup.id= :instanceGroupId "
                        + "AND i.instanceStatus = 'UNREGISTERED'"),
        @NamedQuery(
                name = "InstanceMetaData.findNotTerminatedForStack",
                query = "SELECT i FROM InstanceMetaData i "
                        + "WHERE i.instanceGroup.stack.id= :stackId "
                        + "AND i.instanceStatus <> 'TERMINATED' "),
        @NamedQuery(
                name = "InstanceMetaData.findAllInStack",
                query = "SELECT i FROM InstanceMetaData i "
                        + "WHERE i.instanceGroup.stack.id= :stackId"),
        @NamedQuery(
                name = "InstanceMetaData.findByInstanceId",
                query = "SELECT i FROM InstanceMetaData i "
                        + "WHERE i.instanceId= :instanceId AND i.instanceGroup.stack.id= :stackId "),
        @NamedQuery(
                name = "InstanceMetaData.findAliveInstancesHostNamesInInstanceGroup",
                query = "SELECT i.discoveryFQDN FROM InstanceMetaData i "
                        + "WHERE i.instanceGroup.id = :instanceGroupId "
                        + "AND i.instanceStatus <> 'TERMINATED' "),
        @NamedQuery(
                name = "InstanceMetaData.findRemovableInstances",
                query = "SELECT i FROM InstanceMetaData i "
                        + "WHERE i.instanceGroup.stack.id= :stackId "
                        + "AND i.instanceGroup.groupName= :groupName "
                        + "AND (i.instanceStatus= 'DECOMMISSIONED' OR i.instanceStatus= 'UNREGISTERED')"),
        @NamedQuery(
                name = "InstanceMetaData.findNotTerminatedByPrivateAddress",
                query = "SELECT i FROM InstanceMetaData i "
                        + "WHERE i.instanceGroup.stack.id= :stackId "
                        + "AND i.privateIp= :privateAddress "
                        + "AND i.instanceStatus <> 'TERMINATED' ")
})
public class InstanceMetaData implements ProvisionEntity {

    @Id
    @GeneratedValue
    private Long id;
    private Long privateId;
    private String privateIp;
    private String publicIp;
    private Integer volumeCount;
    private String instanceId;
    private Boolean ambariServer;
    private Boolean consulServer;
    private String dockerSubnet;
    private String discoveryFQDN;
    @Enumerated(EnumType.STRING)
    private InstanceStatus instanceStatus;
    private Integer containerCount = 0;
    @ManyToOne
    private InstanceGroup instanceGroup;
    private Long startDate;
    private Long terminationDate;

    public InstanceMetaData() {

    }

    public InstanceGroup getInstanceGroup() {
        return instanceGroup;
    }

    public void setInstanceGroup(InstanceGroup instanceGroup) {
        this.instanceGroup = instanceGroup;
    }

    public String getPrivateIp() {
        return privateIp;
    }

    public void setPrivateIp(String privateIp) {
        this.privateIp = privateIp;
    }

    public String getPublicIp() {
        return publicIp;
    }

    public void setPublicIp(String publicIp) {
        this.publicIp = publicIp;
    }

    public Integer getVolumeCount() {
        return volumeCount;
    }

    public void setVolumeCount(Integer volumeCount) {
        this.volumeCount = volumeCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPrivateId() {
        return privateId;
    }

    public void setPrivateId(Long privateId) {
        this.privateId = privateId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public Boolean getAmbariServer() {
        return ambariServer;
    }

    public void setAmbariServer(Boolean ambariServer) {
        this.ambariServer = ambariServer;
    }

    public String getDockerSubnet() {
        return dockerSubnet;
    }

    public void setDockerSubnet(String dockerSubnet) {
        this.dockerSubnet = dockerSubnet;
    }

    public String getDiscoveryFQDN() {
        return discoveryFQDN;
    }

    public String getDiscoveryName() {
        return discoveryFQDN.split("\\.")[0];
    }

    public void setDiscoveryFQDN(String discoveryFQDN) {
        this.discoveryFQDN = discoveryFQDN;
    }

    public InstanceStatus getInstanceStatus() {
        return instanceStatus;
    }

    public void setInstanceStatus(InstanceStatus instanceStatus) {
        this.instanceStatus = instanceStatus;
    }

    public Integer getContainerCount() {
        return containerCount;
    }

    public void setContainerCount(Integer containerCount) {
        this.containerCount = containerCount;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getTerminationDate() {
        return terminationDate;
    }

    public void setTerminationDate(Long terminationDate) {
        this.terminationDate = terminationDate;
    }

    public Boolean getConsulServer() {
        return consulServer;
    }

    public void setConsulServer(Boolean consulServer) {
        this.consulServer = consulServer;
    }

    public boolean isDecommissioned() {
        return InstanceStatus.DECOMMISSIONED.equals(instanceStatus);
    }

    public boolean isUnRegistered() {
        return InstanceStatus.UNREGISTERED.equals(instanceStatus);
    }

    public boolean isTerminated() {
        return InstanceStatus.TERMINATED.equals(instanceStatus);
    }

    public boolean isRegistered() {
        return InstanceStatus.REGISTERED.equals(instanceStatus);
    }

    public boolean isRunning() {
        return InstanceStatus.REGISTERED.equals(instanceStatus) || InstanceStatus.UNREGISTERED.equals(instanceStatus);
    }

}