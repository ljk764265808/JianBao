package us.mifeng.been;

import java.util.List;

/**
 * Created by admin on 2016/11/28.
 */

public class DetailsBeen {

    public int id;//编号
    public int user_id;
    public String title;//标题
    public String description;//描述
    public String price;//价格
    public String contact;//联系人
    public String mobile;//手机
    public String qq;//QQ
    public String wechat;//微信
    public String email;//电子邮箱
    public List<String> list;//照片
    public int issue_time;//发布时间
    public int follow;//关注数量
    public int state;//状态
    public int owner;//本人所属

    public DetailsBeen() {
    }

    public DetailsBeen(int id, int user_id, String title, String description, String price, String contact, String mobile, String qq, String wechat, String email, List<String> list, int issue_time, int follow, int state, int owner) {
        this.id = id;
        this.user_id = user_id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.contact = contact;
        this.mobile = mobile;
        this.qq = qq;
        this.wechat = wechat;
        this.email = email;
        this.list = list;
        this.issue_time = issue_time;
        this.follow = follow;
        this.state = state;
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public int getIssue_time() {
        return issue_time;
    }

    public void setIssue_time(int issue_time) {
        this.issue_time = issue_time;
    }

    public int getFollow() {
        return follow;
    }

    public void setFollow(int follow) {
        this.follow = follow;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }
}
