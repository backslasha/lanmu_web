package lanmu.entity.card;

import com.google.gson.annotations.Expose;

import lanmu.entity.db.User;

public class UserCard {

    @Expose
    private long id;
    @Expose
    private String name;
    @Expose
    private String avatarUrl;
    @Expose
    private String introduction;
    @Expose
    private String gender;
    @Expose
    private String phone;

    public UserCard(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.avatarUrl = user.getAvatarUrl();
        this.phone = user.getPhone();
        this.introduction = user.getIntroduction();
        this.gender = user.getGender();
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}
