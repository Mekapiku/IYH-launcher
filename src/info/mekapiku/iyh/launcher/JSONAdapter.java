package info.mekapiku.iyh.launcher;

import org.json.JSONObject;

import android.content.Context;
import android.widget.ArrayAdapter;

public class JSONAdapter extends ArrayAdapter<JSONObject> {
	public JSONAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}

	public void setData(JSONObject data) {
		this.add(data);
	}
}
