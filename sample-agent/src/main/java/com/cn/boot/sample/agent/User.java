package com.cn.boot.sample.agent;

/**
 * @author Chen Nan
 */
public class User {
    private Integer id;
    private String username;

    public User() {
    }

    public User(Integer id, String username) {
        this.id = id;
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSchoolName(Integer id) {
        System.out.println("当前id=" + id);
        return "A中学";
    }

}
