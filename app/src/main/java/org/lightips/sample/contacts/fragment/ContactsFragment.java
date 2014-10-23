package org.lightips.sample.contacts.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.lightips.sample.contacts.ContactDetailActivity;
import org.lightips.sample.contacts.Main;
import org.lightips.sample.contacts.R;

import org.lightips.sample.contacts.entity.Contact;
import org.lightips.sample.contacts.fragment.adapter.ContactAdapter;

import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends Fragment implements AbsListView.OnItemClickListener {

    protected static final String TAG = ContactsFragment.class.getSimpleName();
    private static final String ARG_SECTION_NUMBER = "section_number";

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;
    private List<Contact> contacts;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ContactAdapter mAdapter;

    // TODO: Rename and change types of parameters
    public static ContactsFragment newInstance(int sectionNumber) {
        ContactsFragment fragment = new ContactsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ContactsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.contacts = new ArrayList<Contact>();
        for(int idx = 0; idx <=2; idx++){
            Contact contact = new Contact();
            contact.setFirstName("firstName"+idx);
            contact.setLastName("lastName"+idx);
            this.contacts.add(contact);
        }
        mAdapter = new ContactAdapter(getActivity(), R.layout.contact_item, this.contacts);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);
        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((Main) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Contact contact=this.mAdapter.getItem(position);
        Intent intent = new Intent(this.getActivity(), ContactDetailActivity.class);
        intent.putExtra(ContactDetailActivity.CONTACT_DETAIL, contact);
        startActivity(intent);
    }


    public void update(List<Contact> contacts){
        this.mAdapter.clear();
        this.mAdapter.addAll(contacts);
    }

    public void search(){
        Contact contact = new Contact();
        contact.setFirstName("newFirst");
        contact.setLastName("newLast");
        this.mAdapter.add(contact);
        this.mAdapter.notifyDataSetChanged();
    }

    /**
    * This interface must be implemented by activities that contain this
    * fragment to allow an interaction in this fragment to be communicated
    * to the activity and potentially other fragments contained in that
    * activity.
    * <p>
    * See the Android Training lesson <a href=
    * "http://developer.android.com/training/basics/fragments/communicating.html"
    * >Communicating with Other Fragments</a> for more information.
    */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}
