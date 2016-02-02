/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.activeinstalls.internal.client.data;

import org.xwiki.query.Query;
import org.xwiki.query.QueryFilter;
import org.xwiki.query.QueryManager;
import org.xwiki.query.internal.DefaultQuery;
import org.xwiki.query.internal.ScriptQuery;
import org.xwiki.test.mockito.MockitoComponentMockingRule;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import net.sf.json.JSONObject;
import net.sf.json.test.JSONAssert;

/**
 * Unit tests for {@link org.xwiki.activeinstalls.internal.client.data.UsersPingDataProvider}.
 *
 * @version: $Id: $
 * @since 7.14
 */
public class UsersPingDataProviderTest
{
    @Rule
    public MockitoComponentMockingRule<UsersPingDataProvider> mocker =
            new MockitoComponentMockingRule<>(UsersPingDataProvider.class);

    @Test
    public void testProvideMapping() throws Exception {
        JSONAssert.assertEquals("{\"userCount\":{\"type\":\"long\"}}",
                JSONObject.fromObject(this.mocker.getComponentUnderTest().provideMapping()));
    }

    @Test
    public void testProvideData() throws Exception {
        Query q = mock(DefaultQuery.class);
        when(q.addFilter(any(QueryFilter.class))).thenReturn(q);
        when(q.execute()).thenReturn(new ArrayList(Collections.singleton(12L)));

        QueryManager qm = this.mocker.getInstance(QueryManager.class);
        when(qm.createQuery(anyString(), anyString())).thenReturn(q);

        JSONAssert.assertEquals("{\"userCount\":12}",
                JSONObject.fromObject(this.mocker.getComponentUnderTest().provideData()));
    }
}
