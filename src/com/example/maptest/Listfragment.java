package com.example.maptest;

import java.util.ArrayList;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link Listfragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link Listfragment#newInstance} factory method
 * to create an instance of this fragment.
 * 
 */
public class Listfragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	private OnFragmentInteractionListener mListener;
	ListView lv;
	 ArrayList<String> list;
	 ArrayAdapter<String>adapter;
	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment Listfragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static Listfragment newInstance(String param1, String param2) {
		Log.d("list fragment", "new instance");
		Listfragment fragment = new Listfragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		
		return fragment;
	}

	public Listfragment() {
		// Required empty public constructor
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("list fragment", "on create");
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
		list = new ArrayList<String>();
		for (int i = 0; i < 20; i++) {
			list.add("hoge" + i);
		}
		list.add(mParam1);
		list.add(mParam2);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		Log.d("list fragment", "on create view");
		View view = inflater.inflate(R.layout.fragment_listfragment, container,
				false);

		
		lv = (ListView) view.findViewById(R.id.listView1);
		if(lv == null)
			Log.d("list fragment", "list view is null on createview");
		adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, list);
		lv.setAdapter(adapter);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onActivityCreated(savedInstanceState);
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onFragmentInteraction(Uri uri);
		
	}
		


	public void change_listitem() {
		Log.d("list fragment", "add item");
		list.add("aa");
		adapter.notifyDataSetChanged();
	}
}
