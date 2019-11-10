package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.graphics.Bitmap;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import android.content.res.AssetFileDescriptor;
import android.widget.Toast;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.io.FileInputStream;
import org.tensorflow.lite.Interpreter;

public class mentaltestActivity extends AppCompatActivity {
    Interpreter tflite1;
    Button imageCapture;
    ByteBuffer inputBuffer = null;
    float[][] output = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentaltest);
        try {
            imageCapture = (Button) findViewById(R.id.captureImage);

            imageCapture.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(intent, 0);
                    }
                }
            });
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode!=RESULT_OK){
            return;
        }
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");

        try{
            tflite1 = new Interpreter(loadModelFile());
            inputBuffer = ByteBuffer.allocateDirect(4*1*150*150*3);
            inputBuffer.order(ByteOrder.nativeOrder());
            output = new float[1][2];
        } catch(Exception ex){
            ex.printStackTrace();
        }
        float[][] prediction = detect(bitmap);
        float first = prediction[0][0];
        float second = prediction[0][1];

        if(first>second){
            Intent i31 = new Intent(this, yesparkinsonActivity.class);
            startActivity(i31);
        } else if(second>first){
            Intent i32 = new Intent(this, noparkinsonActivity.class);
            startActivity(i32);
        }

    }

    public float[][] detect(Bitmap bitmap){
        preprocess(bitmap);
        tflite1.run(inputBuffer, output);
        return output;
    }

    public void preprocess(Bitmap bitmap){
        inputBuffer.rewind();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        for(int i=0; i<pixels.length; i++){
            int pixel = pixels[i];
            int channel  = pixel & 0xff;
            inputBuffer.putFloat(0xff - channel);
        }
    }

    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = this.getAssets().openFd("parkinson2.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

}
