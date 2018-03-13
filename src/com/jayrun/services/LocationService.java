package com.jayrun.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.jayrun.fragments.RecommendFragment;

public class LocationService extends Service implements AMapLocationListener {
	private AMapLocationClient locationClient = null;
	private AMapLocationClientOption locationOption = null;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		locationClient = new AMapLocationClient(this.getApplicationContext());
		locationOption = new AMapLocationClientOption();
		// ���ö�λģʽΪ�߾���ģʽ
		locationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
		// ���ö�λ����
		locationClient.setLocationListener(this);
		locationOption.setOnceLocation(true);
		locationOption.setNeedAddress(true);
		locationClient.setLocationOption(locationOption);
		locationClient.startLocation();
		return super.onStartCommand(intent, flags, startId);

	}

	@Override
	public void onLocationChanged(AMapLocation location) {
		if (location == null) {
			if (locationClient != null) {
				locationClient.startLocation();
			}
			Toast.makeText(LocationService.this, "��λʧ��", Toast.LENGTH_SHORT)
					.show();
		} else if (location.getErrorCode() != 0) {
			Toast.makeText(LocationService.this, "��λʧ��", Toast.LENGTH_SHORT)
					.show();
		} else {
			String city = location.getCity().replace("��", "");
			String province = location.getProvince().replace("ʡ", "");
			province = province.replace("׳��������", "");
			province = province.replace("ά���������", "");
			province = province.replace("��������", "");
			province = province.replace("������", "");
			Intent intent = new Intent(RecommendFragment.ACTION_LOCATION);
			intent.putExtra("city", city);
			intent.putExtra("province", province);
			sendBroadcast(intent);
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (null != locationClient) {
			locationClient.onDestroy();
			locationClient = null;
			locationOption = null;
		}
	}
}
