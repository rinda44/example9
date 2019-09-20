package example.myapplication27.http;

import android.os.AsyncTask;

import org.apache.myhttp.HttpEntity;
import org.apache.myhttp.StatusLine;
import org.apache.myhttp.client.config.RequestConfig;
import org.apache.myhttp.client.methods.CloseableHttpResponse;
import org.apache.myhttp.client.methods.HttpPost;
import org.apache.myhttp.entity.ContentType;
import org.apache.myhttp.entity.StringEntity;
import org.apache.myhttp.impl.client.CloseableHttpClient;
import org.apache.myhttp.impl.client.HttpClients;
import org.apache.myhttp.util.EntityUtils;
import org.myjson.JSONObject;

/**
 * Created by Administrator on 2019/9/20 0020.
 */

public class RdRest extends AsyncTask
{
    String url;
    JSONObject request;
    int connnectTimeout = 3000;
    int socketTimeout = 3000;

    //构造方法
    public RdRest(String url, JSONObject request)
    {
        this.url = url;
        this.request = request;
    }

    //回调接口
    public interface Callback
    {
        public void onResult(JSONObject jresp) throws Exception;//定义
    }
    public Callback callback;//创建

    //外部调用的执行方法
    public static void post(String url, JSONObject request, Callback callback)
    {
        RdRest rdRest = new RdRest(url, request);
        rdRest.callback = callback;
        rdRest.execute();
    }

    //重写两个方法
    @Override
    protected Object doInBackground(Object[] params)
    {
       //发起请求
        JSONObject response = new JSONObject();
        try
        {
            String sreq = request.toString();
            String sresp = doPost(url, sreq);
            response = new JSONObject(sresp);

        }catch (Exception e)
        {
            try{
                response.put("errorCode", -1001);
                response.put("reason", e.getMessage());
            }catch (Exception e2){}

        }
        return response;
    }

    @Override
    protected void onPostExecute(Object o)
    {
        JSONObject response = (JSONObject)o;
        //TODO: 如果在界面线程里处理这个返回数据，可能会引起界面卡顿

        if(callback != null)
        {
            try{
                callback.onResult(response);
            }catch (Exception e3)
            {
                e3.printStackTrace();
            }
        }
    }

    //编写服务器交互方法
    private String doPost(String url, String reqText)throws Exception
    {
        //发起post请求
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connnectTimeout).build();

        HttpPost httppost = new HttpPost(url);
        httppost.setConfig(requestConfig);

        //上行数据
        StringEntity dataSent = new StringEntity(reqText, ContentType.create("text/plain", "UTF-8"));
        httppost.setEntity(dataSent);
        CloseableHttpResponse response = httpClient.execute(httppost);

        try{
            StatusLine statusLine = response.getStatusLine();
            int status = statusLine.getStatusCode();
            if(status != 200)
                throw new Exception("HTTP POST 出错：" + status + "," + statusLine.getReasonPhrase());

            //下行数据
            HttpEntity dataRecv = response.getEntity();
            String ss = EntityUtils.toString(dataRecv);
            return ss;
        }finally
        {
            try
            {
                httpClient.close();
            }catch (Exception e){}
        }
    }
}
