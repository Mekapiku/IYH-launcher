package info.mekapiku.iyh.launcher;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.app.SherlockListFragment;

public class MenuFragment extends SherlockListFragment {

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1);
		StoreDataManager storeList = StoreDataManager.getInstance();

		for (int i = 0; i < StoreDataManager.length; i++) {
			adapter.add(StoreDataManager.getItem(i).getName());
		}

		setListAdapter(adapter);
	}
}
