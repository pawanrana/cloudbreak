package com.sequenceiq.cloudbreak.converter;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sequenceiq.cloudbreak.TestUtil;
import com.sequenceiq.cloudbreak.api.model.InstanceGroupResponse;
import com.sequenceiq.cloudbreak.api.model.InstanceGroupType;
import com.sequenceiq.cloudbreak.api.model.InstanceStatus;
import com.sequenceiq.cloudbreak.domain.InstanceGroup;
import com.sequenceiq.cloudbreak.domain.InstanceMetaData;

public class InstanceGroupToJsonConverterTest extends AbstractEntityConverterTest<InstanceGroup> {

    @InjectMocks
    private InstanceGroupToJsonConverter underTest;

    @Mock
    private ConversionService conversionService;

    @Before
    public void setUp() {
        underTest = new InstanceGroupToJsonConverter();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConvert() {
        // GIVEN
        given(conversionService.convert(any(Object.class), any(TypeDescriptor.class), any(TypeDescriptor.class)))
                .willReturn(getInstanceMetaData(getSource()));
        // WHEN
        InstanceGroupResponse result = underTest.convert(getSource());
        // THEN
        assertEquals(1, result.getNodeCount());
        assertEquals(InstanceGroupType.CORE, result.getType());
        assertAllFieldsNotNull(result, Lists.newArrayList("template", "securityGroup"));
    }

    @Override
    public InstanceGroup createSource() {
        InstanceGroup instanceGroup = TestUtil.instanceGroup(1L, InstanceGroupType.CORE, TestUtil.gcpTemplate(1L));
        instanceGroup.setInstanceMetaData(getInstanceMetaData(instanceGroup));
        return instanceGroup;
    }

    private Set<InstanceMetaData> getInstanceMetaData(InstanceGroup instanceGroup) {
        InstanceMetaData metadata = TestUtil.instanceMetaData(1L, 1L, InstanceStatus.REGISTERED, false, instanceGroup);
        return Sets.newHashSet(metadata);
    }
}
