package com.csii.simpleone;

public interface Constant {
    String authorities = "com.csii.ContentProvider";
    String DB_NAME = "csii.db";
    String DB_BOOKS_TABLE_NAME = "books";
    int DB_VERSION = 2;
    String CRAETE_BOOKS_TABLE_SQL = "CREATE TABLE IF NOT EXISTS "
            + DB_BOOKS_TABLE_NAME
            + "(_id INTEGER PRIMARY KEY,name TEXT NOT NULL,author TEXT NOT NULL,publish_time TEXT NOT NULL,modifiy TEXT NOT NULL);";
}
