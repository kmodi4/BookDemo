package com.example.karan.bookdemo.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.karan.bookdemo.R;

import java.util.ArrayList;


public class ChatMessageAdapter extends ArrayAdapter<ChatMessage> {
    private final int MY_MESSAGE = 0, OTHER_MESSAGE = 1, MY_IMAGE = 2, OTHER_IMAGE = 3;

    public ChatMessageAdapter(Context context, ArrayList<ChatMessage> data) {
        super(context, R.layout.chatrow, data);
    }

    @Override
    public int getViewTypeCount() {
        // my message, other message, my image, other image
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage item = getItem(position);

        if (item.isMine() && !item.isImage()) return MY_MESSAGE;
        else if (!item.isMine() && !item.isImage()) return OTHER_MESSAGE;
        else if (item.isMine() && item.isImage()) return MY_IMAGE;
        else /*if(!item.isMine() && item.isImage())*/ return OTHER_IMAGE;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);
        if (viewType == MY_MESSAGE) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.chatrow, parent, false);

            TextView textView = (TextView) convertView.findViewById(R.id.text);
            textView.setText(getItem(position).getContent());

        } else if (viewType == OTHER_MESSAGE) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.otherchat, parent, false);

            TextView textView = (TextView) convertView.findViewById(R.id.text);
            textView.setText(getItem(position).getContent());
        }/* else if (viewType == MY_IMAGE) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.chatrow, parent, false);
        } else {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.otherchat, parent, false);
        }*/


        return convertView;
    }
}