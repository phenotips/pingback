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
package org.phenotips.pingback.internal;

import org.phenotips.pingback.ActiveInstallsConfiguration;

import org.xwiki.component.annotation.Component;
import org.xwiki.configuration.ConfigurationSource;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * All configuration options for the Active Installs module.
 *
 * @version $Id$
 * @since 5.2M2
 */
@Component
@Singleton
public class DefaultActiveInstallsConfiguration implements ActiveInstallsConfiguration
{
    /**
     * Prefix for configuration keys for the Active Installs module.
     */
    private static final String PREFIX = "activeinstalls.";

    /**
     * @see #getPingInstanceURL()
     */
    private static final String DEFAULT_PING_URL = "http://extensions.xwiki.org/activeinstalls";

    private static final String DEFAULT_USER_AGENT = "XWikiActiveInstalls";

    /**
     * Defines from where to read the rendering configuration data.
     */
    @Inject
    private ConfigurationSource configuration;

    @Override
    public String getPingInstanceURL()
    {
        return this.configuration.getProperty(PREFIX + "pingURL", DEFAULT_PING_URL);
    }

    @Override
    public String getUserAgent()
    {
        return this.configuration.getProperty(PREFIX + "userAgent", DEFAULT_USER_AGENT);
    }
}
