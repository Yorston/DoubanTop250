package com.zjm.doubantop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zjm.doubantop.R;


/**
 * Created by B on 2016/7/29.
 */
public class LoginActivity extends AppCompatActivity{

    private EditText edt_name;
    private EditText edt_psd;
    private Button login_btn;
    private String name = "admin";
    private String psd = "admin";




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_vertical);
        edt_name = (EditText) findViewById(R.id.name);
        edt_psd = (EditText) findViewById(R.id.psd);
        login_btn = (Button) findViewById(R.id.login_btn);

        //edt_psd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        //edt_psd.setTransformationMethod(PasswordTransformationMethod.getInstance());

        //edt_name.setSingleLine(true);
        //edt_psd.setSingleLine(true);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input_name = edt_name.getText().toString();
                String input_psd = edt_psd.getText().toString();
                System.out.println(input_name.matches("[^a-zA-Z0-9\\\\s](.*?)"));
                //String i = "gk@#j^1h5(3!";
                //ShowToast(i.replaceAll("[^a-zA-Z0-9\\\\s]", ""));
                if(input_name.equals("")){
                    ShowToast("用户名不能为空!");
                }else if(input_name.equals("")){
                    ShowToast("密码不能为空!");
                }else if(/*input_name.equals("\\S(.*?)")||*/input_name.matches("[^a-zA-Z0-9\\\\s](.*?)")){
                    ShowToast("非法输入!");
                }else if(/*input_psd.equals("\\S(.*?)")||*/input_psd.matches("[^a-zA-Z0-9\\\\s](.*?)")){
                    ShowToast("非法输入!");
                }else{
                    if(input_name.equals(name)){
                        if(input_psd.equals(psd)){
                            Intent intent = new Intent();
                            intent.setAction("com.zjm.DoubanTop.ListActivity");
                            startActivity(intent);
                        }else{
                            ShowToast("密码错误!");
                        }
                    }else{
                        ShowToast("用户名错误!");
                    }
                }
            }
        });

    }

    public void ShowToast(String msg){
        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();
    }

}
