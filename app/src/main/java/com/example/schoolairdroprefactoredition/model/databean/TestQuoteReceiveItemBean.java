package com.example.schoolairdroprefactoredition.model.databean;

import androidx.annotation.IntDef;

public class TestQuoteReceiveItemBean {
    private boolean isHandled = true;

    /**
     * 用注解将方法参数或返回值限定在指定的值中
     * https://stackoverflow.com/a/37148627/12429663
     */
    @IntDef({ACCEPT, REFUSE, OUT_OF_DATE})
    public @interface HandleResult {
    }

    public static final int ACCEPT = 666;
    public static final int REFUSE = 555;
    public static final int OUT_OF_DATE = 444;
    private int result = 0;

    public boolean isHandled() {
        return isHandled;
    }

    public void setHandled(boolean handled) {
        isHandled = handled;
    }

    @HandleResult
    public int getResult() {
        return result;
    }

    public void setResult(@HandleResult int result) {
        this.result = result;
    }
}
