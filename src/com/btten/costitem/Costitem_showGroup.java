package com.btten.costitem;

import java.util.List;

public class Costitem_showGroup {
    public String title;

    private boolean checked;

    public List<Costitem_showListItem> children;

    public Costitem_showGroup(String title, boolean checked,List<Costitem_showListItem> children) {
        this.title = title;
        setChecked(checked);
        this.children = children;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean b) {
        checked = b;
        if (children != null && children.size() > 0) {// 若children不为空，循环设置children的checked
            for (Costitem_showListItem each : children) {
                each.checked = checked;
            }
        }
    }
}
