package com.light.fragment;

import io.rong.imkit.RongIM;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.SectionIndexer;
import android.widget.TabHost;
import android.widget.TextView;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.lidroid.xutils.exception.DbException;
import com.light.R;
import com.light.bean.FriendBean;
import com.light.bean.ResponseBean;
import com.light.constant.SysConst;
import com.light.network.Constant;
import com.light.presenter.IHomePresenter;
import com.light.presenter.impl.HomePresenterImpl;
import com.light.util.CnToSpell;
import com.light.util.LibraryUtils;
import com.light.util.SessionUtils;
import com.light.view.IMessageView;
import com.light.widget.IndexableListView;

/**
 * 消息页面
 * 
 * @author zdnuist
 * @dateTime 2015-6-7下午5:04:49
 */
public class MessageFragment extends BaseFragment implements IMessageView {

	public static final String PAGE_NAME = "MessageFragment";

	TabHost tabHost;

	private IndexableListView friendsListView;

	private QuickAdapter<FriendBean> mAdapter;

	private IHomePresenter homePresenter;

	private String[] layoutShows = { "新的朋友" };
	private int[] layoutImg = { R.drawable.ic_launcher };

	private ViewGroup commonNpcsLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Log.d(PAGE_NAME, "onCreateView");
		View view = super.onCreateView(inflater, container, savedInstanceState,
				R.layout.fragment_message, "messageFragment");

		// 获取TabHost对象
		tabHost = (TabHost) view.findViewById(R.id.tabhost);
		// 如果没有继承TabActivity时，通过该种方法加载启动tabHost
		tabHost.setup();
		createTab("消息", R.id.ll_view1);
		createTab("通讯录", R.id.friends_layout);

		friendsListView = (IndexableListView) view
				.findViewById(R.id.lv_show_friends);
		friendsListView.setFastScrollEnabled(true);

		homePresenter = new HomePresenterImpl(this);
		homePresenter.queryFriends();

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(SysConst.FRIENDS_LIST_CHANGE_ACTION);
		context.registerReceiver(friendsListChangeReceiver, intentFilter);

		commonNpcsLayout = (ViewGroup) view
				.findViewById(R.id.layout_common_npcs);

