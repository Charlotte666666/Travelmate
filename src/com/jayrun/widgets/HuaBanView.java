package com.jayrun.widgets;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

/** ʵ�ֻ��幦�ܵ�View */
public class HuaBanView extends View {

	private float mX, mY;
	private static final float TOUCH_TOLERANCE = 4;
	private PaintPath pp;

	/** ����λͼ */
	private Bitmap cacheBitmap;
	/** ����λͼ�Ļ��� */
	private Canvas cacheCanvas;
	/** ���廭�� */
	private Paint paint;
	/** ʵ�ʻ��� */
	private Paint BitmapPaint;
	/** �����������·�� */
	private Path path;
	/** ������ */
	private int height;
	/** ������ */
	private int width;

	/** ������һ�λ��Ƶ��յ������ */
	private float pX;
	/** ������һ�λ��Ƶ��յ������� */
	private float pY;

	/** ���ʳ�ʼ��ɫ */
	public static int paintColor = Color.RED;
	/** ��״״̬ */
	public static Paint.Style paintStyle = Paint.Style.STROKE;
	/** ���ʴ�ϸ */
	private static int paintWidth = 10;
	/** ����͸���� */
	private static int paintAlpha = 255;

	private Canvas canvas;

	// �����һ�δ���ĺ�����
	private float fX;
	// �����һ�δ����������
	private float fY;
	private ArrayList<PaintPath> deletedPaths;
	private ArrayList<PaintPath> drewPaths;

	private boolean isEraser = false;

	private class PaintPath {
		Paint paint;
		Path path;
	}

	public ArrayList<PaintPath> getDeletedPaths() {
		return deletedPaths;
	}

	public void setDeletedPaths(ArrayList<PaintPath> deletedPaths) {
		this.deletedPaths = deletedPaths;
	}

	public ArrayList<PaintPath> getDrewPaths() {
		return drewPaths;
	}

	public void setDrewPaths(ArrayList<PaintPath> drewPaths) {
		this.drewPaths = drewPaths;
	}

	private void init() {
		cacheBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		cacheCanvas = new Canvas(cacheBitmap);
		cacheCanvas.drawColor(Color.argb(0, 255, 255, 255));
		paint = new Paint();
		path = new Path();
		BitmapPaint = new Paint();
		updatePaint();
	}

	private void updatePaint() {
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setStrokeWidth(paintWidth);
		if (isEraser) {
			paint.setAlpha(0);
			// ������ͼ�ཻʱ��ģʽ
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
			paint.setAntiAlias(true);
			paint.setDither(true);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeJoin(Paint.Join.ROUND);// ƽ��
			paint.setStrokeCap(Paint.Cap.ROUND);// Բͷ
		} else {
			paint.setStyle(paintStyle);
			paint.setColor(paintColor);
			paint.setAlpha(paintAlpha);
		}

	}

	public HuaBanView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// �õ���Ļ�ķֱ���
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);

		width = dm.widthPixels;
		height = dm.heightPixels - 2 * 45;
		init();
		// undo();
		deletedPaths = new ArrayList<PaintPath>();
		drewPaths = new ArrayList<PaintPath>();
	}

	public HuaBanView(Context context) {
		super(context);
		// �õ���Ļ�ķֱ���
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);

		width = dm.widthPixels;
		height = dm.heightPixels - 2 * 45;
		init();
		// undo();
		deletedPaths = new ArrayList<PaintPath>();
		drewPaths = new ArrayList<PaintPath>();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		this.canvas = canvas;
		BitmapPaint = new Paint();
		canvas.drawBitmap(cacheBitmap, 0, 0, BitmapPaint);
		if (null != path) {
			// ʵʱ����ʾ
			canvas.drawPath(path, paint);
		}
	}

	/** ���»�����ɫ */
	public void setPaintColor(int color) {
		paintColor = color;
		updatePaint();
	}

	/** ���»���͸���� */
	public void setPaintAlpha(int alpha) {
		paintAlpha = alpha;
		updatePaint();
	}

	/** ���û��ʴ�ϸ */
	public void setPaintWidth(int width) {
		paintWidth = width;
		updatePaint();
	}

	public static final int PEN = 1;
	public static final int PAIL = 2;
	public static final int ERASER = 3;

	/** ���û�����ʽ */
	public void setStyle(int style) {
		switch (style) {
		case PEN:
			paintStyle = Paint.Style.STROKE;
			isEraser = false;
			updatePaint();
			break;
		case PAIL:
			paintStyle = Paint.Style.FILL;
			isEraser = false;
			updatePaint();
			break;
		case ERASER:
			// paint.setColor(eraserColor);
			isEraser = true;
			paint.setStyle(Paint.Style.STROKE);
			updatePaint();
			break;
		}

	}

	/** ��ջ��� */
	public void clearScreen() {
		init();
		deletedPaths.clear();
		drewPaths.clear();
		invalidate();
	}

	/**
	 * �����ĺ���˼����ǽ�������գ� ������������Path·�����һ���Ƴ����� ���½�·�����ڻ������档
	 */
	public void undo() {
		if (drewPaths != null && drewPaths.size() > 0) {
			// ���ó�ʼ��������������ջ���
			init();
			// ��·�������б��е����һ��Ԫ��ɾ�� ,�����䱣����·��ɾ���б���
			PaintPath paintPath = drewPaths.get(drewPaths.size() - 1);
			deletedPaths.add(paintPath);
			drewPaths.remove(drewPaths.size() - 1);

			// ��·�������б��е�·���ػ��ڻ�����
			for (int i = 0; i < drewPaths.size(); i++) {
				PaintPath pp = drewPaths.get(i);
				cacheCanvas.drawPath(pp.path, pp.paint);
			}
			invalidate();// ˢ��
		}
	}

	/**
	 * �ָ��ĺ���˼����ǽ�������·�����浽����һ���б�����(ջ)�� Ȼ���redo���б�����ȡ����˶��� ���ڻ������漴��
	 */
	public void redo() {
		if (deletedPaths.size() > 0) {
			// ��ɾ����·���б��е����һ����Ҳ�������·��ȡ����ջ��,������·�������б���
			PaintPath pp = deletedPaths.get(deletedPaths.size() - 1);
			drewPaths.add(pp);
			// ��ȡ����·���ػ��ڻ�����
			cacheCanvas.drawPath(pp.path, pp.paint);
			// ����·����ɾ����·���б���ȥ��
			deletedPaths.remove(deletedPaths.size() - 1);
			invalidate();
		}
	}

	private void touch_start(float x, float y) {
		path.reset();// ���path
		path.moveTo(x, y);
		mX = x;
		mY = y;
		fX = x;
		fY = y;
	}

	private void touch_move(float x, float y) {
		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);// Դ����������д�ģ�������û��Ū���ף�ΪʲôҪ������
			mX = x;
			mY = y;
		}
	}

	private void touch_up(float x, float y) {
		if (fX != x && fY != y) {
			path.lineTo(mX, mY);
			cacheCanvas.drawPath(path, paint);
			drewPaths.add(pp);
			deletedPaths.clear();
			path = null;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:

			path = new Path();
			pp = new PaintPath();
			pp.path = path;
			pp.paint = paint;

			touch_start(x, y);
			invalidate(); // ����
			break;
		case MotionEvent.ACTION_MOVE:
			touch_move(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			touch_up(x, y);
			invalidate();
			break;
		}
		return true;
	}
}
