package info.mekapiku.iyh.launcher;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CustomWebViewClient extends WebViewClient {
	private static ProgressDialog waitDialog;

	// 読み込み開始時に呼び出されるメソッドをオーバーライドし、
	// そのなかでプログレスダイアログを画面上に表示を行う。
	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		super.onPageStarted(view, url, favicon);

		waitDialog = new ProgressDialog(view.getContext());
		waitDialog.setMessage("読込中");
		waitDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		waitDialog.show();
	}

	// 読み込み終了時に呼び出されるメソッドをオーバーライドし、
	// そのなかでプログレスダイアログの消去を行う。
	@Override
	public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);

		// ページ読み込み終了メソッドが複数回呼ばれることがあるため
		// 存在チェックを行う
		if (waitDialog != null) {
			waitDialog.dismiss();
			waitDialog = null;
		}
	}

	// 読み込み失敗時に呼び出されるメソッドをオーバーライドし、
	// エラーダイアログを画面に表示させる。
	@Override
	public void onReceivedError(WebView view, int errorCode,
			String description, String failingUrl) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());

		dialog.setTitle("エラー");
		dialog.setMessage("読み込みに失敗しました");

		dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});

		if (waitDialog != null) {
			waitDialog.dismiss();
			waitDialog = null;
		}

		dialog.setCancelable(false).create().show();
	}
}
