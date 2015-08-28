/**
 * Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.inbound.custom.listening;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.inbound.InboundProcessorParams;
import org.wso2.carbon.inbound.endpoint.protocol.generic.GenericInboundListener;

public class SampleListeningEP extends GenericInboundListener{

	private static final Log log = LogFactory.getLog(org.wso2.carbon.inbound.custom.listening.SampleListeningEP.class);

    /**
     * Constructor
     *
     * @param params  Parameters of the inbound endpoint
     */
   public SampleListeningEP(InboundProcessorParams params) {
       super(params);
	   log.info("Initialized the custom listening inbound endpoint.");
   }

    /**
     * Initialize the listening
     */
    public void init() {
        //TODO need to implement the logic here
        log.info("Inside the init method, listening starts here ...");
    }

    /**
     * Stopping the inbound endpoint
     */
    public void destroy() {
        //TODO need to implement the logic here
        log.info("Inside the destroy method, destroying the listening inbound ...");
    }

}
