/*
 * Copyright 2012 digital publishing AG. All rights reserved.
 */

package dp.verp.persistence.controller;

import com.google.common.eventbus.EventBus;
import dp.common.Datastore;

import javax.persistence.EntityManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Alexander Orlov <a.orlov@digitalpublishing.de>
 */
@SuppressWarnings("deprecation")
public abstract class Data {

    public final static Integer testUserId = 178428;
    final static int phoneTrainerUserId = 155154;
    public final static Integer testUsageOwnerId = phoneTrainerUserId;
    final static int phoneTrainingArticleId = 23027;
    final static int phoneTrainingProductId = -27045;

    final static int bookingQuota = 100;
    final static String testUserPhone = "+41 123 4567-890";
    final static Integer testUserResourceId = 1;
    int wholeDaySlotResourceId = 6;
    final static Integer sessionId = testUserResourceId;
    final static Integer bookingId = testUserResourceId;
    final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    static int oneDay = 1000 * 60 * 60 * 24;
    static Date now = new Date();
    static Date nowPlusOneDays = new Date(now.getTime() + oneDay);
    static Date nowPlusTwoDays = new Date(now.getTime() + (2 * oneDay));
    static Date nowMinusOneDays = new Date(now.getTime() - oneDay);
    public static String sampleAuthToken = "25DC78FC-C460-4047-B1F9-1C2A3A23E20A";

    final static String testUserMail = "alexander.orlov@loxal.net";
    final static String testUserName = "Alex";

    // slots
    final static Integer[] slotResourceIds = new Integer[]{
            testUserResourceId, testUserResourceId, testUserResourceId,
            2, 4, 3,
            3, 3, 3,
            3, testUserResourceId, testUserResourceId,
            testUserResourceId, 1, 7,
            4, 10, 2,
            8, 3, 11,
            6, 6, testUserResourceId,
            12,
    };
    final static String[] slotNames = new String[]{
            "Ähm", "ßom", "Dune",
            "Delirium", "name5", "name6",
            "Goodness", "name8", "name9",
            "DBK", "ZKB", "Credit Suisse",
            "Limmat", "Best Time", "Worst Time",
            "Lost Time", "Bad Time", "Good Spirit",
            "After Workout", "Show Time", "Inspirational",
            "ENTIRE DAY", "ENTIRE NEXT DAY", "Chrisis",
            "Architecture",
    };

    final Boolean[] excludes = new Boolean[]{
            false, false, false,
            false, false, false,
            false, false, true,
            false, true, true,
            true, false, false,
            false, false, false,
            false, false, false,
            false, false, false,
            false,
    };

    final Date[] slotBeginTimestamps = new Date[]{
            createTimestamp("08:00:00", now),
            createTimestamp("13:00:00", now),
            createTimestamp("11:00:00", nowPlusOneDays),

            createTimestamp("11:00:00", now),
            createTimestamp("13:00:00", now),
            createTimestamp("13:45:00", nowPlusOneDays),

            createTimestamp("13:00:00", nowPlusTwoDays),
            createTimestamp("13:00:00", nowMinusOneDays),
            createTimestamp("14:00:00", nowMinusOneDays),

            createTimestamp("12:30:00", now),
            createTimestamp("09:30:00", now),
            createTimestamp("14:30:00", now),

            createTimestamp("12:30:00", nowPlusOneDays),
            createTimestamp("14:00:00", nowMinusOneDays),
            createTimestamp("15:00:00", nowMinusOneDays),

            createTimestamp("16:00:00", nowMinusOneDays),
            createTimestamp("17:00:00", nowMinusOneDays),
            createTimestamp("18:00:00", nowMinusOneDays),

            createTimestamp("19:00:00", nowMinusOneDays),
            createTimestamp("20:00:00", nowMinusOneDays),
            createTimestamp("21:00:00", nowMinusOneDays),

            createTimestamp("00:00:00", now),
            createTimestamp("00:00:00", nowPlusOneDays),
            createTimestamp("20:00:00", nowPlusOneDays),

            createTimestamp("18:00:00", nowPlusOneDays),
    };

