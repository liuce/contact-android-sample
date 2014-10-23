package org.lightips.sample.contacts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import org.apache.commons.lang3.StringUtils;
import org.lightips.sample.contacts.entity.Contact;


public class ContactDetailActivity extends Activity {

    public final static String CONTACT_DETAIL = "contact_detail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_contact_detail);
        Intent intent = getIntent();
        Contact contact=(Contact)intent.getSerializableExtra(CONTACT_DETAIL);
        EditText firstNameEt=(EditText)this.findViewById(R.id.contact_item_firstName);
        EditText lastNameEt=(EditText)this.findViewById(R.id.contact_item_lastName);
        EditText phoneEt=(EditText)this.findViewById(R.id.contact_item_phone);
        EditText emailEt=(EditText)this.findViewById(R.id.contact_item_email);
        if(contact!=null){
            this.fillEditText(firstNameEt, contact.getFirstName());
            this.fillEditText(lastNameEt,contact.getLastName());
            this.fillEditText(phoneEt, contact.getPhone());
            this.fillEditText(emailEt, contact.getEmail());
        }
    }

    private void fillEditText(EditText editText, String text){
        if(StringUtils.isNoneBlank(text)){
            editText.setText(text);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contact_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
//        if (id == android.R.id.home) {
//            // This ID represents the Home or Up button. In the case of this
//            // activity, the Up button is shown. Use NavUtils to allow users
//            // to navigate up one level in the application structure. For
//            // more details, see the Navigation pattern on Android Design:
//            //
//            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
//            //
////            NavUtils.navigateUpTo(this, new Intent(this, Main.class));
////            NavUtils.navigateUpFromSameTask(this);
////            this.onBackPressed();
//
//            return this.onNavigateUpFromChild(this);
//        }
        return super.onOptionsItemSelected(item);
    }

}
