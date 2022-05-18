package com.hdh.assignment.bean;

public class Server{
    private String name;
    private String ip;

    public Server() {
    }

    public Server(String name,String ip){
        this.name = name;
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public String getIp() {
        return ip;
    }
}
