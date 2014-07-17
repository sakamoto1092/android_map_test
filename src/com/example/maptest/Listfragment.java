package com.example.maptest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

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
	private ArrayList<Favorite> mParam1;
	private String mParam2;

	private OnFragmentInteractionListener mListener;
	ListView lv;
	ArrayList<String> list;
	ArrayAdapter<String> adapter;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param fl
	 *            Parameter 1.
	 * 
	 * @return A new instance of fragment Listfragment.
	 */
	// TODO: Rename and change types and number of parameters
	public Listfragment newInstance(FavoriteList fl) {
		Log.d("list fragment", "new instance");
		Listfragment fragment = new Listfragment();
		Bundle args = new Bundle();
		args.putParcelableArrayList(ARG_PARAM1, fl.favlist);
		// args.putSerializable(ARG_PARAM1, fl.favlist);
		// args.putString(ARG_PARAM2, param2);
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
			mParam1 = getArguments().getParcelableArrayList(ARG_PARAM1);
			// mParam2 = getArguments().getString(ARG_PARAM2);
		}
		list = new ArrayList<String>();
		for (int i = 0; i < mParam1.size(); i++) {
			list.add(mParam1.get(i).title);
		}
		;
		// list.add(mParam2);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		Log.d("list fragment", "on create view");
		View view = inflater.inflate(R.layout.fragment_listfragment, container,
				false);

		lv = (ListView) view.findViewById(R.id.listView1);
		if (lv == null)
			Log.d("list fragment", "list view is null on createview");
		adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, list);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO 自動生成されたメソッド・スタブ
				LayoutInflater inflater = getActivity().getLayoutInflater();
				View v = inflater.inflate(R.layout.dialog1, null);
				final EditText editText1 = (EditText) v
						.findViewById(R.id.dialogText1);
				final EditText editText2 = (EditText) v
						.findViewById(R.id.dialogText2);
				final EditText editText3 = (EditText) v
						.findViewById(R.id.dialogText6);
                editText1.setText(mParam1.get(position).title);
                editText2.setText(mParam1.get(position).addres);
                editText3.setText(mParam1.get(position).tag);
				new AlertDialog.Builder(getActivity())
						.setTitle("Hello, AlertDialog!")
						.setIcon(R.drawable.ic_drawer)
						.setView(v)
						.setPositiveButton("show map",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // TODO mapへジャンプする機能の実装
                                        Log.d("dialog on listview ",
                                                "clicked show map");
                                        Intent i = new Intent(getActivity(), MainActivity.class);
                                              i.putExtra("fav",mParam1.get(position) );
                                        	startActivity(i);
                                    }
                                }
                        )
						.setNegativeButton("Close",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        Log.d("dialog on listview ",
                                                "clicked Close");
                                    }
                                }
                        )
						.setNeutralButton("適応",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // TODO ここで入力されたものをお気に入りに反映させる
                                        String path = "/data/data/" + getActivity().getPackageName() + "/files/favorite1.txt";
                                        String str;
                                        StringBuffer sb = new StringBuffer();
                                        FileWriter fw = null;
                                        int count = 0;
                                        BufferedReader br = null;
                                        Favorite fv = new Favorite();

                                        try {
                                            br = new BufferedReader(new FileReader(path));
                                            try {
                                                while ((str = br.readLine()) != null) {
                                                    if (position != count++) {
                                                        sb.append(str);

                                                    } else {
                                                        String string;
                                                        String s1,s2,s3;
                                                        s1 = editText1.getText().toString().equals("") ? 
                                                        		"empty" : editText1.getText().toString();
																s2 = editText2.getText().toString().equals("") ? 
																		"empty" : editText2.getText().toString();
																s3 = editText3.getText().toString().equals("") ? 
																		"empty" : editText3.getText().toString();
																
                                                        String[]  s = str.split("\t");
                                                        string = s1 + "\t" + s2 + "\t" + s[2] + "\t" + s[3]
                                                                +"\t" + s3 + "\n";
                                                        list.set(position,s1);
                                                        fv.title = editText1.getText().toString();
                                                        fv.addres = editText2.getText().toString();
                                                        fv.latlng = new LatLng(Double.valueOf(s[2]),Double.valueOf(s[3]));
                                                        fv.tag = editText3.getText().toString();

                                                        sb.append(string);
                                                    }
                                                }
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        }

                                        // 削除をファイルに反映
                                        try {
                                            fw = new FileWriter(path);
                                            fw.write(sb.toString());
                                            fw.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        mParam1.set(position,fv);
                                        adapter.notifyDataSetChanged();

                                    }
                                }
                        ).show();

			}
		});
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO お気に入りを削除するかしないかのダイアログを出す
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						getActivity());
				alertDialogBuilder.setTitle("お気に入りの削除");
				alertDialogBuilder.setMessage("このお気に入りを削除しますか？");
				alertDialogBuilder.setPositiveButton("はい",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO お気に入りの削除をする
                                String path = "/data/data/" + getActivity().getPackageName() + "/files/favorite1.txt";
                                String str;
                                StringBuffer sb = new StringBuffer();
                                FileWriter fw = null;
                                int count = 0;
                                BufferedReader br = null;

                                try {
                                    br = new BufferedReader(new FileReader(path));
                                    try {
                                        while ((str = br.readLine()) != null) {
                                            if (position != count++) {
                                                sb.append(str);
                                            }
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }

                                // 削除をファイルに反映
                                try {
                                    fw = new FileWriter(path);
                                    fw.write(sb.toString());
                                    fw.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                mParam1.remove(position);
                                list.remove(position);
                                adapter.notifyDataSetChanged();

                            }

						});
				alertDialogBuilder.setNegativeButton("いいえ",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						});
				
				alertDialogBuilder.setCancelable(false);
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();

				return true;
			}

		});

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
