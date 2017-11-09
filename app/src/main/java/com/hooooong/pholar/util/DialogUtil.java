package com.hooooong.pholar.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.hooooong.pholar.R;

/**
 * Created by Android Hong on 2017-11-08.
 */

public class DialogUtil {

    /**
     * Dialog 호출하는 메소드
     *
     * @param title : 타이틀
     * @param msg : 메세지
     * @param activity : 실행하는 Activity
     * @param activityFinish : Activity 종료에 대한 flag
     */
    public static void exitDialog(String title, String msg, final Activity activity, final boolean activityFinish){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        dialogBuilder
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(false) //false면 버튼을 단다는 것(다른곳을 눌러도 사라지지 않는다.), true면 반대
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if(activityFinish){
                            activity.finish();
                        }
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

}
