package com.ktgame.operationclipboard;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Build;
import android.os.PersistableBundle;
import android.util.Log;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

public class OperationClipboard extends UnityPlayerActivity {
    public static String GetClipBoard() {
        try {
            ClipboardManager clipboard = (ClipboardManager) UnityPlayer.currentActivity.getSystemService("clipboard");
            if (clipboard != null && clipboard.hasPrimaryClip() && clipboard.getPrimaryClip().getItemCount() > 0) {
                CharSequence text = clipboard.getPrimaryClip().getItemAt(0).getText();
                return text != null ? text.toString() : "";
            }
        } catch (Exception e) {
            Log.e("Unity", "[Clipboard] Bị Android chặn đọc ngầm: " + e.getMessage());
        }
        return "";
    }

    public static void SetClipBoard(final String text) {
        UnityPlayer.currentActivity.runOnUiThread(new Runnable() {
            public void run() {
                try {
                    ClipboardManager clipboard = (ClipboardManager) UnityPlayer.currentActivity.getSystemService("clipboard");
                    ClipData clip = ClipData.newPlainText("Copied Text", text);
                    
                    // Ý Tưởng 3: Tắt thông báo rác (Toast) trên Android 13 trở lên
                    if (Build.VERSION.SDK_INT >= 33) {
                        PersistableBundle bundle = new PersistableBundle();
                        bundle.putBoolean("android.content.extra.IS_SENSITIVE", true);
                        clip.getDescription().setExtras(bundle);
                    }
                    
                    if (clipboard != null) {
                        clipboard.setPrimaryClip(clip);
                    }
                } catch (Exception e) {
                    Log.e("Unity", "[Clipboard] Lỗi ghi dữ liệu: " + e.getMessage());
                }
            }
        });
    }
}
