package com.example.renderscriptdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.Type;
import android.util.Log;
import android.widget.ImageView;

import java.nio.ByteBuffer;

import render.test.ScriptC_render;

public class MainActivity extends AppCompatActivity {
    ImageView iv;
    ImageView outiv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = findViewById(R.id.iv);

        outiv = findViewById(R.id.out);
    }

    @Override
    protected void onResume() {
        super.onResume();
        test2();
    }


    void test2() {
        RenderScript RS = RenderScript.create(this);
        ScriptC_render script = new ScriptC_render(RS);
        script.set_radians_rot(90.0f);
        Bitmap bitmap = getBitmap(this, R.mipmap.timg);
        Bitmap bitmappiap = getBitmap(this, R.mipmap.timg1);
        script.set_height((float) bitmap.getHeight());
        script.set_widht((float) bitmap.getWidth());
        Log.v("MainActivity", "======init  bitmap:" + bitmap.getHeight() + "  " + bitmap.getWidth());
        Log.v("MainActivity", "======init  bitmappiap:" + bitmappiap.getHeight() + "  " + bitmappiap.getWidth());
        iv.setImageBitmap(bitmap);
        Bitmap outputBitmap = Bitmap.createBitmap(bitmappiap);
        Allocation inputAllocation = Allocation.createFromBitmap(RS, bitmap);
        Allocation mOutAllocation = Allocation.createTyped(RS, inputAllocation.getType());
        Log.v("MainActivity", "======init:" + inputAllocation.getType().getElement().getDataType());
        Type intType = Type.createXY(RS, inputAllocation.getType().getElement(), bitmap.getHeight(), bitmap.getWidth());
        Allocation mOutputAllocationInt = Allocation.createTyped(RS, intType,
                Allocation.USAGE_SCRIPT);
        script.set_gIntFrame(mOutputAllocationInt);
        script.forEach_xxxinvert(inputAllocation, mOutAllocation);
        mOutputAllocationInt.copyTo(outputBitmap);
        outiv.setImageBitmap(outputBitmap);
    }

    private static Bitmap getBitmap(Context context, int vectorDrawableId) {
        Bitmap bitmap = null;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            Drawable vectorDrawable = context.getDrawable(vectorDrawableId);
            bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                    vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            vectorDrawable.draw(canvas);
        } else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), vectorDrawableId);
        }
        return bitmap;
    }

}
