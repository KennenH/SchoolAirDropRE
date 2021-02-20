package com.example.schoolairdroprefactoredition.domain;

import java.io.Serializable;
import java.util.List;

/**
 * 获取七牛云上传凭证之后获取的文件路径前缀和文件名
 */
public class DomainUploadPath implements Serializable {

    @Override
    public String toString() {
        return "DomainUploadPath{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    private int code;

    private String msg;

    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {

        @Override
        public String toString() {
            return "DataBean{" +
                    "keys=" + keys +
                    ", path='" + taskId + '\'' +
                    '}';
        }

        /**
         * 文件名
         */
        private List<String> keys;

        /**
         * 任务组
         */
        private String taskId;

        public List<String> getKeys() {
            return keys;
        }

        public void setKeys(List<String> keys) {
            this.keys = keys;
        }

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }
    }
}
