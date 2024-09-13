package com.quick.adapter;

import com.quick.pojo.po.QuickChatContact;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-07-08  10:20
 * @Description: 通讯录-适配器
 * @Version: 1.0
 */
public class ContactAdapter {

    public static QuickChatContact buildContactPO(String accountId, Long groupId, Integer type, String noteName) {
        QuickChatContact contact = new QuickChatContact();
        contact.setFromId(accountId);
        contact.setToId(groupId.toString());
        contact.setType(type);
        contact.setNoteName(noteName);
        return contact;
    }

    public static QuickChatContact buildContactPO(String accountId, Long groupId, Integer type) {
        QuickChatContact contact = new QuickChatContact();
        contact.setFromId(accountId);
        contact.setToId(groupId.toString());
        contact.setType(type);
        return contact;
    }
}
