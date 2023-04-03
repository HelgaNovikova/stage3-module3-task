package com.mjc.school.service.utils;

import com.mjc.school.service.exception.AuthorNameException;
import com.mjc.school.service.exception.ContentLengthException;
import com.mjc.school.service.exception.TitleLengthException;


public class NewsValidator {
    private static final int MIN_AUTHOR_LENGTH = 3;
    private static final int MAX_AUTHOR_LENGTH = 15;
    private static final int MIN_TITLE_LENGTH = 5;
    private static final int MAX_TITLE_LENGTH = 30;
    private static final int MIN_CONTENT_LENGTH = 5;
    private static final int MAX_CONTENT_LENGTH = 255;

    public static boolean isAuthorValid(String author) {
        return author.length() >= MIN_AUTHOR_LENGTH && author.length() <= MAX_AUTHOR_LENGTH;
    }

    public static boolean isTitleValid(String title) {
        return title.length() >= MIN_TITLE_LENGTH && title.length() <= MAX_TITLE_LENGTH;
    }

    public static boolean isContentValid(String content) {
        return content.length() >= MIN_CONTENT_LENGTH && content.length() <= MAX_CONTENT_LENGTH;
    }

    public static void validateTitle(String title) {
        if (!isTitleValid(title)) {
            throw new TitleLengthException
                    (String.format("News title can not be less than %d and more than %d symbols. News title is %s", MIN_TITLE_LENGTH, MAX_TITLE_LENGTH, title));
        }
    }

    public static void validateContent(String content) {
        if (!isContentValid(content)) {
            throw new ContentLengthException
                    (String.format("News content can not be less than %d and more than %d symbols. News content is %s", MIN_CONTENT_LENGTH, MAX_CONTENT_LENGTH, content));
        }
    }

    public static void validateAuthor(String author) {
        if (!isAuthorValid(author)) {
            throw new AuthorNameException(String.format("Author's name can not be less than %d and more than %d symbols. Author's name is %s", MIN_AUTHOR_LENGTH, MAX_AUTHOR_LENGTH, author));
        }
    }
}
