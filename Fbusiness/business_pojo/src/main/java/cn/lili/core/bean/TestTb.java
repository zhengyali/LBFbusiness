package cn.lili.core.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ABEL on 2018/5/16.
 */
public class TestTb implements Serializable {
    private Integer id;
    private String name;
    private Date birthday;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Date getBirthday() {
        return birthday;
    }
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
    public String toString() {
        return super.toString();
    }
}
