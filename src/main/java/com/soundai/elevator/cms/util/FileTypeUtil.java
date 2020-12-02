package com.soundai.elevator.cms.util;


/**
 *
 */
public class FileTypeUtil {


    public static Integer getType(String url) {
        String fileTyle = url.substring(url.lastIndexOf(".") + 1, url.length()).toLowerCase();
        int type = 0;
        if (fileTyle.contains("bmp") ||
                fileTyle.contains("jpg") ||
                fileTyle.contains("jpeg") ||
                fileTyle.contains("png") ||
                fileTyle.contains("gif")
        ) {
            type = 1;
        } else if (fileTyle.contains("mp4") ||
                fileTyle.contains("avi") ||
                fileTyle.contains("mov") ||
                fileTyle.contains("wmv") ||
                fileTyle.contains("3gp") ||
                fileTyle.contains("f4v") ||
                fileTyle.contains("rmvb")
        ) {
            type = 2;
        }
        return type;
    }


    public static void main(String[] args) {
        String url = "dsdsdsddsds._d.jpeg";

        System.out.println(FileTypeUtil.getType(url));
    }
}
