package cn.gzw.progressbar;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private MyCircleView circleView;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progressBar.setProgress(msg.what);
            progressBar.setSecondaryProgress(msg.what + 5);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        circleView = (MyCircleView) findViewById(R.id.circlePro);
        progressBar.setIndeterminate(false);//设置进度值是不是确定，true表示不确定，false表示确定
        new Thread() {
            @Override
            public void run() {
                super.run();
                int progress = 0;
                while (progress <= 100) {
                    handler.sendEmptyMessage(progress);
                    circleView.setPercent(progress);
                    progress++;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
