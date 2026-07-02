package com.oplus.media;

public class MediaFile {
    public static class MediaFileType {
        public final int fileType;
        public final String mimeType;
        public MediaFileType(int fileType, String mimeType) {
            this.fileType = fileType;
            this.mimeType = mimeType;
        }
    }

    public static MediaFileType getFileType(String path) {
        if (path == null) return null;
        String lowerPath = path.toLowerCase();
        if (lowerPath.endsWith(".mp4")) return new MediaFileType(21, "video/mp4");
        if (lowerPath.endsWith(".jpg") || lowerPath.endsWith(".jpeg")) return new MediaFileType(31, "image/jpeg");
        if (lowerPath.endsWith(".heic") || lowerPath.endsWith(".heif")) return new MediaFileType(37, "image/heif");
        if (lowerPath.endsWith(".png")) return new MediaFileType(33, "image/png");
        if (lowerPath.endsWith(".gif")) return new MediaFileType(32, "image/gif");
        if (lowerPath.endsWith(".webp")) return new MediaFileType(36, "image/webp");
        return new MediaFileType(0, "");
    }
    
    public static String getMimeTypeForFile(String path) {
        MediaFileType type = getFileType(path);
        return type != null ? type.mimeType : "application/octet-stream";
    }

    public static int getFileTypeForMimeType(String mimeType) {
        if (mimeType == null) return 0;
        String lower = mimeType.toLowerCase();
        if (lower.equals("video/mp4")) return 21;
        if (lower.equals("image/jpeg")) return 31;
        if (lower.equals("image/heic") || lower.equals("image/heif")) return 37;
        if (lower.equals("image/png")) return 33;
        if (lower.equals("image/gif")) return 32;
        if (lower.equals("image/webp")) return 36;
        if (lower.startsWith("image/")) return 31;
        if (lower.startsWith("video/")) return 21;
        return 0;
    }

    public static boolean isAudioFileType(int fileType) { return false; }
    
    public static boolean isVideoFileType(int fileType) { 
        return fileType == 21; 
    }
    
    public static boolean isImageFileType(int fileType) { 
        return (fileType >= 31 && fileType <= 37) || (fileType >= 300 && fileType <= 309); 
    }

    public static boolean isMimeTypeMedia(String mimeType) {
        int fileType = getFileTypeForMimeType(mimeType);
        return isAudioFileType(fileType) || isVideoFileType(fileType) || isImageFileType(fileType);
    }

    public static boolean isExifMimeType(String mimeType) {
        return isImageMimeType(mimeType);
    }

    public static boolean isAudioMimeType(String mimeType) {
        return mimeType != null && mimeType.startsWith("audio/");
    }

    public static boolean isVideoMimeType(String mimeType) {
        return mimeType != null && mimeType.startsWith("video/");
    }

    public static boolean isImageMimeType(String mimeType) {
        return mimeType != null && mimeType.startsWith("image/");
    }
}
