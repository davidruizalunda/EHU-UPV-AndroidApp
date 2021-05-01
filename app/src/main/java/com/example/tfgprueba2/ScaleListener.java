package com.example.tfgprueba2;

import android.util.Log;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
    private ImageView imageView;
    private float scale  = 1f;

    public ScaleListener(ImageView imageView){
        this.imageView = imageView;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        scale = scale * detector.getScaleFactor();
        Log.d("Escala:", scale+"");
        imageView.setScaleX(scale);
        imageView.setScaleY(scale);
        return true;
    }


}
