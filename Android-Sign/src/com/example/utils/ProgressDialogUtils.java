package com.example.utils;

import com.example.testmap.MainActivity;
import com.example.testmap.ToastUtil;
import com.zhy.http.okhttp.request.RequestCall;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
/**
 * 进度对话框(ProgressDialog)工具类
 * @author lzh
 *
 */
public class ProgressDialogUtils {
	private static ProgressDialog dialog;
	/**
	 * 显示进度对话框
	 * @param flag01 是否可以通过点击Back键取消对话框 
	 * @param flag02 是否可以通过点击Dialog外取消Dialog进度条
	 * @param context 上下文参数
	 * @param title   对话框的标题
	 * @param message 对话框的内容
	 * @param resId   对话框的图标
	 * @param canCelMessage 手动关闭对话框需要显示的信息 
	 */
	public static void show(boolean flag01,boolean flag02,final Context context,String title,String message,int resId,final String canCelMessage){
		dialog = new ProgressDialog(context);
		// 设置进度条的形式为圆形转动的进度条  
    	dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    	// 设置是否可以通过点击Back键取消 (点击Dialog外会取消Dialog进度条 )
    	dialog.setCancelable(flag01);
    	// 设置在点击Dialog外是否取消Dialog进度条 
        dialog.setCanceledOnTouchOutside(flag02);
        // 设置提示的title的图标，默认是没有的，如果没有设置title的话只设置Icon是不会显示图标的  
        dialog.setTitle(title);
        // 设置进度对话框的图标
        dialog.setIcon(resId);
        // 设置进度对话框的文字内容
        dialog.setMessage(message);
        //这里模拟手动取消
        dialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				if(!"".equals(canCelMessage)){
					ToastUtil.show(context, canCelMessage);
					if(MainActivity.call!=null){
						MainActivity.call.cancel();
					}
				}
			}
		});
        // 显示进度对话框
        dialog.show();
	}
	/**
	 * 对话框是否在显示状态
	 * @return
	 */
	public static boolean isShowing(){
		if(dialog!=null&&dialog.isShowing()){
			return true;
		}
		return false;
	}
	/**
	 * 关闭进度对话框(cancel:会触发对应的回调方法)
	 */
	public static void cancel(){
		if(dialog!=null&&dialog.isShowing()){
			//关闭对话框
			dialog.cancel();
		}
	}
	/**
	 * 关闭进度对话框(dismiss:也会触发对应的回调方法)
	 */
	public static void dismiss(){
		if(dialog!=null&&dialog.isShowing()){
			//关闭对话框
			dialog.dismiss();
		}
	}
} 
