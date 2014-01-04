package info.mekapiku.iyh.launcher;

import java.util.ArrayList;

public final class StoreDataManager {
	private String name[] = { "coneco.net", "ベストゲート", "価格.com" };
	private String homeUrl[] = { "http://www.coneco.net", "http://www.bestgate.net",
			"http://kakaku.com" };
	private String searchUrl[] = {
			"http://www.coneco.net/PriceList.asp?FREE_WORD={Shift_JIS}&SEARCHALL=1",
			"http://www.bestgate.net/search.phtml?word={Shift_JIS}",
			"http://kakaku.com/search_results/?query={Shift_JIS}&category=0001" };

	static private ArrayList<StoreData> list;
	static public int length;

	public StoreDataManager() {
		list = new ArrayList<StoreData>();
		for (int i = 0; i < name.length; i++) {
			list.add(new StoreData(i, name[i], homeUrl[i], searchUrl[i]));
		}
		length = list.size();
	}

	private static class StoreDataManagerHolder {
		private static final StoreDataManager instance = new StoreDataManager();
	}

	public static StoreDataManager getInstance() {
		return StoreDataManagerHolder.instance;
	}

	public static StoreData getItem(int id) {
		if (id > list.size()) {
			return null;
		} else {
			return list.get(id);
		}
	}
}
