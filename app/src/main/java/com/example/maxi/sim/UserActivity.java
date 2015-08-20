package com.example.maxi.sim;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class UserActivity {
    private String user;
    private String password;
    private String rol;

    public UserActivity(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser() {

        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
