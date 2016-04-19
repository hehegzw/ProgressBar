package cn.gzw.progressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by gzw on 2016/4/10.
 */
public class MyCircleView extends View {
    private int firstColor;
    private int scendColor;
    private int speed;
    private int circleSize;
    private int currentProgress;
    private Paint paint;
    private boolean isNext = false;
    private boolean isRunning;
    private float progress;
    private int present;
    private String text = "100%";

    public MyCircleView(Context context) {
        this(context, null);
    }

    public MyCircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyCircleView, defStyleAttr, 0);
        for (int i = 0; i < ta.getIndexCount(); i++) {
            int index = ta.getIndex(i);
            switch (index) {
                case R.styleable.MyCircleView_firstColor:
                    firstColor = ta.getColor(index, Color.BLACK);
                    break;
                case R.styleable.MyCircleView_secondColor:
                    scendColor = ta.getColor(index, Color.BLACK);
                    break;
                case R.styleable.MyCircleView_speed:
                    speed = ta.getInt(index, 10);
                    break;
                case R.styleable.MyCircleView_circleSize:
                    circleSize = ta.getDimensionPixelSize(index, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
            }
        }
        ta.recycle();
        isRunning = true;
        paint = new Paint();
    }

    public void setPercent(int percent) {
        this.progress = percent*3.6f;
        this.present = percent;
        this.text = percent+"%";
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centre = getWidth() / 2;
        int radius = centre - circleSize / 2;// 半径
        paint.setStrokeWidth(circleSize);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        RectF oval = new RectF(centre - radius, centre - radius, centre + radius, centre + radius);
        paint.setColor(firstColor);
        canvas.drawCircle(centre, centre, radius, paint);
        paint.setColor(scendColor);
        canvas.drawArc(oval, currentProgress, progress, false, paint);
        Paint tPaint = new Paint();
        tPaint.setTextSize(radius/2);
        tPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        int baseline = (int) (getHeight()/2+(fontMetrics.descent - fontMetrics.ascent)/2 - fontMetrics.descent);
        canvas.drawText(text,centre,baseline,tPaint);

        //canvas.drawText();
//        if(!isNext){
//            paint.setColor(firstColor);
//            canvas.drawCircle(centre,centre,radius,paint);
//            paint.setColor(scendColor);
//            canvas.drawArc(oval,-90,currentProgress,false,paint);
//        }else{
//            paint.setColor(scendColor);
//            canvas.drawCircle(centre,centre,radius,paint);
//            paint.setColor(firstColor);
//            canvas.drawArc(oval,-90,currentProgress,false,paint);
//        }
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE) {
            isRunning = true;
            new Thread(new MyThread()).start();
        } else {
            isRunning = false;
        }
    }

    private class MyThread implements Runnable {

        @Override
        public void run() {
            while (isRunning) {
                Log.d("MyThread", "isRunning");
                currentProgress++;
                if (currentProgress == 360) {
                    currentProgress = 0;
                    if (isNext) {
                        isNext = false;
                    } else {
                        isNext = true;
                    }
                }
                postInvalidate();
                try {
                    Thread.sleep(speed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
