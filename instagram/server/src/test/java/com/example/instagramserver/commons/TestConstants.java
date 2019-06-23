package com.example.instagramserver.commons;

import java.util.UUID;

public class TestConstants {
    private TestConstants(){}

    public static final Long PERSON_ID = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    public static final Long ANOTHER_PERSON_ID = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    public static final String USERNAME = "me@gmail.com";
    public static final String ANOTHER_USERNAME = "another@gmail.com";
    public static final String PASSWORD = "Test3850";
    public static final Long POST_ID = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    public static final Long ANOTHER_POST_ID = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    public static final String POST_IMG_PATH = "http://blah.com/images/image.png";
    public static final String ANOTHER_POST_IMG_PATH = "http://blah.com/images/another.png";
    public static final String POST_DESCRIPTION = "Post1";
    public static final String ANOTHER_POST_DESCRIPTION = "Post2";
    public static final String TAG_TEXT_1 = "Tag1";
    public static final String TAG_TEXT_2 = "Tag2";
    public static final String TAG_TEXT_3 = "Tag3";

}
