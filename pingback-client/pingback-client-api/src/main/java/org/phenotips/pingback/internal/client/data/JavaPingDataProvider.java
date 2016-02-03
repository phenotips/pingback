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
 * Provide the JVM's vendor and version.
 *
 * @version $Id: 238f3067bd176eb32bd68f4477d0d6203a0d54e3 $
 * @since 6.1M1
 */
@Component
@Named("java")
@Singleton
public class JavaPingDataProvider implements PingDataProvider
{
    private static final String PROPERTY_JAVA_VENDOR = "javaVendor";

    private static final String PROPERTY_JAVA_VERSION = "javaVersion";

    @Override
    public Map<String, Object> provideMapping() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "string");
        map.put("index", "not_analyzed");

        Map<String, Object> propertiesMap = new HashMap<>();
        propertiesMap.put(PROPERTY_JAVA_VENDOR, map);
        propertiesMap.put(PROPERTY_JAVA_VERSION, map);

        return propertiesMap;
    }

    @Override
    public Map<String, Object> provideData() {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put(PROPERTY_JAVA_VENDOR, System.getProperty("java.vendor"));
        jsonMap.put(PROPERTY_JAVA_VERSION, System.getProperty("java.version"));
        return jsonMap;
    }
}
