package com.example.schoolairdroprefactoredition.domain;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

public class DomainOffline implements Serializable {

    /**
     * success : true
     * message : 离线消息获取成功
     * data : [{"offlineID":4,"fingerPrint":"715b07be-7e89-4120-bbf8-b65cb351a345","senderID":"15","receiverID":"16","message":"测试测试测试测试地第n条消息正在发达送松松","messageType":-1,"sendTime":"2021-01-21T11:39:19.000+0000","received":0},{"offlineID":3,"fingerPrint":"8c74fbc3-7625-4b1b-82f4-34908400221a","senderID":"15","receiverID":"16","message":"这个到底多少钱这么多害怕2花不玩的吗你这个人怎么对与这个样子是什么意思呢","messageType":-1,"sendTime":"2021-01-21T11:38:46.000+0000","received":0},{"offlineID":2,"fingerPrint":"40aca6c1-79d5-4526-9b22-0353e9cfe276","senderID":"15","receiverID":"16","message":"哦哦哈哈哈还有这种哦你事情的吗，还有这种人？可能这就是人家不是某某某的缘故那么这件事情会变得怎么会这个样子呢","messageType":-1,"sendTime":"2021-01-21T11:36:07.000+0000","received":0},{"offlineID":1,"fingerPrint":"46319d09-ba23-457a-b903-e6d80b60bbe4","senderID":"15","receiverID":"16","message":"至于这个问题吗3你应该要去问她而不是我，毕竟决定全豆子啊她哪里，我完全没有任何异议，换研制只要她同意一切都你呢个水到渠成","messageType":-1,"sendTime":"2021-01-21T11:33:10.000+0000","received":0}]
     */

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    /**
     * offlineID : 4
     * fingerPrint : 715b07be-7e89-4120-bbf8-b65cb351a345
     * senderID : 15
     * receiverID : 16
     * message : 测试测试测试测试地第n条消息正在发达送松松
     * messageType : -1
     * sendTime : 2021-01-21T11:39:19.000+0000
     * received : 0
     */



    private List<DataBean> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {

        @NotNull
        @Override
        public String toString() {
            return "DataBean{" +
                    "offline_id=" + offline_id +
                    ", finger_print='" + finger_print + '\'' +
                    ", sender_id='" + sender_id + '\'' +
                    ", receiver_id='" + receiver_id + '\'' +
                    ", message='" + message + '\'' +
                    ", message_type=" + message_type +
                    ", send_time=" + send_time +
                    ", received=" + received +
                    '}';
        }

        private int offline_id;
        private String finger_print;
        private String sender_id;
        private String receiver_id;
        private String message;
        private int message_type;
        private long send_time;
        private int received;

        public int getOffline_id() {
            return offline_id;
        }

        public void setOffline_id(int offline_id) {
            this.offline_id = offline_id;
        }

        public String getFinger_print() {
            return finger_print;
        }

        public void setFinger_print(String finger_print) {
            this.finger_print = finger_print;
        }

        public String getSender_id() {
            return sender_id;
        }

        public void setSender_id(String sender_id) {
            this.sender_id = sender_id;
        }

        public String getReceiver_id() {
            return receiver_id;
        }

        public void setReceiver_id(String receiver_id) {
            this.receiver_id = receiver_id;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getMessage_type() {
            return message_type;
        }

        public void setMessage_type(int message_type) {
            this.message_type = message_type;
        }

        public long getSend_time() {
            return send_time;
        }

        public void setSend_time(long send_time) {
            this.send_time = send_time;
        }

        public int getReceived() {
            return received;
        }

        public void setReceived(int received) {
            this.received = received;
        }
    }
}
