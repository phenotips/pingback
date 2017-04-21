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
 * Provide the Operating System's architecture, name and version.
 *
 * @version $Id$
 * @since 6.1M1
 */
@Component
@Named("os")
@Singleton
public class OsPingDataProvider implements PingDataProvider
{
    private static final String PROPERTY_OS_ARCH = "osArch";

    private static final String PROPERTY_OS_NAME = "osName";

    private static final String PROPERTY_OS_VERSION = "osVersion";

    @Override
    public Map<String, Object> provideMapping()
    {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "string");
        map.put("index", "not_analyzed");

        Map<String, Object> propertiesMap = new HashMap<>();
        propertiesMap.put(PROPERTY_OS_ARCH, map);
        propertiesMap.put(PROPERTY_OS_NAME, map);
        propertiesMap.put(PROPERTY_OS_VERSION, map);

        return propertiesMap;
    }

    @Override
    public Map<String, Object> provideData()
    {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put(PROPERTY_OS_ARCH, System.getProperty("os.arch"));
        jsonMap.put(PROPERTY_OS_NAME, System.getProperty("os.name"));
        jsonMap.put(PROPERTY_OS_VERSION, System.getProperty("os.version"));
        return jsonMap;
    }
}
