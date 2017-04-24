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
import org.xwiki.configuration.ConfigurationSource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;

/**
 * Provide external IP address of current instance.
 *
 * @version $Id$
 * @since 1.0
 */
@Component
@Named("ip")
@Singleton
public class IPPingDataProvider implements PingDataProvider
{
    static final String IP_FETCH_URL_PROPERTY = "activeinstalls.ipFetchURL";

    private static final String PROPERTY_IP = "ip";

    @Inject
    private Logger logger;

    @Inject
    private ConfigurationSource configuration;

    private CloseableHttpClient client = HttpClients.createSystem();

    @Override
    public Map<String, Object> provideMapping()
    {
        Map<String, Object> map = new HashMap<>();
        map.put("type", PROPERTY_IP);

        Map<String, Object> propertiesMap = new HashMap<>();
        propertiesMap.put(PROPERTY_IP, map);

        return propertiesMap;
    }

    @Override
    public Map<String, Object> provideData()
    {
        Map<String, Object> jsonMap = new HashMap<>();

        CloseableHttpResponse response = null;
        try {
            String uri = this.configuration.getProperty(IP_FETCH_URL_PROPERTY);
            HttpGet method = new HttpGet(uri);
            RequestConfig config = RequestConfig.custom().setSocketTimeout(2000).build();
            method.setConfig(config);
            response = this.client.execute(method);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                return jsonMap;
            }
            JSONObject obj =
                new JSONObject(IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8));

            if (obj.has(PROPERTY_IP)) {
                jsonMap.put(PROPERTY_IP, obj.get(PROPERTY_IP));
            }
        } catch (Exception e) {
            logWarning("Making IP request failed.", e);
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consumeQuietly(response.getEntity());
                    response.close();
                } catch (IOException ex) {
                    // Not dangerous
                }
            }
        }

        return jsonMap;
    }

    private void logWarning(String explanation, Throwable e)
    {
        this.logger.warn("{}. This information has not been added to the Active Installs ping data. Reason [{}]",
            explanation, ExceptionUtils.getRootCauseMessage(e), e);
    }

}
