package io.github.lunasaw.gbproxy.server.transimit.request.notify;

import io.github.lunasaw.gb28181.common.entity.notify.DeviceOtherUpdateNotify;
import io.github.lunasaw.sip.common.entity.Device;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * @author luna
 * @date 2023/12/29
 */
@Component
@ConditionalOnMissingBean(NotifyProcessorServer.class)
public class CustomNotifyProcessorServer implements NotifyProcessorServer {
    @Override
    public void deviceNotifyUpdate(String userId, DeviceOtherUpdateNotify deviceOtherUpdateNotify) {

    }

    @Override
    public Device getToDevice(String userId) {
        return null;
    }

    @Override
    public Device getFromDevice() {
        return null;
    }
}
