/**
 * Đối tượng tin nhắn
 * @author Danh, To Huu
 */

package com.danh.common.model;

import com.danh.common.enums.ActionType;
import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private ActionType action;
    private Object content;
    private String sender;

    public Message() {
    }

    public Message(ActionType action, Object content, String sender) {
        this.action = action;
        this.content = content;
        this.sender = sender;
    }

    public ActionType getAction() {
        return action;
    }

    public void setAction(ActionType action) {
        this.action = action;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}