package com.pes.spb.t1.util;

public class UtilParams {
    public static final Integer EXIST_TEST_MODEL_ID = 1;
    public static final String EXIST_TEST_MODEL_NAME = "1";
    public static final String EXIST_TEST_MODEL_SURNAME = "1";

    public static final Integer ANOTHER_EXIST_TEST_MODEL_ID = 2;

    public static final Integer NOT_EXIST_TEST_MODEL_ID = 1;

    public static final String NOT_EXIST_TEST_MODEL_NAME = "11111111";

    public static final Integer ANOTHER_NOT_EXIST_TEST_MODEL_ID = 100;

    public static final Integer NEW_TEST_MODEL_ID = 1;
    public static final String NEW_TEST_MODEL_NAME = "1";
    public static final String NEW_TEST_MODEL_SURNAME = "1";

    public static final String ANOTHER_FIRST_NEW_TEST_MODEL_NAME = "another_1";
    public static final String ANOTHER_FIRST_NEW_TEST_MODEL_SURNAME = "another_1";

    public static final String ANOTHER_SECOND_NEW_TEST_MODEL_NAME = "another_2";
    public static final String ANOTHER_SECOND_NEW_TEST_MODEL_SURNAME = "another_2";

    public static final String ANOTHER_SECOND_UPDATED_NEW_TEST_MODEL_NAME = "another_2";

    public static final String NEW_EXIST_MAME_TEST_MODEL_NAME = "1";
    public static final String NEW_EXIST_MAME_TEST_MODEL_SURNAME = "1";

    public static final Integer OLD_TEST_MODEL_ID = 1;

    public static String getTooBig(int maxLen) {
        StringBuilder buf = new StringBuilder("");
        for(int i = 0; i < maxLen + 1; i++) {
            buf.append("a");
        }
        return buf.toString();
    }

    public static String getTooLittle(int minLen) {
        if(minLen < 0) {
            throw new IllegalArgumentException("Bad minLen < 0!!!");
        }
        StringBuilder buf = new StringBuilder("");
        for(int i = 0; i < minLen - 1; i++) {
            buf.append("a");
        }
        return buf.toString();
    }

    public static String getNull() {
        return null;
    }
}
