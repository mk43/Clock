package org.fitzeng.clock;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Fitzeng on 2017/2/17.
 */

public class StopWatchView extends LinearLayout {

    private TextView tvHour;
    private TextView tvMin;
    private TextView tvSec;
    private TextView tvMSec;

    private Button btnStart;
    private Button btnPause;
    private Button btnResume;
    private Button btnLab;
    private Button btnReset;

    private ListView lvTimeList;

    private ArrayAdapter<String> adapter;
    private Timer timer = new Timer();
    private TimerTask timerTask = null;
    private TimerTask showTimeTask = null;
    private int tenMsec = 0;
    private static final int MSG_WHAT_SHOW_TIME = 1;

    private void init() {
        tvHour = (TextView) findViewById(R.id.timeHour);
        tvMin = (TextView) findViewById(R.id.timeMin);
        tvSec = (TextView) findViewById(R.id.timeSec);
        tvMSec = (TextView) findViewById(R.id.timeMSec);

        btnStart = (Button) findViewById(R.id.btnSWStart);
        btnPause = (Button) findViewById(R.id.btnSWPause);
        btnResume = (Button) findViewById(R.id.btnSWResume);
        btnLab = (Button) findViewById(R.id.btnSWLab);
        btnReset = (Button) findViewById(R.id.btnSWReset);

        lvTimeList = (ListView) findViewById(R.id.lvWatchTimeList);

        tvHour.setText(R.string.init00);
        tvMin.setText(R.string.init00);
        tvSec.setText(R.string.init00);
        tvMSec.setText(R.string.init00);

        btnPause.setVisibility(View.GONE);
        btnResume.setVisibility(View.GONE);
        btnLab.setVisibility(View.GONE);
        btnReset.setVisibility(View.GONE);

        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
        lvTimeList.setAdapter(adapter);
    }

    private void startTime() {
        if (timerTask == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    tenMsec++;
                }
            };
            timer.schedule(timerTask, 10, 10);
        }
    }

    private void stopTime() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    @Override
    protected void onFinishInflate() {

        super.onFinishInflate();

        init();

        showTimeTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(MSG_WHAT_SHOW_TIME);
            }
        };
        timer.schedule(showTimeTask, 200, 200);
        btnStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime();

                btnStart.setVisibility(View.GONE);
                btnPause.setVisibility(View.VISIBLE);
                btnLab.setVisibility(View.VISIBLE);
            }
        });
        btnPause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTime();

                btnPause.setVisibility(View.GONE);
                btnResume.setVisibility(View.VISIBLE);
                btnLab.setVisibility(View.GONE);
                btnReset.setVisibility(View.VISIBLE);
            }
        });
        btnResume.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime();
                btnResume.setVisibility(View.GONE);
                btnPause.setVisibility(View.VISIBLE);
                btnReset.setVisibility(View.GONE);
                btnLab.setVisibility(View.VISIBLE);
            }
        });
        btnLab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.insert(String.format("%d:%d:%d.%d", tenMsec / 100 / 60 / 60, tenMsec / 100 / 60 % 60, tenMsec / 100 % 60, tenMsec % 100), 0);
            }
        });
        btnReset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTime();

                tenMsec = 0;
                adapter.clear();

//                tvHour.setText(R.string.init00);
//                tvMin.setText(R.string.init00);
//                tvSec.setText(R.string.init00);
//                tvMSec.setText(R.string.init00);

                btnStart.setVisibility(View.VISIBLE);
                btnPause.setVisibility(View.GONE);
                btnResume.setVisibility(View.GONE);
                btnLab.setVisibility(View.GONE);
                btnReset.setVisibility(View.GONE);
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_WHAT_SHOW_TIME:
                    tvHour.setText(tenMsec / 100 / 60 / 60 + "");
                    tvMin.setText((tenMsec / 100 / 60) % 60 + "");
                    tvSec.setText((tenMsec / 100) % 60 + "");
                    tvMSec.setText(tenMsec % 100 + "");
                    break;
                default:
                    break;
            }
        }
    };

    public StopWatchView(Context context) {
        super(context);
    }

    public StopWatchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StopWatchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void onDestroy() {
        timer.cancel();
    }
}
