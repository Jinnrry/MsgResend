package jiangwei.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import java.sql.Date;
import java.text.SimpleDateFormat;


public class MyReceiver extends BroadcastReceiver {
    public MyReceiver()
    {

    }
    public SmsMessage msg = null;
    public  String receiveTime;
    public   MailUtils mail;
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        if (null != bundle) {
            Object[] smsObj = (Object[]) bundle.get("pdus");
            for (Object object : smsObj) {
                msg = SmsMessage.createFromPdu((byte[]) object);
                Date date = new Date(msg.getTimestampMillis());//时间
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                receiveTime = format.format(date);
              /*  System.out.println("number:" + msg.getOriginatingAddress()
                       + "   body:" + msg.getDisplayMessageBody() + "  time:"
                   +receiveTime);
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        MailUtils mail=new MailUtils("smtp.163.com", "jiangwei1995910@163.com", "jw78667602");
                        mail.setAddress("jiangwei1995910@163.com", "631106246@qq.com");
                        mail.send("您的手机收到了一条新短信","短信来至:" + msg.getOriginatingAddress()
                                + "        短信内容:" + msg.getDisplayMessageBody() + "        手机收到该短信的时间:"
                                +receiveTime);
                    }
                }.start();

*/
            }
        }
    }
}
