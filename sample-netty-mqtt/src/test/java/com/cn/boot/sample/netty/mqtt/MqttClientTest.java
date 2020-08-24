package com.cn.boot.sample.netty.mqtt;

import com.cn.boot.sample.netty.mqtt.model.ReqDeviceData;
import com.cn.boot.sample.netty.mqtt.model.RspDeviceData;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.*;

import java.util.List;

@Slf4j
public class MqttClientTest implements Listener {
    private final String userId;
    private final String clientId;
    private final String host;
    private final int port;
    private ConnectionStatusCallback connectionStatusCallback;
    private ReceiveMessageCallback receiveMessageCallback;
    private ConnectionStatus connectionStatus;

    protected String mqttServerIp;
    protected long mqttServerPort;

    private transient MQTT mqtt = new MQTT();
    private transient CallbackConnection connection = null;


    public interface ReceiveMessageCallback {
        void onReceiveMessages(List<String> messageList, boolean hasMore, int size, long nowTime, long notifyTime);

        void onRecallMessage(long messageUid);
    }

    public interface ConnectionStatusCallback {
        void onConnectionStatusChanged(ConnectionStatus newStatus);
    }

    public interface SendMessageCallback {
        void onSuccess(long messageUid, long timestamp);

        void onFailure(int errorCode);
    }

    public enum ConnectionStatus {
        ConnectionStatus_Unconnected,
        ConnectionStatus_Connecting,
        ConnectionStatus_Connected,
    }

    public static void main(String[] args) {
        String userId;
        String clientId;

        userId = "66";
        clientId = "d001";

        String host = "127.0.0.1";
        int port = 50001;
        MqttClientTest client = new MqttClientTest(userId, clientId, host, port);

        ReqDeviceData.DeviceData req = ReqDeviceData.DeviceData.newBuilder()
                .setTag("hello")
                .setData("1111")
                .build();

        client.setConnectionStatusCallback((ConnectionStatus newStatus) -> {
            if (newStatus == ConnectionStatus.ConnectionStatus_Connected) {
                log.info("newStatus={}", newStatus);
                client.sendMessage(req, new SendMessageCallback() {
                    @Override
                    public void onSuccess(long messageUid, long timestamp) {
                        log.info("send success");
                    }

                    @Override
                    public void onFailure(int errorCode) {
                        log.info("send failure, errorCode={}", errorCode);
                    }
                });
            }
        });

        client.connect();

        try {
            Thread.sleep(1000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public MqttClientTest(String userId, String clientId, String host, int port) {
        this.userId = userId;

        this.clientId = clientId;
        this.host = host;
        this.port = port;

        this.setReceiveMessageCallback(new MqttClientTest.ReceiveMessageCallback() {
            @Override
            public void onReceiveMessages(List<String> messageList, boolean hasMore, int size, long nowTime, long notifyTime) {
                log.info("=======onReceiveMessages======");
            }

            @Override
            public void onRecallMessage(long messageUid) {
                log.info("recalled messages");
            }
        });
    }


    public void connect() {
        log.info("connect");
        try {
            mqttServerIp = host;
            mqttServerPort = port;
            log.info("mqttServerIp={} mqttServerPort={}", mqttServerIp, mqttServerPort);
            mqtt.setHost("tcp://" + mqttServerIp + ":" + mqttServerPort);
            mqtt.setVersion("3.1.1");
            mqtt.setKeepAlive((short) 180);

            mqtt.setClientId(clientId);
            mqtt.setConnectAttemptsMax(100);
            mqtt.setReconnectAttemptsMax(100);

            mqtt.setUserName(userId);
            mqtt.setPassword("123");


            connection = mqtt.callbackConnection();
            connection.listener(this);

            //connecting
            connectionStatus = ConnectionStatus.ConnectionStatus_Connecting;
            if (connectionStatusCallback != null) {
                connectionStatusCallback.onConnectionStatusChanged(connectionStatus);
            }

            connection.connect(new Callback<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    log.info("on connect success");

                    connectionStatus = ConnectionStatus.ConnectionStatus_Connected;
                    if (connectionStatusCallback != null) {
                        connectionStatusCallback.onConnectionStatusChanged(connectionStatus);
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {
                    log.info("on connect failure code={}", throwable.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(ReqDeviceData.DeviceData req, final SendMessageCallback callback) {
        byte[] data = req.toByteArray();
        connection.publish("MS", data, QoS.AT_LEAST_ONCE, false, new Callback<Void>() {
            @Override
            public void onSuccess(Void value) {
                log.info("on sendMessage success");
            }

            @Override
            public void onFailure(Throwable value) {
                log.info("on sendMessage failure code={}", value.getMessage());
                callback.onFailure(-1);
            }
        });
    }

    @Override
    public void onPublish(UTF8Buffer topic, Buffer body, Runnable ack) {
        try {
            RspDeviceData.DeviceData data = RspDeviceData.DeviceData.parseFrom(body.toByteArray());
            log.info("onPublish deviceNo={} tag={} data={}", data.getDeviceNo(), data.getTag(), data.getData());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        ack.run();
    }

    public void disconnect(boolean clearSession, final Callback<Void> onComplete) {
        this.connection.disconnect(onComplete);
    }

    @Override
    public void onConnected() {
        log.info("onConnected userId={}", userId);
        connectionStatus = ConnectionStatus.ConnectionStatus_Connecting;
        if (connectionStatusCallback != null) {
            connectionStatusCallback.onConnectionStatusChanged(connectionStatus);
        }
    }

    @Override
    public void onDisconnected() {
        log.info("onDisconnected" + userId);
        connectionStatus = ConnectionStatus.ConnectionStatus_Unconnected;
        if (connectionStatusCallback != null) {
            connectionStatusCallback.onConnectionStatusChanged(connectionStatus);
        }
    }

    @Override
    public void onFailure(Throwable value) {
        log.info("onDisconnected" + value.toString());
        if (connectionStatusCallback != null) {
            connectionStatusCallback.onConnectionStatusChanged(ConnectionStatus.ConnectionStatus_Unconnected);
        }
    }

    public void setConnectionStatusCallback(ConnectionStatusCallback connectionStatusCallback) {
        this.connectionStatusCallback = connectionStatusCallback;
    }

    public void setReceiveMessageCallback(ReceiveMessageCallback receiveMessageCallback) {
        this.receiveMessageCallback = receiveMessageCallback;
    }
}
