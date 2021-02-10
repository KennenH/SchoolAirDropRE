package com.example.schoolairdroprefactoredition.domain;

import com.blankj.utilcode.util.TimeUtils;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class DomainOfflineNum implements Serializable {

    @Override
    public String toString() {
        return "DomainOfflineNum{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    /**
     * success : true
     * message : 离线消息数量获取成功
     * data : [{"sender_id":"14","receiver_id":"16","offline_num":7,"finger_print":"3d22e95e-9750-4be3-81a2-8f7545c96390","sender_info":{"sender_id":14,"sender_name":"冲冲Developer","sender_avatar":"assets/user/avatars/14_20201209000844.jpg"},"offline":[{"finger_print":"3c1dfaa6-36ae-4ba9-a1b9-5d6f351594ab","message_type":0,"message":"回家付款哦II如有人同花顺今年附近大家符合解放军不减肥据还能男女才能看到看到可","send_time":"2021-01-21T14:00:53.000+0000"},{"finger_print":"17ac394e-cff0-4f04-8b14-24ddd4d8537f","message_type":0,"message":"回家啊开始看到五十三阿斯房间里开始劳动法看看发生领导电视卡。，上的方面，地方士大夫上的","send_time":"2021-01-21T14:01:09.000+0000"},{"finger_print":"b6501698-a033-4c3b-a8dc-b14a355e815c","message_type":0,"message":"真不戳","send_time":"2021-01-21T14:01:17.000+0000"},{"finger_print":"49ded713-4dab-4b84-bdc1-8deb89bb9311","message_type":0,"message":"hahhaha呵呵呵呵呵嘿嘿嘿嘿和","send_time":"2021-01-27T09:01:29.000+0000"},{"finger_print":"9ab3c263-d5ee-4b03-b83f-18cea6e6d00b","message_type":0,"message":"呼呼呼呼呼呼哦哦哦哦哦哦嚯嚯嚯嚯嚯","send_time":"2021-01-27T09:02:05.000+0000"},{"finger_print":"16578d1e-cda4-4852-a7c5-42fe9309d47c","message_type":0,"message":"嘻嘻嘻嘻咦翼咦咦","send_time":"2021-01-27T09:02:27.000+0000"},{"finger_print":"3d22e95e-9750-4be3-81a2-8f7545c96390","message_type":0,"message":"再试一遍","send_time":"2021-01-27T09:02:38.000+0000"}]},{"sender_id":"15","receiver_id":"16","offline_num":4,"finger_print":"715b07be-7e89-4120-bbf8-b65cb351a345","sender_info":{"sender_id":15,"sender_name":"kennenx","sender_avatar":"assets/user/avatars/796394343681228800.jpg"},"offline":[{"finger_print":"46319d09-ba23-457a-b903-e6d80b60bbe4","message_type":0,"message":"至于这个问题吗3你应该要去问她而不是我，毕竟决定全豆子啊她哪里，我完全没有任何异议，换研制只要她同意一切都你呢个水到渠成","send_time":"2021-01-21T11:33:10.000+0000"},{"finger_print":"40aca6c1-79d5-4526-9b22-0353e9cfe276","message_type":0,"message":"哦哦哈哈哈还有这种哦你事情的吗，还有这种人？可能这就是人家不是某某某的缘故那么这件事情会变得怎么会这个样子呢","send_time":"2021-01-21T11:36:07.000+0000"},{"finger_print":"8c74fbc3-7625-4b1b-82f4-34908400221a","message_type":0,"message":"这个到底多少钱这么多害怕2花不玩的吗你这个人怎么对与这个样子是什么意思呢","send_time":"2021-01-21T11:38:46.000+0000"},{"finger_print":"715b07be-7e89-4120-bbf8-b65cb351a345","message_type":0,"message":"测试测试测试测试地第n条消息正在发达送松松","send_time":"2021-01-21T11:39:19.000+0000"}]},{"sender_id":"17","receiver_id":"16","offline_num":2,"finger_print":"388a685b-dc05-495c-a145-c401e90ae558","sender_info":{"sender_id":17,"sender_name":"大帅哥","sender_avatar":"assets/user/avatars/17_20201109155445.jpg"},"offline":[{"finger_print":"d0fba29f-9531-4a78-aa78-16f428f787a3","message_type":0,"message":"我是测我是测试测试啊老师大家好卡时间里卡上的","send_time":"2021-01-27T09:03:20.000+0000"},{"finger_print":"388a685b-dc05-495c-a145-c401e90ae558","message_type":0,"message":"为什么打击啊健康撒大家拉萨空间领导卡上的","send_time":"2021-01-27T09:03:27.000+0000"}]},{"sender_id":"18","receiver_id":"16","offline_num":2,"finger_print":"26a92574-5e8d-4b58-b586-fa2323af02e3","sender_info":{"sender_id":18,"sender_name":"Sage","sender_avatar":"assets/user/avatars/18_20201112003104.jpg"},"offline":[{"finger_print":"30028004-1469-4ca9-925c-c8229397b6c8","message_type":0,"message":"阿斯大十大上的阿斯顿阿斯顿阿斯顿阿斯顿观点风格电饭锅","send_time":"2021-01-27T09:04:09.000+0000"},{"finger_print":"26a92574-5e8d-4b58-b586-fa2323af02e3","message_type":0,"message":"这个东西哦怎么卖","send_time":"2021-01-27T09:04:21.000+0000"}]}]
     */

    @SerializedName("success")
    private boolean success;
    @SerializedName("message")
    private String message;
    /**
     * sender_id : 14
     * receiver_id : 16
     * offline_num : 7
     * finger_print : 3d22e95e-9750-4be3-81a2-8f7545c96390
     * sender_info : {"sender_id":14,"sender_name":"冲冲Developer","sender_avatar":"assets/user/avatars/14_20201209000844.jpg"}
     * offline : [{"finger_print":"3c1dfaa6-36ae-4ba9-a1b9-5d6f351594ab","message_type":0,"message":"回家付款哦II如有人同花顺今年附近大家符合解放军不减肥据还能男女才能看到看到可","send_time":"2021-01-21T14:00:53.000+0000"},{"finger_print":"17ac394e-cff0-4f04-8b14-24ddd4d8537f","message_type":0,"message":"回家啊开始看到五十三阿斯房间里开始劳动法看看发生领导电视卡。，上的方面，地方士大夫上的","send_time":"2021-01-21T14:01:09.000+0000"},{"finger_print":"b6501698-a033-4c3b-a8dc-b14a355e815c","message_type":0,"message":"真不戳","send_time":"2021-01-21T14:01:17.000+0000"},{"finger_print":"49ded713-4dab-4b84-bdc1-8deb89bb9311","message_type":0,"message":"hahhaha呵呵呵呵呵嘿嘿嘿嘿和","send_time":"2021-01-27T09:01:29.000+0000"},{"finger_print":"9ab3c263-d5ee-4b03-b83f-18cea6e6d00b","message_type":0,"message":"呼呼呼呼呼呼哦哦哦哦哦哦嚯嚯嚯嚯嚯","send_time":"2021-01-27T09:02:05.000+0000"},{"finger_print":"16578d1e-cda4-4852-a7c5-42fe9309d47c","message_type":0,"message":"嘻嘻嘻嘻咦翼咦咦","send_time":"2021-01-27T09:02:27.000+0000"},{"finger_print":"3d22e95e-9750-4be3-81a2-8f7545c96390","message_type":0,"message":"再试一遍","send_time":"2021-01-27T09:02:38.000+0000"}]
     */

    @SerializedName("data")
    private List<DataBean> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {

        @Override
        public String toString() {
            return "DataBean{" +
                    "senderId='" + senderId + '\'' +
                    ", receiverId='" + receiverId + '\'' +
                    ", offlineNum=" + offlineNum +
                    ", fingerPrint='" + fingerPrint + '\'' +
                    ", senderInfo=" + senderInfo +
                    ", offline=" + offline +
                    '}';
        }

        @SerializedName("sender_id")
        private String senderId;
        @SerializedName("receiver_id")
        private String receiverId;
        @SerializedName("offline_num")
        private int offlineNum;
        @SerializedName("finger_print")
        private String fingerPrint;
        /**
         * sender_id : 14
         * sender_name : 冲冲Developer
         * sender_avatar : assets/user/avatars/14_20201209000844.jpg
         */

        @SerializedName("sender_info")
        private SenderInfoBean senderInfo;
        /**
         * finger_print : 3c1dfaa6-36ae-4ba9-a1b9-5d6f351594ab
         * message_type : 0
         * message : 回家付款哦II如有人同花顺今年附近大家符合解放军不减肥据还能男女才能看到看到可
         * send_time : 2021-01-21T14:00:53.000+0000
         */

        @SerializedName("offline")
        private List<OfflineBean> offline;

        public String getSenderId() {
            return senderId;
        }

        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }

        public String getReceiverId() {
            return receiverId;
        }

        public void setReceiverId(String receiverId) {
            this.receiverId = receiverId;
        }

        public int getOfflineNum() {
            return offlineNum;
        }

        public void setOfflineNum(int offlineNum) {
            this.offlineNum = offlineNum;
        }

        public String getFingerPrint() {
            return fingerPrint;
        }

        public void setFingerPrint(String fingerPrint) {
            this.fingerPrint = fingerPrint;
        }

        public SenderInfoBean getSenderInfo() {
            return senderInfo;
        }

        public void setSenderInfo(SenderInfoBean senderInfo) {
            this.senderInfo = senderInfo;
        }

        public List<OfflineBean> getOffline() {
            return offline;
        }

        public void setOffline(List<OfflineBean> offline) {
            this.offline = offline;
        }

        public static class SenderInfoBean implements Serializable {
            @SerializedName("sender_id")
            private int senderId;
            @SerializedName("sender_name")
            private String senderName;
            @SerializedName("sender_avatar")
            private String senderAvatar;

            public int getSenderId() {
                return senderId;
            }

            public void setSenderId(int senderId) {
                this.senderId = senderId;
            }

            public String getSenderName() {
                return senderName;
            }

            public void setSenderName(String senderName) {
                this.senderName = senderName;
            }

            public String getSenderAvatar() {
                return senderAvatar;
            }

            public void setSenderAvatar(String senderAvatar) {
                this.senderAvatar = senderAvatar;
            }
        }

        public static class OfflineBean implements Serializable {

            @Override
            public String toString() {
                return "OfflineBean{" +
                        "fingerPrint='" + fingerPrint + '\'' +
                        ", messageType=" + messageType +
                        ", message='" + message + '\'' +
                        ", sendTime=" + sendTime +
                        '}';
            }

            @SerializedName("finger_print")
            private String fingerPrint;
            @SerializedName("message_type")
            private int messageType;
            @SerializedName("message")
            private String message;
            @SerializedName("send_time")
            private Date sendTime;

            public String getFingerPrint() {
                return fingerPrint;
            }

            public void setFingerPrint(String fingerPrint) {
                this.fingerPrint = fingerPrint;
            }

            public int getMessageType() {
                return messageType;
            }

            public void setMessageType(int messageType) {
                this.messageType = messageType;
            }

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }

            public Date getSendTime() {
                return sendTime;
            }

            public void setSendTime(String sendTime) {
                this.sendTime = TimeUtils.string2Date(sendTime.substring(0, 19).replace('T', ' '));
            }
        }
    }
}

