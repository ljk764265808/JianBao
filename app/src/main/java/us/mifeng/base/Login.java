package us.mifeng.base;

/**
 * Created by admin on 2016/11/28.
 */

public class Login {

    /**
     * status : 200
     * info : 成功
     * data : {"id":2,"mobile":"13800138000","name":"钟移动","gender":"男","password":"E10ADC3949BA59ABBE56E057F20F883E","token":"786914AB5E1746E68F176D455DF49A33","state":0}
     */

    private String status;
    private String info;
    /**
     * id : 2
     * mobile : 13800138000
     * name : 钟移动
     * gender : 男
     * password : E10ADC3949BA59ABBE56E057F20F883E
     * token : 786914AB5E1746E68F176D455DF49A33
     * state : 0
     */

    private DataBean data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int id;
        private String mobile;
        private String name;
        private String gender;
        private String password;
        private String token;
        private int state;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }
    }
}
