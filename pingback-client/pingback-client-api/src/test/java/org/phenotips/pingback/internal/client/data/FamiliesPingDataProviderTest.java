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

import org.xwiki.query.Query;
import org.xwiki.query.QueryException;
import org.xwiki.query.QueryFilter;
import org.xwiki.query.QueryManager;
import org.xwiki.query.internal.DefaultQuery;
import org.xwiki.test.mockito.MockitoComponentMockingRule;

import java.util.Collections;

import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link FamiliesPingDataProvider}.
 *
 * @version: $Id$
 */
public class FamiliesPingDataProviderTest
{
    @Rule
    public MockitoComponentMockingRule<PingDataProvider> mocker =
        new MockitoComponentMockingRule<PingDataProvider>(FamiliesPingDataProvider.class);

    @Test
    public void testProvideMapping() throws Exception
    {
        JSONAssert.assertEquals("{\"families\":{\"type\":\"long\"}}",
            new JSONObject(this.mocker.getComponentUnderTest().provideMapping()), false);
    }

    @Test
    public void testProvideData() throws Exception
    {
        Query q = mock(DefaultQuery.class);
        when(q.addFilter(any(QueryFilter.class))).thenReturn(q);
        when(q.execute()).thenReturn(Collections.<Object>singletonList(12L));

        QueryManager qm = this.mocker.getInstance(QueryManager.class);
        when(qm.createQuery(anyString(), anyString())).thenReturn(q);

        JSONAssert.assertEquals("{\"families\":12}",
            new JSONObject(this.mocker.getComponentUnderTest().provideData()), false);
    }

    @Test
    public void queryExceptionReturnsEmptyData() throws Exception
    {
        Query q = mock(DefaultQuery.class);
        when(q.addFilter(any(QueryFilter.class))).thenReturn(q);
        when(q.execute()).thenThrow(new QueryException("failed", q, null));

        QueryManager qm = this.mocker.getInstance(QueryManager.class);
        when(qm.createQuery(anyString(), anyString())).thenReturn(q);

        JSONAssert.assertEquals("{}", new JSONObject(this.mocker.getComponentUnderTest().provideData()), false);
    }
}
