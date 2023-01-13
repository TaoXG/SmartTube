package com.liskovsoft.smartyoutubetv2.common.exoplayer.selector.track;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.youtubeapi.videoinfo.models.TranslatedCaptionTrack;

public class SubtitleTrack extends MediaTrack {
    public SubtitleTrack(int rendererIndex) {
        super(rendererIndex);
    }

    @Override
    public int inBounds(MediaTrack track2) {
        return compare(track2);
    }

    @Override
    public int compare(MediaTrack track2) {
        if (format == null) {
            return -1;
        }

        if (track2 == null || track2.format == null) {
            return 1;
        }

        int result = -1;

        // Subs id is not constant. Don't rely on it.
        if (Helpers.equals(track2.format.language, format.language)) { // exact match
            result = 0;
        } else if (Helpers.startsWith(track2.format.language, trim(format.language))) { // partial match
            // Use autogenerated subs only if ones was selected before
            if (isAutogen(track2.format.language) && !isAutogen(format.language)) {
                // Use autogenerated subs only if ones was selected before
                //result = -1;
                // Override: any match
                result = 1;
            } else {
                result = 1;
            }
        }

        return result;
    }

    /**
     * Contains autogenerated subs?
     */
    private static boolean isAutogen(String language) {
        if (language == null) {
            return false;
        }

        return language.contains("(") && language.contains(")");
    }

    /**
     * Remove autogenerated and other stuff
     */
    public static String trim(String language) {
        if (language == null) {
            return null;
        }

        return trimAuto(language)
                .replaceAll(" \\(.*", "") // english (us) bla -> english
                .replaceAll(" - .*", ""); // english - us bla -> english
    }

    /**
     * Removes auto translate marker
     */
    public static String trimAuto(String language) {
        if (language != null && language.endsWith(TranslatedCaptionTrack.TRANSLATE_MARKER)) {
            return language.replaceFirst(".$","");
        } else {
            return language;
        }
    }

    public static boolean isAuto(String language) {
        return language != null && language.endsWith(TranslatedCaptionTrack.TRANSLATE_MARKER);
    }
}
