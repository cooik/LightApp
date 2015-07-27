package com.light.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.widget.SectionIndexer;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.light.R;
import com.light.bean.FriendBean;
import com.light.bean.ResponseBean;
import com.light.network.Constant;
import com.light.presenter.IHomePresenter;
import com.light.presenter.impl.HomePresenterImpl;
import com.light.util.CnToSpell;
import com.light.util.ToastUtils;
import com.light.widget.IndexableListView;

public class FriendsActivity extends BaseActivity {

	private IndexableListView friendsListView;

	private QuickAdapter<FriendBean> mAdapter;

	private IHomePresenter homePresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.activity_friends,
				"FriendsActivity");
		
		friendsListView = (IndexableListView) findViewById(R.id.lv_show_friends);
		friendsListView.setFastScrollEnabled(true);

		homePresenter = new HomePresenterImpl();
		homePresenter.queryFriends();
	}

	@Override
	public void onEvent(ResponseBean event) {
		if (event.getResult_code() == Constant.CODE_SUCCESS) {
			if (Constant.QUERY_FRIENDS.equals(event.getRequestUrl())) {
				List<FriendBean> friendsListData = new ArrayList<FriendBean>();
				List<FriendBean> friendsListData2 = event.getFriends();
				FriendBean bean ;
				for(int i = 0 ; i < mSections.length();i++){
					String val = String.valueOf(mSections.charAt(i));
					
					for(FriendBean fb : friendsListData2){
						if(String.valueOf(CnToSpell.getSpell(fb.getName(),true).charAt(0)).toUpperCase().equals(val)){
							bean = new FriendBean();
							bean.setName(val);
							
							friendsListData.add(bean);
							break;
						}
					}
					
				}
				friendsListData.addAll(friendsListData2);
				Collections.sort(friendsListData, new Comparator<FriendBean>(){
					@Override
					public int compare(FriendBean o1,FriendBean o2) {

						String oo1 = CnToSpell.getSpell(o1.getName(), true).toLowerCase();
						String oo2 = CnToSpell.getSpell(o2.getName(), true).toLowerCase();
						Character c1 = new Character(oo1.toCharArray()[0]);
						Character c2 = new Character(oo2.toCharArray()[0]);
						if(c1 > c2) return 1;
						if(c1 < c2) return -1;
						return 0;
					}
				});
				
				mAdapter = new ContentAdapter<FriendBean>(FriendsActivity.this,
						R.layout.friend_list_item, friendsListData) {

					@Override
					protected void convert(BaseAdapterHelper helper,
							FriendBean item) {

						if(item.getId()!=0){
							helper.setText(R.id.tv_friend_name, item.getName());
							helper.setImageUrl(R.id.iv_friend_avatar,
									item.getAvatar());
							helper.setText(R.id.tv_friend_id, item.getId() + "");
							helper.setVisible(R.id.tv_index_tip, false);
							helper.setVisible(R.id.tv_friend_name, true);
							helper.setVisible(R.id.iv_friend_avatar, true);
						}else{
							helper.setText(R.id.tv_index_tip, item.getName());
							helper.setVisible(R.id.tv_index_tip, true);
							helper.setVisible(R.id.tv_friend_name, false);
							helper.setVisible(R.id.iv_friend_avatar, false);
						}
					}
				};
				// 设置适配器
				friendsListView.setAdapter(mAdapter);
			}

		} else {
			ToastUtils.show(context, event.getResult_msg());
		}
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
			// If there is no item for current section, previous section will be
			// selected
			for (int i = section; i >= 0; i--) {
				for (int j = 0; j < getCount(); j++) {
					if (i == 0) {
						// For numeric section
						for (int k = 0; k <= 9; k++) {
							if (String.valueOf(CnToSpell.getSpell(((FriendBean)getItem(j)).getName(),true).charAt(0)).equals(
									String.valueOf(k)))
								return j;
						}
					} else {
						if (
								String.valueOf(CnToSpell.getSpell(((FriendBean)getItem(j)).getName(),true).charAt(0)).toUpperCase().equals(
								String.valueOf(mSections.charAt(i))))
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

}
