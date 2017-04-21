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

import org.xwiki.context.Execution;
import org.xwiki.context.ExecutionContext;
import org.xwiki.test.mockito.MockitoComponentMockingRule;

import java.sql.DatabaseMetaData;

import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import com.xpn.xwiki.XWikiContext;
import com.xpn.xwiki.store.XWikiCacheStoreInterface;
import com.xpn.xwiki.store.XWikiHibernateStore;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link DatabasePingDataProvider}.
 *
 * @version $Id$
 * @since 6.1M1
 */
public class DatabasePingDataProviderTest
{
    @Rule
    public MockitoComponentMockingRule<DatabasePingDataProvider> mocker =
        new MockitoComponentMockingRule<>(DatabasePingDataProvider.class);

    @Test
    public void provideMapping() throws Exception
    {
        JSONAssert.assertEquals("{\"dbName\":{\"index\":\"not_analyzed\",\"type\":\"string\"},"
            + "\"dbVersion\":{\"index\":\"not_analyzed\",\"type\":\"string\"}}",
            new JSONObject(this.mocker.getComponentUnderTest().provideMapping()), false);
    }

    @Test
    public void provideData() throws Exception
    {
        Execution execution = this.mocker.getInstance(Execution.class);
        ExecutionContext executionContext = mock(ExecutionContext.class);
        when(execution.getContext()).thenReturn(executionContext);
        XWikiContext xwikiContext = mock(XWikiContext.class);
        when(executionContext.getProperty(XWikiContext.EXECUTIONCONTEXT_KEY)).thenReturn(xwikiContext);
        com.xpn.xwiki.XWiki xwiki = mock(com.xpn.xwiki.XWiki.class);
        when(xwikiContext.getWiki()).thenReturn(xwiki);
        XWikiCacheStoreInterface cacheStore = mock(XWikiCacheStoreInterface.class);
        when(xwiki.getStore()).thenReturn(cacheStore);
        XWikiHibernateStore store = mock(XWikiHibernateStore.class);
        when(cacheStore.getStore()).thenReturn(store);
        DatabaseMetaData databaseMetaData = mock(DatabaseMetaData.class);
        when(store.getDatabaseMetaData()).thenReturn(databaseMetaData);
        when(databaseMetaData.getDatabaseProductName()).thenReturn("HSQL Database Engine");
        when(databaseMetaData.getDatabaseProductVersion()).thenReturn("2.2.9");

        JSONAssert.assertEquals("{\"dbName\":\"HSQL Database Engine\",\"dbVersion\":\"2.2.9\"}",
            new JSONObject(this.mocker.getComponentUnderTest().provideData()), false);
    }
}
