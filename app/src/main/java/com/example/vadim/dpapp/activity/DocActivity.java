package com.example.vadim.dpapp.activity;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vadim.dpapp.R;
import com.example.vadim.dpapp.application.AppConfig;
import com.example.vadim.dpapp.application.DBHelper;
import com.example.vadim.dpapp.application.RESTController;
import com.example.vadim.dpapp.containers.DocContainer;

import java.util.ArrayList;

/**
 * Created by Vadim on 14.04.2017.
 */
public class DocActivity extends AppCompatActivity {
    DBHelper dbHelper;
    Button saveMessage;
    String code,avtor,message;
    TextView codeDoc = null, avtorDoc,messageText;
    EditText messageDoc;
    String isEdit = "";

    RESTController rest;
    boolean docFlag = true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doc_element);
        dbHelper = new DBHelper(this);
        saveMessage = (Button) findViewById(R.id.saveMessage);
        codeDoc =(TextView) findViewById(R.id.codeDoc);
        messageDoc = (EditText) findViewById(R.id.messageDoc);
        messageText = (TextView) findViewById(R.id.tvMessage);
        avtorDoc = (TextView) findViewById(R.id.avtorDoc);
        isEdit = getIntent().getStringExtra("isEdit");
        if(isEdit.equals("true")){
            saveMessage.setVisibility(View.GONE);
            messageDoc.setVisibility(View.GONE);
            messageText.setVisibility(View.VISIBLE);
        }
        else {
            saveMessage.setVisibility(View.VISIBLE);
            messageDoc.setVisibility(View.VISIBLE);
            messageText.setVisibility(View.GONE);
        }
        final Toast t = Toast.makeText(this,"Сообщение успешно отправлено!",Toast.LENGTH_LONG);
        InitializationVariable();
        rest = new RESTController(this,DocActivity.class.getSimpleName());
        saveMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                docFlag = true;
                if(messageDoc.getText().toString().equals("")){
                    messageDoc.setHint("Заполните поле!");
                    messageDoc.setHintTextColor(Color.RED);
                    docFlag = false;
                }
                if(docFlag)
                    rest.sendDocuments(code,codeDoc.getText().toString(), avtorDoc.getText().toString(), messageDoc.getText().toString());
                    t.show();
            }
        });

    }
    public void InitializationVariable(){
        code = getIntent().getStringExtra("codeDoc");
        avtor = getIntent().getStringExtra("nameDoc");
        ArrayList<DocContainer> list = dbHelper.getAllDocuments();
        if(code==null|code==""){
            if(list.size()!=0) {
                int last = Integer.parseInt(list.get(list.size() - 1).getCodeDoc()) + 1;
                code = String.valueOf(last);
            }
            else{
                code=String.valueOf(1);
            }
        }
        if(avtor==null | avtor==""){
            avtor = AppConfig.User;
        }
        for(DocContainer doc:list){
            if (doc.getCodeDoc().equals(code)){
                message = doc.getMessageDoc();
                break;
            }
        }
        codeDoc.setText(code);
        avtorDoc.setText(avtor);
        messageDoc.setText(message);
        messageText.setText(message);
    }
}
