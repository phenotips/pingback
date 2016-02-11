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
import org.xwiki.context.Execution;
import org.xwiki.query.Query;
import org.xwiki.query.QueryException;
import org.xwiki.query.QueryFilter;
import org.xwiki.query.QueryManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;

/**
 * Provide database name and version.
 *
 * @version $Id: 1f34ade15fc5c1b8f8e93ca0f022c04ebda37ee7 $
 * @since 6.1M1
 */
@Component
@Named("patients")
@Singleton
public class PatientsPingDataProvider implements PingDataProvider
{
    private static final String PROPERTY_PATIENT_COUNT = "patientCount";

    @Inject
    private Logger logger;

    @Inject
    private QueryManager qm;

    @Inject
    @Named("count")
    private QueryFilter countFilter;

    @Override
    public Map<String, Object> provideMapping() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "long");

        Map<String, Object> propertiesMap = new HashMap<>();
        propertiesMap.put(PROPERTY_PATIENT_COUNT, map);

        return propertiesMap;
    }

    @Override
    public Map<String, Object> provideData() {
        Map<String, Object> jsonMap = new HashMap<>();

        try {
            Query q = this.qm.createQuery(
                    "from doc.object(PhenoTips.PatientClass) as patient "
                            + "where patient.name<>'PhenoTips.PatientTemplate'", Query.XWQL);
            List<Object> results = q.addFilter(countFilter).execute();
            long count = (long) results.get(0);
            jsonMap.put(PROPERTY_PATIENT_COUNT, count);
        } catch (QueryException e) {
            logWarning("Error getting patient count", e);
        }

        return jsonMap;
    }

    private void logWarning(String explanation, Throwable e) {
        this.logger.warn("{}. This information has not been added to the Active Installs ping data. Reason [{}]",
                explanation, ExceptionUtils.getRootCauseMessage(e), e);
    }
}
