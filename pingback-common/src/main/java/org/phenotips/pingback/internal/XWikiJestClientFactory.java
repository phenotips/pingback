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

import org.apache.http.impl.client.HttpClientBuilder;

import io.searchbox.client.JestClientFactory;

/**
 * Ensure that Jest works through an HTTP Proxy.
 *
 * @version $Id$
 * @since 1.0
 */
public class XWikiJestClientFactory extends JestClientFactory
{
    private ActiveInstallsConfiguration configuration;

    /**
     * @param configuration the object from which to extract the user agent to use when sending HTTP request (pings) to
     *            the remote server
     */
    public XWikiJestClientFactory(ActiveInstallsConfiguration configuration)
    {
        this.configuration = configuration;
    }

    @Override
    protected HttpClientBuilder configureHttpClient(HttpClientBuilder builder)
    {
        HttpClientBuilder modifiedBuilder = builder.useSystemProperties();
        modifiedBuilder.setUserAgent(this.configuration.getUserAgent());
        return modifiedBuilder;
    }
}
