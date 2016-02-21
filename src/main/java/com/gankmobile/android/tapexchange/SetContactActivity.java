package com.gankmobile.android.tapexchange;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class SetContactActivity extends ActionBarActivity {

    Button mSetInfoButton;
    EditText fullName, phoneNum, emailAddr, nickname, organization;
    String[] contactInfo = new String[5];
    //String mFullName, mPhoneNum, mEmailAddr, mNickname, mOrganization;

    String encodedContactInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_contact);

        fullName = (EditText) findViewById(R.id.full_name_text);
        phoneNum = (EditText) findViewById(R.id.phone_num_text);
        emailAddr = (EditText) findViewById(R.id.email_text);
        nickname  = (EditText) findViewById(R.id.home_addr_text);
        organization = (EditText) findViewById(R.id.organization_text);


        mSetInfoButton = (Button) findViewById(R.id.set_button);
        mSetInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactInfo[0] =  fullName.getText().toString();
                contactInfo[1] = phoneNum.getText().toString();
                contactInfo[2] = emailAddr.getText().toString();
                contactInfo[3] = nickname.getText().toString();
                contactInfo[4] = organization.getText().toString();

                for(int i = 0; i < contactInfo.length; i++)
                {
                    if(contactInfo[i] != null)
                    {
                        encodedContactInfo += contactInfo[i];
                    }
                    else
                    {
                        encodedContactInfo += "&";
                    }

                    encodedContactInfo += "\n";
                }

                //WritePhoneContact(mFullName, mPhoneNum, mEmailAddr, mNickname, mOrganization);
            }
        });
    }
    private boolean saveData(JSONSerializer serializer, int month, int day)
    {
        try
        {
            //serializer.saveFile( new String[] {postID, month + "", day + ""});
            Log.d("save_file", "Intent info saved to file");
            return true;
        }
        catch (Exception e)
        {
            Log.e("save_file", "File save unsuccessful");
            return false;
        }
    }

    public void WritePhoneContact(String displayName, String number, String email, String nickname, String organization)
    {
        Context context = getApplicationContext(); //Application's context or Activity's context

        ArrayList<ContentProviderOperation> cntProOper = new ArrayList<ContentProviderOperation>();
        int contactIndex = cntProOper.size();//ContactSize

        //Newly Inserted contact
        // A raw contact will be inserted ContactsContract.RawContacts table in contacts database.
        cntProOper.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)//Step1
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());

        //Display name will be inserted in ContactsContract.Data table
        cntProOper.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)//Step2
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,contactIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, displayName) // Name of the contact
                .build());

        //Mobile number will be inserted in ContactsContract.Data table
        cntProOper.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)//Step 3
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,contactIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, number) // Number to be added
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build()); //Type like HOME, MOBILE etc

        //Email will be inserted as well
        cntProOper.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)//Step2
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, contactIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.DISPLAY_NAME, email).build());

        // Insert nickname into contact
        cntProOper.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)//Step2
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,contactIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Nickname.NAME, nickname).build());

        // Organization
        cntProOper.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)//Step2
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, contactIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, organization).build());
        try
        {
            // We will do batch operation to insert all above data
            //Contains the output of the app of a ContentProviderOperation.
            //It is sure to have exactly one of uri or count set
            ContentProviderResult[] contentProresult = null;
            contentProresult = context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, cntProOper); //apply above data insertion into contacts list
        }
        catch (RemoteException exp)
        {
            Toast.makeText(getApplicationContext(), "Something went wrong, yo", Toast.LENGTH_SHORT).show();
        }
        catch (OperationApplicationException exp)
        {
            Toast.makeText(getApplicationContext(), "Something went wrong, yo", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_set_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
