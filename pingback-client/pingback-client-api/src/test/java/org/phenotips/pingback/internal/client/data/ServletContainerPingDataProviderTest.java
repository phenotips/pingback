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

import org.xwiki.component.util.ReflectionUtils;
import org.xwiki.environment.internal.ServletEnvironment;
import org.xwiki.test.mockito.MockitoComponentMockingRule;

import javax.servlet.ServletContext;

import org.junit.Rule;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import net.sf.json.JSONObject;
import net.sf.json.test.JSONAssert;

/**
 * Unit tests for {@link ServletContainerPingDataProvider}.
 *
 * @version $Id: 752ccb064424a6ebe3655bda1593a1a3f2e6752f $
 * @since 6.1M1
 */
public class ServletContainerPingDataProviderTest
{
    @Rule
    public MockitoComponentMockingRule<ServletContainerPingDataProvider> mocker =
            new MockitoComponentMockingRule<>(ServletContainerPingDataProvider.class);

    @Test
    public void provideMapping() throws Exception {
        JSONAssert.assertEquals("{\"servletContainerVersion\":{\"index\":\"not_analyzed\",\"type\":\"string\"},"
                        + "\"servletContainerName\":{\"index\":\"not_analyzed\",\"type\":\"string\"}}",
                JSONObject.fromObject(this.mocker.getComponentUnderTest().provideMapping())
        );
    }

    @Test
    public void provideData() throws Exception {
        ServletEnvironment servletEnvironment = mock(ServletEnvironment.class);
        ReflectionUtils.setFieldValue(this.mocker.getComponentUnderTest(), "environment", servletEnvironment);

        ServletContext servletContext = mock(ServletContext.class);
        when(servletEnvironment.getServletContext()).thenReturn(servletContext);
        when(servletContext.getServerInfo()).thenReturn("Apache Tomcat/7.0.4 (optional text)");

        JSONAssert.assertEquals("{\"servletContainerVersion\":\"7.0.4\",\"servletContainerName\":\"Apache Tomcat\"}",
                JSONObject.fromObject(this.mocker.getComponentUnderTest().provideData()));
    }
}
