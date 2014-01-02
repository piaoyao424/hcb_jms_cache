package com.btten.calltaxi.Gonggao;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.btten.Jms.BaseActivity;
import com.btten.Jms.R;
import com.btten.network.NetSceneBase;
import com.btten.network.OnSceneCallBack;
import com.btten.tools.Util;

public class GonggaoContentActivity extends BaseActivity {

	private TextView tv_ggconten_biaoti;
	private TextView tv_ggconten_riqi;
	private TextView tv_ggconten_neirong;
	private TextView tv_ggconten_name;
	MyGGContentScene ggScene = null;
	String ggId = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gonggaocontent_activity);
		setCurrentTitle("公告信息");

		init();
		// initPersonalInfo();
	}

	private void init() {

		tv_ggconten_biaoti = (TextView) findViewById(R.id.tvId_ggconten_biaoti);
		tv_ggconten_riqi = (TextView) findViewById(R.id.tvId_ggconten_riqi);
		tv_ggconten_neirong = (TextView) findViewById(R.id.tvId_ggconten_neirong);
		tv_ggconten_name = (TextView) findViewById(R.id.tvId_ggconten_name);
		// 取得当前登录的用户的userid
		Bundle bundle = this.getIntent().getExtras();
		ggId = bundle.getString("KEY_GGID");
		tv_ggconten_biaoti.setText(bundle.getString("KEY_TITLE"));
		tv_ggconten_riqi.setText(bundle.getString("KEY_DATE"));
		System.out.println("userid : >>>>>>>>>>>>> " + ggId);
		DoRequest();
		// getListContent();

	}

	private void getListContent() {
		tv_ggconten_biaoti.setText("华盛顿枪击案已致13人死 被毙凶手为退伍军人");
		tv_ggconten_riqi.setText("20130906");
		String neirong = "2岁孙儿被车轮碾轧殒命 民警提醒：未满12岁不得坐在副驾驶位,华西都市报：昨日上午11点过，在成都郫县安靖镇某小区内，坐在副驾驶的2岁大的乐乐意外跌出车外，而作为驾驶员的奶奶并未发现这一情况，在倒车的过程中，车轮不幸碾轧到了乐乐。最终，乐乐因受伤过重，经医院抢救无效后身亡。昨日下午，家住成都郫县安靖镇某小区的任先生一家个个愁眉紧锁。几个小时前，一场突如其来的意外夺走了任先生2岁的孩子乐乐，而不慎造成这次悲剧的，竟是任先生的母亲。";

		tv_ggconten_neirong.setText(neirong);
		tv_ggconten_name.setText("惠车宝");
	}

	private void DoRequest() {
		ggScene = new MyGGContentScene();
		// mLoadMore.ShowLoading();
		ggScene.doscene(callBack, ggId);
	}

	OnSceneCallBack callBack = new OnSceneCallBack() {

		@Override
		public void OnSuccess(Object data, NetSceneBase netScene) {
			// TODO Auto-generated method stub
			// 设置所有的item都是没有被选中的。
			GGContentItems items = (GGContentItems) data;
			
			tv_ggconten_neirong.setText(items.item.content);
			tv_ggconten_name.setText("惠车宝");

			return;
		}

		@Override
		public void OnFailed(int status, String info, NetSceneBase netScene) {
			// TODO Auto-generated method stub
			// mLoadMore.ShowNoMore();
			ErrorAlert(status, info);
		}
	};

	/***
	 * 半角转换为全角
	 * 
	 * @param input
	 * @return
	 */
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}
}
