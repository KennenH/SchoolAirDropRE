package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.schoolairdroprefactoredition.R;

public class CreditAssessment extends CardView {
    public static final int TYPE_MONTHLY = R.string.monthlyAssessment;
    public static final int TYPE_REPORT = R.string.reportAssessment;

    public static final int DES_MONTHLY = R.string.monthlyAssessmentDes;
    public static final int DES_REPORT = R.string.reportAssessmentDes;

    public static final int RESULT_1 = R.string.assessResult1;
    public static final int RESULT_2 = R.string.assessResult2;
    public static final int RESULT_3 = R.string.assessResult3;
    public static final int RESULT_4 = R.string.assessResult4;
    public static final int RESULT_5 = R.string.assessResult5;

    public static final int CHANGE_UP = R.drawable.ic_up;
    public static final int CHANGE_DOWN = R.drawable.ic_down;

    public static final int COLOR_GOOD = R.color.colorCreditGood;
    public static final int COLOR_lOW = R.color.colorCreditLow;

    private TextView mResultType;
    private TextView mTime;
    private TextView mDescription;
    private TextView mResult;

    public CreditAssessment(@NonNull Context context) {
        this(context, null);
    }

    public CreditAssessment(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CreditAssessment(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.item_credit_assessment, this, true);

        mResultType = findViewById(R.id.assessment_type);
        mTime = findViewById(R.id.assessment_time);
        mDescription = findViewById(R.id.assessment_description);
        mResult = findViewById(R.id.assessment_result);
    }

//    public void setData(TestCreditHistory bean) {
//        switch (bean.getCredits()) {
//            case RESULT_TYPE1:
//                mResultType.setText(RESULT_1);
//                mTime.setText();
//                mDescription.setText();
//                mResult.setText();
//            case RESULT_TYPE2:
//
//            case RESULT_TYPE3:
//
//            case RESULT_TYPE4:
//
//            case RESULT_TYPE5:
//
//        }
//    }
}
