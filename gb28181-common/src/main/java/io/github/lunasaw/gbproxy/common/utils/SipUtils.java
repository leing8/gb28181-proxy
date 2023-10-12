package io.github.lunasaw.gbproxy.common.utils;

import org.springframework.util.ObjectUtils;

import javax.sip.PeerUnavailableException;
import javax.sip.SipFactory;
import javax.sip.header.UserAgentHeader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author weidian
 */
public class SipUtils {

    public static UserAgentHeader createUserAgentHeader(String agent) throws PeerUnavailableException, ParseException {
        List<String> agentParam = new ArrayList<>();
        agentParam.add(agent);

        return SipFactory.getInstance().createHeaderFactory().createUserAgentHeader(agentParam);
    }

}