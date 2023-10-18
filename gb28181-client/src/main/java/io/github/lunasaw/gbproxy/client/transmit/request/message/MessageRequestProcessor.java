package io.github.lunasaw.gbproxy.client.transmit.request.message;

import javax.sip.RequestEvent;

import gov.nist.javax.sip.message.SIPRequest;
import io.github.lunasaw.sip.common.entity.SipTransaction;
import io.github.lunasaw.sip.common.transmit.event.request.SipRequestProcessorAbstract;
import io.github.lunasaw.sip.common.utils.SipUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author luna
 */
@Component
@Getter
@Setter
@Slf4j
public class MessageRequestProcessor extends SipRequestProcessorAbstract {

    public static final String METHOD = "MESSAGE";


    private String method = METHOD;

    @Override
    public void process(RequestEvent evt) {
        SIPRequest sipRequest = (SIPRequest)evt.getRequest();

        if (dealMessage(sipRequest)) {
            success(sipRequest);
        } else {
            dealMessage(sipRequest);
        }
    }

    public boolean dealMessage(SIPRequest sipRequest) {
        // 发送方用户
        String userId = SipUtils.getUserIdFromFromHeader(sipRequest);
        // 收到处理

        // 根据不同cmdType处理
        return true;
    }

    public void success(SIPRequest sipRequest) {
        // 成功处理
    }

    public void fail(SIPRequest sipRequest) {
        // 失败回复
    }
}
