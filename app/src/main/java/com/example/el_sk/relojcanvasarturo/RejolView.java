package com.example.el_sk.relojcanvasarturo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.Calendar;

public class RejolView extends View {

    private int alto, ancho = 0;
    private int margenes = 0;
    private int tamachoLetra = 0;
    private int numeralSpacing = 0;
    private int handTruncation, hourHandTruncation = 0;
    private int radius = 0;
    private Paint paint;
    private boolean isInit;
    private int[] numbers ={1,2,3,4,5,6,7,8,9,10,11,12};
    private Rect rect = new Rect();

    public RejolView(Context context) {
        super(context);
    }

    public RejolView(Context context,  AttributeSet attrs) {
        super(context, attrs);
    }

    public RejolView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initClock(){

        alto = getHeight();
        ancho = getWidth();
        margenes =  numeralSpacing + 50;
        tamachoLetra = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,13,getResources().getDisplayMetrics());

        int min = Math.min(alto, ancho);
        radius = min / 2 - margenes;
        handTruncation = min / 20;
        hourHandTruncation = min / 7;
        paint = new Paint();
        isInit = true;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(!isInit){
            initClock();
        }

        canvas.drawColor(Color.BLACK);
        drawCircle(canvas);
        drawCenter(canvas);
        drawNumeral(canvas);
        drawHands(canvas);

        postInvalidateDelayed(500);
        invalidate();
    }

    private void drawHand (Canvas canvas, double loc, boolean isHour){
        double angle = Math.PI * loc/ 30 - Math.PI /2 ;
        int handRadius = isHour ? radius - handTruncation - hourHandTruncation : radius - handTruncation;
        canvas.drawLine(ancho / 2, alto / 2, (float)(ancho / 2 + Math.cos(angle)*handRadius),
                (float)(alto / 2 + Math.sin(angle)*handRadius),paint);
    }

    private void drawHands(Canvas canvas) {
        Calendar c = Calendar.getInstance();
        float hour = c.get(Calendar.HOUR_OF_DAY);
        hour = hour > 12 ? hour - 12 : hour;
        drawHand(canvas,(hour + c.get(Calendar.MINUTE)/ 60) * 5f, true);
        drawHand(canvas, c.get(Calendar.MINUTE),false);
        drawHand(canvas, c.get(Calendar.SECOND),false);

    }

    private void drawNumeral(Canvas canvas) {
        paint.setTextSize(tamachoLetra);

        for(int number : numbers){
            String tmp  = String.valueOf(number);
            paint.getTextBounds(tmp, 0,tmp.length(), rect);
            double angle = Math.PI / 6 * (number - 3);
            int x = (int) (ancho / 2 + Math.cos(angle) * radius - rect.width() / 2);
            int y = (int)(alto / 2 + Math.sin(angle) * radius + rect.width() / 2);
            canvas.drawText(tmp, x, y, paint);
        }
    }

    private void drawCenter(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(ancho / 2, alto /2, 12, paint);

    }

    private void drawCircle(Canvas canvas) {

        paint.reset();
        paint.setColor(getResources().getColor(android.R.color.white));
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        canvas.drawCircle(ancho / 2, alto / 2, radius + margenes -10,paint);
    }


}
