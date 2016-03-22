/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/
 */
package org.phenotips.pingback.internal.client.data;

import org.xwiki.extension.CoreExtension;
import org.xwiki.extension.ExtensionId;
import org.xwiki.extension.repository.CoreExtensionRepository;
import org.xwiki.instance.InstanceId;
import org.xwiki.instance.InstanceIdManager;
import org.xwiki.test.mockito.MockitoComponentMockingRule;

import java.util.UUID;

import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

/**
 * Unit tests for {@link DistributionPingDataProvider}.
 *
 * @version $Id$
 * @since 6.1M1
 */
public class DistributionPingDataProviderTest
{
    @Rule
    public MockitoComponentMockingRule<DistributionPingDataProvider> mocker =
            new MockitoComponentMockingRule<>(DistributionPingDataProvider.class);

    @Test
    public void provideMapping() throws Exception {
        JSONAssert.assertEquals("{\"distributionId\":{\"index\":\"not_analyzed\",\"type\":\"string\"},"
                        + "\"distributionVersion\":{\"index\":\"not_analyzed\",\"type\":\"string\"},"
                        + "\"instanceId\":{\"index\":\"not_analyzed\",\"type\":\"string\"}}",
                new JSONObject(this.mocker.getComponentUnderTest().provideMapping()), false
        );
    }

    @Test
    public void provideData() throws Exception {
        InstanceId id = new InstanceId(UUID.randomUUID().toString());
        InstanceIdManager idManager = this.mocker.getInstance(InstanceIdManager.class);
        when(idManager.getInstanceId()).thenReturn(id);

        ExtensionId environmentExtensionId = new ExtensionId("environmentextensionid", "2.0");
        CoreExtension environmentExtension = mock(CoreExtension.class);
        when(environmentExtension.getId()).thenReturn(environmentExtensionId);
        CoreExtensionRepository CoreExtensionRepository = this.mocker.getInstance(CoreExtensionRepository.class);
        when(CoreExtensionRepository.getEnvironmentExtension()).thenReturn(environmentExtension);

        JSONAssert.assertEquals("{\"distributionId\":\"environmentextensionid\","
                        + "\"distributionVersion\":\"2.0\","
                        + "\"instanceId\":\"" + id.getInstanceId() + "\"}",
                new JSONObject(this.mocker.getComponentUnderTest().provideData()), false
        );
    }
}
