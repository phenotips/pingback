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
import org.xwiki.extension.CoreExtension;
import org.xwiki.extension.repository.CoreExtensionRepository;
import org.xwiki.extension.version.Version;
import org.xwiki.instance.InstanceIdManager;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Provide the instance's unique id and distribution id and version.
 *
 * @version $Id$
 * @since 6.1M1
 */
@Component
@Named("distribution")
@Singleton
public class DistributionPingDataProvider implements PingDataProvider
{
    private static final String PROPERTY_INSTANCE_ID = "instanceId";

    private static final String PROPERTY_DISTRIBUTION_VERSION = "distributionVersion";

    private static final String PROPERTY_DISTRIBUTION_ID = "distributionId";

    @Inject
    private CoreExtensionRepository coreExtensionRepository;

    @Inject
    private InstanceIdManager instanceIdManager;

    @Override
    public Map<String, Object> provideMapping()
    {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "string");
        map.put("index", "not_analyzed");

        Map<String, Object> propertiesMap = new HashMap<>();
        propertiesMap.put(PROPERTY_INSTANCE_ID, map);
        propertiesMap.put(PROPERTY_DISTRIBUTION_VERSION, map);
        propertiesMap.put(PROPERTY_DISTRIBUTION_ID, map);

        return propertiesMap;
    }

    @Override
    public Map<String, Object> provideData()
    {
        Map<String, Object> jsonMap = new HashMap<>();

        String instanceId = this.instanceIdManager.getInstanceId().toString();
        jsonMap.put(PROPERTY_INSTANCE_ID, instanceId);

        CoreExtension distributionExtension = this.coreExtensionRepository.getEnvironmentExtension();
        if (distributionExtension != null) {
            String distributionId = distributionExtension.getId().getId();
            if (distributionId != null) {
                jsonMap.put(PROPERTY_DISTRIBUTION_ID, distributionId);
            }
            Version distributionVersion = distributionExtension.getId().getVersion();
            if (distributionVersion != null) {
                jsonMap.put(PROPERTY_DISTRIBUTION_VERSION, distributionVersion.toString());
            }
        }
        return jsonMap;
    }
}
