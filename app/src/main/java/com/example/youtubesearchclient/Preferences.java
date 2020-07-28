package com.example.youtubesearchclient;

public class Preferences {
    private static String app_Theme;
    private static String video_Size;
    private static String show_Videos;
    private static String account_Name;

    public static String getApp_Theme() {
        return app_Theme;
    }

    public static void setApp_Theme(String theme) {
        app_Theme = theme;
    }

    public static void setAccount_Name(String name) {
        account_Name = name;
    }

    public static String getAccount_Name() {
        return account_Name;
    }

    public static String getShow_Videos() {
        return show_Videos;
    }

    public static void setShow_Videos(String show_videos) {
        show_Videos = show_videos;
    }

    public static String getVideo_Size() {
        return video_Size;
    }

    public static void setVideo_Size(String video_Size) {
        Preferences.video_Size = video_Size;
    }
}
