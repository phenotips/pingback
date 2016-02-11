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
import org.xwiki.extension.InstalledExtension;
import org.xwiki.extension.repository.InstalledExtensionRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.json.JSONObject;

/**
 * Provide the list of installed extensions and their versions.
 *
 * @version $Id: 9a69c4efc2ba12a8beeb871bc771365b34e065c0 $
 * @since 6.1M1
 */
@Component
@Named("extensions")
@Singleton
public class ExtensionPingDataProvider implements PingDataProvider
{
    private static final String PROPERTY_ID = "id";

    private static final String PROPERTY_VERSION = "version";

    private static final String PROPERTY_EXTENSIONS = "extensions";

    @Inject
    private InstalledExtensionRepository extensionRepository;

    @Override
    public Map<String, Object> provideMapping() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "string");
        map.put("index", "not_analyzed");

        Map<String, Object> propertiesMap = new HashMap<>();
        propertiesMap.put(PROPERTY_ID, map);
        propertiesMap.put(PROPERTY_VERSION, map);

        return Collections.singletonMap(PROPERTY_EXTENSIONS,
                (Object) Collections.singletonMap("properties", propertiesMap));
    }

    @Override
    public Map<String, Object> provideData() {
        Collection<InstalledExtension> installedExtensions = this.extensionRepository.getInstalledExtensions();
        JSONObject[] extensions = new JSONObject[installedExtensions.size()];
        Iterator<InstalledExtension> it = installedExtensions.iterator();
        int i = 0;
        while (it.hasNext()) {
            InstalledExtension extension = it.next();
            Map<String, Object> extensionMap = new HashMap<>();
            extensionMap.put(PROPERTY_ID, extension.getId().getId());
            extensionMap.put(PROPERTY_VERSION, extension.getId().getVersion().toString());
            extensions[i] = new JSONObject(extensionMap);
            i++;
        }
        return Collections.singletonMap(PROPERTY_EXTENSIONS, (Object) extensions);
    }
}
