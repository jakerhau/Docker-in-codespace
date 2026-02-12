package demo.common.model;

import java.io.Serializable;

public class Person implements Serializable {
    private String id;
    private String name;
    private int age;

    public Person() {
    }

    public Person(int age, String id, String name) {
        this.age = age;
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Person{");
        sb.append("id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", age=").append(age);
        sb.append('}');
        return sb.toString();
    }

}