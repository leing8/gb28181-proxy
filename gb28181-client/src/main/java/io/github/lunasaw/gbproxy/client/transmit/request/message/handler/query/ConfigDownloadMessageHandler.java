package io.github.lunasaw.gbproxy.client.transmit.request.message.handler.query;

import javax.sip.RequestEvent;

import io.github.lunasaw.gbproxy.client.transmit.cmd.ClientSendCmd;
import io.github.lunasaw.sip.common.entity.FromDevice;
import io.github.lunasaw.sip.common.entity.ToDevice;
import io.github.lunasaw.sip.common.entity.base.DeviceSession;
import io.github.lunasaw.sip.common.entity.query.DeviceConfigDownload;
import io.github.lunasaw.sip.common.entity.response.DeviceConfigResponse;
import org.springframework.stereotype.Component;

import io.github.lunasaw.gbproxy.client.transmit.request.message.MessageClientHandlerAbstract;
import io.github.lunasaw.gbproxy.client.transmit.request.message.MessageProcessorClient;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 响应设备配置查询
 * 
 * @author luna
 * @date 2023/10/19
 */
@Component
@Slf4j
@Getter
@Setter
public class ConfigDownloadMessageHandler extends MessageClientHandlerAbstract {

    public static final String CMD_TYPE = "ConfigDownload";

    private String             cmdType  = CMD_TYPE;

    public ConfigDownloadMessageHandler(MessageProcessorClient messageProcessorClient) {
        super(messageProcessorClient);
    }

    @Override
    public String getRootType() {
        return QUERY;
    }


    @Override
    public void handForEvt(RequestEvent event) {
        DeviceSession deviceSession = getDeviceSession(event);
        String userId = deviceSession.getUserId();
        String sipId = deviceSession.getSipId();

        // 设备查询
        FromDevice fromDevice = (FromDevice)messageProcessorClient.getFromDevice();
        ToDevice toDevice = (ToDevice)messageProcessorClient.getToDevice(sipId);

        DeviceConfigDownload deviceConfigDownload = parseRequest(event, fromDevice.getCharset(), DeviceConfigDownload.class);

        DeviceConfigResponse deviceConfigResponse = messageProcessorClient.getDeviceConfigResponse(deviceConfigDownload);
        deviceConfigResponse.setSn(deviceConfigDownload.getSn());

        ClientSendCmd.deviceConfigResponse(fromDevice, toDevice, deviceConfigResponse);
    }

    @Override
    public String getCmdType() {
        return cmdType;
    }
}
