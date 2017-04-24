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

import org.xwiki.extension.ExtensionId;
import org.xwiki.extension.InstalledExtension;
import org.xwiki.extension.repository.InstalledExtensionRepository;
import org.xwiki.test.mockito.MockitoComponentMockingRule;

import java.util.Collections;

import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link ExtensionsPingDataProvider}.
 *
 * @version $Id$
 */
public class ExtensionsPingDataProviderTest
{
    @Rule
    public MockitoComponentMockingRule<PingDataProvider> mocker =
        new MockitoComponentMockingRule<PingDataProvider>(ExtensionsPingDataProvider.class);

    @Test
    public void provideMapping() throws Exception
    {
        JSONAssert.assertEquals("{\"extensions\":{\"properties\":{\"id\":{\"index\":\"not_analyzed\","
            + "\"type\":\"string\"},\"version\":{\"index\":\"not_analyzed\",\"type\":\"string\"}}}}",
            new JSONObject(this.mocker.getComponentUnderTest().provideMapping()), false);
    }

    @Test
    public void provideData() throws Exception
    {
        ExtensionId extensionId = new ExtensionId("extensionid", "1.0");
        InstalledExtension extension = mock(InstalledExtension.class);
        when(extension.getId()).thenReturn(extensionId);

        InstalledExtensionRepository repository = this.mocker.getInstance(InstalledExtensionRepository.class);
        when(repository.getInstalledExtensions()).thenReturn(Collections.singletonList(extension));

        JSONAssert.assertEquals("{\"extensions\":[{\"id\":\"extensionid\",\"version\":\"1.0\"}]}",
            new JSONObject(this.mocker.getComponentUnderTest().provideData()), false);
    }
}
