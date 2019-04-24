package lanmu.entity;

public class User {
    private static final int SEX_MALE = 1;
    private static final int SEX_FEMALE = 2;

    private String name;
    private int age;
    private int sex;

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
