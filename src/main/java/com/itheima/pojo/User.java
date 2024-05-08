package com.itheima.pojo;
//封装User对象的属性
public class User {
    private int id;
    private String name;
    private int age;
    private String posotion;

    // 无参数构造函数
    public User() {
    }
    // 构造函数
    public User(int id, String name, int age, String posotion) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.posotion = posotion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPosotion() {
        return posotion;
    }

    public void setPosotion(String posotion) {
        this.posotion = posotion;
    }

}