    final Date[] slotEndTimestamps = new Date[]{
            createTimestamp("12:30:00", now),
            createTimestamp("17:45:00", now),
            createTimestamp("17:30:00", nowPlusOneDays),

            createTimestamp("15:30:00", now),
            createTimestamp("15:30:00", now),
            createTimestamp("15:30:00", nowPlusOneDays),

            createTimestamp("15:30:00", nowPlusTwoDays),
            createTimestamp("17:30:00", nowMinusOneDays),
            createTimestamp("15:30:00", nowMinusOneDays),

            createTimestamp("19:30:00", now),
            createTimestamp("10:30:00", now),
            createTimestamp("15:45:00", now),

            createTimestamp("14:00:00", nowPlusOneDays),
            createTimestamp("17:00:00", nowMinusOneDays),
            createTimestamp("18:00:00", nowMinusOneDays),

            createTimestamp("19:00:00", nowMinusOneDays),
            createTimestamp("20:00:00", nowMinusOneDays),
            createTimestamp("21:00:00", nowMinusOneDays),

            createTimestamp("22:00:00", nowMinusOneDays),
            createTimestamp("23:00:00", nowMinusOneDays),
            createTimestamp("00:00:00", now),

            createTimestamp("00:00:00", nowPlusOneDays),
            createTimestamp("00:00:00", nowPlusTwoDays),
            createTimestamp("05:00:00", nowPlusTwoDays),

            createTimestamp("07:00:00", nowPlusTwoDays),
    };

    // slot: remove
    final static int[] slotToRemove = new int[]{7,};

