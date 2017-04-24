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
import org.xwiki.component.manager.ComponentLifecycleException;
import org.xwiki.component.phase.Disposable;
import org.xwiki.component.phase.Initializable;
import org.xwiki.component.phase.InitializationException;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;

/**
 * Factory to get a singleton {@link JestClient} instance since it's threadsafe. The URL to connect to is defined in the
 * Active Install configuration.
 *
 * @version $Id$
 * @since 1.0
 */
@Component
@Singleton
public class DefaultJestClientManager implements JestClientManager, Initializable, Disposable
{
    @Inject
    private ActiveInstallsConfiguration configuration;

    /**
     * The Jest Client singleton instance to use to connect to the remote instance.
     */
    private JestClient client;

    @Override
    public void initialize() throws InitializationException
    {
        String pingURL = this.configuration.getPingInstanceURL();
        HttpClientConfig clientConfig = new HttpClientConfig.Builder(pingURL).multiThreaded(true).build();
        JestClientFactory factory = new XWikiJestClientFactory(this.configuration);
        factory.setHttpClientConfig(clientConfig);
        this.client = factory.getObject();
    }

    @Override
    public void dispose() throws ComponentLifecycleException
    {
        if (this.client != null) {
            this.client.shutdownClient();
        }
    }

    @Override
    public JestClient getClient()
    {
        return this.client;
    }
}
