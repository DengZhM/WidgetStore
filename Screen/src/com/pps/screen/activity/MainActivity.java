package com.pps.screen.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * ģ��ʵ�ֽ�ͼ���ܣ�(��Ȼ���Խ���������ܽ�����չ(���磺ҡһҡ���н�ͼ,���߿�����ʱ��ͼ����))
 * @author jiangqingqing
 * @time 2013/09/29
 */
public class MainActivity extends Activity {
    private Button btn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		btn=(Button)this.findViewById(R.id.btn);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean result = ScreenShotUtils.shotBitmap(MainActivity.this);
				if(result)
				{
					Toast.makeText(MainActivity.this, "��ͼ�ɹ�.", Toast.LENGTH_SHORT).show();
				}else {
					Toast.makeText(MainActivity.this, "��ͼʧ��.", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
	}

	

}
