package info.mekapiku.iyh.launcher;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CustomWebViewClient extends WebViewClient {
	private static ProgressDialog waitDialog;

	// �ǂݍ��݊J�n���ɌĂяo����郁�\�b�h���I�[�o�[���C�h���A
	// ���̂Ȃ��Ńv���O���X�_�C�A���O����ʏ�ɕ\�����s���B
	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		super.onPageStarted(view, url, favicon);

		waitDialog = new ProgressDialog(view.getContext());
		waitDialog.setMessage("�Ǎ���");
		waitDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		waitDialog.show();
	}

	// �ǂݍ��ݏI�����ɌĂяo����郁�\�b�h���I�[�o�[���C�h���A
	// ���̂Ȃ��Ńv���O���X�_�C�A���O�̏������s���B
	@Override
	public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);

		// �y�[�W�ǂݍ��ݏI�����\�b�h��������Ă΂�邱�Ƃ����邽��
		// ���݃`�F�b�N���s��
		if (waitDialog != null) {
			waitDialog.dismiss();
			waitDialog = null;
		}
	}

	// �ǂݍ��ݎ��s���ɌĂяo����郁�\�b�h���I�[�o�[���C�h���A
	// �G���[�_�C�A���O����ʂɕ\��������B
	@Override
	public void onReceivedError(WebView view, int errorCode,
			String description, String failingUrl) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());

		dialog.setTitle("�G���[");
		dialog.setMessage("�ǂݍ��݂Ɏ��s���܂���");

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
