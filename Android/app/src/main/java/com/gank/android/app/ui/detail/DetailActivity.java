package com.gank.android.app.ui.detail;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gank.android.app.R;
import com.gank.android.app.controller.CollectController;
import com.gank.android.app.ui.base.BaseActivity;
import com.gank.android.app.ui.dialog.MoreDialog;
import com.gank.android.app.even.RequestEventID;
import com.gank.android.mvc.events.BaseEvent;
import com.gank.android.mvc.events.DataEvent;
import com.gank.android.mvc.events.DataEventListener;
import com.gank.android.mvc.events.EventDispatcherUtils;
import com.gank.android.mvc.events.NoticeEventListener;
import com.just.library.AgentWeb;
import com.just.library.ChromeClientCallbackManager;

import butterknife.BindView;
import cn.gank.androidlibs.httphelper.Result;
import cn.gank.androidlibs.log.XLog;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/** web页面
 * @author shijunxing
 * @date 2017/9/8
 */

public class DetailActivity extends BaseActivity<CollectController> {


    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;

    @BindView(R.id.ll_toolbar_right)
    LinearLayout llToolbarRight;

    @BindView(R.id.ll_toolbar_left)
    LinearLayout llToolbarLeft;

    @BindView(R.id.ll_detail_content)
    LinearLayout llContent;


    protected AgentWeb mAgentWeb;
    protected MoreDialog moreDialog;

    private String id;
    private String link;
    private String title;
    private String content;

    private boolean isShowMore;
    private boolean isShowCollect;

    public static final String ID = "id";
    public static final String LINK = "link";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final String SHOWMORE = "ShowMore";
    public static final String SHOWCOLLECT = "isShowCollect";


    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //do you  work
            Log.i("Info", "BaseWebActivity onPageStarted");
        }
    };
    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {

        }
    };


    private ChromeClientCallbackManager.ReceivedTitleCallback mCallback = new ChromeClientCallbackManager.ReceivedTitleCallback() {
        @Override
        public void onReceivedTitle(WebView view, String title) {

        }
    };


    @Override
    public void initVariables() {

        id = getIntent().getStringExtra(ID);
        link = getIntent().getStringExtra(LINK);
        title = getIntent().getStringExtra(TITLE);
        content = getIntent().getStringExtra(CONTENT);
        isShowMore = getIntent().getBooleanExtra(SHOWMORE, true);
        isShowCollect = getIntent().getBooleanExtra(SHOWCOLLECT, true);

        if ((id == null || "".equals(id))) {
            id = "-1";
        }
    }

    @Override
    public void initViews(View view, Bundle savedInstanceState) {
        initToolbar();
        initWebView();
    }

    @Override
    public void loadData() {
        if (isShowMore) {
            isCollect();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_detail;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("Info", "result:" + requestCode + " result:" + resultCode);
        mAgentWeb.uploadFileResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAgentWeb.getWebLifeCycle().onDestroy();
    }

    private void initToolbar() {

        llToolbarRight.setVisibility(isShowMore ? View.VISIBLE : View.GONE);
        tvToolbarTitle.setText(title);
        moreDialog = MoreDialog.getInstance(!(content == null || "".equals(content)) && isShowCollect);
        moreDialog.setShareInfo(link, title);
        moreDialog.setListener(new MoreDialog.OnDialogSelectListener() {
            @Override
            public void onDialogSelect(int type) {
                switch (type) {

                    case MoreDialog.SHARE_TYPE_BROWSER:
                        openBrowser(link);
                        break;
                    case MoreDialog.SHARE_TYPE_COPY_LINK:
                        copyLink(DetailActivity.this, mAgentWeb.getWebCreator().get().getUrl());
                        mAgentWeb.getJsInterfaceHolder();
                        break;
                    case MoreDialog.SHARE_TYPE_FRESH:
                        if (mAgentWeb != null) {
                            mAgentWeb.getLoader().reload();
                        }
                        break;
                    case MoreDialog.SHARE_TYPE_COLLECT:
                        if (moreDialog.isCollect()) {
                            unCollect();
                        } else {
                            collect();
                        }
                        break;
                    case MoreDialog.SHARE_TYPE_SHARE:
                        share();
                        break;

                    default:
                        break;
                }
            }
        });

        llToolbarRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreDialog.show(getSupportFragmentManager());
            }
        });
        llToolbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    private void initWebView() {

        OverScrollDecoratorHelper.setUpStaticOverScroll(llContent, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

        tvToolbarTitle.setText(title);
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(llContent, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .defaultProgressBarColor()
                .setReceivedTitleCallback(mCallback)
                .setWebChromeClient(mWebChromeClient)
                .setWebViewClient(mWebViewClient)
                .setSecutityType(AgentWeb.SecurityType.strict)
                .createAgentWeb()
                .ready()
                .go(link);

    }

    private void collect() {
        EventDispatcherUtils.addListener(getReceiverIDCard(), RequestEventID.COLLECT, new NoticeEventListener() {

            @Override
            public void onEvent(BaseEvent event) {
                getUiDelegate().toast(event.getMsg());
                moreDialog.setCollect(event.isSuccess());
            }

        });
        controller.collect(this, content);

    }

    private void unCollect() {
        EventDispatcherUtils.addListener(getReceiverIDCard(), RequestEventID.UN_COLLECT, new NoticeEventListener() {

            @Override
            public void onEvent(BaseEvent event) {
                getUiDelegate().toast(event.getMsg());
                moreDialog.setCollect(!event.isSuccess());
            }


        });
        controller.unCollect(this, id);

    }

    private void isCollect() {
        EventDispatcherUtils.addListener(getReceiverIDCard(), RequestEventID.IS_COLLECT, new NoticeEventListener() {

            @Override
            public void onEvent(BaseEvent event) {
                if (event.isSuccess()) {
                    moreDialog.setCollect(true);
                }
            }
        });

        controller.isCollect(this, id);
    }

    private void openBrowser(String targetUrl) {
        if (TextUtils.isEmpty(targetUrl) || targetUrl.startsWith("file://")) {
            getUiDelegate().toast(targetUrl + " 该链接无法使用浏览器打开。");
            return;
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri url = Uri.parse(targetUrl);
        intent.setData(url);
        startActivity(intent);
    }


    private void share() {
        Intent intent1 = new Intent(Intent.ACTION_SEND);
        intent1.putExtra(Intent.EXTRA_TEXT, link);
        intent1.setType("text/plain");
        startActivity(Intent.createChooser(intent1, "分享到"));

    }

    private void copyLink(Context context, String text) {
        ClipboardManager mClipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        mClipboardManager.setPrimaryClip(ClipData.newPlainText(null, text));
        getUiDelegate().toast("已经复制到剪切板");
    }

}
