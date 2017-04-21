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
package org.phenotips.pingback.internal.client;

import org.phenotips.pingback.ActiveInstallsConfiguration;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xpn.xwiki.util.AbstractXWikiRunnable;

/**
 * Thread that regularly sends information about the current instance (its unique id + the id and versions of all
 * installed extensions) to a central active installs Elastic Search server in order to count the number of active
 * installs of XWiki (and to know what extensions and in which versions they use).
 *
 * @version $Id$
 * @since 5.2M2
 */
public class ActiveInstallsPingThread extends AbstractXWikiRunnable
{
    /**
     * The logger to use when logging in this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ActiveInstallsPingThread.class);

    /**
     * Once every 24 hours.
     */
    private static final long WAIT_TIME = 1000L * 60L * 60L * 24L;

    /**
     * @see #ActiveInstallsPingThread(ActiveInstallsConfiguration, PingSender)
     */
    private PingSender manager;

    /**
     * @see #ActiveInstallsPingThread(ActiveInstallsConfiguration, PingSender)
     */
    private ActiveInstallsConfiguration configuration;

    /**
     * @param configuration used to nicely display the ping URL in logs if there's an error...
     * @param manager used to send the ping to the remote instance
     */
    public ActiveInstallsPingThread(ActiveInstallsConfiguration configuration, PingSender manager)
    {
        this.configuration = configuration;
        this.manager = manager;
    }

    @Override
    protected void runInternal()
    {
        while (true) {
            try {
                this.manager.sendPing();
            } catch (Exception e) {
                // Failed to connect or send the ping to the remote Elastic Search instance, will try again after the
                // sleep.
                LOGGER.warn(
                    "Failed to send Active Installation ping to [{}]. Error = [{}]. Will retry in [{}] seconds...",
                    this.configuration.getPingInstanceURL(),
                    ExceptionUtils.getRootCauseMessage(e),
                    WAIT_TIME / 1000,
                    e);
            }
            try {
                Thread.sleep(WAIT_TIME);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