/*
{
    "success": true,
    "message": "离线消息数量获取成功",
    "data": [
        {
            "sender_id": "14",
            "receiver_id": "16",
            "offline_num": 7,
            "finger_print": "3d22e95e-9750-4be3-81a2-8f7545c96390",
            "sender_info": {
                "sender_id": 14,
                "sender_name": "冲冲Developer",
                "sender_avatar": "assets/user/avatars/14_20201209000844.jpg"
            },
            "offline": [
                {
                    "finger_print": "3c1dfaa6-36ae-4ba9-a1b9-5d6f351594ab",
                    "message_type": 0,
                    "message": "回家付款哦II如有人同花顺今年附近大家符合解放军不减肥据还能男女才能看到看到可",
                    "send_time": "2021-01-21T14:00:53.000+0000"
                },
                {
                    "finger_print": "17ac394e-cff0-4f04-8b14-24ddd4d8537f",
                    "message_type": 0,
                    "message": "回家啊开始看到五十三阿斯房间里开始劳动法看看发生领导电视卡。，上的方面，地方士大夫上的",
                    "send_time": "2021-01-21T14:01:09.000+0000"
                },
                {
                    "finger_print": "b6501698-a033-4c3b-a8dc-b14a355e815c",
                    "message_type": 0,
                    "message": "真不戳",
                    "send_time": "2021-01-21T14:01:17.000+0000"
                },
                {
                    "finger_print": "49ded713-4dab-4b84-bdc1-8deb89bb9311",
                    "message_type": 0,
                    "message": "hahhaha呵呵呵呵呵嘿嘿嘿嘿和",
                    "send_time": "2021-01-27T09:01:29.000+0000"
                },
                {
                    "finger_print": "9ab3c263-d5ee-4b03-b83f-18cea6e6d00b",
                    "message_type": 0,
                    "message": "呼呼呼呼呼呼哦哦哦哦哦哦嚯嚯嚯嚯嚯",
                    "send_time": "2021-01-27T09:02:05.000+0000"
                },
                {
                    "finger_print": "16578d1e-cda4-4852-a7c5-42fe9309d47c",
                    "message_type": 0,
                    "message": "嘻嘻嘻嘻咦翼咦咦",
                    "send_time": "2021-01-27T09:02:27.000+0000"
                },
                {
                    "finger_print": "3d22e95e-9750-4be3-81a2-8f7545c96390",
                    "message_type": 0,
                    "message": "再试一遍",
                    "send_time": "2021-01-27T09:02:38.000+0000"
                }
            ]
        },
        {
            "sender_id": "15",
            "receiver_id": "16",
            "offline_num": 4,
            "finger_print": "715b07be-7e89-4120-bbf8-b65cb351a345",
            "sender_info": {
                "sender_id": 15,
                "sender_name": "kennenx",
                "sender_avatar": "assets/user/avatars/796394343681228800.jpg"
            },
            "offline": [
                {
                    "finger_print": "46319d09-ba23-457a-b903-e6d80b60bbe4",
                    "message_type": 0,
                    "message": "至于这个问题吗3你应该要去问她而不是我，毕竟决定全豆子啊她哪里，我完全没有任何异议，换研制只要她同意一切都你呢个水到渠成",
                    "send_time": "2021-01-21T11:33:10.000+0000"
                },
                {
                    "finger_print": "40aca6c1-79d5-4526-9b22-0353e9cfe276",
                    "message_type": 0,
                    "message": "哦哦哈哈哈还有这种哦你事情的吗，还有这种人？可能这就是人家不是某某某的缘故那么这件事情会变得怎么会这个样子呢",
                    "send_time": "2021-01-21T11:36:07.000+0000"
                },
                {
                    "finger_print": "8c74fbc3-7625-4b1b-82f4-34908400221a",
                    "message_type": 0,
                    "message": "这个到底多少钱这么多害怕2花不玩的吗你这个人怎么对与这个样子是什么意思呢",
                    "send_time": "2021-01-21T11:38:46.000+0000"
                },
                {
                    "finger_print": "715b07be-7e89-4120-bbf8-b65cb351a345",
                    "message_type": 0,
                    "message": "测试测试测试测试地第n条消息正在发达送松松",
                    "send_time": "2021-01-21T11:39:19.000+0000"
                }
            ]
        },
        {
            "sender_id": "17",
            "receiver_id": "16",
            "offline_num": 2,
            "finger_print": "388a685b-dc05-495c-a145-c401e90ae558",
            "sender_info": {
                "sender_id": 17,
                "sender_name": "大帅哥",
                "sender_avatar": "assets/user/avatars/17_20201109155445.jpg"
            },
            "offline": [
                {
                    "finger_print": "d0fba29f-9531-4a78-aa78-16f428f787a3",
                    "message_type": 0,
                    "message": "我是测我是测试测试啊老师大家好卡时间里卡上的",
                    "send_time": "2021-01-27T09:03:20.000+0000"
                },
                {
                    "finger_print": "388a685b-dc05-495c-a145-c401e90ae558",
                    "message_type": 0,
                    "message": "为什么打击啊健康撒大家拉萨空间领导卡上的",
                    "send_time": "2021-01-27T09:03:27.000+0000"
                }
            ]
        },
        {
            "sender_id": "18",
            "receiver_id": "16",
            "offline_num": 2,
            "finger_print": "26a92574-5e8d-4b58-b586-fa2323af02e3",
            "sender_info": {
                "sender_id": 18,
                "sender_name": "Sage",
                "sender_avatar": "assets/user/avatars/18_20201112003104.jpg"
            },
            "offline": [
                {
                    "finger_print": "30028004-1469-4ca9-925c-c8229397b6c8",
                    "message_type": 0,
                    "message": "阿斯大十大上的阿斯顿阿斯顿阿斯顿阿斯顿观点风格电饭锅",
                    "send_time": "2021-01-27T09:04:09.000+0000"
                },
                {
                    "finger_print": "26a92574-5e8d-4b58-b586-fa2323af02e3",
                    "message_type": 0,
                    "message": "这个东西哦怎么卖",
                    "send_time": "2021-01-27T09:04:21.000+0000"
                }
            ]
        }
    ]
}
*/