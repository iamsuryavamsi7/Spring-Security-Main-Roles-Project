package com.crypto07.SpringSecurityMain.Error;

public class UserIsNotAuthenticated extends Exception {

    public UserIsNotAuthenticated(String msg){

        super(msg);

    }

}
