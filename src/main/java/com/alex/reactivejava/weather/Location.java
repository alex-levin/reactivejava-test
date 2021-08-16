package com.alex.reactivejava.weather;

import java.io.Serializable;

/**
 * @author mertcaliskan
 */
public class Location implements Serializable {

	private static final long serialVersionUID = 6818047071284185358L;
	
	String name;

    public Location() {
    }

    public Location(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
