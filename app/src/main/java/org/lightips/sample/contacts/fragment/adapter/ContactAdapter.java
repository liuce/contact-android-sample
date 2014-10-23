package org.lightips.sample.contacts.fragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.lightips.sample.contacts.R;
import org.lightips.sample.contacts.entity.Contact;
import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by liuce on 10/17/14.
 */
public class ContactAdapter extends ArrayAdapter<Contact> {
    private int resource;

    public ContactAdapter(Context context, int resource, List<Contact> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Contact contact = getItem(position);
        LinearLayout linearLayout;

        if(convertView == null){
            linearLayout = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi;
            vi = (LayoutInflater)getContext().getSystemService(inflater);
            vi.inflate(resource, linearLayout, true);
        }
        else{
            linearLayout = (LinearLayout)convertView;
        }
        TextView firstName = (TextView)linearLayout.findViewById(R.id.firstName);
        TextView lastName =(TextView)linearLayout.findViewById(R.id.lastName);

        firstName.setText(contact.getFirstName());
        lastName.setText(contact.getLastName());

        return linearLayout;
    }
}
