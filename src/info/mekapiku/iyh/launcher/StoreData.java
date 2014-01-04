package info.mekapiku.iyh.launcher;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

import org.apache.commons.codec.net.URLCodec;

import android.util.Log;

public class StoreData {
	private int id;
	private String name;
	private String homeUrl;
	private String searchUrl;
	private String encodeType;

	/**
	 * �R���X�g���N�^
	 *
	 * @param id
	 * @param name
	 * @param url
	 * @param searchUrl
	 */
	StoreData(int id, String name, String url, String searchUrl) {
		this.id = id;
		this.name = name;
		this.homeUrl = url;
		this.searchUrl = searchUrl;
		encodeType = getEncodeType(searchUrl);
	}

	/**
	 * �G���R�[�h�̎�ނ�\�ߎ擾
	 *
	 * @param searchUrl
	 * @return
	 */
	public String getEncodeType(String searchUrl) {
		String encodeType = null;

		if (searchUrl.matches(".*" + Pattern.quote("{Shift_JIS}") + ".*"))
			encodeType = "sjis";
		else if (searchUrl.matches(".*" + Pattern.quote("{EUC-JP}") + ".*"))
			encodeType = "euc_jp";
		else if (searchUrl.matches(".*" + Pattern.quote("{UTF-8}") + ".*"))
			encodeType = "utf8";

		return encodeType;
	}

	/**
	 * �����pURL���쐬
	 *
	 * @param str
	 * @return
	 */
	public String makeSearchUrl(String str) {
		URLCodec codec = new URLCodec();
		try {
			str = codec.encode(str, this.encodeType);
		} catch (UnsupportedEncodingException e) {
			// TODO �����������ꂽ catch �u���b�N
			Log.d("encode", "������");
		}
		return searchUrl.replaceAll("\\{.*\\}", str);
	}

	// �ȉ��Q�b�^�[
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getHomeUrl() {
		return homeUrl;
	}

	public String getSearchUrl() {
		return searchUrl;
	}
}
