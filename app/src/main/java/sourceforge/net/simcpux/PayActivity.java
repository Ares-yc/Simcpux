package sourceforge.net.simcpux;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONObject;

public class PayActivity extends AppCompatActivity {

    private Button payBtn;
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        payBtn = (Button) findViewById(R.id.btn_pay);
        api = WXAPIFactory.createWXAPI(this, Constant.APP_ID, false);
        // 将该app注册到微信
        api.registerApp(Constant.APP_ID);

    }

    //点击支付
    public void onPay(View view) {

        String url = "http://wxpay.wxutil.com/pub_v2/app/app_pay.php";
        payBtn.setEnabled(false);
        Toast.makeText(PayActivity.this, "获取订单中...", Toast.LENGTH_SHORT).show();
        try {
            Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
            Response<String> response = NoHttp.startRequestSync(request);
            if (response != null && response.get() != null) {
                JSONObject json = new JSONObject(response.get());
                if (!json.has("retcode")) {
                    PayReq req = new PayReq();
                    req.appId = json.getString("appid");
                    req.partnerId = json.getString("partnerid");
                    req.prepayId = json.getString("prepayid");
                    req.nonceStr = json.getString("noncestr");
                    req.timeStamp = json.getString("timestamp");
                    req.packageValue = json.getString("package");
                    req.sign = json.getString("sign");
                    req.extData = "app data"; // optional
                    Toast.makeText(PayActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                    api.sendReq(req);
                } else {
                    Log.d("PAY_GET", "返回错误" + json.getString("retmsg"));
                    Toast.makeText(PayActivity.this, "返回错误" + json.getString("retmsg"), Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d("PAY_GET", "服务器请求错误");
                Toast.makeText(PayActivity.this, "服务器请求错误", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        payBtn.setEnabled(true);
    }
}
