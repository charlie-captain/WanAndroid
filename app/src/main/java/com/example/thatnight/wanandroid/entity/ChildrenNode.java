package com.example.thatnight.wanandroid.entity;

import java.util.List;

/**
 * Created by ThatNight on 2017.12.16.
 */

public class ChildrenNode {
    /**
     * id : 60
     * name : Android Studio相关
     * courseId : 13
     * parentChapterId : 150
     * order : 1000.0
     * visible : 1
     * children : []
     */

    private int id;
    private String name;
    private int courseId;
    private int parentChapterId;
    private double order;
    private int visible;
    private List<?> children;

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

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getParentChapterId() {
        return parentChapterId;
    }

    public void setParentChapterId(int parentChapterId) {
        this.parentChapterId = parentChapterId;
    }

    public double getOrder() {
        return order;
    }

    public void setOrder(double order) {
        this.order = order;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public List<?> getChildren() {
        return children;
    }

    public void setChildren(List<?> children) {
        this.children = children;
    }
}
