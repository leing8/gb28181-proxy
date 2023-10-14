package io.github.lunasaw.gbproxy.common.transmit;

import java.text.ParseException;

import javax.sip.SipException;
import javax.sip.header.CallIdHeader;
import javax.sip.header.UserAgentHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.Message;
import javax.sip.message.Request;
import javax.sip.message.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import gov.nist.javax.sip.SipProviderImpl;
import io.github.lunasaw.gbproxy.common.constant.Constant;
import io.github.lunasaw.gbproxy.common.layer.SipLayer;
import io.github.lunasaw.gbproxy.common.transmit.event.Event;
import io.github.lunasaw.gbproxy.common.transmit.event.SipSubscribe;
import io.github.lunasaw.gbproxy.common.utils.SipRequestUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 发送SIP消息
 * 
 * @author lin
 */
@Slf4j
@Data
@Component
public class SipSender {

    public void transmitRequest(String ip, Message message) throws SipException, ParseException {
        transmitRequest(ip, message, null, null);
    }

    public void transmitRequest(String ip, Message message, Event errorEvent) throws SipException, ParseException {
        transmitRequest(ip, message, errorEvent, null);
    }

    public void transmitRequest(String ip, Message message, Event errorEvent, Event okEvent) throws SipException {
        ViaHeader viaHeader = (ViaHeader)message.getHeader(ViaHeader.NAME);
        String transport = "UDP";
        if (viaHeader == null) {
            log.warn("[消息头缺失]： ViaHeader， 使用默认的UDP方式处理数据");
        } else {
            transport = viaHeader.getTransport();
        }
        if (message.getHeader(UserAgentHeader.NAME) == null) {
            message.addHeader(SipRequestUtils.createUserAgentHeader(Constant.AGENT));
        }

        CallIdHeader callIdHeader = (CallIdHeader)message.getHeader(CallIdHeader.NAME);
        // 添加错误订阅
        if (errorEvent != null) {
            SipSubscribe.addErrorSubscribe(callIdHeader.getCallId(), (eventResult -> {
                errorEvent.response(eventResult);
                SipSubscribe.removeErrorSubscribe(eventResult.callId);
                SipSubscribe.removeOkSubscribe(eventResult.callId);
            }));
        }
        // 添加订阅
        if (okEvent != null) {
            SipSubscribe.addOkSubscribe(callIdHeader.getCallId(), eventResult -> {
                okEvent.response(eventResult);
                SipSubscribe.removeOkSubscribe(eventResult.callId);
                SipSubscribe.removeErrorSubscribe(eventResult.callId);
            });
        }
        if (Constant.TCP.equals(transport)) {
            SipProviderImpl tcpSipProvider = SipLayer.getTcpSipProvider(ip);
            if (tcpSipProvider == null) {
                log.error("[发送信息失败] 未找到tcp://{}的监听信息", ip);
                return;
            }
            if (message instanceof Request) {
                tcpSipProvider.sendRequest((Request)message);
            } else if (message instanceof Response) {
                tcpSipProvider.sendResponse((Response)message);
            }

        } else if (Constant.UDP.equals(transport)) {
            SipProviderImpl sipProvider = SipLayer.getUdpSipProvider(ip);
            if (sipProvider == null) {
                log.error("[发送信息失败] 未找到udp://{}的监听信息", ip);
                return;
            }
            if (message instanceof Request) {
                sipProvider.sendRequest((Request)message);
            } else if (message instanceof Response) {
                sipProvider.sendResponse((Response)message);
            }
        }
    }

    public CallIdHeader getNewCallIdHeader(String ip, String transport) {
        if (ObjectUtils.isEmpty(transport)) {
            return SipLayer.getUdpSipProvider().getNewCallId();
        }
        SipProviderImpl sipProvider;
        if (ObjectUtils.isEmpty(ip)) {
            sipProvider = transport.equalsIgnoreCase(Constant.TCP) ? SipLayer.getTcpSipProvider()
                : SipLayer.getUdpSipProvider();
        } else {
            sipProvider = transport.equalsIgnoreCase(Constant.TCP) ? SipLayer.getTcpSipProvider(ip)
                : SipLayer.getUdpSipProvider(ip);
        }

        if (sipProvider == null) {
            sipProvider = SipLayer.getUdpSipProvider();
        }

        return sipProvider.getNewCallId();
    }
}
