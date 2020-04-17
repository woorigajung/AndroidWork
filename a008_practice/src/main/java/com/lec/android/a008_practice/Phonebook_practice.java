package com.lec.android.a008_practice;

import java.io.Serializable;

public class Phonebook_practice implements Serializable {
    String name;
    String age;
    String address;

    public Phonebook_practice() {}

    public Phonebook_practice(String name, String age, String address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }

    public String getName() {return name; }


    public String getAge() {return age; }

    public String getAddress() {return address; }

    public void setName(String name) { this.name = name; }

    public void setAge(String age) {this.age = age; }

    public void setAddress(String address) { this.address = address; }
}
