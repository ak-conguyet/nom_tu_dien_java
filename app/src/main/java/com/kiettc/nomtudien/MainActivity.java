package com.kiettc.nomtudien;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private EditText editText;
    private Button button,button2;
    private sql db;
    private list ls;
    private Context context;
    //dialog
    private Dialog dialog;
    private Button buttonClose,buttonWebView,charView;
    private TextView chars,mean,dock;

    //private final String databaseURL = "jdbc:ucanaccess:hannom.accdb";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.input);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        listView = findViewById(R.id.list);
        //khoi tao cho dialog detail
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_detail);
        buttonClose=(Button) dialog.findViewById(R.id.close);
        buttonWebView =(Button) dialog.findViewById(R.id.webview);
        chars = (TextView) dialog.findViewById(R.id.chars);
        mean = (TextView) dialog.findViewById(R.id.mean);
        dock = (TextView) dialog.findViewById(R.id.dock);
        //
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                System.out.println(1);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                System.out.println(2);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                System.out.println(3);
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @SuppressLint("NewApi")
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                execute(1);
                return false;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                hideKeybord();
                execute(2);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                hideKeybord();
                execute(1);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                detail(ls.getItem(i));
            }
        });
    }

    //hien thi chi tiet bang dialog
    void detail(HanNom hanNom){

        chars.setText(hanNom.getHanNom());
        mean.setText(hanNom.getMean());
        dock.setText(hanNom.getAmVKhac());
        mean.setMovementMethod(new ScrollingMovementMethod());

        chars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager =(ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("label",hanNom.getHanNom());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(MainActivity.this,getString(R.string.coppied_to_clipbord),Toast.LENGTH_SHORT).show();
            }
        });

        buttonWebView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://hvdic.thivien.net/whv/"+hanNom.getHanNom()));
                startActivity(intent);
            }
        });

        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    //tìm trong dadatabase và add vào
    @RequiresApi(api = Build.VERSION_CODES.N)
    void execute(int i){
        try {
            db = new sql(getBaseContext(),1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Editable text =editText.getText();
        if (!(text.toString()).isEmpty()){
            String string = String.valueOf(editText.getText());
            if (i==1){
                if (db.searchInNom(string).isEmpty()){
                    sendMess(getString(R.string.no_data));
                }else {
                    ls = new list(db.searchInNom(string));
                    listView.setAdapter(ls);
                }
            }
            else {
                if (db.searchInHan(string).isEmpty()){
                    sendMess(getString(R.string.no_data));
                }else {
                    ls = new list(db.searchInHan(string));
                    listView.setAdapter(ls);
                }
            }
        }else{
            sendMess(getString(R.string.no_input));
        }
    }


    //hide keyboard

    private void hideKeybord(){
        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(button.getWindowToken(), 0);
    }
    
    private void sendMess(String message){
        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).setMessage(message).show();
    }
}