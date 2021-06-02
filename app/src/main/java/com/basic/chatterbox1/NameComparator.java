package com.basic.chatterbox1;

import java.util.Comparator;

public class NameComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {

        UserModal user1 = (UserModal) o1;
        UserModal user2 = (UserModal) o2;


        return user1.name.compareTo(user2.name);
    }
}
