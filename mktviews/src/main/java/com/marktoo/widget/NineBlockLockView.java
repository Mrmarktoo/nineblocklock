
package com.marktoo.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Class Name: NineBlockLockView Description: Created by marktoo on 2017/5/25
 * Phone: 18910103220 Email: 18910103220@189.cn
 */

public class NineBlockLockView extends View {

    private String TAG = "NineBlockLockView";

    public String getTAG() {
        return TAG;
    }

    public NineBlockLockView(Context context) {
        this(context, null);
    }

    private boolean showAssistant = false;

    private int assistantColor;

    private int normalColor, seletedColor, bestColor;

    private int borderWidth, pathWidth, blockPadding;

    /* every block style */
    private int pointStyle;

    /* the range gravity in this view */
    private int gravity;

    /* background image */
    private Bitmap backgroundImage = null;

    public NineBlockLockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // this(context, attrs, 0);
        LogUtil.getInstance().setTAG(getTAG());
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.NineBlockLockView);
            showAssistant = ta.getBoolean(R.styleable.NineBlockLockView_nbl_show_assistant, false);
            normalColor = ta.getColor(R.styleable.NineBlockLockView_nbl_normal_color, Color.GRAY);
            seletedColor = ta.getColor(R.styleable.NineBlockLockView_nbl_selected_color,
                    Color.BLUE);
            bestColor = ta.getColor(R.styleable.NineBlockLockView_nbl_best_color, Color.GREEN);
            keyValue = ta.getString(R.styleable.NineBlockLockView_nbl_index_value);

            borderWidth = ta.getDimensionPixelSize(R.styleable.NineBlockLockView_nbl_border_width,
                    2);
            pathWidth = ta.getDimensionPixelSize(R.styleable.NineBlockLockView_nbl_path_width, 5);
            blockPadding = ta.getDimensionPixelSize(R.styleable.NineBlockLockView_nbl_block_padding,
                    10);

            pointStyle = ta.getInteger(R.styleable.NineBlockLockView_nbl_point_style, 0);
            gravity = ta.getInteger(R.styleable.NineBlockLockView_nbl_gravity, 0);
            int backResId = ta.getResourceId(R.styleable.NineBlockLockView_nbl_background, 0);
            if (backResId != 0) {
                backgroundImage = BitmapFactory.decodeResource(getResources(), backResId);
            }
            if (showAssistant) {
                assistantColor = ta.getColor(R.styleable.NineBlockLockView_nbl_assistant_color,
                        Color.RED);
                LogUtil.getInstance()
                        .e("showAssistant=" + showAssistant + ",assistantColor=" + assistantColor
                                + ",normalColor=" + normalColor + ",seletedColor=" + seletedColor
                                + ",bestColor=" + bestColor + ",keyValue=[" + keyValue
                                + "],borderWidth=" + borderWidth + ",pathWidth=" + pathWidth
                                + ",blockPadding=" + blockPadding + ",pointStyle=" + pointStyle
                                + ",gravity=" + gravity);
            }
            ta.recycle();
            setKeyValue(keyValue);
        }
    }

    private String keyValue = null;

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
        checkValue(keyValue);
    }

    private List<String> keyValues = new ArrayList<>();

    private void checkValue(String keyValue) {
        if (!TextUtils.isEmpty(keyValue) && keyValue.contains(",")) {
            String[] values = keyValue.split(",");
            if (values.length == 9) {
                for (String value : values) {
                    if (keyValues.contains(value)) {
                        throw new IllegalArgumentException("value not repeat!");
                    } else {
                        keyValues.add(value);
                    }
                }
            }
        }
    }

    /* view canvas range */
    private int width, height;

    private int realWidth, realHeight, offsetX, offsetY;

    private int perWidth, perHeight, radius, strokeRadius;

    /* init state */
    private boolean isInited = false;

    /* short password role */
    private int shortPassRole = 4;

    public int getShortPassRole() {
        return shortPassRole;
    }

    public void setShortPassRole(int shortPassRole) {
        this.shortPassRole = shortPassRole;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LogUtil.getInstance().e("onFinishInflate()");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
                getDefaultSize(0, heightMeasureSpec));
        int childWidthSize = getMeasuredWidth();
        heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize,
                MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        realWidth = getDefaultSize(0, widthMeasureSpec);
        realHeight = getDefaultSize(0, heightMeasureSpec);

        int minWidth = Math.min(realWidth, realHeight);
        width = height = minWidth;

        perWidth = width / 3;
        perHeight = height / 3;

        switch (gravity) {
            case 0:// fix
                offsetX = 0;
                offsetY = 0;
                width = realWidth;
                height = realHeight;
                break;
            case 1:// top
                offsetX = (realWidth - width) / 2;
                offsetY = 0;
                break;
            case 2:// center
                offsetX = (realWidth - width) / 2;
                offsetY = (realHeight - height) / 2;
                break;
            case 3:// bottom
                offsetX = (realWidth - width) / 2;
                offsetY = realHeight - height;
                break;
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        if (!isInited) {
            backgroundImage = Bitmap.createScaledBitmap(backgroundImage, width, height, true);
        }

        drawBackGround(canvas, paint);

        if (showAssistant) {
            drawRange(canvas, paint);
            drawRangeLine(canvas, paint);
            drawSubRangeLine(canvas, paint);
        }

        drawCircle(canvas, paint);

        if (pathPoints.size() > 0) {
            drawPath(canvas, paint);
        }
    }

    private void drawBackGround(Canvas canvas, Paint paint) {
        if (backgroundImage != null) {
            canvas.drawBitmap(backgroundImage, offsetX, offsetY, paint);
        }
    }

    private List<PathPoint> points = new ArrayList<>();

    private List<PathPoint> pathPoints = new ArrayList<>();

    private void drawCircle(Canvas canvas, Paint paint) {

        int startX = offsetX + perWidth / 2;
        int startY = offsetY + perHeight / 2;

        if (!isInited) {
            if (points.size() > 0) {
                points.clear();
            }
            for (int i = 0; i < 3; i++) {
                int currentY = startY + perHeight * i;
                for (int j = 0; j < 3; j++) {
                    int currentX = startX + perWidth * j;
                    PathPoint pathPoint = new PathPoint(currentX, currentY);
                    pathPoint.setIndex(i * 3 + j);
                    points.add(pathPoint);
                }
            }
            isInited = true;
        }

        if (pointStyle == 0) {
            radius = Math.min(perWidth, perHeight) / 4;

            for (PathPoint pathPoint : points) {
                if (pathPoint.isSelect()) {
                    if (pathPoints.size() >= shortPassRole) {
                        paint.setColor(bestColor);
                    } else {
                        paint.setColor(seletedColor);
                    }
                } else {
                    paint.setColor(normalColor);
                }
                canvas.drawCircle(pathPoint.getCenterX(), pathPoint.getCenterY(), radius, paint);
            }
        } else {
            strokeRadius = Math.min(perWidth, perHeight) / 3;
            radius = Math.min(perWidth, perHeight) / 6;

            for (PathPoint pathPoint : points) {

                /* circle center point */
                paint.setStyle(Paint.Style.FILL);
                if (pathPoint.isSelect()) {
                    if (pathPoints.size() >= shortPassRole) {
                        paint.setColor(bestColor);
                    } else {
                        paint.setColor(seletedColor);
                    }
                } else {
                    paint.setColor(normalColor);
                }
                canvas.drawCircle(pathPoint.getCenterX(), pathPoint.getCenterY(), radius, paint);

                /* circle border */
                paint.setStrokeWidth(borderWidth);
                paint.setColor(normalColor);
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawCircle(pathPoint.getCenterX(), pathPoint.getCenterY(), strokeRadius,
                        paint);
            }
        }
    }

    /* assistant */
    private void drawRange(Canvas canvas, Paint paint) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderWidth);
        paint.setColor(assistantColor);
        canvas.drawRect(new Rect(offsetX, offsetY, width + offsetX, height + offsetY), paint);
    }

    private void drawRangeLine(Canvas canvas, Paint paint) {
        paint.setColor(assistantColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(borderWidth);

        int currentX, currentY;

        /* vertical */
        for (int i = 1; i < 3; i++) {
            currentX = perWidth * i + offsetX;
            LogUtil.getInstance()
                    .e("currentx=" + currentX + ",perWidth=" + perWidth + ",offsetX=" + offsetX);
            canvas.drawLine(currentX, offsetY, currentX, offsetY + height, paint);
        }

        /* horizontal */
        for (int i = 1; i < 3; i++) {
            currentY = perHeight * i + offsetY;
            canvas.drawLine(offsetX, currentY, width + offsetX, currentY, paint);
        }

    }

    private void drawSubRangeLine(Canvas canvas, Paint paint) {
        paint.setColor(assistantColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(borderWidth);

        int startX = offsetX + perWidth / 2;
        int startY = offsetY + perHeight / 2;

        int currentX, currentY;

        /* 绘制九宫格格中线 */
        for (int i = 0; i < 3; i++) {
            currentY = startY + perHeight * i;
            canvas.drawLine(offsetX, currentY, width + offsetX, currentY, paint);
        }
        for (int j = 0; j < 3; j++) {
            currentX = startX + perWidth * j;
            canvas.drawLine(currentX, offsetY, currentX, offsetY + height, paint);
        }
    }
    /* assistant */

    private PathPoint currentPoint = null;

    private void drawPath(Canvas canvas, Paint paint) {
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(pathWidth);
        if (pathPoints.size() >= shortPassRole) {
            paint.setColor(bestColor);
        } else {
            paint.setColor(seletedColor);
        }

        for (int i = 1; i < pathPoints.size(); i++) {
            canvas.drawLine(pathPoints.get(i - 1).getCenterX(), pathPoints.get(i - 1).getCenterY(),
                    pathPoints.get(i).getCenterX(), pathPoints.get(i).getCenterY(), paint);
        }

        if (currentPoint != null) {
            canvas.drawLine(pathPoints.get(pathPoints.size() - 1).getCenterX(),
                    pathPoints.get(pathPoints.size() - 1).getCenterY(), currentPoint.getCenterX(),
                    currentPoint.getCenterY(), paint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float currentX = event.getX();
        float currentY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                PathPoint startPoint = new PathPoint(currentX, currentY);
                for (int i = 0; i < points.size(); i++) {
                    if (!points.get(i).isSelect()
                            && points.get(i).getDistance(startPoint) <= radius) {
                        points.get(i).setSelect(true);
                        if (!pathPoints.contains(points.get(i))) {
                            pathPoints.add(points.get(i));
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                currentPoint = new PathPoint(currentX, currentY);
                for (int i = 0; i < points.size(); i++) {
                    // LogUtil.getInstance().e("pathPoint is select=" +
                    // points.get(i).isSelect());
                    if (!points.get(i).isSelect()
                            && currentPoint.getDistance(points.get(i)) <= radius) {
                        points.get(i).setSelect(true);
                        if (!pathPoints.contains(points.get(i))) {
                            pathPoints.add(points.get(i));
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (pathPoints.size() < shortPassRole) {
                    LogUtil.getInstance().e("bad password");
                    lockListener.onPassFailed();
                } else {
                    LogUtil.getInstance().e("password=" + getPassword());
                    lockListener.onPassSuccess(getPassword());
                }
                reset();
                currentPoint = null;
                break;
        }

        invalidate();
        return true;
    }

    private void reset() {
        for (PathPoint pathPoint : points) {
            pathPoint.setSelect(false);
        }
        pathPoints.clear();
    }

    private String getPassword() {
        StringBuffer stringBuffer = new StringBuffer();
        if (keyValues.size() == 9) {
            for (PathPoint pathPoint : pathPoints) {
                stringBuffer.append(keyValues.get(pathPoint.getIndex()));
            }
        } else {
            for (PathPoint pathPoint : pathPoints) {
                stringBuffer.append(pathPoint.getIndex());
            }
        }
        return stringBuffer.toString();
    }

    private static class PathPoint {

        public static int STATE_NORMAL = 0;

        public static int STATE_PRESSED = 1;

        private float centerX, centerY;

        public boolean isSelect = false;

        private int index;

        private PathPoint(float centerX, float centerY) {
            this.centerX = centerX;
            this.centerY = centerY;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        public float getCenterX() {
            return centerX;
        }

        public void setCenterX(float centerX) {
            this.centerX = centerX;
        }

        public float getCenterY() {
            return centerY;
        }

        public void setCenterY(float centerY) {
            this.centerY = centerY;
        }

        public float getDistance(PathPoint pathPoint) {
            float x = pathPoint.getCenterX() - this.getCenterX();
            float y = pathPoint.getCenterY() - this.getCenterY();
            return (float)Math.sqrt(x * x + y * y);
        }
    }

    private OnLockListener lockListener;

    public OnLockListener getLockListener() {
        return lockListener;
    }

    public void setLockListener(OnLockListener lockListener) {
        this.lockListener = lockListener;
    }

    /* callback listener */
    public interface OnLockListener {
        void onPassSuccess(String passCode);

        void onPassFailed();
    }

}
