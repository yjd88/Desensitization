package com.midou.sensitive.entity;

import com.midou.sensitive.aspect.annotation.SensitiveValid;
import com.midou.sensitive.aspect.type.DesensitizeEnum;

/**
 * @author: Yang Jundong
 * @date: 2021/3/12 0012 9:32
 * @description: JavaBean实体类
 */
public class User {

    @SensitiveValid(prefix = 1,suffix = 0,symbol = "^")
    private String name;
    @SensitiveValid(DesensitizeEnum.MOBILE_PHONE)
    private String phone;
    @SensitiveValid(DesensitizeEnum.ID_CARD)
    private String IDCard;
    @SensitiveValid(prefix = 3,suffix = 4,separator = "@")
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIDCard() {
        return IDCard;
    }

    public void setIDCard(String IDCard) {
        this.IDCard = IDCard;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", IDCard='" + IDCard + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
