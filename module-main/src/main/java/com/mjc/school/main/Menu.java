package com.mjc.school.main;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

@Component
public class Menu implements InitializingBean {
    private final Map<Integer, MenuItem> menuMap = new LinkedHashMap<>();

    private final CommandInvoker commandInvoker;

    public Menu(CommandInvoker commandInvoker) {
        this.commandInvoker = commandInvoker;
    }

    public MenuItem getMenuItemById(int id) {
        return menuMap.get(id);
    }

    void addMenuItem(int index, String title, Consumer<Scanner> run) {
        menuMap.put(index, new MenuItem(index, title) {
            @Override
            public void run(Scanner sc) {
                try {
                    run.accept(sc);
                } catch (RuntimeException e) {
                    System.out.println(e.getMessage());
                }
                showMenu();
            }
        });
    }

    @PostConstruct
    private void createMenu() {
        addMenuItem(1, "Get all news.",
                sc -> System.out.println(commandInvoker.readAllNews()));

        addMenuItem(2, "Get news by id.", sc -> {
            System.out.println("Operation: Get news by id.");
            Long newsId = getNewsIdLong(sc);
            System.out.println(commandInvoker.readNewsById(newsId));
        });

        addMenuItem(3, "Create news.", sc -> {
            System.out.println("Operation: Create news.");
            String title = getTitle(sc);
            String content = getContent(sc);
            String authorId = getAuthorId(sc);
            System.out.println(commandInvoker.createNews(title, content, authorId));
        });

        addMenuItem(4, "Update news.", sc -> {
            System.out.println("Operation: Update news.");
            String newsId = getNewsId(sc);
            String title = getTitle(sc);
            String content = getContent(sc);
            String authorId = getAuthorId(sc);
            System.out.println(commandInvoker.updateNews(newsId, title, content, authorId));
        });

        addMenuItem(5, "Remove news by id.", sc -> {
            System.out.println("Operation: Remove news by id.");
            Long newsId = getNewsIdLong(sc);
            System.out.println(commandInvoker.deleteNewsById(newsId));
        });

        addMenuItem(6, "Get all authors.",
                sc -> System.out.println(commandInvoker.readAllAuthors()));

        addMenuItem(7, "Get author by id.", sc -> {
            System.out.println("Operation: Get author by id.");
            Long authorId = getAuthorIdLong(sc);
            System.out.println(commandInvoker.readAuthorsById(authorId));
        });

        addMenuItem(8, "Create author.", sc -> {
            System.out.println("Operation: Create author.");
            String name = getName(sc);
            System.out.println(commandInvoker.createAuthors(name));
        });

        addMenuItem(9, "Update author.", sc -> {
            System.out.println("Operation: Update author.");
            String authorId = getAuthorId(sc);
            String name = getAuthorName(sc);
            System.out.println(commandInvoker.updateAuthors(authorId, name));
        });

        addMenuItem(10, "Remove author by id.", sc -> {
            System.out.println("Operation: Remove author by id.");
            Long authorId = getAuthorIdLong(sc);
            System.out.println(commandInvoker.deleteAuthorsById(authorId));
        });

        addMenuItem(11, "Get all tags.",
                sc -> System.out.println(commandInvoker.readAllAuthors()));

        addMenuItem(0, "Exit.", sc -> {
        });
    }

    private String getNewsId(Scanner sc) {
        return requestLine("Enter news id:", sc);
    }

    private String requestLine(String x, Scanner sc) {
        System.out.println(x);
        return getNextLine(sc);
    }

    private Long getAuthorIdLong(Scanner sc) {
        System.out.println("Enter author id:");
        return sc.nextLong();
    }

    private String getAuthorName(Scanner sc) {
        System.out.println("Enter new author's name:");
        return getNextLine(sc);
    }

    private String getTitle(Scanner sc) {
        System.out.println("Enter news title:");
        return getNextLine(sc);
    }

    private String getContent(Scanner sc) {
        return requestLine("Enter news content:", sc);
    }

    private Long getNewsIdLong(Scanner sc) {
        System.out.println("Enter news id:");
        return sc.nextLong();
    }

    private String getName(Scanner sc) {
        System.out.println("Enter author's name:");
        return getNextLine(sc);
    }

    private String getNextLine(Scanner sc) {
        String line;
        do {
            line = sc.nextLine();
        } while (!StringUtils.hasText(line));
        return line;
    }

    private String getAuthorId(Scanner sc) {
        return requestLine("Enter author id:", sc);
    }

    public void showMenu() {
        System.out.println("Enter the number of operation:");
        for (MenuItem item : menuMap.values()) {
            System.out.println(item.toString());
        }
    }

    @Override
    public void afterPropertiesSet() {
        createMenu();
    }
}
