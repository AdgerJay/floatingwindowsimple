package com.forms.floatingwindowsimple.util;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.forms.floatingwindowsimple.R;


public class DialogUtil {

    public static AlertDialog showWithTwoBtn(Context context, String message, String positiveStr, String negativeStr,
                                             final OnClickListener onPositiveClickListener,
                                             final OnClickListener onNegativeClickListener) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        final AlertDialog dia = alert.create();
        View view = View.inflate(context, R.layout.dialog_common, null);
        AppCompatTextView tvMessage = (AppCompatTextView) view.findViewById(R.id.tvMessage);
        AppCompatButton btnCancel = (AppCompatButton) view.findViewById(R.id.btnCancel);
        AppCompatButton btnConfirm = (AppCompatButton) view.findViewById(R.id.btnConfirm);
        tvMessage.setText(message);
        btnCancel.setText(negativeStr);
        btnConfirm.setText(positiveStr);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dia.cancel();
                if (onNegativeClickListener != null) {
                    onNegativeClickListener.onClick();
                }

            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dia.cancel();
                if (onPositiveClickListener != null) {
                    onPositiveClickListener.onClick();
                }

            }
        });

        dia.setView(view);
        dia.setCancelable(false);
        return dia;

    }

    public interface OnClickListener {
        void onClick();
    }

}
