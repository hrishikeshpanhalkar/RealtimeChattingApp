package com.example.socketapp;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter {

    private static final int TYPE_MESSAGE_SENT = 0;
    private static final int TYPE_MESSAGE_RECEIVED = 1;
    private static final int TYPE_IMAGE_SENT = 2;
    private static final int TYPE_IMAGE_RECEIVED = 3;

    private LayoutInflater inflater;
    private List<JSONObject> messages = new ArrayList<>();

    public ChatAdapter (LayoutInflater inflater){
        this.inflater = inflater;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case TYPE_MESSAGE_SENT:
                view = inflater.inflate(R.layout.item_send_message, parent, false);
                return new SentMessageViewHolder(view);
            case TYPE_IMAGE_SENT:
                view = inflater.inflate(R.layout.item_send_image, parent, false);
                return new SentImageViewHolder(view);
            case TYPE_MESSAGE_RECEIVED:
                view = inflater.inflate(R.layout.item_recieved_message, parent, false);
                return new ReceivedMessageViewHolder(view);
            case TYPE_IMAGE_RECEIVED:
                view = inflater.inflate(R.layout.item_received_image, parent, false);
                return new ReceivedImageViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        JSONObject message = messages.get(position);
        try{
            if(message.getBoolean("isSent")){
                if(message.has("message")){
                    SentMessageViewHolder messageViewHolder = (SentMessageViewHolder) holder;
                    messageViewHolder.messageText.setText(message.getString("message"));
                }else{
                    SentImageViewHolder imageViewHolder = (SentImageViewHolder) holder;
                    Bitmap bitmap = getBitmapFromString(message.getString("image"));
                    imageViewHolder.imageView.setImageBitmap(bitmap);
                }
            }else {
                if(message.has("message")){
                    ReceivedMessageViewHolder messageViewHolder = (ReceivedMessageViewHolder) holder;
                    messageViewHolder.messageText.setText(message.getString("message"));
                    messageViewHolder.messageName.setText(message.getString("name"));
                }else{
                    ReceivedImageViewHolder imageViewHolder = (ReceivedImageViewHolder) holder;
                    imageViewHolder.messageName.setText(message.getString("name"));
                    Bitmap bitmap = getBitmapFromString(message.getString("image"));
                    imageViewHolder.imageView.setImageBitmap(bitmap);
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private Bitmap getBitmapFromString(String image) {
        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addItem(JSONObject jsonObject){
        messages.add(jsonObject);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    private class SentMessageViewHolder extends RecyclerView.ViewHolder {
        private TextView messageText;

        public SentMessageViewHolder(@NonNull View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.sendEdittext);
        }
    }

    private class SentImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public SentImageViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.sendImage);
        }
    }

    private class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        private TextView messageText;
        private TextView messageName;

        public ReceivedMessageViewHolder(@NonNull View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.receivedEdittext);
            messageName = itemView.findViewById(R.id.receivedNameEdittext);
        }
    }

    private class ReceivedImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView messageName;

        public ReceivedImageViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.receivedImage);
            messageName = itemView.findViewById(R.id.receivedNameEdittext);
        }
    }

    @Override
    public int getItemViewType(int position) {
        JSONObject message = messages.get(position);
        try{
            if(message.getBoolean("isSent")){
                if(message.has("message")){
                    return TYPE_MESSAGE_SENT;
                }else {
                    return TYPE_IMAGE_SENT;
                }
            }else{
                if(message.has("message")){
                    return TYPE_MESSAGE_RECEIVED;
                }else {
                    return TYPE_IMAGE_RECEIVED;
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return -1;
    }
}
