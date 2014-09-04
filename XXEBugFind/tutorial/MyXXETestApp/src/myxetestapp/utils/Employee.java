package myxetestapp.utils;

public class Employee {

    private String name;

    private int age;

    private int id;

    private String type;

    public Employee() {

    }

    public Employee(String name, int id, int age, String type) {
        this.name = name;
        this.age = age;
        this.id = id;
        this.type = type;

    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Employee Details - ");
        sb.append("Name:" + getName());
        sb.append(", ");
        sb.append("Type:" + getType());
        sb.append(", ");
        sb.append("Id:" + getId());
        sb.append(", ");
        sb.append("Age:" + getAge());
        sb.append(".");

        return sb.toString();
    }
}
