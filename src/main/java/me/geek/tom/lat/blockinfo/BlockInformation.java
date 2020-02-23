package me.geek.tom.lat.blockinfo;

import com.google.common.collect.Lists;

import java.util.List;

public class BlockInformation {

    private String name;
    private String modName;

    private List<String> otherInformation = Lists.newArrayList();

    public void setName(String name) {
        this.name = name;
    }

    public void setModName(String modName) {
        this.modName = modName;
    }

    public void addInformation(String info) {
        otherInformation.add(info);
    }
}
