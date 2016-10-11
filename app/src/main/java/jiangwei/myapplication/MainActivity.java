package jiangwei.myapplication;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText e;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(this);
        e= (EditText) findViewById(R.id.editText);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.button :
                if(e.getText().toString().isEmpty())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("请设置接收信息的邮箱地址");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                    {public void onClick(DialogInterface dialog, int which) {}});
                    builder.show();
                }
                else
                {

                    Intent i=new Intent(this,MyService.class);

                    i.putExtra("mailAd",e.getText().toString());

                    startService(i);  //开启service
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("开始运行");
                    builder.setMessage("程序可以关闭，但是不能清除内存！");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                    {public void onClick(DialogInterface dialog, int which) {}});
                    builder.show();
                }
                break;


        }




    }
}
