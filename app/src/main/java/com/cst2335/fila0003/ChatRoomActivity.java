package com.cst2335.fila0003;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import static com.cst2335.fila0003.MyOpener.COL_ID;
import static com.cst2335.fila0003.MyOpener.TABLE_NAME;

public class ChatRoomActivity extends AppCompatActivity {

    private ArrayList<Message> elements = new ArrayList<>( Arrays.asList( ) );
    private MyListAdapter myAdapter;
    private SQLiteDatabase db;
    //private int previouseState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat_room);
        TextView typeHere = findViewById(R.id.typeHere);
        Button sentButton = findViewById(R.id.sentButton);
        Button receiveButton = findViewById(R.id.receiveButton);
        ListView myList = findViewById(R.id.listView);
        loadDataFromDataBase();
        myList.setAdapter( myAdapter = new MyListAdapter()); // setAdapter is looping through

// myAdapter.notifyDataSetChanged(); // do we need this or no?
        String [] columns = {
                MyOpener.COL_ID, MyOpener.COL_TEXT, MyOpener.COL_SENDER};
       // previouseState = getLastID(db);
       // db.execSQL("delete from " + TABLE_NAME);

        sentButton.setOnClickListener( click -> {
            String text = typeHere.getEditableText().toString();
            ContentValues newRowValues = new ContentValues();
            newRowValues.put(MyOpener.COL_TEXT, text);
            newRowValues.put(MyOpener.COL_SENDER, true);
            long newId = db.insert(MyOpener.TABLE_NAME,null,newRowValues);

            Cursor results = db.query(false, MyOpener.TABLE_NAME,columns,null,null,null,null,null,null);
            printCursor(results,db.getVersion());
            Message message = new Message(text, true, newId);
            elements.add(message);
            typeHere.getEditableText().clear(); // will clean the text in the "Type here" field
            myAdapter.notifyDataSetChanged(); // this line is updating screen without it items will be added to elements, but nothing will be changed on screen

        });

        receiveButton.setOnClickListener( click -> {
            String text = typeHere.getEditableText().toString();
            ContentValues newRowValues = new ContentValues();
            newRowValues.put(MyOpener.COL_TEXT, text);
            newRowValues.put(MyOpener.COL_SENDER, false);
            long newId = db.insert(MyOpener.TABLE_NAME,null,newRowValues);
            Cursor results = db.query(false, MyOpener.TABLE_NAME,columns,null,null,null,null,null,null);
            printCursor(results,db.getVersion());
            Message message = new Message(text, true, newId);
            elements.add(message);
            typeHere.getEditableText().clear(); // will clean the text on Type here
            myAdapter.notifyDataSetChanged();
        });



        myList.setOnItemLongClickListener( (parent, view, pos, id) -> {

            String str =  R.string.dialogMessage2 + " " + (pos + 1)+ "\n" + R.string.dialogMessage3 + " "+ id;
            //Message selectedMessage = elements.get(pos);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(R.string.Title)
                    .setMessage(getString(R.string.dialogMessage2) + " " + (pos + 1)+ "\n" + getString(R.string.dialogMessage3) + " "+ id)
                    .setPositiveButton("Yes", (click, arg) -> {
                        // delete from database
                        db.delete(MyOpener.TABLE_NAME, MyOpener.COL_ID + "= ?", new String[]{Long.toString(elements.get(pos).getId())});
                       // db.execSQL("delete from " + TABLE_NAME + " where _id='"+ elements.get(pos).getId()+previouseState+ "'");

                        Cursor results = db.query(false, TABLE_NAME, columns, null, null,null, null, null, null);
                        printCursor(results, db.getVersion());
                        elements.remove(pos);
                        myAdapter.notifyDataSetChanged();
                    }).setNegativeButton("NO", (click, arg) -> {})

                    .setView(getLayoutInflater().inflate(R.layout.dialog_layout, null))
                    .create().show();
            return true;
        });
    }
  /*  public int getLastID(SQLiteDatabase db)
    {
        Cursor cursor = db.rawQuery("SELECT MAX("+COL_ID+") FROM "+TABLE_NAME, null);
        int maxid = (cursor.moveToFirst() ? cursor.getInt(0) : 0);
        return maxid;
    }*/

    private void loadDataFromDataBase(){
        MyOpener myOpener = new MyOpener(this);
        db = myOpener.getWritableDatabase();
        String [] columns = {
                MyOpener.COL_ID, MyOpener.COL_TEXT, MyOpener.COL_SENDER};
        Cursor results = db.query(false, TABLE_NAME, columns,null,null,null,null,null,null);
        int idColumnIndex = results.getColumnIndex(MyOpener.COL_ID);
        int messageColumnIndex = results.getColumnIndex(MyOpener.COL_TEXT);
        int senderColumnIndex = results.getColumnIndex(MyOpener.COL_SENDER);
        while(results.moveToNext())
        {
            boolean sender = !(results.getInt(senderColumnIndex) == 0);
            String message = results.getString(messageColumnIndex);
            long id = results.getLong(idColumnIndex);
            //add the new message to the array list:
            elements.add(new Message(message, sender, id));
        }
        printCursor(results, db.getVersion());
        //At this point, the contactsList array has loaded every row from the cursor.
        }

        private void printCursor(Cursor c, int version){
        Log.e("DB Version","" + version);
        Log.e("Number columns","" + c.getColumnCount());
        String[] columnNames = c.getColumnNames();
        Log.e ("Column names:","");

        for( int i=0; i < c.getColumnCount(); i++){
            Log.e("Column" + i, columnNames[i]);
        }
        c.moveToPosition(-1);
        Log.e("Number of rows:", ""+ c.getCount());
          while (c.moveToNext()){
              Log.e("" + MyOpener.COL_ID," " + c.getInt(0));
              Log.e("" + MyOpener.COL_TEXT," " + c.getString(1));
              Log.e("" + MyOpener.COL_SENDER," " + c.getInt(2));

          }
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
            if (m.isSender()) {
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