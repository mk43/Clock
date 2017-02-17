package org.fitzeng.clock;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringDef;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Calendar;

/**
 * Created by Fitzeng on 2017/2/13.
 */

public class TimeView extends LinearLayout {

    private TextView tvTime;

    public TimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeView(Context context) {
        super(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvTime.setText(R.string.hello);

        timeHandler.sendEmptyMessage(0);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

        if (View.VISIBLE == visibility) {
            timeHandler.sendEmptyMessage(0);
        } else {
            timeHandler.removeMessages(0);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void refreshTime() {
        Calendar c = Calendar.getInstance();
        tvTime.setText(String.format("%d:%d:%d", c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND)));
    }

    private Handler timeHandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            refreshTime();

            if (getVisibility() == View.VISIBLE) {
                timeHandler.sendEmptyMessageDelayed(0, 1000);
            }
        }
    };
}
