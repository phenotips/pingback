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
package org.phenotips.pingback.internal.client;

import org.phenotips.pingback.internal.JestClientManager;

import org.xwiki.test.mockito.MockitoComponentMockingRule;

import org.junit.Rule;
import org.junit.Test;

import com.google.gson.Gson;
import io.searchbox.client.JestClient;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link DefaultPingSender}.
 *
 * @version $Id$
 * @since 1.0
 */
public class DefaultPingSenderTest
{
    @Rule
    public MockitoComponentMockingRule<PingSender> mocker =
        new MockitoComponentMockingRule<PingSender>(DefaultPingSender.class);

    @Test
    public void sendPing() throws Exception
    {
        JestClient client = mock(JestClient.class);
        DocumentResult indexResult = new DocumentResult(new Gson());
        indexResult.setSucceeded(true);
        when(client.execute(any(Index.class))).thenReturn(indexResult);

        JestClientManager jestManager = this.mocker.getInstance(JestClientManager.class);
        when(jestManager.getClient()).thenReturn(client);

        PingDataProvider pingDataProvider = this.mocker.registerMockComponent(PingDataProvider.class, "test");

        this.mocker.getComponentUnderTest().sendPing();

        // Verify that provideMapping() and provideData() are called
        verify(pingDataProvider).provideMapping();
        verify(pingDataProvider).provideData();
    }

    @Test(expected = Exception.class)
    public void sendPingFailureThrowsException() throws Exception
    {
        JestClient client = mock(JestClient.class);
        DocumentResult indexResult = new DocumentResult(new Gson());
        indexResult.setSucceeded(false);
        when(client.execute(any(Index.class))).thenReturn(indexResult);

        JestClientManager jestManager = this.mocker.getInstance(JestClientManager.class);
        when(jestManager.getClient()).thenReturn(client);

        this.mocker.getComponentUnderTest().sendPing();
    }
}
