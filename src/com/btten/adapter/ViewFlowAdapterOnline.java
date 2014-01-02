package com.btten.adapter;

import java.util.List;

import com.btten.Jms.BtAPPJMS;
import com.btten.Jms.R;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class ViewFlowAdapterOnline extends BaseAdapter {
	private Activity context = null;
	private List<Bitmap> images = null;

	public void setImages(List<Bitmap> imageids) {
		this.images = imageids;
	}

	public Activity getContext() {
		return context;
	}

	public ViewFlowAdapterOnline(Activity context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return images.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ImageView image = null;

		if (convertView != null) {
			image = (ImageView) convertView;
		} else {
			image = new ImageView(context);
		}
		image.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		image.setScaleType(ScaleType.CENTER_CROP);

		if (position == -1) {
			Resources rs = BtAPPJMS.getInstance().getResources();
			image.setImageBitmap(BitmapFactory.decodeResource(rs,
					R.drawable.gonggao1));
		} else if (position >= 0) {
			image.setImageBitmap(images.get(position));
		}

		return image;
	}
}
