package com.example.schoolairdroprefactoredition.ui.adapter;

import android.os.Build;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.scene.credit.CreditActivity;
import com.example.schoolairdroprefactoredition.model.databean.TestCreditHistory;
import com.example.schoolairdroprefactoredition.ui.components.CreditAssessment;

import org.jetbrains.annotations.NotNull;

public class CreditRecyclerAdapter extends BaseQuickAdapter<TestCreditHistory, BaseViewHolder> {
    public CreditRecyclerAdapter() {
        super(R.layout.item_credit_assessment);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, TestCreditHistory data) {
        final TextView title = holder.itemView.findViewById(R.id.assessment_type);
        final ImageView change = holder.itemView.findViewById(R.id.assessment_change);
        final TextView time = holder.itemView.findViewById(R.id.assessment_time);
        final TextView description = holder.itemView.findViewById(R.id.assessment_description);
        final TextView result = holder.itemView.findViewById(R.id.assessment_result);

        final int type = data.getType();
        title.setText(type);
        if (type == CreditAssessment.TYPE_MONTHLY)
            description.setText(CreditAssessment.DES_MONTHLY);
        else
            description.setText(CreditAssessment.DES_REPORT);

        if (data.isUp())
            change.setImageResource(CreditAssessment.CHANGE_UP);
        else
            change.setImageResource(CreditAssessment.CHANGE_DOWN);

        switch (data.getCredits()) {
            case CreditActivity.CREDIT1:
                result.setText(CreditAssessment.RESULT_1);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    result.setTextColor(getContext().getResources().getColor(CreditAssessment.COLOR_lOW, getContext().getTheme()));
                else
                    result.setTextColor(getContext().getResources().getColor(CreditAssessment.COLOR_lOW));

                break;
            case CreditActivity.CREDIT2:
                result.setText(CreditAssessment.RESULT_2);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    result.setTextColor(getContext().getResources().getColor(CreditAssessment.COLOR_lOW, getContext().getTheme()));
                else
                    result.setTextColor(getContext().getResources().getColor(CreditAssessment.COLOR_lOW));

                break;
            case CreditActivity.CREDIT3:
                result.setText(CreditAssessment.RESULT_3);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    result.setTextColor(getContext().getResources().getColor(CreditAssessment.COLOR_GOOD, getContext().getTheme()));
                else
                    result.setTextColor(getContext().getResources().getColor(CreditAssessment.COLOR_GOOD));

                break;
            case CreditActivity.CREDIT4:
                result.setText(CreditAssessment.RESULT_4);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    result.setTextColor(getContext().getResources().getColor(CreditAssessment.COLOR_GOOD, getContext().getTheme()));
                else
                    result.setTextColor(getContext().getResources().getColor(CreditAssessment.COLOR_GOOD));
                break;
            case CreditActivity.CREDIT5:
                result.setText(CreditAssessment.RESULT_5);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    result.setTextColor(getContext().getResources().getColor(CreditAssessment.COLOR_GOOD, getContext().getTheme()));
                else
                    result.setTextColor(getContext().getResources().getColor(CreditAssessment.COLOR_GOOD));
                break;
        }
    }

}
