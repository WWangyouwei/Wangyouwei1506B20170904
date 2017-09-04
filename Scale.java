package com.dell.wangyouwei1506b20170904;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;

/**
 * 姓名：王有为
 * 时间：2017/9/4.
 */

public class Scale extends View {
    private float  currentValue = 0;
    private float[] pos;
    private float[] tan;
    private Bitmap bitmap;
    private Matrix mMatrix;
    private final Paint paint;
    private int mViewWidth;
    private int mViewHeight;
    public Scale(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.Scale);
        int color = a.getColor(R.styleable.Scale_ScaleColor, Color.BLACK);
        float dimension = a.getDimension(R.styleable.Scale_ScaleWhi, 0);
        a.recycle();
        paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(dimension);
        init(context);
    }
    private void init(Context context) {
        pos = new float[2];
        tan = new float[2];
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;       // 缩放图片
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ss, options);
        mMatrix = new Matrix();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mViewWidth / 2, mViewHeight / 2);      // 平移坐标系
        Path path = new Path();                                 // 创建 Path
        path.addCircle(200, 200, 200, Path.Direction.CW);

        PathMeasure measure = new PathMeasure(path, false);     // 创建 PathMeasure
        currentValue += 0.005;                                  // 计算当前的位置在总长度上的比例[0,1]
        if (currentValue >= 1) {
            currentValue = 0;
        }
        measure.getPosTan(measure.getLength() * currentValue, pos, tan);        // 获取当前位置的坐标以及趋势
        mMatrix.reset();                                                        // 重置Matrix
        float degrees = (float) (Math.atan2(tan[1], tan[0]) * 180.0 / Math.PI); // 计算图片旋转角度
        mMatrix.postRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);   // 旋转图片
        mMatrix.postTranslate(pos[0] - bitmap.getWidth() / 2, pos[1] - bitmap.getHeight() / 2);   // 将图片绘制中心调整到与当前点重合
        canvas.drawPath(path, paint);                                   // 绘制 Path
        canvas.drawBitmap(bitmap, mMatrix, paint);                     // 绘制箭头
        invalidate();
    }

}
