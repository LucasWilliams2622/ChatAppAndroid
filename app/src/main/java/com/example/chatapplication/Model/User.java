package com.example.chatapplication.Model;

import java.io.Serializable;

public class User implements Serializable {
    public String name ,image,email,token,id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
