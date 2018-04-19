package com.example.hhj73.fix;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Created by hhj73 on 2018-04-09.
 */

public class StudentJoinActivity extends Activity {
    EditText emailText;
    LinearLayout zero;
    LinearLayout first;
    LinearLayout second;
    EditText addEdit;
    static final int REQUEST_IMAGE_CAPTURE=123;
    static final int REQUEST_STORAGE =234;
    static final int REQUEST_EMAIL = 345;

    String strt;        //xml 중 address2 에 들어가는 string
    boolean addcount=false;   //화면 다시 시작한 위치
    private View view;
    boolean emailPassed=false;

    DatabaseReference databaseReference;

    String id;
    String pw;
    String name;
    boolean gender;
    String address;
    boolean smoking;
    boolean curfew;
    boolean pet;
    boolean help;
    String uniqueness;

    EditText editId;
    EditText editPw1;
    EditText editPw2;
    EditText editName;
    RadioGroup genderGroup;
    RadioButton femaleButton;
    RadioButton maleButton;
    EditText editAddress;
    EditText editAddress2;
    CheckBox smokeCheck;
    CheckBox curfewCheck;
    CheckBox petCheck;
    CheckBox helpCheck;
    EditText editUniqueness;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_student);
        zero = (LinearLayout)findViewById(R.id.zero);
        first = (LinearLayout)findViewById(R.id.first);
        second = (LinearLayout)findViewById(R.id.second);
        emailInit();
        init();
    }

    private void init() {
        Intent intent = getIntent();
        strt = intent.getStringExtra("ADDRESS");
        addcount=intent.getBooleanExtra("ADDCOUNT",false);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        id = null;
        pw = null;

        editId = (EditText) findViewById(R.id.editId);
        editPw1 = (EditText) findViewById(R.id.editPw1);
        editPw2 = (EditText) findViewById(R.id.editPw2);
        editName = (EditText) findViewById(R.id.editName);
        RadioGroup genderGroup = (RadioGroup) findViewById(R.id.genderGroup);
        femaleButton = (RadioButton) findViewById(R.id.femaleButton);
        maleButton = (RadioButton) findViewById(R.id.maleButton);
        // 주소도 init해야함
        smokeCheck = (CheckBox) findViewById(R.id.smokeCheck);
        curfewCheck = (CheckBox) findViewById(R.id.curfewCheck);
        petCheck = (CheckBox) findViewById(R.id.petCheck);
        helpCheck = (CheckBox) findViewById(R.id.helpCheck);
        editUniqueness = (EditText) findViewById(R.id.editUniqueness);

        if(addcount==true){
            zero.setVisibility(view.GONE);
            first.setVisibility(view.VISIBLE);
            addEdit= (EditText)findViewById(R.id.address);
            addEdit.setText(strt);
            // Toast.makeText(this, strt, Toast.LENGTH_SHORT).show();   //test intent values
            addcount=false;
        }
    }

    public void emailInit(){
        emailText = (EditText)findViewById(R.id.email);
        emailText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS|InputType.TYPE_CLASS_TEXT);
        emailText.setEnabled(!emailPassed);
        Button button = (Button)findViewById(R.id.btn_email);
        button.setEnabled(!emailPassed);
        if(emailPassed)
            button.setText("인증완료");
    }
    public void studentJoinSuccess(View view) {
        // 학생회원 가입 완료
        if(emailPassed) {
            databaseReference.child(id).child("type").setValue(false); // 청년 회원임
            databaseReference.child(id).child("pw").setValue(pw); // 비밀번호
            databaseReference.child(id).child("name").setValue(name); // 이름
            databaseReference.child(id).child("gender").setValue(gender); // 성별
            // databaseReference.child(id).child("address").setValue(address); // 주소
            databaseReference.child(id).child("smoking").setValue(smoking); // 흡연
            databaseReference.child(id).child("curfew").setValue(curfew); // 통금
            databaseReference.child(id).child("pet").setValue(pet); // 반려동물
            databaseReference.child(id).child("help").setValue(help); // 도움
            databaseReference.child(id).child("unique").setValue(uniqueness); // 특이사항

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(this,"이메일을 확인해주세요.",Toast.LENGTH_SHORT).show();
        }
    }
    public boolean isValidEmail(String email) {
        boolean err = false;
        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);

        if(m.matches())
            err = true;
        return err;
    }

    public void next(View view) {
        switch (view.getId()){
            case R.id.next0:
                CheckBox agree = (CheckBox) findViewById(R.id.agree);
                if (agree.isChecked()) {
                    zero.setVisibility(view.GONE);
                    first.setVisibility(view.VISIBLE);
                } else
                    Toast.makeText(this, "동의하셔야 가입이 가능합니다", Toast.LENGTH_SHORT).show();
                break;
            case R.id.next1:
                setInfo();
                if(id != null && pw != null) {
                    first.setVisibility(view.GONE);
                    second.setVisibility(view.VISIBLE);
                } else {
                    Toast.makeText(this, "가입 정보를 확인하세요.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void checkID(View view) { //아이디 중복확인

        // 아이디 가져오기
        id = editId.getText().toString();

        // 중복 확인
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();

                while(child.hasNext()) {
                    if(child.next().getKey().equals(id)) {
                        Toast.makeText(StudentJoinActivity.this, "중복되는 ID입니다.", Toast.LENGTH_SHORT).show();
                        editId.setText("");
                        id = null;
                        databaseReference.removeEventListener(this);
                        return;
                    }
                }
                Toast.makeText(StudentJoinActivity.this, "사용할 수 있는 ID입니다.", Toast.LENGTH_SHORT).show();
                editId.setEnabled(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void setInfo() {

        // 비밀번호 확인
        String str1 = editPw1.getText().toString();
        String str2 = editPw2.getText().toString();

        if(str1.equals(str2))
            pw = str1;
        else
            pw = null;

        name = editName.getText().toString(); // 이름

        // 성별
        if(femaleButton.isChecked())
            gender = true;
        else if(maleButton.isChecked())
            gender = false;
        
        // 주소
        
        // 특이사항
        if(smokeCheck.isChecked())
            smoking = true;
        else
            smoking = false;
        
        if(curfewCheck.isChecked())
            curfew = true;
        else
            curfew = false;

        if(petCheck.isChecked())
            pet = true;
        else
            pet = false;

        if(helpCheck.isChecked())
            help = true;
        else
            help = false;
        
        // 그 외 특이사항
        uniqueness = editUniqueness.getText().toString();
    }

    public void search(View view) { //주소 API 검색

        // Toast.makeText(this, "주소 불러왔음",Toast.LENGTH_SHORT).show();      //delete
        Intent address = new Intent(this,Address.class);
        address.putExtra("activity",true);
        startActivity(address);

    }

    public void sendEmail(View view) { //이메일 보내기
        String email = emailText.getText().toString();
        if(isValidEmail(email)){
            Intent intent = new Intent(this,EmailCertifActivity.class);
            intent.putExtra("client_email",email);
            startActivityForResult(intent,REQUEST_EMAIL);
        }else{
            Toast.makeText(this,"유효하지 않은 형식입니다.",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EMAIL){
            if(resultCode == RESULT_OK){
                emailPassed = data.getBooleanExtra("email_certification", true);
                emailInit();
            }
        }else if(requestCode==REQUEST_IMAGE_CAPTURE){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap)extras.get("data");
            Matrix matrix = new Matrix();
            matrix.postRotate(90); //90도 회전
            Bitmap.createBitmap(imageBitmap, 0,0,
                    imageBitmap.getWidth(),imageBitmap.getHeight(),matrix,true);
            ((ImageView)findViewById(R.id.imageview)).setImageBitmap(imageBitmap);
        }else if(requestCode == REQUEST_STORAGE && resultCode==RESULT_OK){
            try{
                Bitmap image = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                Matrix matrix = new Matrix();
                matrix.postRotate(90); //90도 회전
                Bitmap.createBitmap(image, 0,0,
                        image.getWidth(),image.getHeight(),matrix,true);
                ((ImageView)findViewById(R.id.imageview)).setImageBitmap(image);
            }
            catch (IOException ex){}
        }
    }

    public void takePhoto(View view) { //사진찍어 신분증 인증
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(checkAppPermission(new String[]{android.Manifest.permission.CAMERA})){
            startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);
        }
        else
            askPermission(new String[]{android.Manifest.permission.CAMERA},REQUEST_IMAGE_CAPTURE);
    }

    boolean checkAppPermission(String[] requestPermission){
        boolean[] requestResult = new boolean[requestPermission.length];
        for(int i=0; i< requestResult.length; i++){
            requestResult[i] = (ContextCompat.checkSelfPermission(this,
                    requestPermission[i]) == PackageManager.PERMISSION_GRANTED );
            if(!requestResult[i]){
                return false;
            }
        }
        return true;
    }

    void askPermission(String[] requestPermission, int REQ_PERMISSION) {
        ActivityCompat.requestPermissions(
                this,
                requestPermission,
                REQ_PERMISSION
        );
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE :
                if (checkAppPermission(permissions)) {
                    Toast.makeText(this, "승인완료",Toast.LENGTH_SHORT).show();
                    takePhoto(view);
                    // 퍼미션 동의했을 때 할 일
                } else {
                    Toast.makeText(this, "사용 불가",Toast.LENGTH_SHORT).show();
                    // 퍼미션 동의하지 않았을 때 할일
                    finish();
                }
                break;
            case REQUEST_STORAGE:
                if (checkAppPermission(permissions)) {
                    Toast.makeText(this, "승인완료",Toast.LENGTH_SHORT).show();
                    loadPhote(view);
                    // 퍼미션 동의했을 때 할 일
                } else {
                    Toast.makeText(this, "사용 불가",Toast.LENGTH_SHORT).show();
                    // 퍼미션 동의하지 않았을 때 할일
                    finish();
                }
                break;
        }
    }
    public void loadPhote(View view) { //앨범에서 신분증 가져오기
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(checkAppPermission(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE})){
            if(intent.resolveActivity(getPackageManager())!=null)
                startActivityForResult(intent,REQUEST_STORAGE);
        }
        else
            askPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_STORAGE);
    }
}

