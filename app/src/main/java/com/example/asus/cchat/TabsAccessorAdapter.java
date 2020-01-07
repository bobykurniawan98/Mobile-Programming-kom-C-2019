package com.example.asus.cchat;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by asus on 1/7/2019.
 */

public class TabsAccessorAdapter extends FragmentPagerAdapter{
    public TabsAccessorAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        switch(position)
        {
            case 0:
                 ChatsFragment chatsFragment = new ChatsFragment();
                 return  chatsFragment;


            case 1:
                 GroupsFragment groupsFragment = new GroupsFragment();
                 return  groupsFragment;

            case 2:
                ContacsFragment contacsFragment = new ContacsFragment();
                return  contacsFragment;

            case 3:
                RequestFragment requestFragment = new RequestFragment();
                return  requestFragment;

            default:
                 return null;
        }

    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position)
        {
            case 0:
                return "Chats";
            case 1:
                return "Groups";
            case 2:
                return "Contact";
            case 3:
                return "Request";
            default:
                return null;
        }
    }
}
