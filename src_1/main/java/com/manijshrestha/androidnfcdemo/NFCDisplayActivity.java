package com.manijshrestha.androidnfcdemo;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by mshrestha on 7/23/2014.
 */
public class NFCDisplayActivity extends Activity {

    private TextView mTextView;
    private TextView mTextViewName, mTextViewPhone, mTextViewAddress, mTextViewEmail, mTextViewOrginization;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_display);
        mTextView = (TextView) findViewById(R.id.text_view);
        mTextViewName = (TextView)findViewById(R.id.full_name_text);
        mTextViewPhone = (TextView)findViewById(R.id.phone_num_text);
        mTextViewAddress = (TextView)findViewById(R.id.home_addr_text);
        mTextViewEmail = (TextView)findViewById(R.id.email_text);
        mTextViewOrginization = (TextView)findViewById(R.id.organization_text);

    }

    @Override
    protected void onResume(){
        super.onResume();
        Intent intent = getIntent();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);

            NdefMessage message = (NdefMessage) rawMessages[0]; // only one message transferred
//            String[] strArr = new String[4];
//            for(int i = 0; i < strArr.length;i++)
//            {
//
//            }
            mTextView.setText(new String(message.getRecords()[0].getPayload()));

        } else
            mTextView.setText("Waiting for NDEF Message");

    }
}
