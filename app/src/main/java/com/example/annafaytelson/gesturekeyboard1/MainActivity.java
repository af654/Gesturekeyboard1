package com.example.annafaytelson.gesturekeyboard1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    Button BTRegister;
    EditText UserName;
    String NewUsername;
    String UserList;
    FileIO tempFileService;
    ConstantValue constantValue = new ConstantValue();

    String filePath = constantValue.filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UserList = "userlist";
        BTRegister = (Button) findViewById(R.id.buttonRegister);

        BTRegister.setOnClickListener(BTRegisterS);
//        filePath= Environment.getExternalStorageDirectory().toString()+ File.separator+ "Attack"+ File.separator;
        tempFileService = new FileIO();
        tempFileService.setPath(filePath);
        //accountNameAlert();
    }

    private View.OnClickListener BTRegisterS=new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            Button vAddTemplates = (Button) v;
            welcomAlert();
        }
    };

        public void welcomAlert() {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setTitle("Welcome!");
            builder1.setMessage("In our gesture user study, there are two sessions:\n" +
                    "1. Free form gesture session. \n" +
                    "2. Common used gesture session.");
            builder1.setCancelable(true);
            builder1.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int arg1) {
                            Intent intentMainActivity2 = new Intent();
                            intentMainActivity2.putExtra("UserName", NewUsername);

                            try {
                                FileIO tempIO = new FileIO();
                                tempIO.setPath(filePath);
                                tempIO.updateProgress(getString(R.string.session2Progress_user), 300);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            String NewUsernameWrite = NewUsername + "\r\n";
                            try {
                                tempFileService.setPath(filePath);
                                tempFileService.save(UserList, NewUsernameWrite, true);
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            intentMainActivity2.setClass(MainActivity.this, Create_UniTCH_UniSTK.class);

                            MainActivity.this.startActivity(intentMainActivity2);

                            dialog.cancel();

                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
}
