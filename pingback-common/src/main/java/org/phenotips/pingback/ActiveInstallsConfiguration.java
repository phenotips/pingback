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
package org.phenotips.pingback;

import org.xwiki.component.annotation.Role;
import org.xwiki.stability.Unstable;

/**
 * Configuration properties for the Active Installs module.
 *
 * @version $Id$
 * @since 1.0
 */
@Role
public interface ActiveInstallsConfiguration
{
    /**
     * @return the URL (as a string) of where the Active Installs module should connect to in order to send a ping of
     *         activity
     */
    String getPingInstanceURL();

    /**
     * @return the user agent to use when sending pings to the remote server
     * @since 1.1
     */
    @Unstable
    String getUserAgent();
}
