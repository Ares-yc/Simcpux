package sourceforge.net.simcpux;

import android.app.Application;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.OkHttpNetworkExecutor;

/**
 * ====================================
 * 作    者：Ares(颜崔)
 * 地    址：https://github.com/Ares-yc
 * 描    述：
 * 版    本：1.0.0
 * 创建时间：2017/8/31/031.
 * 修改时间：2017/8/31/031.
 * ====================================
 */

public class App extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        initNohttp();
    }

    //初始化NoHttp
    private void initNohttp() {
        NoHttp.initialize(this, new NoHttp.Config()
                .setConnectTimeout(30 * 1000) // 全局连接超时时间，单位毫秒。
                .setReadTimeout(30 * 1000) // 全局服务器响应超时时间，单位毫秒。
                .setNetworkExecutor(new OkHttpNetworkExecutor()));  // 使用OkHttp做网络层。
    }
}
