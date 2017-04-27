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

import org.xwiki.component.annotation.Component;
import org.xwiki.query.Query;
import org.xwiki.query.QueryException;
import org.xwiki.query.QueryFilter;
import org.xwiki.query.QueryManager;
import org.xwiki.wiki.descriptor.WikiDescriptorManager;
import org.xwiki.wiki.manager.WikiManagerException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;

/**
 * Provide the number of family records.
 *
 * @version $Id$
 * @since 1.1
 */
@Component
@Named("families")
@Singleton
public class FamiliesPingDataProvider implements PingDataProvider
{
    private static final String PROPERTY_FAMILIES_COUNT = "families";

    @Inject
    private Logger logger;

    @Inject
    private QueryManager qm;

    @Inject
    @Named("count")
    private QueryFilter countFilter;

    @Inject
    private WikiDescriptorManager wikiManager;

    @Override
    public Map<String, Object> provideMapping()
    {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "long");

        Map<String, Object> propertiesMap = new HashMap<>();
        propertiesMap.put(PROPERTY_FAMILIES_COUNT, map);

        return propertiesMap;
    }

    @Override
    public Map<String, Object> provideData()
    {
        Map<String, Object> jsonMap = new HashMap<>();

        try {
            Query q = this.qm.createQuery(
                "from doc.object(PhenoTips.FamilyClass) as family "
                    + "where doc.fullName<>'PhenoTips.FamilyTemplate'",
                Query.XWQL);
            q.addFilter(this.countFilter);
            long count = 0;

            for (String instanceId : this.wikiManager.getAllIds()) {
                q.setWiki(instanceId);
                List<Object> results = q.execute();
                long instanceCount = (long) results.get(0);
                count += instanceCount;
            }
            jsonMap.put(PROPERTY_FAMILIES_COUNT, count);
        } catch (QueryException | WikiManagerException e) {
            logWarning("Error getting families count", e);
        }

        return jsonMap;
    }

    private void logWarning(String explanation, Throwable e)
    {
        this.logger.warn("{}. This information has not been added to the Active Installs ping data. Reason [{}]",
            explanation, ExceptionUtils.getRootCauseMessage(e), e);
    }
}
