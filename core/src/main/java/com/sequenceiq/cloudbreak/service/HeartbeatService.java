package com.sequenceiq.cloudbreak.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.api.client.util.Lists;
import com.sequenceiq.cloudbreak.core.flow2.Flow2Handler;
import com.sequenceiq.cloudbreak.domain.CloudbreakNode;
import com.sequenceiq.cloudbreak.domain.FlowLog;
import com.sequenceiq.cloudbreak.repository.CloudbreakNodeRepository;
import com.sequenceiq.cloudbreak.repository.FlowLogRepository;
import com.sequenceiq.cloudbreak.service.flowlog.FlowLogService;

@Service
public class HeartbeatService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HeartbeatService.class);

    private static final int HEARTBEAT_RATE = 30_000;
    private static final int HEARTBEAT_THRESHOLD_RATE = 35_000;

    @Value("${cb.instance.uuid:}")
    private String uuid;

    @Inject
    private CloudbreakNodeRepository cloudbreakNodeRepository;

    @Inject
    private FlowLogRepository flowLogRepository;

    @Inject
    private Flow2Handler flow2Handler;

    @Inject
    private FlowLogService flowLogService;

    @Transactional
    @Scheduled(fixedRate = HEARTBEAT_RATE)
    public void heartbeat() {
        if (StringUtils.isNoneBlank(uuid)) {
            CloudbreakNode self = cloudbreakNodeRepository.findOne(uuid);
            if (self == null) {
                self = new CloudbreakNode(uuid);
            }
            self.setLastUpdated(System.currentTimeMillis());
            cloudbreakNodeRepository.save(self);
        }
    }

    @Transactional
    @Scheduled(fixedRate = HEARTBEAT_THRESHOLD_RATE, initialDelay = HEARTBEAT_RATE)
    public void reScheduleFlow() {
        if (StringUtils.isNoneBlank(uuid)) {
            ArrayList<CloudbreakNode> cloudbreakNodes = Lists.newArrayList(cloudbreakNodeRepository.findAll());
            long currentTimeMillis = System.currentTimeMillis();
            for (CloudbreakNode cloudbreakNode : cloudbreakNodes) {
                String nodeId = cloudbreakNode.getUuid();
                if (currentTimeMillis - cloudbreakNode.getLastUpdated() > 2 * HEARTBEAT_RATE) {
                    LOGGER.warn("Cloudbreak instance is not responding: " + nodeId);
                    List<Object[]> suspendedFlows = flowLogRepository.findAllRunningFlowIdsByCloudbreakId(nodeId);
                    for (Object[] flow : suspendedFlows) {
                        String flowId = (String) flow[0];
                        LOGGER.info("Resuming flow: " + flowId);
                        try {
                            flow2Handler.restartFlow(flowId);
                            Set<FlowLog> flowLogs = flowLogRepository.findAllByFlowId(flowId);
                            for (FlowLog flowLog : flowLogs) {
                                flowLog.setCloudbreakNodeId(uuid);
                            }
                            flowLogRepository.save(flowLogs);
                        } catch (Exception e) {
                            LOGGER.error(String.format("Failed to resume flow %s on stack %s", flowId, flow[1].toString()), e);
//                            flowLogService.terminate((Long) flow[1], flowId);
                        }
                    }
                }
            }
        }
    }

}
