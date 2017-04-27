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

import org.xwiki.configuration.ConfigurationSource;
import org.xwiki.test.mockito.MockitoComponentMockingRule;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.localserver.LocalServerTestBase;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link IPPingDataProvider}.
 *
 * @version: $Id$
 */
public class IPPingDataProviderTest extends LocalServerTestBase
{
    @Rule
    public MockitoComponentMockingRule<PingDataProvider> mocker =
        new MockitoComponentMockingRule<PingDataProvider>(IPPingDataProvider.class);

    @After
    @Override
    public void shutDown() throws Exception
    {
        if (this.server != null) {
            this.server.shutdown(100, TimeUnit.MILLISECONDS);
            this.server = null;
        }
        super.shutDown();
    }

    @Test
    public void testProvideMapping() throws Exception
    {
        JSONAssert.assertEquals("{\"ip\":{\"type\":\"ip\"}}",
            new JSONObject(this.mocker.getComponentUnderTest().provideMapping()), false);
    }

    @Test
    public void testProvideData() throws Exception
    {
        this.serverBootstrap.registerHandler("/*", new HttpRequestHandler()
        {
            @Override
            public void handle(HttpRequest request, HttpResponse response, HttpContext context)
                throws IOException
            {
                response.setEntity(new StringEntity("{\"ip\":\"192.168.1.1\"}"));
            }
        });

        HttpHost host = super.start();

        ConfigurationSource configuration = this.mocker.getInstance(ConfigurationSource.class);
        when(configuration.getProperty(IPPingDataProvider.IP_FETCH_URL_PROPERTY))
            .thenReturn(host.toURI() + "/get/Stats/Id");

        Map<String, Object> actual = this.mocker.getComponentUnderTest().provideData();
        JSONAssert.assertEquals("{\"ip\":\"192.168.1.1\"}",
            new JSONObject(actual), false);
    }

    @Test
    public void notFoundResponseReturnsEmptyData() throws Exception
    {
        this.serverBootstrap.registerHandler("/*", new HttpRequestHandler()
        {
            @Override
            public void handle(HttpRequest request, HttpResponse response, HttpContext context)
                throws IOException
            {
                response.setStatusCode(403);
                response.setEntity(new StringEntity("Not found"));
            }
        });

        HttpHost host = super.start();

        ConfigurationSource configuration = this.mocker.getInstance(ConfigurationSource.class);
        when(configuration.getProperty(IPPingDataProvider.IP_FETCH_URL_PROPERTY))
            .thenReturn(host.toURI() + "/get/Stats/Id");

        Map<String, Object> actual = this.mocker.getComponentUnderTest().provideData();
        JSONAssert.assertEquals("{}", new JSONObject(actual), true);
    }

    @Test
    public void requestErrorReturnsEmptyData() throws Exception
    {
        this.serverBootstrap.registerHandler("/*", new HttpRequestHandler()
        {
            @Override
            public void handle(HttpRequest request, HttpResponse response, HttpContext context)
                throws IOException
            {
                throw new IOException("failed");
            }
        });

        HttpHost host = super.start();

        ConfigurationSource configuration = this.mocker.getInstance(ConfigurationSource.class);
        when(configuration.getProperty(IPPingDataProvider.IP_FETCH_URL_PROPERTY))
            .thenReturn(host.toURI() + "/get/Stats/Id");

        Map<String, Object> actual = this.mocker.getComponentUnderTest().provideData();
        JSONAssert.assertEquals("{}", new JSONObject(actual), false);
    }
}
