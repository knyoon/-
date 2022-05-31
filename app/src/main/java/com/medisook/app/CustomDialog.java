package com.medisook.app;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomDialog extends AlertDialog implements View.OnClickListener{
    ArrayList<FilterItem> filterItemArrayList, filtered_filterList;
    ArrayList<String> listItemArrayList;
    LinearLayoutManager linearLayoutManager;
    EditText searchET;
    RecyclerView recyclerView, recyclerView_list;
    Adapter_filter adapter;
    Adapter_list adapter_list;
    private Button okButton;
    private Context context;
    private CustomDialogListener customDialogListener;
    public CustomDialog(Context context) {
        super(context);
        this.context = context;
    }
    interface CustomDialogListener{
        void onOkClicked(ArrayList<String> text);
    }
    public void setDialogListener(CustomDialogListener customDialogListener){
        this.customDialogListener = customDialogListener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.redpop);
        okButton = (Button) findViewById(R.id.popup_ok_btn);
        okButton.setOnClickListener(this);
        searchET = findViewById(R.id.search_bar);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view_filter);
        recyclerView_list = (RecyclerView)findViewById(R.id.recycler_view_list);
        filtered_filterList = new ArrayList<>();
        filterItemArrayList = new ArrayList<>();
        listItemArrayList = new ArrayList<>();
        adapter = new Adapter_filter(filterItemArrayList, listItemArrayList, adapter_list, this);
        //adapter_list = new Adapter_list(listItemArrayList, adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.context, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        recyclerView_list.setLayoutManager(new LinearLayoutManager(this.context, RecyclerView.VERTICAL, false));
        recyclerView_list.setAdapter(adapter_list);
        adapter.notifyDataSetChanged();
        //adapter_list.notifyDataSetChanged();
        //adapter_list.setArrayData("안녕");
//        Log.d("리스트", String.valueOf(listItemArrayList.size()));
//        for(int i = 0; i<listItemArrayList.size(); i++){
//
//        }
        for(int i = 0; i<100; i++){
            adapter.setArrayData(new FilterItem(i+"번째 필터", false));
        }
        adapter.setOnItemClicklistener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewHolder_filter holder, View view, int position) {
                Log.d("리스트", "클릭 테스트");
            }
        });
        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String searchText = searchET.getText().toString();
                searchFilter(searchText);
            }
        });
        recyclerView.setAdapter(adapter);
        //recyclerView_list.setAdapter(adapter_list);
        searchET.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                String searchText = searchET.getText().toString();
                switch (i){
                    case KeyEvent.KEYCODE_ENTER:
                        if (keyEvent.getAction() == keyEvent.ACTION_UP) {
                            Log.d("키보드", searchText);
                            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(searchET.getWindowToken(), 0);
                        } return true;
                    case KeyEvent.KEYCODE_DEL:
                }
                return false;
            }
        });
    }
//    protected void onResume() {
//        onResume();
//    }
    public void searchFilter(String searchText){
        filtered_filterList.clear();
        for (int i=0; i<filterItemArrayList.size(); i++){
            if(filterItemArrayList.get(i).getFilterName().toLowerCase().contains(searchText.toLowerCase())){
                filtered_filterList.add(filterItemArrayList.get(i));
            }
        }
        adapter.filterList(filtered_filterList);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.popup_ok_btn:
                String text = searchET.getText().toString();
                //customDialogListener.onOkClicked(text);
                ArrayList<String> data = new ArrayList<>();
                ArrayList<FilterItem> filterItemArrayList = adapter.getFilterItemArrayList();
                for (int i = 0; i<filterItemArrayList.size(); i++){
                    FilterItem filteredItem = filterItemArrayList.get(i);
                    if(filteredItem.isSelected() == true) {
                        //adapter_list.setArrayData(String.valueOf(filterItemArrayList.get(i)));
                        Log.d("필터", filteredItem.getFilterName());
                        data.add(filteredItem.getFilterName());
                    }
                    //adapter.setArrayData(new FilterItem(i+" 번째 필터"));
                }
                Toast.makeText(this.context, "Selected filter : "+data, Toast.LENGTH_LONG).show();
                //String text = searchET.getText().toString();
                customDialogListener.onOkClicked(data);
                dismiss();
                break;
        }
    }
}
