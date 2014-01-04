package info.mekapiku.iyh.launcher;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;

public class StoreListFragment extends SherlockListFragment implements
		LoaderManager.LoaderCallbacks<JSONObject> {

	/** �A�_�v�^�[ */
	private JSONAdapter mAdapter;

	/** ���[�_�[ */
	private AsyncFetchJSONLoader mLoader;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		final ListView listView = getListView();
		listView.setItemsCanFocus(false);
		//�I�����[�h���w�肷��
	    listView.setChoiceMode(ListView.CHOICE_MODE_NONE);

		setEmptyText("�f�[�^�Ȃ�");
		mAdapter = new JSONAdapter(getActivity(),
				android.R.layout.simple_list_item_multiple_choice);
		setListAdapter(mAdapter);

		setListShown(false);

		LoaderManager manager = getLoaderManager();
		manager.initLoader(0, null, this);
	}

	@Override
	public Loader<JSONObject> onCreateLoader(int id, Bundle args) {
		mLoader = new AsyncFetchJSONLoader(getActivity(),
				"http://iyh-api.herokuapp.com/");
		return mLoader;
	}

	@Override
	public void onLoadFinished(Loader<JSONObject> loader, JSONObject data) {
		mAdapter.setData(data);
		if (isResumed()) {
			// ���X�g��\��
			setListShown(true);
		} else {
			// �ꎞ��~���̓A�j���[�V�����Ȃ��ŕ\��
			setListShown(true);
			// setListShownNoAnimation(true);
		}
	}

	/**
	 * ���[�_�[���ēǂݍ��݂��邽�߂̃��\�b�h �@
	 */
	public void loderContentChanged() {
		mLoader.onContentChanged();
	}

	@Override
	public void onLoaderReset(Loader<JSONObject> arg0) {
		mAdapter.setData(null);
	}
}