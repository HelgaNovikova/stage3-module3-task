package com.mjc.school.main;

import java.util.Scanner;

public abstract class MenuItem {
    String title;
    int index;

    public abstract void run(Scanner sc);

    public MenuItem(int index, String title) {
        this.title = title;
        this.index = index;
    }

    @Override
    public String toString() {
        return this.index + " - " + this.title;
    }
}