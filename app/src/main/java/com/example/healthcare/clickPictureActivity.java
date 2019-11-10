package com.example.healthcare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import android.content.res.AssetFileDescriptor;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.io.FileInputStream;
import org.tensorflow.lite.Interpreter;



public class clickPictureActivity extends AppCompatActivity {
    Interpreter tflite1;
    Button btnCaptureImage;
    ImageView imageDisplay;
    ByteBuffer inputBuffer = null;
    float[][] output = null;
    //StorageReference mStorageRef;
    //this is a test comment for commit and push demonstration

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_picture);
        //mStorageRef = FirebaseStorage.getInstance()

        btnCaptureImage = (Button) findViewById(R.id.btn_captureImage);
        //imageDisplay = (ImageView) findViewById(R.id.imageCapture);

        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);

            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode!=RESULT_OK){
            return;
        }

        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        //imageDisplay.setImageBitmap(bitmap);

        try {
            tflite1 = new Interpreter(loadModelFile());
            Bitmap.createScaledBitmap(bitmap, 150, 150, false);
            inputBuffer = ByteBuffer.allocateDirect(4*150*150*3);
            inputBuffer.order(ByteOrder.nativeOrder());
            output = new float[1][3];
        } catch(Exception ex){
            ex.printStackTrace();
        }
        float[][] prediction = detect(bitmap);
        float first = prediction[0][0];
        float second = prediction[0][1];
        float third = prediction[0][2];
        //float fourth = prediction[0][3];
        if(first>second && first>third){
            Intent intent41 = new Intent(this, abrasionActivity.class);
            startActivity(intent41);
        } else if(second>first && second>third){
            Intent intent42 = new Intent(this, lacerationActivity.class);
            startActivity(intent42);
        } else if(third>first && third>second){
            Intent intent43 = new Intent(this, burnActivity.class);
            startActivity(intent43);
        } /*else if(fourth>first && fourth>second && fourth>third){
            Intent intent44 = new Intent(this, punctureActivity.class);
            startActivity(intent44);
        }*/

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
        bitmap.getPixels(pixels,0, width, 0, 0, width, height);

        for(int i=0; i<pixels.length; i++){
            int pixel = pixels[i];
            int channel = pixel & 0xff;
            inputBuffer.putFloat(0xff - channel);
        }
    }

    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = this.getAssets().openFd("woundClassification3hackk.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }
}
