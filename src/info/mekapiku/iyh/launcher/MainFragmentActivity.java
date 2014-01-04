package info.mekapiku.iyh.launcher;

import net.simonvt.widget.MenuDrawer;
import net.simonvt.widget.MenuDrawerManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;

public class MainFragmentActivity extends SherlockFragmentActivity implements
		OnItemClickListener, OnQueryTextListener, OnClickListener,
		OnFocusChangeListener {

	private static final String STATE_MENUDRAWER = MainFragmentActivity.class
			.getName() + ".menuDrawer";

	private MenuDrawerManager mMenuDrawer;
	private SearchView searchView;
	private WebView webView;
	private StoreDataManager dataManager;

	private int selectId = 0;
	private String searchQuery = "";

	private boolean mTimerRunning = false;
	private boolean doubleTapFlag = false;
	private final int doubleTime = ViewConfiguration.getDoubleTapTimeout();

	private StoreListFragment storeListFragment;
	private FragmentTransaction mFragmentTransaction;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Sherlock);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setSupportProgressBarIndeterminateVisibility(true);

		super.onCreate(savedInstanceState);

		mMenuDrawer = new MenuDrawerManager(this, MenuDrawer.MENU_DRAG_WINDOW);
		// mMenuDrawer.setContentView(R.layout.activity_main);
		mMenuDrawer.setContentView(R.layout.web_view);
		mMenuDrawer.setMenuView(R.layout.menu);

		MenuFragment menu = (MenuFragment) getSupportFragmentManager()
				.findFragmentById(R.id.f_menu);
		menu.getListView().setOnItemClickListener(this);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// WebView�̐ݒ�
		webView = (WebView) findViewById(R.id.f_web);
		WebSettings webSetting = webView.getSettings();
		webSetting.setBuiltInZoomControls(true);
		webSetting.setJavaScriptEnabled(true);
		webSetting.setLoadWithOverviewMode(true);
		webSetting.setSaveFormData(true);
		webSetting.setSavePassword(true);
		webSetting.setUseWideViewPort(true);
		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		webView.setVerticalScrollbarOverlay(true);
		webView.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				setSupportProgress(newProgress);
			}
		});

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				setSupportProgressBarIndeterminateVisibility(true);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				setSupportProgressBarIndeterminateVisibility(false);
				if (searchQuery.equals("")) {
					setTitle(view.getTitle());
				}
			}
		});
		webView.loadUrl("http://iyhoo.net/");

		// �f�[�^�}�l�[�W���[
		dataManager = StoreDataManager.getInstance();

		// TODO �N�V�����o�[�ɃC�x���g���X�i��ǉ�

		// �ݒ��ʂ̃t���O�����g��ǉ�
		storeListFragment = new StoreListFragment();

		// Activity��Fragment��o�^����B
		mFragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		// Layout�ʒu��̎w��
		mFragmentTransaction.replace(R.id.f_web, storeListFragment);
		// Fragment�̕ω����̃A�j���[�V�������w��
		mFragmentTransaction
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		mFragmentTransaction.addToBackStack(null);
	}

	/**
	 * ��Ԃ̕���
	 */
	@Override
	protected void onRestoreInstanceState(Bundle inState) {
		super.onRestoreInstanceState(inState);
		mMenuDrawer.onRestoreDrawerState(inState
				.getParcelable(STATE_MENUDRAWER));
		webView.restoreState(inState);
		searchQuery = inState.getString("query");
		selectId = inState.getInt("selectId");
		setTitle(searchQuery);
	}

	/**
	 * ��Ԃ̕ۑ�
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(STATE_MENUDRAWER,
				mMenuDrawer.onSaveDrawerState());
		webView.saveState(outState);
		outState.putString("query", searchQuery);
		outState.putInt("selectId", selectId);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);
		// �A�N�V�����o�[�ɃT�[�`�A�C�e�����Z�b�g���܂�
		final MenuItem item = menu.add("Search");
		item.setIcon(android.R.drawable.ic_menu_search);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

		// �T�[�`�A�C�e�����N���b�N������T�[�`�r���[�ɐ؂�ւ���悤�ɂ��܂�
		searchView = new SearchView(this);
		searchView.setOnQueryTextListener(this);
		searchView.setOnSearchClickListener(this);
		searchView.setOnFocusChangeListener(this);
		searchView.setQueryHint("������������͂��Ă�������");
		item.setActionView(searchView);

		// ���j���[�ɐF�X�ǉ�
		menu.add(Menu.NONE, 2, Menu.NONE, "���̃u���E�U�ŊJ��");

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d("Menu", item.getItemId() + "");

		switch (item.getItemId()) {
		case android.R.id.home:
			mMenuDrawer.toggleMenu();
			break;
		case R.id.menu_settings:
			mFragmentTransaction.commit();
			break;
		case 2:
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(webView
					.getUrl()));
			startActivityForResult(intent, 0);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		// �_�u���^�b�v�Ȃ�I���Ƃ���
		if (!mTimerRunning) {
			mTimerRunning = true;
			// �J�E���g�_�E������
			new CountDownTimer(doubleTime, 1) {
				@Override
				public void onTick(long millisUntilFinished) {
					// �J�E���g�_�E������
				}

				@Override
				public void onFinish() {
					// �J�E���g��0�ɂȂ������̏���
					if (doubleTapFlag) {
						// �_�u���^�b�v�̏���
						doubleTapFlag = false;
						finish();
					} else {
						// �V���O���^�b�v�̏���
						final int drawerState = mMenuDrawer.getDrawerState();
						if (drawerState == MenuDrawer.STATE_OPEN
								|| drawerState == MenuDrawer.STATE_OPENING) {
							mMenuDrawer.closeMenu();
						} else if (webView.hasFocus()) {
							goBack();
						}
					}
					mTimerRunning = false;
				}
			}.start();
		} else {
			// �_�u���^�b�v
			doubleTapFlag = true;
		}
		return;
		// super.onBackPressed();
	}

	// �O�̃y�[�W�ɖ߂�
	private void goBack() {
		// WebView�Ŗ߂�
		if (webView.hasFocus()) {
			if (webView.canGoBack()) {
				webView.goBack();
				return;
			} else {
				// �߂�Ȃ��Ȃ�A�v���I��
				finish();
			}
		} else {
			// WebView�ȊO�Ƀt�H�[�J�X������Ƃ�
			super.onBackPressed();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		mMenuDrawer.setActiveView(view);

		if (!searchQuery.equals("")) {
			webView.loadUrl(StoreDataManager.getItem((int) id).makeSearchUrl(
					searchQuery));
		} else {
			webView.loadUrl(StoreDataManager.getItem((int) id).getHomeUrl());
		}

		selectId = (int) id;
		mMenuDrawer.closeMenu();
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		if (!query.equals("")) {
			webView.loadUrl(StoreDataManager.getItem(selectId).makeSearchUrl(
					query));
		}

		searchQuery = query;

		// �^�C�g����ύX
		setTitle(query);
		// SearchView���B��
		searchView.onActionViewCollapsed();
		getSherlock().getActionBar().setDisplayHomeAsUpEnabled(false);
		getSherlock().getActionBar().setHomeButtonEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		return true;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		// �������͖��̃C�x���g
		return false;
	}

	@Override
	public void onClick(View v) {
		// ���j���[�o�[�N���b�N���Ɍ����o�[�Ƀt�H�[�J�X
		searchView.requestFocus();
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// SearchView����t�H�[�J�X���O�ꂽ����IME����
		if (!hasFocus) {
			hideKeyBoard(v);
		}
	}

	// �L�[�{�[�h���B��
	private void hideKeyBoard(View v) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}
}
