package com.example.mobiledictionary.TranslateOnline;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.mobiledictionary.R;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

public class TranslateOnline extends AppCompatActivity {

    private Spinner fromSprinner, toSprinner;
    private TextInputEditText sourceEdt;
    private ImageView micIV,cameraIV;
    private MaterialButton translateBtn;
    private TextView translateTV;
    private Bitmap bitmap;
    String[] fromLanguages = {"Form", "English", "VietNamese"};
    String[] toLanguages = {"Form", "English", "VietNamese"};

    private  static final int REQUEST_PERMISSION_CODE = 1, REQUEST_CAMERA_CODE = 100;;
    String fromLanguadeCode, toLanguageCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_translateonl);
        initUi();
        fromSprinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                fromLanguadeCode = getLanguageCode(fromLanguages[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter fromAdapter = new ArrayAdapter(this, R.layout.spinner_item, fromLanguages);
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        fromSprinner.setAdapter(fromAdapter);

        toSprinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                toLanguageCode = getLanguageCode(toLanguages[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter toAdapter = new ArrayAdapter(this, R.layout.spinner_item, toLanguages);
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        toSprinner.setAdapter(toAdapter);
        translateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translate_api translate=new translate_api();
                translate.setOnTranslationCompleteListener(new translate_api.OnTranslationCompleteListener() {
                    @Override
                    public void onStartTranslation() {
                        // here you can perform initial work before translated the text like displaying progress bar
                    }

                    @Override
                    public void onCompleted(String text) {
                        // "text" variable will give you the translated text
                        translateTV.setText(text);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
                translate.execute(sourceEdt.getText().toString(),fromLanguadeCode,toLanguageCode);
            }
        });

        micIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech recognition demo");

                    startActivityForResult(intent, REQUEST_PERMISSION_CODE);

                } catch(ActivityNotFoundException e) {
                    Intent your_browser_intent = new Intent(Intent.ACTION_VIEW,

                            Uri.parse("https://market.android.com/details?id=APP_PACKAGE_NAME"));
                    startActivity(your_browser_intent);
                }
            }
        });

        if (ContextCompat.checkSelfPermission(TranslateOnline.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(TranslateOnline.this, new String[]{
                    Manifest.permission.CAMERA
            }, REQUEST_CAMERA_CODE);
        }
        cameraIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(TranslateOnline.this);
            }
        });
    }

    private void initUi() {
        fromSprinner = findViewById(R.id.idFromSpinner);
        toSprinner = findViewById(R.id.idToSpinner);
        sourceEdt = findViewById(R.id.ideditTextSource);
        micIV = findViewById(R.id.idIVmic);
        translateBtn = findViewById(R.id.idBtnTranslate);
        translateTV =findViewById(R.id.idTranslateTV);
        cameraIV = findViewById(R.id.idCamera);
    }

    public String getLanguageCode(String language) {
        String languageCode = "";
        switch (language) {
            case "English":
                languageCode = "en";
                break;
            case "VietNamese":
                languageCode = "vi";
                break;
            default:
                languageCode = "";
        }
        return languageCode;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if(requestCode==REQUEST_PERMISSION_CODE) {
            if(resultCode==RESULT_OK && data!=null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                sourceEdt.setText(result.get(0));
            }
        }*/
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    getTExtFromImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void getTExtFromImage(Bitmap bitmap){
        TextRecognizer recognizer = new TextRecognizer.Builder(this).build();
        if(!recognizer.isOperational()) {
            Toast.makeText(TranslateOnline.this, "ERROR", Toast.LENGTH_SHORT);

        } else {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> textBlockSparseArray = recognizer.detect(frame);
            StringBuilder stringBuilder = new StringBuilder();
            for (int i=0; i<textBlockSparseArray.size(); i++) {
                TextBlock textBlock = textBlockSparseArray.valueAt(i);
                stringBuilder.append(textBlock.getValue());
                stringBuilder.append("\n");
            }
            sourceEdt.setText(stringBuilder.toString());
        }
    }
}