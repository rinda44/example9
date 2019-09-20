package example.myapplication27;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

/**
 * Created by Administrator on 2019/9/20 0020.
 */

public class Permissions
{
    // 检查和申请权限
    static final int PERMISSION_REQ_CODE = 1;
    public static void check(Activity activity)
    {
        if(Build.VERSION.SDK_INT < 23) return; // API 23以下不需要动态授权

        // 要申请的权限列表
        final String[] iwant = {
                //Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET,
        };

        // 查看已经有了哪些权限
        ArrayList<String> ihavenot = new ArrayList();
        for(int i=0; i<iwant.length; i++)
        {
            if( ContextCompat.checkSelfPermission(activity, iwant[i])
                    != PackageManager.PERMISSION_GRANTED )
            {
                ihavenot.add( iwant[i]);
            }
        }

        // 如果有未授权内容, 则申请授权
        if (ihavenot.size() > 0)
        {
            // 系统将弹出一个对话框，询问用户是否授权
            String[] ineed = new String[ihavenot.size()];
            for(int i=0; i<ihavenot.size(); i++)
            {
                ineed[i] = ihavenot.get(i);
            }
            ActivityCompat.requestPermissions(activity, ineed, PERMISSION_REQ_CODE);
        }
    }

    // 权限申请的结果
    // requestCode：请求码
    // permissions: 申请的N个权限
    // grantResults: 每个权限是否被授权
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
//    {
//
//        if(requestCode == PERMISSION_REQ_CODE)
//        {
//            for(int i=0; i<permissions.length;i++)
//            {
//                if(grantResults[i] != PackageManager.PERMISSION_GRANTED)
//                {
//                    // 惨,用户没给我们授权...这意味着有此功能就不能用了
//                }
//            }
//        }
//    }
}
