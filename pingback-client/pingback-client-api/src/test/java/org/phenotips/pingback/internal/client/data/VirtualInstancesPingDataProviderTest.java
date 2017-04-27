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

import org.phenotips.pingback.internal.client.PingDataProvider;

import org.xwiki.test.mockito.MockitoComponentMockingRule;
import org.xwiki.wiki.descriptor.WikiDescriptorManager;
import org.xwiki.wiki.manager.WikiManagerException;

import java.util.Arrays;

import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link VirtualInstancesPingDataProvider}.
 *
 * @version: $Id$
 */
public class VirtualInstancesPingDataProviderTest
{
    @Rule
    public MockitoComponentMockingRule<PingDataProvider> mocker =
        new MockitoComponentMockingRule<PingDataProvider>(VirtualInstancesPingDataProvider.class);

    @Test
    public void testProvideMapping() throws Exception
    {
        JSONAssert.assertEquals("{\"virtualInstances\":{\"type\":\"long\"}}",
            new JSONObject(this.mocker.getComponentUnderTest().provideMapping()), true);
    }

    @Test
    public void testProvideData() throws Exception
    {
        WikiDescriptorManager wdm = this.mocker.getInstance(WikiDescriptorManager.class);
        when(wdm.getAllIds()).thenReturn(Arrays.asList("xwiki", "gc"));

        JSONAssert.assertEquals("{\"virtualInstances\":2}",
            new JSONObject(this.mocker.getComponentUnderTest().provideData()), true);
    }

    @Test
    public void exceptionsAreCaughtAndOneInstanceIsReported() throws Exception
    {
        WikiDescriptorManager wdm = this.mocker.getInstance(WikiDescriptorManager.class);
        when(wdm.getAllIds()).thenThrow(new WikiManagerException("Database not ready"));

        JSONAssert.assertEquals("{\"virtualInstances\":1}",
            new JSONObject(this.mocker.getComponentUnderTest().provideData()), true);
    }
}
