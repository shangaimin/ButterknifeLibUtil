package com.sgx.kotlin;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

@Entity
public class JPushMessages {
    @Id(autoincrement = true)
    private Long id;
    private String title;
    public String msgId;
    private String content;
    private String time;
    private int state;

    public JPushMessages(String title, String msgId, String content, String time, int state) {
        this.title = title;
        this.msgId = msgId;
        this.content = content;
        this.time = time;
        this.state = state;
    }

    @Generated(hash = 1484778356)
    public JPushMessages(Long id, String title, String msgId, String content,
            String time, int state) {
        this.id = id;
        this.title = title;
        this.msgId = msgId;
        this.content = content;
        this.time = time;
        this.state = state;
    }
    @Generated(hash = 463435904)
    public JPushMessages() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getMsgId() {
        return this.msgId;
    }
    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public int getState() {
        return this.state;
    }
    public void setState(int state) {
        this.state = state;
    }

}
