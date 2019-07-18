package cl.json.social;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;

import cl.json.ShareFiles;

public class WechatShare extends SingleShareIntent {

  private static final String PACKAGE = "com.tencent.mm";
  private static final String PLAY_STORE_LINK = "market://details?id=com.tencent.mm";
  private static final String SHARE_TO_TIMELINE = "com.tencent.mm.ui.tools.ShareToTimeLineUI";
  private static final String SHARE_TO_SESSTION = "com.tencent.mm.ui.tools.ShareImgUI";

  public WechatShare(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public void open(ReadableMap options) throws ActivityNotFoundException {
    super.open(options);
    // 打开系统分享弹框
    // this.openIntentChooser();
    // 绕过系统分享，直接分享对话
    this.openWechatShare(options);
  }

  @Override
  protected String getPackage() {
    return PACKAGE;
  }

  @Override
  protected String getDefaultWebLink() {
    return null;
  }

  @Override
  protected String getPlayStoreLink() {
    return PLAY_STORE_LINK;
  }

  private void openWechatShare(ReadableMap options) {
    Intent intent = new Intent();
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    if (options.getBoolean("isTimeLine")) {
      intent.setComponent(new ComponentName(PACKAGE, SHARE_TO_TIMELINE));
    }else {
      intent.setComponent(new ComponentName(PACKAGE, SHARE_TO_SESSTION));
    }
    if (options.getString("type").contains("image")) {
      intent.setAction("android.intent.action.SEND_MULTIPLE");
      intent.setType("image/*");
      ShareFiles shareFiles = this.getFileShares(this.options);
      if (shareFiles != null) {
        intent.putExtra(Intent.EXTRA_STREAM, shareFiles.getURI());
      }
    } else {
      intent.setAction(Intent.ACTION_SEND);
      intent.setType(options.getString("type"));
      intent.putExtra(Intent.EXTRA_TEXT, options.getString("message"));
      intent.putExtra("Kdescription", options.getString("message"));
    }
    this.reactContext.startActivity(intent);
  }
}