		// 设置标签切换动作
		tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			@Override
			public void onTabChanged(String s) {
				Log.d(PAGE_NAME, "tag__:" + s);
				// if("通讯录".equals(s)){
				// if(friendsListData!=null && friendsListData.size()>0){
				// }else{
				// ToastUtils.show(context, "您还没有好友，赶快去添加吧");
				// }
				// }
			}
		});

		return view;
	}

	private void createTab(String text, int id) {
		tabHost.addTab(tabHost.newTabSpec(text)
				.setIndicator(createTabView(text)).setContent(id));
	}

	private View createTabView(String text) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.tab_indicator, null);
		TextView tv = (TextView) view.findViewById(R.id.tv_tab);
		tv.setText(text);
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	private void openConversationList(View v) {
		// RongIM.getInstance().startConversationList(context);
	}

	private void openConverstation(View v) {
		// RongIM.getInstance().startConversation(context,
		// ConversationType.PRIVATE, "9528", "聊天标题");
		RongIM.getInstance().startPrivateChat(context,
				SessionUtils.getInstance().getUserBean().getId() + "",
				SessionUtils.getInstance().getUserBean().getName());
	}

	private void openKefu(View v) {
		// RongIM.getInstance().startConversation(context,
		// ConversationType.PRIVATE, "9528", "聊天标题");
		String customerServiceId = "KEFU1433838742094"; // 客服Id。
		// 启动客服。
		RongIM.getInstance().startCustomerServiceChat(context,
				customerServiceId, "客服会话标题");
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onEvent(ResponseBean event) {
		if (Constant.QUERY_FRIENDS.equals(event.getRequestUrl())) {
			// if (event.getResult_code() == Constant.CODE_SUCCESS) {
			List<FriendBean> friendsListData2 = event.getFriends();
			doData(friendsListData2, true);
			// } else {
			// ToastUtils.show(context, event.getResult_msg());
			// }

		}
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	private String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	private abstract class ContentAdapter<T> extends QuickAdapter<FriendBean>
			implements SectionIndexer {

		public ContentAdapter(Context context, int layoutResId,
				List<FriendBean> data) {
			super(context, layoutResId, data);
		}

		@Override
		public int getPositionForSection(int section) {
			for (int i = section; i >= 0; i--) {
				for (int j = 0; j < getCount(); j++) {
					if (i == 0) {
						if (String.valueOf(
								CnToSpell.getSpell(
										((FriendBean) getItem(j)).getName(),
										true).charAt(0)).equals(
								String.valueOf(mSections.charAt(i))))
							return j;
					} else {
						if (String
								.valueOf(
										CnToSpell.getSpell(
												((FriendBean) getItem(j))
														.getName(), true)
												.charAt(0)).toUpperCase()
								.equals(String.valueOf(mSections.charAt(i))))
							return j;
					}
				}
			}
			return 0;
		}

		@Override
		public int getSectionForPosition(int position) {
			return 0;
		}

		@Override
		public Object[] getSections() {
			String[] sections = new String[mSections.length()];
			for (int i = 0; i < mSections.length(); i++)
				sections[i] = String.valueOf(mSections.charAt(i));
			return sections;
		}

	}

	@Override
	public void showFriendsListFromLocal(List<FriendBean> friendsListData2) {
		Log.d(PAGE_NAME, " showFriendsListFromLocal");
		doData(friendsListData2, false);
	}

	List<FriendBean> friendsListData;

	private void doData(List<FriendBean> friendsListData2, boolean restore) {
		FriendBean bean;
		friendsListData = new ArrayList<FriendBean>();
		if (friendsListData2 != null) {
			int j = 0;
			for (int i = 0; i < mSections.length(); i++) {
				String val = String.valueOf(mSections.charAt(i));

				for (FriendBean fb : friendsListData2) {
					fb.setUserId(SessionUtils.getInstance().getUserBean()
							.getId());
					fb.setStatus(-1);
					if (String
							.valueOf(
									CnToSpell.getSpell(fb.getName(), true)
											.charAt(0)).toUpperCase()
							.equals(val)) {
						bean = new FriendBean();
						bean.setName(val);

						friendsListData.add(bean);

						j++;
						break;
					}
				}

			}
			Log.d(PAGE_NAME, "j:" + j);
			if (j < mSections.length() - 1) {
				bean = new FriendBean();
				bean.setName("#");
				friendsListData.add(0, bean);
			}

			friendsListData.addAll(friendsListData2);

			SessionUtils.getInstance().setFriends(friendsListData2);

			if (restore) {

				try {
					dbUtils.saveAll(friendsListData2);
				} catch (DbException e) {
					LibraryUtils.onError(context, e.getMessage());
				}
			}

		}
		Collections.sort(friendsListData, new Comparator<FriendBean>() {
			@Override
			public int compare(FriendBean o1, FriendBean o2) {

				String oo1 = CnToSpell.getSpell(o1.getName(), true)
						.toLowerCase();
				String oo2 = CnToSpell.getSpell(o2.getName(), true)
						.toLowerCase();
				Character c1 = new Character(oo1.toCharArray()[0]);
				Character c2 = new Character(oo2.toCharArray()[0]);
				if (c1 > c2)
					return 1;
				if (c1 < c2)
					return -1;
				return 0;
			}
		});

		if (friendsListData.size() > 1
				&& friendsListData.get(1).getFriendId() == 0) {
			friendsListData.remove(0);
		}

		for (int i = 0; i < layoutShows.length; i++) {

			bean = new FriendBean();
			bean.setFriendId(-1);
			bean.setAvatar(layoutImg[i] + "");
			bean.setName(layoutShows[i]);
			friendsListData.add(i, bean);
		}

		mAdapter = new ContentAdapter<FriendBean>(context,
				R.layout.friend_list_item, friendsListData) {

			@Override
			protected void convert(BaseAdapterHelper helper, FriendBean item) {

				if (item.getFriendId() != 0) {
					helper.setText(R.id.tv_friend_name, item.getName());
					if (item.getFriendId() == -1) {
						helper.setImageResource(R.id.iv_friend_avatar,
								Integer.valueOf(item.getAvatar()));
					} else {
						helper.setImageUrl(R.id.iv_friend_avatar,
								item.getAvatar());
					}
					helper.setText(R.id.tv_friend_id, item.getFriendId() + "");
					helper.setVisible(R.id.tv_index_tip, false);
					helper.setVisible(R.id.tv_friend_name, true);
					helper.setVisible(R.id.iv_friend_avatar, true);
					helper.setBackgroundColor(R.id.layout_friends, context
							.getResources().getColor(R.color.text_wite_color));
				} else {
					helper.setText(R.id.tv_index_tip, item.getName());
					helper.setVisible(R.id.tv_index_tip, true);
					helper.setVisible(R.id.tv_friend_name, false);
					helper.setVisible(R.id.iv_friend_avatar, false);
					helper.setBackgroundColor(R.id.layout_friends, context
							.getResources().getColor(R.color.grey));
				}
				final int position = helper.getPosition();
				switch (helper.getPosition()) {
				case 0:
					helper.setOnClickListener(R.id.friend_item_layout,
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									openNewFriendsPage();
								}
							});
					break;
				default:
					helper.setOnClickListener(R.id.friend_item_layout,
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									RongIM.getInstance().startPrivateChat(
											context,
											mAdapter.getItem(position)
													.getFriendId() + "",
											mAdapter.getItem(position)
													.getName());
								}
							});
					break;
				}
			}
		};
		// 设置适配器
		friendsListView.setAdapter(mAdapter);

		// friendsListView.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// switch (position) {
		// case 0:
		// openNewFriendsPage();
		// break;
		// default:
		//
		// RongIM.getInstance().startPrivateChat(context,
		// mAdapter.getItem(position).getFriendId() + "",
		// mAdapter.getItem(position).getName());
		//
		// break;
		// }
		// }
		//
		// });
	}

	public void addNewFriend(FriendBean bean) {
		// if (mAdapter != null) {
		// mAdapter.add(bean);
		// }
		//
		// try {
		// dbUtils.save(bean);
		// } catch (DbException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		try {
			dbUtils.deleteAll(FriendBean.class);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		homePresenter.queryFriends();
	}

	private void openNewFriendsPage() {
		NewFriendsFragment fragment = new NewFriendsFragment();
		fragment.show(getFragmentManager(), "NewFriendsFragment");
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		commonNpcsLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openNpcsPage(v);
			}
		});
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		context.unregisterReceiver(friendsListChangeReceiver);
	}

	// 通讯录列表更新的广播
	private BroadcastReceiver friendsListChangeReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(PAGE_NAME, "friendsListChangeReceiver");

			if (intent != null
					&& SysConst.FRIENDS_LIST_CHANGE_ACTION.equals(intent
							.getAction())) {
				addNewFriend(null);
			}
		}

	};

	private void openNpcsPage(View view) {
		NpcsFragment fragment = new NpcsFragment();
		fragment.show(getFragmentManager(), "NpcsFragment");
	}
}
