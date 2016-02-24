package com.manijshrestha.androidnfcdemo;

import android.app.Activity;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;


public class NFCActivity extends Activity implements NfcAdapter.CreateNdefMessageCallback {

    private EditText mEditText;
    private EditText mFullName, mPhoneNum, mEmailAddr, mNickname, mOrganization;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        //mEditText = (EditText) findViewById(R.id.edit_text_field);
        mFullName = (EditText) findViewById(R.id.full_name_text);
        mPhoneNum = (EditText) findViewById(R.id.phone_num_text);
        mEmailAddr = (EditText) findViewById(R.id.email_text);
        mNickname  = (EditText) findViewById(R.id.home_addr_text);
        mOrganization = (EditText) findViewById(R.id.organization_text);

        NfcAdapter mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
           // mEditText.setText("Sorry this device does not have NFC.");
            return;
        }

        if (!mAdapter.isEnabled()) {
            Toast.makeText(this, "Please enable NFC via Settings.", Toast.LENGTH_LONG).show();
        }

        mAdapter.setNdefPushMessageCallback(this, this);
    }

    /**
     * Ndef Record that will be sent over via NFC
     * @param nfcEvent
     * @return
     */
    @Override
    public NdefMessage createNdefMessage(NfcEvent nfcEvent) {
        String message = mFullName.getText().toString() + "\n" + mPhoneNum.getText().toString()
                + "\n" + mEmailAddr.getText().toString() + "\n" + mNickname.getText().toString()
                + "\n" + mOrganization.getText().toString();

        NdefRecord rtdUriRecord1 = NdefRecord.createUri("http://example.com");

        NdefRecord ndefRecord = NdefRecord.createMime("text/plain", message.getBytes());
        NdefMessage ndefMessage = new NdefMessage(ndefRecord);
        return ndefMessage;
    }
}
