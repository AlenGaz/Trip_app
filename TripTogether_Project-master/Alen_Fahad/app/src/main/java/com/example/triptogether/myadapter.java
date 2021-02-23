package com.example.triptogether;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;

public class myadapter extends FirebaseRecyclerAdapter<model,myadapter.myviewholder>
{
    public Search se = new Search();

    public myadapter(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
    }

    RecyclerView rv_list_frames;


    ListView usersTripPosition;
    TextView usersNameAtPos;
    ArrayList<String> al = new ArrayList<>();
    
    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull model model) {
        holder.name.setText(model.getName());
        holder.course.setText(model.getCourse());
        holder.email.setText(model.getEmail());
        al.add(model.getEmail());

        Log.d("model key:", model.getEmail());
        Log.d("al al al", String.valueOf(al.size()));
        Log.d("al position", String.valueOf(position));
        Glide.with(holder.img.getContext()).load(model.getPurl()).into(holder.img);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.button5) {

                }

            }

        });
    }

    public void onClick(View v) {
        if (v.getId() == R.id.button5){
    
        }
    }



    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent,false);

       return new myviewholder(view);
    }




    class myviewholder extends RecyclerView.ViewHolder
    {
        CircleImageView img;
        TextView name,course,email;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
            img=(CircleImageView)itemView.findViewById(R.id.img1);
            name=(TextView)itemView.findViewById(R.id.nametext);
            course=(TextView)itemView.findViewById(R.id.coursetext);
            email=(TextView)itemView.findViewById(R.id.emailtext);


            itemView.findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userLocalText;
      
                    userLocalText = email.getText().toString();

                    UserDetails.chatWith=userLocalText;

                    Intent i = new Intent(v.getContext(), Chat.class);
                    v.getContext().startActivity(i);


                    //se.CallChatActivity(userLocalText);

                }
            });


        }



        }



    }





