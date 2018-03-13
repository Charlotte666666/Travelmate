package com.jayrun.adapters;

import java.util.ArrayList;
import java.util.List;
import cn.bmob.v3.BmobUser;
import com.jayrun.travelmate.R;
import com.jayrun.beans.User;
import com.jayrun.beans.Words;
import com.jayrun.utils.FriendlyTimeUtil;
import com.jayrun.widgets.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WordsDetailsListAdapter extends BaseAdapter implements
		OnClickListener {
	private Context context;
	private ViewHolder holder;
	private LikeButtonCallBack likeButtonCallBack;
	private List<Words> wordsList = new ArrayList<Words>();
	private Words words;
	private DisplayImageOptions headOptions;
	private DisplayImageOptions graffitiOptions;
	private ImageLoader imageLoader;

	public WordsDetailsListAdapter(Context context, List<Words> wordsList,
			LikeButtonCallBack likeButtonCallBack) {
		this.context = context;
		this.wordsList = wordsList;
		this.likeButtonCallBack = likeButtonCallBack;
	}

	public WordsDetailsListAdapter(Context context,
			LikeButtonCallBack likeButtonCallBack) {
		headOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).showStubImage(R.drawable.head_default) // ����ͼƬ�����ڼ���ʾ��ͼƬ
				.showImageForEmptyUri(R.drawable.head_default)
				// ����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ
				.showImageOnFail(R.drawable.head_default)
				// ����ͼƬ���ػ��������з���������ʾ��ͼƬ
				.displayer(new RoundedBitmapDisplayer(0)) // ���ó�Բ��ͼƬ
				.build(); // �������ù���DisplayImageOption����
		graffitiOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).showStubImage(R.drawable.default_graffiti) // ����ͼƬ�����ڼ���ʾ��ͼƬ
				.showImageForEmptyUri(R.drawable.default_graffiti)
				// ����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ
				.showImageOnFail(R.drawable.default_graffiti)
				// ����ͼƬ���ػ��������з���������ʾ��ͼƬ
				.displayer(new RoundedBitmapDisplayer(0)) // ���ó�Բ��ͼƬ
				.build(); // �������ù���DisplayImageOption����
		imageLoader = ImageLoader.getInstance();
		this.context = context;
		this.likeButtonCallBack = likeButtonCallBack;
	}

	@Override
	public int getCount() {
		return wordsList.size();
		// return 10;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_words_details, null);
			holder.likeImage = (ImageView) convertView
					.findViewById(R.id.words_details_like_img);
			holder.name = (TextView) convertView
					.findViewById(R.id.words_details_user_name);
			holder.content = (TextView) convertView
					.findViewById(R.id.words_details);
			holder.likeCount = (TextView) convertView
					.findViewById(R.id.words_details_like_count);
			holder.head = (CircleImageView) convertView
					.findViewById(R.id.words_details_head);
			holder.wordsTime = (TextView) convertView
					.findViewById(R.id.words_details_time);
			holder.graffitiView = (ImageView) convertView
					.findViewById(R.id.words_graffiti);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		words = wordsList.get(position);
		holder.name.setText(words.getUser().getNickName());
		if (words.getIsText()) {
			holder.graffitiView.setVisibility(View.GONE);
			holder.content.setVisibility(View.VISIBLE);
			holder.content.setText(words.getContent());
		} else {
			holder.graffitiView.setVisibility(View.VISIBLE);
			holder.content.setVisibility(View.GONE);
			imageLoader.displayImage(words.getGraffiti().getFileUrl(),
					holder.graffitiView, graffitiOptions);
			int bgColor = context.getResources().getColor(R.color.white);
			switch (words.getBubbleColor()) {
			case 0:
				bgColor = context.getResources().getColor(R.color.yellow_alpha);
				break;
			case 1:
				bgColor = context.getResources().getColor(R.color.green_alpha);
				break;
			case 2:
				bgColor = context.getResources().getColor(R.color.purple_alpha);
				break;
			case 3:
				bgColor = context.getResources().getColor(R.color.blue_alpha);
				break;
			case 4:
				bgColor = context.getResources().getColor(R.color.black_alpha);
				break;
			case 5:
				bgColor = context.getResources().getColor(R.color.white);
				break;
			}
			holder.graffitiView.setBackgroundColor(bgColor);
		}

		User user = BmobUser.getCurrentUser(User.class);
		if (user == null) {
			holder.likeImage.setEnabled(true);
			holder.likeImage.setOnClickListener(this);
		} else if (words.getLikeUsersId().contains(
				BmobUser.getCurrentUser(User.class).getObjectId())) {
			holder.likeImage.setEnabled(false);
		} else {
			holder.likeImage.setEnabled(true);
			holder.likeImage.setOnClickListener(this);
		}
		holder.likeImage.setTag(position);
		holder.likeCount.setText((words.getLikeUsersId().size() + ""));
		holder.wordsTime.setText(FriendlyTimeUtil.convertTimeToFormat(words
				.getCreatedAt()));
		imageLoader.displayImage(
				words.getUser().getUserHead().getFileUrl(), holder.head,
				headOptions);
		return convertView;
	}

	private class ViewHolder {
		ImageView likeImage;
		CircleImageView head;
		TextView name;
		TextView content;
		TextView likeCount;
		TextView wordsTime;
		ImageView graffitiView;
	}

	public List<Words> getWordsList() {
		return wordsList;
	}

	public void setWordsList(List<Words> wordsList) {
		this.wordsList = wordsList;
	}

	public interface LikeButtonCallBack {
		public void onLikeClick(View v);
	}

	@Override
	public void onClick(View view) {
		likeButtonCallBack.onLikeClick(view);
	}

}
