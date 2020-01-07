package com.example.asus.cchat;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {

    private View privateChatView;
    private RecyclerView chatList;

    private DatabaseReference chatRef, usersRef;
    private FirebaseAuth mAuth;

    private String currentUserID;



    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        privateChatView = inflater.inflate(R.layout.fragment_groups, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        chatRef = FirebaseDatabase.getInstance().getReference().child("Contacts");
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        chatList = privateChatView.findViewById(R.id.chat_list);
        chatList.setLayoutManager(new LinearLayoutManager(getContext()));

        return privateChatView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Contacs> options = new FirebaseRecyclerOptions.Builder<Contacs>()
                .setQuery(chatRef.child(currentUserID), Contacs.class)
                .build();

        FirebaseRecyclerAdapter<Contacs,ChatsViewHolder> adapter =
                new FirebaseRecyclerAdapter<Contacs, ChatsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ChatsViewHolder holder, int position, @NonNull Contacs model)
                    {
                        final String usersIDs = getRef(position).getKey();

                        final String[] retImage = {"default_image"};
                        usersRef.child(usersIDs).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               if(dataSnapshot.exists())
                               {
                                   if(dataSnapshot.hasChild("image"))
                                   {
                                       retImage[0] = dataSnapshot.child("image").getValue().toString();

                                       Picasso.get().load(retImage[0]).into(holder.profileImage);
                                   }

                                   final String retName = dataSnapshot.child("name").getValue().toString();
                                   final String retStatus = dataSnapshot.child("status").getValue().toString();

                                   holder.userName.setText(retName);
                                   holder.userStatus.setText("Last Seen : " + "\n" + "Date" + "Time");

                                   holder.itemView.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                                           chatIntent.putExtra("visit_user_id", usersIDs);
                                           chatIntent.putExtra("visit_user_name", retName);

                                           chatIntent.putExtra("visit_image", retImage[0]);
                                           startActivity(chatIntent);
                                       }
                                   });
                               }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });



                    }

                    @NonNull
                    @Override
                    public ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_display_layout, viewGroup,false);
                        return  new ChatsViewHolder(view);
                    }
                };
        chatList.setAdapter(adapter);
        adapter.startListening();

    }

    public static class ChatsViewHolder extends RecyclerView.ViewHolder
    {
        TextView userName, userStatus;
        CircleImageView profileImage;
        public ChatsViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.user_profile_name);
            userStatus = itemView.findViewById(R.id.user_status);
            profileImage = itemView.findViewById(R.id.profile_image);

        }
    }
}
