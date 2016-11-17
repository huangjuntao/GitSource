package com.huang.api.modules.system.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SYSTEM_USER")
public class SystemUser implements Serializable
{

    private static final long serialVersionUID = 6932445209498352642L;

    @Id
    @Column(name = "C_ID", length = 30)
    private String id;

    @Column(name = "C_USER_NAME")
    private String userName;

    @Column(name = "C_USER_PASSWD")
    private String userpasswd;

    @Column(name = "C_CREATE_TIME")
    private Date createTime;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserpasswd()
    {
        return userpasswd;
    }

    public void setUserpasswd(String userpasswd)
    {
        this.userpasswd = userpasswd;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

}
