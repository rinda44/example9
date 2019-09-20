package example.myapplication27;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.myjson.JSONObject;

import example.myapplication27.http.RdRest;
import example.myapplication27.student.StudentActivity;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button)findViewById(R.id.id_button);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                doLogin(v);
            }
        });
    }

    public void doLogin(View view)
    {
        //获取用户输入
        String username = ((EditText)findViewById(R.id.id_username)).getText().toString();
        String password = ((EditText)findViewById(R.id.id_password)).getText().toString();

        //网站后台交互
        JSONObject jreq = new JSONObject();
        jreq.put("username", username);
        jreq.put("password", password);
        String uri = Config.i.service + "api/Login.api";
        RdRest.post(uri, jreq, new RdRest.Callback()
        {
            @Override
            public void onResult(JSONObject jresp) throws Exception
            {
                //从RdRest线程中回调
                int errorCode = jresp.getInt("errorCode");
                if(errorCode != 0)
                {
                    //服务器如果返回错误，则提示出错
                    String reason = jresp.getString("reason");
                    Toast.makeText(MainActivity.this, "服务器返回错误" + reason, Toast.LENGTH_SHORT).show();
                    return;
                }

                //进入主界面
                Toast.makeText(MainActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                onLoginResult();
            }
        });

    }
    private void onLoginResult()
    {
        Intent intent = new Intent(this, StudentActivity.class);
        startActivity(intent);

        //从返回栈移除本界面
        finish();
    }
}