    Date convertStringToDate(String date, SimpleDateFormat format) {
        try {
            return format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    Date createTimestamp(String time, Date date) {
        final Date timeDate = convertStringToDate(time, timeFormat);
        return new Date(
                Date.UTC(
                        date.getYear(),
                        date.getMonth(),
                        date.getDate(),
                        timeDate.getHours(),
                        timeDate.getMinutes(),
                        timeDate.getSeconds()
                )
        );
    }

    // session&booking (session data): populate
    final Integer[] bookingUsageOwnerIds = new Integer[]{
            phoneTrainerUserId, phoneTrainerUserId, phoneTrainerUserId,
            phoneTrainerUserId, phoneTrainerUserId, phoneTrainerUserId,
            phoneTrainerUserId, phoneTrainerUserId, phoneTrainerUserId,
            phoneTrainerUserId, phoneTrainerUserId, phoneTrainerUserId,
            phoneTrainerUserId, phoneTrainerUserId, phoneTrainerUserId,
            phoneTrainerUserId, phoneTrainerUserId, phoneTrainerUserId,
            phoneTrainerUserId, phoneTrainerUserId, phoneTrainerUserId,
            phoneTrainerUserId,
    };

    final Integer[] bookingArticleIds = new Integer[]{
            phoneTrainingArticleId, phoneTrainingArticleId, phoneTrainingArticleId,
            phoneTrainingArticleId, phoneTrainingArticleId, phoneTrainingArticleId,
            phoneTrainingArticleId, phoneTrainingArticleId, phoneTrainingArticleId,
            phoneTrainingArticleId, phoneTrainingArticleId, phoneTrainingArticleId,
            phoneTrainingArticleId, phoneTrainingArticleId, phoneTrainingArticleId,
            phoneTrainingArticleId, phoneTrainingArticleId, phoneTrainingArticleId,
            phoneTrainingArticleId, phoneTrainingArticleId, phoneTrainingArticleId,
            phoneTrainingArticleId,
    };

    final Integer[] bookingProductIds = new Integer[]{
            phoneTrainingProductId, phoneTrainingProductId, phoneTrainingProductId,
            phoneTrainingProductId, phoneTrainingProductId, phoneTrainingProductId,
            phoneTrainingProductId, phoneTrainingProductId, phoneTrainingProductId,
            phoneTrainingProductId, phoneTrainingProductId, phoneTrainingProductId,
            phoneTrainingProductId, phoneTrainingProductId, phoneTrainingProductId,
            phoneTrainingProductId, phoneTrainingProductId, phoneTrainingProductId,
            phoneTrainingProductId, phoneTrainingProductId, phoneTrainingProductId,
            phoneTrainingProductId,
    };

    final Integer[] resourceIdsOfSessions = new Integer[]{
            testUserResourceId, testUserResourceId, 3,
            3, 3, 3,
            2, 4, 4,
            3, 1, 7,
            4, 10, 2,
            8, 3, 11,
            11, 3, 11,
            testUserResourceId,
    };

    final String[] sessionTitles = new String[]{
            "PT: Advanced English", "PT: Business English", "Voyager",
            "PT", "About Zurich", "About Munich",
            "Hotels in ZRH", "World Cities", "City Ranking",
            "Lake Zurich", "Wall Street", "Main Street",
            "Restaurant", "Entropy", "Swiss Culture",
            "Swiss Mentality", "Living in the Cloud", "SaaS",
            "SCRUM", "TDD", "Agility",
            "Organization",
    };


    final Date[] sessionBeginTimestamps = new Date[]{
            createTimestamp("08:30:00", now),
            createTimestamp("13:00:00", now),
            createTimestamp("13:40:00", now),

            createTimestamp("16:10:00", now),
            createTimestamp("14:45:45", nowPlusOneDays),
            createTimestamp("16:15:11", nowMinusOneDays),

            createTimestamp("11:15:00", now),
            createTimestamp("14:45:00", now),
            createTimestamp("13:15:00", now),

            createTimestamp("19:00:00", now),

            createTimestamp("14:30:00", nowMinusOneDays),
            createTimestamp("15:30:00", nowMinusOneDays),

            createTimestamp("16:30:00", nowMinusOneDays),
            createTimestamp("17:30:00", nowMinusOneDays),
            createTimestamp("18:30:00", nowMinusOneDays),

            createTimestamp("19:30:00", nowMinusOneDays),
            createTimestamp("20:30:00", nowMinusOneDays),
            createTimestamp("21:00:00", nowMinusOneDays),

            createTimestamp("21:45:00", nowMinusOneDays),
            createTimestamp("22:30:00", nowMinusOneDays),
            createTimestamp("23:30:00", nowMinusOneDays),

            createTimestamp("11:30:00", nowPlusOneDays),
    };

    final Date[] sessionEndTimestamps = new Date[]{
            createTimestamp("09:00:00", now),
            createTimestamp("13:30:00", now),
            createTimestamp("14:10:00", now),

            createTimestamp("16:40:00", now),
            createTimestamp("15:15:42", nowPlusOneDays),
            createTimestamp("16:45:22", nowMinusOneDays),

            createTimestamp("11:45:00", now),
            createTimestamp("15:15:00", now),
            createTimestamp("13:45:00", now),

            createTimestamp("19:30:00", now),
            createTimestamp("15:00:00", nowMinusOneDays),
            createTimestamp("16:00:00", nowMinusOneDays),

            createTimestamp("17:00:00", nowMinusOneDays),
            createTimestamp("18:00:00", nowMinusOneDays),
            createTimestamp("19:00:00", nowMinusOneDays),

            createTimestamp("20:00:00", nowMinusOneDays),
            createTimestamp("21:00:00", nowMinusOneDays),
            createTimestamp("21:30:00", nowMinusOneDays),

            createTimestamp("22:15:00", nowMinusOneDays),
            createTimestamp("23:00:00", nowMinusOneDays),
            createTimestamp("00:00:00", now),

            createTimestamp("12:00:00", nowPlusOneDays),
    };

    // create&validate sessions&bookings
    final Integer[] bookingUsageOwnerIdsForValidation = new Integer[]{
            phoneTrainerUserId, phoneTrainerUserId, phoneTrainerUserId,
            phoneTrainerUserId, phoneTrainerUserId, phoneTrainerUserId,
            phoneTrainerUserId, phoneTrainerUserId, phoneTrainerUserId,
            phoneTrainerUserId, phoneTrainerUserId, phoneTrainerUserId,
    };

    final Integer[] bookingArticleIdsForValidation = new Integer[]{
            phoneTrainingArticleId, phoneTrainingArticleId, phoneTrainingArticleId,
            phoneTrainingArticleId, phoneTrainingArticleId, phoneTrainingArticleId,
            phoneTrainingArticleId, phoneTrainingArticleId, phoneTrainingArticleId,
            phoneTrainingArticleId, phoneTrainingArticleId, phoneTrainingArticleId,
    };

    final Integer[] bookingProductIdsForValidation = new Integer[]{
            phoneTrainingProductId, phoneTrainingProductId, phoneTrainingProductId,
            phoneTrainingProductId, phoneTrainingProductId, phoneTrainingProductId,
            phoneTrainingProductId, phoneTrainingProductId, phoneTrainingProductId,
            phoneTrainingProductId, phoneTrainingProductId, phoneTrainingProductId,
    };

    final Integer[] resourceIdsOfSessionsForValidation = new Integer[]{
            testUserResourceId, testUserResourceId,
            testUserResourceId, testUserResourceId, testUserResourceId,
            testUserResourceId, 12, 12,
            12, 12, 12,
            12,
    };

    final String[] sessionTitlesForValidation = new String[]{
            "Örlikon", "The Econimist",
            "WSJ", "Handelsblatt", "Garmisch",
            "Sochi", "Frankfurt", "Vancouver",
            "Aesthetics", "Athletics", "Jogging",
            "Sauna",
    };

    final Date[] sessionBeginTimestampsForValidation = new Date[]{
            createTimestamp("22:00:00", nowPlusOneDays),
            createTimestamp("22:30:00", nowPlusOneDays),

            createTimestamp("23:00:00", nowPlusOneDays),
            createTimestamp("23:30:00", nowPlusOneDays),
            createTimestamp("00:00:00", nowPlusTwoDays),

            createTimestamp("00:30:00", nowPlusTwoDays),
            createTimestamp("22:30:00", nowPlusOneDays),
            createTimestamp("23:00:00", nowPlusOneDays),

            createTimestamp("23:30:00", nowPlusOneDays),
            createTimestamp("00:00:00", nowPlusTwoDays),
            createTimestamp("00:30:00", nowPlusTwoDays),

            createTimestamp("01:00:00", nowPlusTwoDays),
    };

    final Date[] sessionEndTimestampsForValidation = new Date[]{
            createTimestamp("22:30:00", nowPlusOneDays),
            createTimestamp("23:00:00", nowPlusOneDays),

            createTimestamp("23:30:00", nowPlusOneDays),
            createTimestamp("00:00:00", nowPlusTwoDays),
            createTimestamp("00:30:00", nowPlusTwoDays),

            createTimestamp("01:00:00", nowPlusTwoDays),
            createTimestamp("23:00:00", nowPlusOneDays),
            createTimestamp("23:30:00", nowPlusOneDays),

            createTimestamp("00:00:00", nowPlusTwoDays),
            createTimestamp("00:30:00", nowPlusTwoDays),
            createTimestamp("01:00:00", nowPlusTwoDays),

            createTimestamp("01:30:00", nowPlusTwoDays),
    };


    // session&booking (booking data): populate
    int[] bookingUserIds = new int[]{
            testUserId, 1, 2,
            3, testUserId, testUserId,
            testUserId, 1, 1,
            testUserId, 2, 3,
            4, 5, 8,
            9, 10, testUserId,
            testUserId, 4, testUserId,
            testUserId,
    };
    int[] bookingUserIdsForValidation = new int[]{
            testUserId, testUserId,
            testUserId, testUserId, testUserId,
            testUserId, phoneTrainerUserId, phoneTrainerUserId,
            phoneTrainerUserId, phoneTrainerUserId, phoneTrainerUserId,
            phoneTrainerUserId,
    };
    String[] sessionPhone = new String[]{
            "+128 256 512-1024", "+128 256 512-1024", "+1 256 512-1024",
            testUserPhone, "+1 2 512-1024", testUserPhone,
            testUserPhone, "+1 25 5121024", "+1 256 5-12-10-24",
            testUserPhone, "+28 6 5121024", "+01 256 5-12-10-24",
            "+128 256 512-1024", "+128 256 512-1024", "+128 256 512-1024",
            "+128 256 512-1024", "+128 256 512-1024", "+128 256 512-1024",
            "+128 256 512-1024", testUserPhone, "+128 256 512-1024",
            "+023 2 3",
    };
    String[] sessionPhoneForValidation = new String[]{
            "+128 256 5-1", "+128 256 1-1-1",
            "+002 2 512", "+0 2 5", "+06 0 123",
            "+007 0 123", testUserPhone, testUserPhone,
            testUserPhone, testUserPhone, testUserPhone,
            testUserPhone,
    };

    // end for session&booking data generation

    // begin: VC Session generation

    final Date[] vcBeginTimes = {
            createTimestamp("22:00:00", now),
    };
    final Date[] vcEndTimes = {
            createTimestamp("23:00:00", now),

    };
    final Integer[] vcUsageOwnerIds = {
            phoneTrainerUserId,

    };
    final Integer[] vcResourceIds = {
            testUserResourceId,
    };
    // end: VC Session geenration

    // session-to-booking-mapping read check
    int[] sessionIdsToRetrieveCorrespondingBookingsFor = new int[]{
            1, 2, 3,
            4, 5, 6,
            7, 8, 9,
            10,
    };

    String[] userNames = {
            "Tom.test", "Seven.test",
            "BElana.test", "Neelix.test", "Kathryn.test",
            "Doctor.test", "Tuvok.test", "Chakotay.test",
            "Harry.test", "Kes.test",
    };


    // these IDs have to be reset on each data import from the live database:
    //      this insanity is due to the underlying in-house test infrastructure

    // populate Users
    int tomTestUser = 512778;
    int sevenTestUser = 512793;
    int belannaTestUser = 512847;

    int neelixTestUser = 512807;
    int kathrynTestUser = 512808;
    int doctorTestUser = 512809;

    int tuvokTestUser = 512810;
    int chakotayTestUser = 512811;
    int harryTestUser = 512812;

    int kesTestUser = 512813;

    Integer[] userIds = {
            tomTestUser, sevenTestUser,
            belannaTestUser, neelixTestUser, kathrynTestUser,
            doctorTestUser, tuvokTestUser, chakotayTestUser,
            harryTestUser, kesTestUser,
    };

    // resource: populate
    final Integer[] userIdsResource = {
            testUserId, tomTestUser, sevenTestUser,
            belannaTestUser, neelixTestUser, kathrynTestUser,
            doctorTestUser, tuvokTestUser, chakotayTestUser,
            harryTestUser, kesTestUser, phoneTrainerUserId,
    };


    final List<Set<Integer>> resourceLanguages = new ArrayList<Set<Integer>>();
    final int deLangId = 1;
    final int enLangId = 2;
    final int frLangId = 5;
    final int ruLangId = 11;

    {
        Set<Integer> resource = new HashSet<Integer>();
        {
            resource.add(deLangId);
            resource.add(enLangId);
            resource.add(ruLangId);
        }
        Set<Integer> resource1 = new HashSet<Integer>();
        {
            resource1.add(deLangId);
            resource1.add(enLangId);
        }
        Set<Integer> resource2 = new HashSet<Integer>();
        {
            resource2.add(ruLangId);
        }
        Set<Integer> resource3 = new HashSet<Integer>();
        {
            resource3.add(deLangId);
        }
        Set<Integer> resource4 = new HashSet<Integer>();
        {
            resource4.add(enLangId);
        }
        Set<Integer> resource5 = new HashSet<Integer>();
        {
            resource5.add(frLangId);
        }
        Set<Integer> resource6 = new HashSet<Integer>();
        {
            resource6.add(frLangId);
            resource6.add(enLangId);
        }
        Set<Integer> resource7 = new HashSet<Integer>();
        {
            resource7.add(enLangId);
        }
        Set<Integer> resource8 = new HashSet<Integer>();
        {
            resource8.add(enLangId);
        }
        Set<Integer> resource9 = new HashSet<Integer>();
        {
            resource9.add(enLangId);
        }
        Set<Integer> resource10 = new HashSet<Integer>();
        {
            resource10.add(enLangId);
        }
        Set<Integer> resource11 = new HashSet<Integer>();
        {
            resource11.add(enLangId);
        }
        Set<Integer> resource12 = new HashSet<Integer>();
        {
            resource12.add(frLangId);
        }

        {
            resourceLanguages.add(resource);
            resourceLanguages.add(resource1);
            resourceLanguages.add(resource2);
            resourceLanguages.add(resource3);
            resourceLanguages.add(resource4);
            resourceLanguages.add(resource5);
            resourceLanguages.add(resource6);
            resourceLanguages.add(resource7);
            resourceLanguages.add(resource8);
            resourceLanguages.add(resource9);
            resourceLanguages.add(resource10);
            resourceLanguages.add(resource11);
            resourceLanguages.add(resource12);
        }
    }

    public static Date getUpperDateBoundary(final Date date) {
        final Date upperBoundary = new Date(date.getTime());
        upperBoundary.setHours(23);
        upperBoundary.setMinutes(59);
        upperBoundary.setSeconds(59);

        return upperBoundary;
    }

    public static Date getLowerDateBoundary(final Date date) {
        final Date lowerBoundary = new Date(date.getTime());
        lowerBoundary.setHours(0);
        lowerBoundary.setMinutes(0);
        lowerBoundary.setSeconds(0);

        return lowerBoundary;
    }

    public static Integer getUserId(String userName) {
        String getUserId = "SELECT u.ID FROM USERS u WHERE u.USER_NAME = '" + userName + "'";
        final EntityManager em = Datastore.getEMF().createEntityManager();
        try {
            return (Integer) em.createNativeQuery(getUserId).getResultList().get(0);
        } catch (Exception e) {
            System.err.println("e = " + e);
        } finally {
            em.close();
        }
        return 1;
    }
}
