package com.zeldan.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.zeldan.service.DeltaUsersParserService;

/**
 * Unit test for {@link DeltaUsersParserService}.
 */
public class DeltaUsersParserServiceTest {

    private static final String FIRST_USER_ID = "12345678";
    private static final String SECOND_USER_ID = "23456789";

    private static final String NOTIFICATION_BODY = "{\r\n" +
            "    \"list_folder\": {\r\n" +
            "        \"accounts\": [\r\n" +
            "            \"dbid:AAH4f99T0taONIb-OurWxbNQ6ywGRopQngc\",\r\n" +
            "            ...\r\n" +
            "        ]\r\n" +
            "    },\r\n" +
            "    \"delta\": {\r\n" +
            "        \"users\": [\r\n" +
            "            " + FIRST_USER_ID + ",\r\n" +
            "            " + SECOND_USER_ID + "\r\n" +
            "        ]\r\n" +
            "    }\r\n" +
            "}";

    private DeltaUsersParserService underTest;

    @Before
    public void setUp() {
        underTest = new DeltaUsersParserService();
    }

    @Test
    public void shouldParseTwoUsersInDelta() {
        // GIVEN

        // WHEN
        List<String> users = underTest.getUsers(NOTIFICATION_BODY);

        // THEN
        assertEquals(2, users.size());
        assertEquals(FIRST_USER_ID, users.get(0));
        assertEquals(SECOND_USER_ID, users.get(1));
    }

    @Test
    public void shouldReturnWithEmptyListIfTheNoficiationIsWrong() {
        // GIVEN

        // WHEN
        List<String> users = underTest.getUsers("WRONG NOTIFICATION {BODY} !");

        // THEN
        assertTrue(users.isEmpty());
    }
}