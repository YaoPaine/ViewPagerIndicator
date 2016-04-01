package com.example.indicator;

import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewPagerIndicator extends LinearLayout {

	private Paint mPaint;
	private Path mPath;
	private int mTriangleWidth;
	private int mTriangleHeight;
	private int mTabVisibleCount;
	private static final int COUNT_DEFAULT_TAB = 4;
	private static final float RADIO_TRIANGLE_WIDTH = 1 / 6F;
	private int mInitTranslationX;
	private int mTranslationX;
	private List<String> mTitles;
	private static final int COLOR_TEXT_NORMAL = 0X77FFFFFF;
	private static final int COLOR_TEXT_HIGHLIGHT = 0XFFFFFFFF;
	// 三角形底边的最大宽度
	private final int DIMENSION_TRIAGNLE_WIDTH_MAX = (int) (getscreenWidth() / 3 * RADIO_TRIANGLE_WIDTH);

	public ViewPagerIndicator(Context context) {
		this(context, null);
	}

	public ViewPagerIndicator(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// 获取可见Tab的数量
		TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator);

		mTabVisibleCount = attributes.getInt(R.styleable.ViewPagerIndicator_visible_tab_count, COUNT_DEFAULT_TAB);

		if (mTabVisibleCount < 0) {
			mTabVisibleCount = COUNT_DEFAULT_TAB;
		}

		attributes.recycle();
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Style.FILL);
		mPaint.setColor(Color.parseColor("#ffffff"));
		mPaint.setPathEffect(new CornerPathEffect(3));
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		canvas.save();
		canvas.translate(mInitTranslationX + mTranslationX, getHeight() + 2);
		canvas.drawPath(mPath, mPaint);

		canvas.restore();
		super.dispatchDraw(canvas);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mTriangleWidth = (int) (w / mTabVisibleCount * RADIO_TRIANGLE_WIDTH);
		mTriangleWidth = Math.min(mTriangleWidth, DIMENSION_TRIAGNLE_WIDTH_MAX);
		mInitTranslationX = w / mTabVisibleCount / 2 - mTriangleWidth / 2;
		initTriangle();
	}

	// xml加载完成以后调用该方法
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		int cCount = getChildCount();
		if (cCount == 0)
			return;
		for (int i = 0; i < cCount; i++) {
			View view = getChildAt(i);
			LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
			layoutParams.weight = 0;
			layoutParams.width = getscreenWidth() / mTabVisibleCount;
			view.setLayoutParams(layoutParams);
		}

		setItemClickEvent();
	}

	/**
	 * 获得屏幕宽度
	 * 
	 * @return
	 */
	private int getscreenWidth() {
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.widthPixels;
	}

	/**
	 * 初始化三角形
	 */
	private void initTriangle() {
		mTriangleHeight = mTriangleWidth / 2;
		mPath = new Path();
		mPath.moveTo(0, 0);
		mPath.lineTo(mTriangleWidth, 0);
		mPath.lineTo(mTriangleWidth / 2, -mTriangleHeight);
		mPath.close();

	}

	/**
	 * 指示器跟随手指滚动
	 * 
	 * @param position
	 * @param positionOffset
	 */
	public void scroll(int position, float offset) {
		int tabWidth = getWidth() / mTabVisibleCount;
		mTranslationX = (int) (tabWidth * (offset + position));

		if (position >= (mTabVisibleCount - 1) && offset > 0 && getChildCount() > mTabVisibleCount) {
			this.scrollTo((int) ((position - (mTabVisibleCount - 1)) * tabWidth + tabWidth * offset), 0);
		}
		invalidate();
	}

	/**
	 * 设置可见Tab数量，必须在生成TextView代码之前执行
	 * 
	 * @param count
	 */
	public void setVisibleCount(int count) {
		mTabVisibleCount = count;
	}

	public void setTabItemTitles(List<String> titles) {
		if (titles != null && titles.size() > 0) {
			this.removeAllViews();
			mTitles = titles;
			for (String title : mTitles) {
				addView(generateTextView(title));
			}
			setItemClickEvent();
		}
	}

	private View generateTextView(String title) {
		TextView textView = new TextView(getContext());
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.width = getscreenWidth() / mTabVisibleCount;
		textView.setLayoutParams(params);
		textView.setText(title);
		textView.setGravity(Gravity.CENTER);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		textView.setTextColor(COLOR_TEXT_NORMAL);
		return textView;
	}

	private ViewPager mViewPager;
	private onPageChangeListener listener;

	public interface onPageChangeListener {
		void onPageSelected(int position);

		void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

		void onPageScrollStateChanged(int state);
	}

	public void setOnpageChangeListener(onPageChangeListener listener) {
		this.listener = listener;
	}

	/**
	 * 设置关联该指示器的ViewPager
	 * 
	 * @param viewPager
	 * @param pos
	 */
	public void setViewPager(ViewPager viewPager, int pos) {
		mViewPager = viewPager;
		mViewPager.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				if (listener != null)
					listener.onPageSelected(position);
				highLightTextVeiw(position);
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				scroll(position, positionOffset);
				if (listener != null)
					listener.onPageScrolled(position, positionOffset, positionOffsetPixels);
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				if (listener != null)
					listener.onPageScrollStateChanged(state);
			}

		});

		mViewPager.setCurrentItem(pos);
		highLightTextVeiw(pos);
		int tabWidth = getscreenWidth() / mTabVisibleCount;
		if (pos >= (mTabVisibleCount - 1))
			this.scrollTo((int) ((pos - (mTabVisibleCount - 1)) * tabWidth), 0);
	}

	/**
	 * 重置TextView Color
	 */
	private void resetTextViewColor() {
		for (int i = 0; i < getChildCount(); i++) {
			View view = getChildAt(i);
			if (view instanceof TextView)
				((TextView) view).setTextColor(COLOR_TEXT_NORMAL);
		}
	}

	/**
	 * 高亮选中Tab的文本
	 * 
	 * @param index
	 */
	public void highLightTextVeiw(int index) {
		resetTextViewColor();
		View view = getChildAt(index);
		if (view instanceof TextView)
			((TextView) view).setTextColor(COLOR_TEXT_HIGHLIGHT);
	}

	private void setItemClickEvent() {
		int cCount = getChildCount();
		for (int i = 0; i < cCount; i++) {
			final int j = i;
			View view = getChildAt(i);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mViewPager.setCurrentItem(j);
				}
			});
		}

	}
}
