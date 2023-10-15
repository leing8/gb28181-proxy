package io.github.lunasaw.gbproxy.client.entity.notify;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.github.lunasaw.sip.common.entity.xml.DeviceBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <?xml version="1.0" encoding="gb2312"?>
 * <Notify>
 * <CmdType>MediaStatusNotify</CmdType>
 * <SN>226063</SN>
 * <DeviceID>12312</DeviceID>
 * <NotifyType>121</NotifyType>
 * </Notify>

 * @author luna
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "Notify")
@XmlAccessorType(XmlAccessType.FIELD)
public class MobilePositionNotify extends DeviceBase {


    @XmlElement(name = "NotifyType")
    private String notifyType;

    public MobilePositionNotify(String cmdType, String sn, String deviceId) {
        super(cmdType, sn, deviceId);
    }

}
