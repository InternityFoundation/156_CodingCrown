package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class uploadToPredictActivity extends AppCompatActivity {

    Interpreter tflite1;
    ByteBuffer byteBuffer = null;
    float[][] output = null;
    private Button btnChoose;
    private Uri filePath;
    Bitmap bitmap;
    Bitmap scaledBitmap;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_to_predict);

        btnChoose = (Button) findViewById(R.id.button);

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Filechooser();
            }
        });
    }

    private void Filechooser() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == 2 && data != null && data.getData() != null) {


            filePath = data.getData();
            try {
                bitmap = (Bitmap) MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, false);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                tflite1 = new Interpreter(loadModelFile());
                byteBuffer = ByteBuffer.allocateDirect(4 * 150 * 150 * 3);
                byteBuffer.order(ByteOrder.nativeOrder());
                output = new float[1][3];
            } catch(Exception ex1){
                ex1.printStackTrace();
            }
            float[][] prediction = detect(bitmap);
            float first = prediction[0][0];
            float second = prediction[0][1];
            float third = prediction[0][2];
            //float fourth = prediction[0][3];
            if (first > second && first > third) {
                Intent intent41 = new Intent(this, abrasionActivity.class);
                startActivity(intent41);
            } else if (second > first && second > third) {
                Intent intent42 = new Intent(this, lacerationActivity.class);
                startActivity(intent42);
            } else if (third > first && third > second) {
                Intent intent43 = new Intent(this, burnActivity.class);
                startActivity(intent43);
            } /*else if(fourth>first && fourth>second && fourth>third){
            Intent intent44 = new Intent(this, punctureActivity.class);
            startActivity(intent44);
            }*/

        }
    }
   // }

    public float[][] detect(Bitmap bitmap){
        preprocess(bitmap);
        tflite1.run(byteBuffer, output);
        return output;
    }

    /*
    public void preprocess(Bitmap bitmap){
        inputBuffer.rewind();

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels,0, width, 0, 0, width, height);

        for (int pixel : pixels) {
            int channel = pixel & 0xff;
            inputBuffer.putFloat(0xff - channel);
        }

    }*/

    /*
    public ByteBuffer preprocess(Bitmap bitmap) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 150 * 150 * 3);
        byteBuffer.order(ByteOrder.nativeOrder());
        int[] intValues = new int[150 * 150];
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        int pixel = 0;
        int var5 = 0;

        for (int var6 = 150; var5 < var6; ++var5) {
            int var7 = 0;

            for (int var8 = 150; var7 < var8; ++var7) {
                int input = intValues[pixel++];
                byteBuffer.putFloat((float) ((input >> 16 & 255) - 1) / 255.0F);
                byteBuffer.putFloat((float) ((input >> 8 & 255) - 1) / 255.0F);
                byteBuffer.putFloat((float) ((input & 255) - 1) / 255.0F);
            }
        }
        return byteBuffer;
    }
    */
    public void preprocess(Bitmap bitmap) {
            byteBuffer.rewind();
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int[] pixels = new int[width * height];
            bitmap.getPixels(pixels,0, width, 0, 0, width, height);
            int pixel = 0;
            for(int i=0; i<150; ++i) {
                for (int j = 0; j < 150; j++) {
                    final int val = pixels[pixel++];
                    byteBuffer.putFloat((((val >> 16) & 0xFF) - 0) / 255.0F);
                    byteBuffer.putFloat((((val >> 8) & 0xFF) - 0) / 255.0F);
                    byteBuffer.putFloat((((val) & 0xFF) - 0) / 255.0F);
                }
            }

    }

    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = this.getAssets().openFd("lin.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }
}
