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

import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Provide memory related informations (max memory, allocated memory, etc).
 *
 * @version $Id$
 * @since 1.1
 */
@Component
@Named("memory")
@Singleton
public class MemoryPingDataProvider implements PingDataProvider
{
    private static final String PROPERTY_MAX_MEMORY = "maxMemory";

    private static final String PROPERTY_TOTAL_MEMORY = "totalMemory";

    private static final String PROPERTY_FREE_MEMORY = "freeMemory";

    private static final String PROPERTY_USED_MEMORY = "usedMemory";

    @Override
    public Map<String, Object> provideMapping()
    {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "long");

        Map<String, Object> propertiesMap = new HashMap<>();
        propertiesMap.put(PROPERTY_MAX_MEMORY, map);
        propertiesMap.put(PROPERTY_TOTAL_MEMORY, map);
        propertiesMap.put(PROPERTY_FREE_MEMORY, map);
        propertiesMap.put(PROPERTY_USED_MEMORY, map);

        return propertiesMap;
    }

    @Override
    public Map<String, Object> provideData()
    {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put(PROPERTY_MAX_MEMORY, Runtime.getRuntime().maxMemory());
        jsonMap.put(PROPERTY_TOTAL_MEMORY, Runtime.getRuntime().totalMemory());
        jsonMap.put(PROPERTY_FREE_MEMORY, Runtime.getRuntime().freeMemory());
        jsonMap.put(PROPERTY_USED_MEMORY, Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
        return jsonMap;
    }
}
