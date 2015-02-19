package org.wso2.handler;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.http.HttpTransportProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHeaders;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.wso2.carbon.identity.oauth2.stub.OAuth2TokenValidationServiceStub;
import org.wso2.carbon.identity.oauth2.stub.dto.OAuth2TokenValidationRequestDTO;
import org.apache.synapse.ManagedLifecycle;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.SynapseEnvironment;
import org.apache.synapse.rest.AbstractHandler;
import org.wso2.carbon.identity.oauth2.stub.dto.OAuth2TokenValidationRequestDTO_OAuth2AccessToken;

import java.util.Map;

public class SimpleOauthHandler extends AbstractHandler implements ManagedLifecycle {

    private static final String CONSUMER_KEY_HEADER = "Bearer";
    private static final String OAUTH_HEADER_SPLITTER = ",";
    private static final String CONSUMER_KEY_SEGMENT_DELIMITER = " ";
    private static final String OAUTH_TOKEN_VALIDATOR_SERVICE = "oauth2TokenValidationService";
    private static final String IDP_LOGIN_USERNAME = "identityServerUserName";
    private static final String IDP_LOGIN_PASSWORD = "identityServerPw";
    private ConfigurationContext configContext;
    private static final Log log = LogFactory.getLog(SimpleOauthHandler.class);

    @Override
    public boolean handleRequest(MessageContext msgCtx) {
        if (this.getConfigContext() == null) {
            log.error("Configuration Context is null");
            return false;
        }
        try{
            //Read parameters from axis2.xml
            String identityServerUrl =
                    msgCtx.getConfiguration().getAxisConfiguration().getParameter(
                            OAUTH_TOKEN_VALIDATOR_SERVICE).getValue().toString();
            String username =
                    msgCtx.getConfiguration().getAxisConfiguration().getParameter(
                            IDP_LOGIN_USERNAME).getValue().toString();
            String password =
                    msgCtx.getConfiguration().getAxisConfiguration().getParameter(
                            IDP_LOGIN_PASSWORD).getValue().toString();
            OAuth2TokenValidationServiceStub stub =
                    new OAuth2TokenValidationServiceStub(this.getConfigContext(), identityServerUrl);
            ServiceClient client = stub._getServiceClient();
            Options options = client.getOptions();
            HttpTransportProperties.Authenticator authenticator = new HttpTransportProperties.Authenticator();
            authenticator.setUsername(username);
            authenticator.setPassword(password);
            authenticator.setPreemptiveAuthentication(true);
            options.setProperty(HTTPConstants.AUTHENTICATE, authenticator);
            client.setOptions(options);
            OAuth2TokenValidationRequestDTO dto = this.createOAuthValidatorDTO(msgCtx);
            return stub.validate(dto).getValid();
        }catch(Exception e){
            log.error("Error occurred while processing the message", e);
            return false;
        }
    }
    private OAuth2TokenValidationRequestDTO createOAuthValidatorDTO(MessageContext msgCtx) {
        OAuth2TokenValidationRequestDTO dto = new OAuth2TokenValidationRequestDTO();
        Map headers = (Map) ((Axis2MessageContext) msgCtx).getAxis2MessageContext().
                getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);
        String apiKey = null;
        if (headers != null) {
            apiKey = extractCustomerKeyFromAuthHeader(headers);
        }
        OAuth2TokenValidationRequestDTO_OAuth2AccessToken token =
                new OAuth2TokenValidationRequestDTO_OAuth2AccessToken();
        token.setTokenType("bearer");
        token.setIdentifier(apiKey);
        dto.setAccessToken(token);
        return dto;
    }
    private String extractCustomerKeyFromAuthHeader(Map headersMap) {
        //From 1.0.7 version of this component onwards remove the OAuth authorization header from
        // the message is configurable. So we dont need to remove headers at this point.
        String authHeader = (String) headersMap.get(HttpHeaders.AUTHORIZATION);
        if (authHeader == null) {
            return null;
        }
        if (authHeader.startsWith("OAuth ") || authHeader.startsWith("oauth ")) {
            authHeader = authHeader.substring(authHeader.indexOf("o"));
        }
        String[] headers = authHeader.split(OAUTH_HEADER_SPLITTER);
        if (headers != null) {
            for (String header : headers) {
                String[] elements = header.split(CONSUMER_KEY_SEGMENT_DELIMITER);
                if (elements != null && elements.length > 1) {
                    boolean isConsumerKeyHeaderAvailable = false;
                    for (String element : elements) {
                        if (!"".equals(element.trim())) {
                            if (CONSUMER_KEY_HEADER.equals(element.trim())) {
                                isConsumerKeyHeaderAvailable = true;
                            } else if (isConsumerKeyHeaderAvailable) {
                                return removeLeadingAndTrailing(element.trim());
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
    private String removeLeadingAndTrailing(String base) {
        String result = base;
        if (base.startsWith("\"") || base.endsWith("\"")) {
            result = base.replace("\"", "");
        }
        return result.trim();
    }
    @Override
    public boolean handleResponse(MessageContext messageContext) {
        return true;
    }
    @Override
    public void init(SynapseEnvironment synapseEnvironment) {
        try {
            this.configContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null);
        } catch (AxisFault axisFault) {
            log.error("Error occurred while initializing Configuration Context", axisFault);
        }
    }
    @Override
    public void destroy() {
        this.configContext = null;
    }
    private ConfigurationContext getConfigContext() {
        return configContext;
    }
}