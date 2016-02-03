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

import org.xwiki.extension.ExtensionId;
import org.xwiki.extension.InstalledExtension;
import org.xwiki.extension.repository.InstalledExtensionRepository;
import org.xwiki.test.mockito.MockitoComponentMockingRule;

import java.util.Collections;

import org.junit.Rule;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import net.sf.json.JSONObject;
import net.sf.json.test.JSONAssert;

/**
 * Unit tests for {@link ExtensionPingDataProvider}.
 *
 * @version $Id: 8598970ba0bf868cda505e1fa13d500456862cf6 $
 * @since 6.1M1
 */
public class ExtensionPingDataProviderTest
{
    @Rule
    public MockitoComponentMockingRule<ExtensionPingDataProvider> mocker =
            new MockitoComponentMockingRule<>(ExtensionPingDataProvider.class);

    @Test
    public void provideMapping() throws Exception {
        JSONAssert.assertEquals("{\"extensions\":{\"properties\":{\"id\":{\"index\":\"not_analyzed\","
                        + "\"type\":\"string\"},\"version\":{\"index\":\"not_analyzed\",\"type\":\"string\"}}}}",
                JSONObject.fromObject(this.mocker.getComponentUnderTest().provideMapping())
        );
    }

    @Test
    public void provideData() throws Exception {
        ExtensionId extensionId = new ExtensionId("extensionid", "1.0");
        InstalledExtension extension = mock(InstalledExtension.class);
        when(extension.getId()).thenReturn(extensionId);

        InstalledExtensionRepository repository = this.mocker.getInstance(InstalledExtensionRepository.class);
        when(repository.getInstalledExtensions()).thenReturn(Collections.singletonList(extension));

        JSONAssert.assertEquals("{\"extensions\":[{\"id\":\"extensionid\",\"version\":\"1.0\"}]}",
                JSONObject.fromObject(this.mocker.getComponentUnderTest().provideData()));
    }
}
