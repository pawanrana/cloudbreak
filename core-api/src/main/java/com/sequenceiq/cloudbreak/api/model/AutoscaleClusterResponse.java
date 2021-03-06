package com.sequenceiq.cloudbreak.api.model;

import com.sequenceiq.cloudbreak.doc.ModelDescriptions;

import io.swagger.annotations.ApiModelProperty;

public class AutoscaleClusterResponse extends ClusterResponse {

    @ApiModelProperty(value = ModelDescriptions.StackModelDescription.PASSWORD)
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
