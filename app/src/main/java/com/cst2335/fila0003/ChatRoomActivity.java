package com.cst2335.fila0003;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class ChatRoomActivity extends AppCompatActivity {




    private class Message {
        private String text; // message string
        private Type type;
        public Message(String text, Type type) {
            this.text = text;
            this.type = type;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Type getType() {
            return this.type;
        }

        public void setType(Type type) {
            this.type = type;
        }


    }
    private ArrayList<Message> elements = new ArrayList<>( Arrays.asList( ) );

    private MyListAdapter myAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat_room);
        TextView typeHere = findViewById(R.id.typeHere);


       Button sentButton = findViewById(R.id.sentButton);
        sentButton.setOnClickListener( click -> {
            Message message = new Message(typeHere.getEditableText().toString(), Type.SEND);
            elements.add(message);
            typeHere.getEditableText().clear(); // will clean the text in the "Type here" field
            myAdapter.notifyDataSetChanged(); // this line is updating screen without it items will be added to elements, but nothing will be changed on screen

        });

        Button receiveButton = findViewById(R.id.receiveButton);
        receiveButton.setOnClickListener( click -> {
            Message message = new Message(typeHere.getEditableText().toString(), Type.RECEIVE);
            elements.add(message);
            typeHere.getEditableText().clear(); // will clean the text on Type here
            myAdapter.notifyDataSetChanged();
        });

        ListView myList = findViewById(R.id.listView);
        myList.setAdapter( myAdapter = new MyListAdapter()); // setAdapter is looping through
        myList.setOnItemLongClickListener( (parent, view, pos, id) -> {

            String str =  R.string.dialogMessage2 + " " + (pos + 1)+ "\n" + R.string.dialogMessage3 + " "+ id;
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(R.string.Title)
                    .setMessage(getString(R.string.dialogMessage2) + " " + (pos + 1)+ "\n" + getString(R.string.dialogMessage3) + " "+ id)
                    .setPositiveButton("Yes", (click, arg) -> {
                        elements.remove(pos);
                        myAdapter.notifyDataSetChanged();
                    }).setNegativeButton("NO", (click, arg) -> {})

                    .setView(getLayoutInflater().inflate(R.layout.dialog_layout, null))
                    .create().show();
            return true;
        });
    }

    private class MyListAdapter extends BaseAdapter {

        public int getCount() { return elements.size();}

       public Object getItem(int position) { return elements.get(position); }
      //public Object getItem(int position) { return elements.get(position) + position; }

        public long getItemId(int position) { return (long) position; }

        public View getView(int position, View old, ViewGroup parent) // getView() will be called by myAdapter.notifyDataSetChanged();
        {
            LayoutInflater inflater = getLayoutInflater();
            View newView = old;

          Message m = elements.get(position);
            if (m.getType()== Type.SEND) {
                //make a new row:
                newView = inflater.inflate(R.layout.send_layout, parent, false);

                //set what the text should be for this row:
                TextView tView = newView.findViewById(R.id.textGoesHere);
                tView.setText(m.getText());
            } else {
                newView = inflater.inflate(R.layout.receive_layout, parent, false);

                //set what the text should be for this row:
                TextView tView = newView.findViewById(R.id.textGoesHere);
                tView.setText(m.getText());
            }
            return newView;
        }
    }




    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}