package com.dazzle.bigappleui.view.listview;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.dazzle.bigappleui.utils.ui.drawable.ArrowDrawable;
import com.dazzle.bigappleui.view.listview.pulltorefresh.FooterView;
import com.dazzle.bigappleui.view.listview.pulltorefresh.HeaderView;
import com.dazzle.bigappleui.view.listview.pulltorefresh.PullDownStateListener;
import com.dazzle.bigappleui.view.listview.pulltorefresh.UIHelper;

/**
 * 扩展了ListView控件，支持下拉刷新和上拉加载更多
 * 
 * @author xuan
 */
public class ANPullToRefreshListView extends ListView implements
		OnScrollListener, PullDownStateListener {
	private static final String TAG = "PullToRefreshListView";
	public static final boolean DEBUG = false;

	/** 表示着列表正处于哪种状态 */
	private int state;
	/** 状态：下拉刷新 */
	private final static int PULL_TO_REFRESH = 0;
	/** 状态：松开刷新 */
	private final static int RELEASE_TO_REFRESH = 1;
	/** 状态：正在刷新 */
	private final static int REFRESHING = 2;
	/** 状态：刷新完成 */
	private final static int DONE = 3;

	/** 头部布局 */
	private HeaderView headerView;
	/** 尾部布局 */
	private FooterView footerView;

	/** 箭头由下到上 */
	private RotateAnimation animation;
	/** 箭头有上到下恢复 */
	private RotateAnimation reverseAnimation;

	// 记录状态切换时的一些标记
	private boolean isRecored;// 表示是否正在记录startY，用于保证startY的值在一个完整的touch事件中只被记录一次
	private int startY;// 移动开始时记录的Y高度

	private boolean isBack;// 标志着那个刷新箭头是否需要设置翻转回去
	private int firstItemIndex;// 记录着第一个可见列表项目的标志，在滚动条监听事件中被设置
	private int visibleItemCount;// 可见item数

	// 头部的一些测量距离参数
	private int headContentHeight;
	private int headContentOriginalTopPadding;

	/** 触发刷新的接口 */
	public ANPullToRefreshListViewListener pullToRefreshListViewListener;

	/** 外部设置的滚动监听 */
	private OnScrollListener externalScrollListener;

	/** 是否可以下拉刷新 */
	private boolean canPullDown;
	/** 是否可以上滚加载更多 */
	private boolean canScrollUp;

	/** 阻尼系数 */
	private float damping = 0.3f;

	public ANPullToRefreshListView(Context context) {
		super(context);
		initView(context);
	}

	public ANPullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public ANPullToRefreshListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private void initView(Context context) {
		setCanPullDown(true);
	}

	@Override
	public void setOnScrollListener(OnScrollListener externalScrollListener) {
		super.setOnScrollListener(this);
		this.externalScrollListener = externalScrollListener;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!canPullDown) {
			return super.onTouchEvent(event);// 不需要下拉刷新啥都不处理
		}

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (firstItemIndex == 0 && !isRecored) {
				startY = (int) event.getY();
				isRecored = true;
			}
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			if (DEBUG) {
				Log.d(TAG, "ACTION_UP:" + state);
			}

			// 当抬起时，如果是正在刷新就不管
			if (state != REFRESHING) {
				if (state == DONE) {
					// 当抬起时，如果是DONE(已经完成状态)，就什么都不做
				} else if (state == PULL_TO_REFRESH) {
					// 当抬起时，如果是PULL_To_REFRESH(下拉刷新提示)，就直接到DONE状态，只改变一下头就可以，不用去加载数据
					state = DONE;
					changeHeaderViewByState();
				} else if (state == RELEASE_TO_REFRESH) {
					// 当抬起时，如果是RELEASE_To_REFRESH(松开刷新提示)，就到REFRESHING(刷新中)状态，改变头，并触发刷新事件
					state = REFRESHING;
					changeHeaderViewByState();
					if (null != pullToRefreshListViewListener) {
						pullToRefreshListViewListener.onPullDownRefresh();
					}
				}
			}

			isRecored = false;
			isBack = false;
			break;
		case MotionEvent.ACTION_MOVE:
			if (DEBUG) {
				Log.d(TAG, "ACTION_MOVE:" + state);
			}

			int tempY = (int) event.getY();
			if (!isRecored && firstItemIndex == 0) {
				isRecored = true;
				startY = tempY;
			}

			// 如果不是正在刷新，并正在记录startY状态，就要执行下面一系列的变化
			if (state != REFRESHING && isRecored) {
				// 滑动时RELEASE_TO_REFRESH状态下，转变有3种情况：1.上推又到PULL_TO_REFRESH（下拉刷新）状态
				// 2.上推回到done状态
				// 3.一直往下拉不用管
				if (state == RELEASE_TO_REFRESH) {
					if ((filterDamping(tempY - startY) < headContentHeight + 20)
							&& (filterDamping(tempY - startY)) > 0) {
						state = PULL_TO_REFRESH;
						changeHeaderViewByState();
					} else if (filterDamping(tempY - startY) <= 0) {
						state = DONE;
						changeHeaderViewByState();
					} else {
					}
				}

				// 滑动时PULL_TO_REFRESH状态下，转变有两种情况：1.下拉到RELEASE_TO_REFRESH（松开刷新）状态
				// 2.上推回到done状态
				else if (state == PULL_TO_REFRESH) {
					// if (tempY - startY >= headContentHeight + 20 &&
					// currentScrollState == SCROLL_STATE_TOUCH_SCROLL)
					// {
					if (filterDamping(tempY - startY) >= headContentHeight + 20) {
						state = RELEASE_TO_REFRESH;
						isBack = true;
						changeHeaderViewByState();
					} else if (filterDamping(tempY - startY) <= 0) {
						state = DONE;
						changeHeaderViewByState();
					}
				}

				// 滑动时DONE状态下，转变只有一种情况：1.下拉到PULL_TO_REFRESH（下拉刷新）状态
				else if (state == DONE) {
					if (filterDamping(tempY - startY) > 0) {
						state = PULL_TO_REFRESH;
						changeHeaderViewByState();
					}
				}

				// PULL_TO_REFRESH状态下,需要根据下拉距离实时更新头部的Padding距离
				if (state == PULL_TO_REFRESH) {
					int topPadding = ((-1 * headContentHeight + filterDamping(tempY
							- startY)));
					headerView.setPadding(headerView.getPaddingLeft(),
							topPadding, headerView.getPaddingRight(),
							headerView.getPaddingBottom());
					headerView.invalidate();
				}

				// RELEASE_TO_REFRESH状态下，需要根据下拉距离实时更新头部的Padding距离
				if (state == RELEASE_TO_REFRESH) {
					int topPadding = ((filterDamping(tempY - startY) - headContentHeight));
					headerView.setPadding(headerView.getPaddingLeft(),
							topPadding, headerView.getPaddingRight(),
							headerView.getPaddingBottom());
					headerView.invalidate();
				}
			}
			break;
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 点击刷新，这种情况就是供，列表外部有个按钮，然后点击就可以刷新列表
	 */
	public void clickRefresh() {
		setSelection(0);
		state = REFRESHING;
		changeHeaderViewByState();
		if (null != pullToRefreshListViewListener) {
			pullToRefreshListViewListener.onPullDownRefresh();
		}
	}

	/**
	 * 设置刷新监听器
	 * 
	 * @param pullToRefreshListViewListener
	 */
	public void setPullToRefreshListViewListener(
			ANPullToRefreshListViewListener pullToRefreshListViewListener) {
		this.pullToRefreshListViewListener = pullToRefreshListViewListener;
	}

	/**
	 * 当数据加载完成后，调用该方法可以手动设置列表为done状态
	 * 
	 * @param update
	 */
	public void onPullDownRefreshComplete(String update) {
		headerView.getLastUpdateHint().setText(update);
		state = DONE;
		changeHeaderViewByState();
	}

	/**
	 * 上滚加载更多完成
	 * 
	 * @param moreStr
	 */
	public void onScrollUpRefreshComplete(String moreStr) {
		footerView.getTextView().setText(moreStr);
		footerView.getProgressBar().setVisibility(View.GONE);
	}

	/**
	 * 加载更多没有更多数据了
	 * 
	 * @param noMoreStr
	 */
	public void onScrollUpNoMoreData(String noMoreStr) {
		setCanScrollUp(false);
		footerView = createFooterView((Activity) getContext());
		footerView.getTextView().setText(noMoreStr);
		footerView.getProgressBar().setVisibility(View.GONE);
		footerView.setVisibility(View.VISIBLE);
		addFooterView(footerView);
	}

	/**
	 * 设置是否需要下拉刷新
	 * 
	 * @param canPullDown
	 */
	public void setCanPullDown(boolean canPullDown) {
		this.canPullDown = canPullDown;
		if (canPullDown) {
			// 初始化箭头的变化动作
			initAnimation();

			// 创建头尾布局
			headerView = createHeaderView((Activity) getContext());

			// 头部的原始paddingTop值
			headContentOriginalTopPadding = headerView.getPaddingTop();
			measureView(headerView);

			// 隐藏头部
			headContentHeight = headerView.getMeasuredHeight();
			headerView.setPadding(headerView.getPaddingLeft(), -1
					* headContentHeight, headerView.getPaddingRight(),
					headerView.getPaddingBottom());
			headerView.invalidate();

			addHeaderView(headerView);
		} else {
			if (null != headerView) {
				removeHeaderView(headerView);
			}
		}
	}

	/**
	 * 设置是否可以上滑加载跟多
	 * 
	 * @param canScrollUp
	 */
	public void setCanScrollUp(boolean canScrollUp) {
		this.canScrollUp = canScrollUp;
		if (canScrollUp) {
			footerView = createFooterView((Activity) getContext());
			addFooterView(footerView);
			super.setOnScrollListener(this);
		} else {
			if (null != footerView) {
				removeFooterView(footerView);
			}
		}
	}

	/**
	 * 设置下拉刷新时的阻尼系数
	 * 
	 * @param damping
	 */
	public void setDamping(float damping) {
		this.damping = damping;
	}

	/** 手动测量指定View */
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}

		child.measure(childWidthSpec, childHeightSpec);
	}

	// 当状态改变时候，调用该方法，以更新头部的显示界面
	private void changeHeaderViewByState() {
		switch (state) {
		case RELEASE_TO_REFRESH:
			releaseToRefresh(headerView);
			break;
		case PULL_TO_REFRESH:
			pullToRefresh(headerView);
			break;
		case REFRESHING:
			refreshing(headerView);
			break;
		case DONE:
			done(headerView);
			break;
		}
	}

	/** 初始化箭头的变化动作 */
	private void initAnimation() {
		// 设置箭头由下朝上的动画
		animation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(100);
		animation.setFillAfter(true);

		// 设置箭头由上朝下恢复的动画
		reverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(100);
		reverseAnimation.setFillAfter(true);
	}

	/** 进过阻尼处理 */
	private int filterDamping(float original) {
		return (int) (original * damping);
	}

	// ///////创建头部和尾部的方法，子类可复写达到自定义的效果//////////////////////////
	/**
	 * 创建头部布局
	 * 
	 * @param activity
	 * @return
	 */
	protected HeaderView createHeaderView(Activity activity) {
		return UIHelper.getHeaderView(activity);
	}

	/**
	 * 创建尾部布局
	 * 
	 * @param activity
	 * @return
	 */
	protected FooterView createFooterView(Activity activity) {
		return UIHelper.getFooterView(activity);
	}

	// ///////OnScrollListener实现/////////////////////////
	@Override
	public void onScroll(AbsListView view, int firstVisiableItem,
			int visibleItemCount, int totalItemCount) {
		if (null != externalScrollListener) {
			externalScrollListener.onScroll(view, firstVisiableItem,
					visibleItemCount, totalItemCount);
		}

		if (!canScrollUp) {
			return;
		}

		if (visibleItemCount == totalItemCount) {
			footerView.setVisibility(View.GONE);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (null != externalScrollListener) {
			externalScrollListener.onScrollStateChanged(view, scrollState);
		}

		if (!canScrollUp) {
			return;
		}

		// 算法是：如果最后一个可见位置等于最后一个item，并且可见数不等于所有item数据时，表示触发加载更多事件
		if (view.getLastVisiblePosition() == view.getCount() - 1
				&& visibleItemCount != view.getCount()) {
			footerView.setVisibility(View.VISIBLE);
			footerView.getTextView().setText("加载中...");
			footerView.getProgressBar().setVisibility(View.VISIBLE);
			if (null != pullToRefreshListViewListener) {
				pullToRefreshListViewListener.onScrollUpRefresh();
			}
		}
	}

	// ///////PullDownStateListener监听实现，子类可复写达到自定义的效果//////////////////////////
	@Override
	public void done(HeaderView headerView) {
		// 完成状态，隐藏头部
		headerView.setPadding(headerView.getPaddingLeft(), -1
				* headContentHeight, headerView.getPaddingRight(),
				headerView.getPaddingBottom());
		headerView.invalidate();
		headerView.getProgressBar().setVisibility(View.GONE);
		headerView.getArrow().clearAnimation();
		headerView.getArrow().setImageDrawable(new ArrowDrawable());
		headerView.getPullToRefreshHint().setText("下拉可以刷新");
		headerView.getLastUpdateHint().setVisibility(View.VISIBLE);
	}

	@Override
	public void pullToRefresh(HeaderView headerView) {
		// 下拉刷新状态
		headerView.getProgressBar().setVisibility(View.GONE);
		headerView.getPullToRefreshHint().setVisibility(View.VISIBLE);
		headerView.getLastUpdateHint().setVisibility(View.VISIBLE);
		headerView.getArrow().clearAnimation();
		headerView.getArrow().setVisibility(View.VISIBLE);
		if (isBack) {
			// 把箭头设置回去
			isBack = false;
			headerView.getArrow().clearAnimation();
			headerView.getArrow().startAnimation(reverseAnimation);
		}
		headerView.getPullToRefreshHint().setText("下拉可以刷新");
	}

	@Override
	public void refreshing(HeaderView headerView) {
		// 正在刷新状态
		headerView.setPadding(headerView.getPaddingLeft(),
				headContentOriginalTopPadding, headerView.getPaddingRight(),
				headerView.getPaddingBottom());
		headerView.invalidate();
		headerView.getProgressBar().setVisibility(View.VISIBLE);
		headerView.getArrow().clearAnimation();
		headerView.getArrow().setVisibility(View.GONE);
		headerView.getPullToRefreshHint().setText("加载中...");
		headerView.getLastUpdateHint().setVisibility(View.GONE);
	}

	@Override
	public void releaseToRefresh(HeaderView headerView) {
		// 松开可以刷新状态
		headerView.getArrow().setVisibility(View.VISIBLE);
		headerView.getProgressBar().setVisibility(View.GONE);
		headerView.getPullToRefreshHint().setVisibility(View.VISIBLE);
		headerView.getLastUpdateHint().setVisibility(View.VISIBLE);
		headerView.getArrow().clearAnimation();
		headerView.getArrow().startAnimation(animation);
		headerView.getPullToRefreshHint().setText("松开可以刷新");
	}

}
