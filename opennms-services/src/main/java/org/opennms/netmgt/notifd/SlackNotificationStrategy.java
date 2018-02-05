/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2010-2014 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2014 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.netmgt.notifd;

import java.io.IOException;
import java.util.List;

import org.opennms.netmgt.config.NotificationManager;
import org.opennms.netmgt.model.notifd.Argument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackMessage;


/**
 * <p>SlackNotificationStrategy class.</p>
 *
 * @author <a href="mailto:jeffg@opennms.org>Jeff Gehlbach</a>
 * @author <a href="http://www.opennms.org/>OpenNMS</a>
 */
public class SlackNotificationStrategy extends HttpNotificationStrategy {
    private static final Logger LOG = LoggerFactory.getLogger(SlackNotificationStrategy.class);
    
//    private List<Argument> m_arguments;

    /**
     * <p>Constructor for SlackNotificationStrategy.</p>
     *
     * @throws java.io.IOException if any.
     */
    public SlackNotificationStrategy() throws IOException {
        super();
    }
    
    
    /** {@inheritDoc} */
    @Override
    public int send(List<Argument> arguments) {

        this.m_arguments = arguments;

        String url = this.getUrl();
        if (url == null) {
            LOG.warn("send: url argument is null, HttpNotification requires a URL");
            return 1;
        }
        SlackApi api = new SlackApi(url);
        
        String text = this.getValue(NotificationManager.PARAM_TEXT_MSG);
        String username = this.getNotificationValue("username");
        String channel = this.getNotificationValue("channel");

        SlackMessage message = new SlackMessage(channel, username, text);
        
        String icon = null;
        if (text.startsWith("RESOLVED:")) {
            icon = this.getNotificationValue("resolved_icon");
        } else {
            icon = this.getNotificationValue("icon");
        }
        if (icon != null) {
            message.setIcon(icon);    
        }
        api.call(message);
        return 0;
    }
    
}


