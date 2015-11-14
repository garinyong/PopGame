package com.haogame.popet;

import java.util.HashMap;

import org.hj20150901.lib.HejuHuafeiCallback;
import org.hj20150901.lib.HejuInstance;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
 
import android.util.Log;
import android.widget.Toast;

 

public class PayUtil {

	public static final int PAY_STATE_SUCCESS = 1;
	public static final int PAY_STATE_FAILED = 2;
	public static final String[] PRO_PRICE = new String[] { "4", "8", "12", "10", "8" };
	public static final String[] PRO_NAME = new String[] { "5个魔法棒", "12个魔法棒", "25个魔法棒", "激活", "复活" };
	public static final int[] PRO_COUNT = new int[] { 5, 12, 25, 1, 1 };
	
	public static void payWithProductIndex(final Context context,final int proIndex,final PayResultListener  payResut) {

		
		
		// 方式二：使用静态方式创建并显示，这种进度条只能是圆形条,设置title和Message提示内容  
	     
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("productName", PRO_NAME[proIndex] );// 商品名
		params.put("appName", "PopET");// 应用名
		params.put("point", Constant.PRO_PRICE[proIndex]); // 计费点数 不为空
		params.put("extraInfo", null); // CP扩展信息 可为空
		params.put("description", "充值");// 资费提示标题
		params.put("ui", "0");// 资费提示界面参数值 默认可传0
		params.put("debug", "1");// 调试信息toast0关闭1开启
		params.put("ShopActivity", "com.haogame.popet.activity.ShopActivity");// 寄主activity路径+名称
		HejuInstance mHejuHuafei = new HejuInstance();
		 final ProgressDialog dialog = ProgressDialog.show(context, null, "正在处理数据，请稍后");
		mHejuHuafei.pay(context, params, new HejuHuafeiCallback() {
			
			@Override
			public void onFail(JSONObject payResult) {
				// TODO Auto-generated method stub
				dialog.dismiss();	 
				String code = null;
				String extraInfo = null;
				try {
					code = payResult.getString("code");
					extraInfo = payResult.getString("extraInfo");

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
  				payResut.onResut(PAY_STATE_FAILED,proIndex);
				Log.e("支付失败  params = ", params.toString() );
				Toast.makeText(context,
						"交易失败--->" + "状态代码：" + code + " | " + "扩展参数："
								+ extraInfo, Toast.LENGTH_SHORT).show();

			}
		 	@Override
			public void onSuccess(JSONObject payResult) {
				// TODO Auto-generated method stub
		 		dialog.dismiss();
				String code = null;
				String extraInfo = null;
				try {
					code = payResult.getString("code");
					extraInfo = payResult.getString("extraInfo");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
 				
				payResut.onResut(PAY_STATE_SUCCESS,proIndex);
			 
				Log.e("支付成功  params = ", params.toString() );
				
				Toast.makeText(
						context,
						"交易成功--->" + "状态代码：" + code + " | " + "扩展参数："
								+ extraInfo, Toast.LENGTH_SHORT).show();

			}

		});
 	
	}
	
	
	
  public interface PayResultListener
  {
     public abstract void onResut(int state,int proIndex);
      
  }
	
}
