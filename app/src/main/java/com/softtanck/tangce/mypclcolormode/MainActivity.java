package com.softtanck.tangce.mypclcolormode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Tanck";

    private FileOutputStream fos;

    private byte[] CID = new byte[]{0, 3, 0, 8, 8, 8};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        try {
            String img = Environment.getExternalStorageDirectory() + File.separator + "Android" + File.separator + "zy.pcl";
            fos = new FileOutputStream(img);
            String path = Environment.getExternalStorageDirectory() + File.separator + "Android" + File.separator + "test_new.png";
            Bitmap bitmap = BitmapFactory.decodeFile(path);
//            bitmap = resizeImage(bitmap, 1160, 1700);
            int height = bitmap.getHeight();
            int width = bitmap.getWidth();
            int r, g, b;
            int color;
            int times = 0;
            initCID();
            setCommand("*r1A");
            setCommand("*t600R");
            byte[] buffers = new byte[3 * width];
            for (int i = 0; i < height; i++) {
//                setCommand("*b" + 3 * width + "W");
                times = 0;
                for (int j = 0; j < width; j++) {
                    color = bitmap.getPixel(j, i);
                    r = Color.red(color);
                    g = Color.green(color);
                    b = Color.blue(color);
                    buffers[3 * j + 0] = (byte) r;
                    buffers[3 * j + 1] = (byte) g;
                    buffers[3 * j + 2] = (byte) b;
//                    writeByte(r);
//                    writeByte(g);
//                    writeByte(b);
                }
                setCommand("*b" + 3 * width + "W");
                writeBytes(buffers);
            }
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initCID() {
        setCommand("*v6W");
        for (int i = 0; i < 6; i++) {
            writeByte(CID[i]);
        }
    }

    private void writeBytes(byte[] b) {
        try {
            fos.write(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeBytes(byte[] b, int length) {
        try {
            fos.write(b, 0, length);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeByte(int b) {
        try {
            fos.write(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setCommand(String cmd) {
        try {
            writeByte(27);
            fos.write(cmd.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap resizeImage(Bitmap bitmap, int newWidth, int newHeight) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return newbm;

    }
}
