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

import org.xwiki.component.util.ReflectionUtils;
import org.xwiki.environment.internal.ServletEnvironment;
import org.xwiki.test.mockito.MockitoComponentMockingRule;

import javax.servlet.ServletContext;

import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link ServletContainerPingDataProvider}.
 *
 * @version $Id$
 */
public class ServletContainerPingDataProviderTest
{
    @Rule
    public MockitoComponentMockingRule<PingDataProvider> mocker =
        new MockitoComponentMockingRule<PingDataProvider>(ServletContainerPingDataProvider.class);

    @Test
    public void provideMapping() throws Exception
    {
        JSONAssert.assertEquals("{\"servletContainerVersion\":{\"index\":\"not_analyzed\",\"type\":\"string\"},"
            + "\"servletContainerName\":{\"index\":\"not_analyzed\",\"type\":\"string\"}}",
            new JSONObject(this.mocker.getComponentUnderTest().provideMapping()), false);
    }

    @Test
    public void provideData() throws Exception
    {
        ServletEnvironment servletEnvironment = mock(ServletEnvironment.class);
        ReflectionUtils.setFieldValue(this.mocker.getComponentUnderTest(), "environment", servletEnvironment);

        ServletContext servletContext = mock(ServletContext.class);
        when(servletEnvironment.getServletContext()).thenReturn(servletContext);
        when(servletContext.getServerInfo()).thenReturn("Apache Tomcat/7.0.4 (optional text)");

        JSONAssert.assertEquals("{\"servletContainerVersion\":\"7.0.4\",\"servletContainerName\":\"Apache Tomcat\"}",
            new JSONObject(this.mocker.getComponentUnderTest().provideData()), false);
    }
}
