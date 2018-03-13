package com.jayrun.travelmate;

import android.app.Application;
import cn.bmob.v3.Bmob;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/*
 * 20160514
 */
public class TravelmateApp extends Application {
	public static final DisplayImageOptions Headoptions = new DisplayImageOptions.Builder()
			.cacheInMemory(true).showStubImage(R.drawable.head_default) // ����ͼƬ�����ڼ���ʾ��ͼƬ
			.showImageForEmptyUri(R.drawable.head_default) // ����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ
			.showImageOnFail(R.drawable.head_default) // ����ͼƬ���ػ��������з���������ʾ��ͼƬ
			.displayer(new RoundedBitmapDisplayer(200)) // ���ó�Բ��ͼƬ
			.build(); // �������ù���DisplayImageOption����

	@Override
	public void onCreate() {
		super.onCreate();
		// ��Ŀ���
		Bmob.initialize(this, "82c796af51018d18faa856486ee46a37");
		// Ѷ�����
//		StringBuffer param = new StringBuffer();
//		param.append("appid=57355924");
//		param.append(",");
//		// ����ʹ��v5+
//		param.append(SpeechConstant.ENGINE_MODE + "=" + SpeechConstant.MODE_MSC);
		// imageLoader��ʼ��
		SpeechUtility.createUtility(TravelmateApp.this, SpeechConstant.APPID + "=57355924");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs().build();
		ImageLoader.getInstance().init(config);

	}
}
