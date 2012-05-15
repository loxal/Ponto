/*
 * Copyright 2012 digital publishing AG. All rights reserved.
 */

package dp.verp.persistence.controller;

import dp.common.Datastore;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 * @author Alexander Orlov <a.orlov@digitalpublishing.de>
 */
public class InitDatabaseFirebird extends Data {
    @Before
    public void setUp() throws Exception {
        Datastore.setAppEnv();
        Datastore.addResetFlag();
    }

    public int deleteTable(final String table) {
        final EntityManager em = Datastore.getEMF().createEntityManager();
        try {
            final EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                final int deletedCount = em.createNativeQuery("DELETE FROM " + table).executeUpdate();
                tx.commit();

                return deletedCount;
            } finally {
                Datastore.rollbackIfStillActive(tx);
            }
        } finally {
            em.close();
        }
    }

    public void injectRawSQL(final String rawSQL) {
        final EntityManager em = Datastore.getEMF().createEntityManager();
        try {
            final EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                em.createNativeQuery(rawSQL).executeUpdate();
                tx.commit();
            } finally {
                Datastore.rollbackIfStillActive(tx);
            }
        } finally {
            em.close();
        }
    }

    public void rawSQLQuery(final String rawSQL) {
        final EntityManager em = Datastore.getEMF().createEntityManager();
        try {
            final EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                em.createNativeQuery(rawSQL).getResultList();
                tx.commit();
            } finally {
                Datastore.rollbackIfStillActive(tx);
            }
        } finally {
            em.close();
        }
    }

    //    @Test
    public void deleteUsers() throws Exception {
        String deleteFromSqlStatement = "; delete from users where id =";
        String deleteUsers = "select rdb$set_context('USER_SESSION', 'SESSION_ID', (select session_id from login_plain('Administrator', 'RosenHeim112'))) from rdb$database; " +
                deleteFromSqlStatement + tomTestUser +
                deleteFromSqlStatement + sevenTestUser +
                deleteFromSqlStatement + belannaTestUser +
                deleteFromSqlStatement + neelixTestUser +
                deleteFromSqlStatement + kathrynTestUser +
                deleteFromSqlStatement + doctorTestUser +
                deleteFromSqlStatement + tuvokTestUser +
                deleteFromSqlStatement + chakotayTestUser +
                deleteFromSqlStatement + harryTestUser +
                deleteFromSqlStatement + kesTestUser + ";";
        injectRawSQL(deleteUsers);
    }

    @Test
    public void deleteFromTable() throws Exception {
        final String[] tables = new String[]{
                "BOOKINGS",
                "SESSIONS",
                "REPORTS",
                "SLOTS",
                "RESOURCES_LANGUAGES",
                "RESOURCES",
        };
        for (final String table : tables) {
            System.out.println("ec.deleteTable(" + table + ") = " + deleteTable(table));
        }
    }

    //    @Test
    public void injectBookingTrigger() throws Exception {
        injectRawSQL("CREATE OR ALTER TRIGGER PT_BOOKING FOR BOOKINGS\n" +
                "ACTIVE AFTER INSERT POSITION 0\n" +
                "AS\n" +
                "  DECLARE VARIABLE BEGIN_TIMESTAMP TIMESTAMP;\n" +
                "  DECLARE VARIABLE USER_ID INTEGER;\n" +
                "BEGIN\n" +
                "  SELECT\n" +
                "    SE.BEGIN_TIMESTAMP,\n" +
                "    RE.USER_ID\n" +
                "  FROM SESSIONS SE, RESOURCES RE\n" +
                "  WHERE (SE.ID = NEW.SESSION_ID) AND (SE.RESOURCE_ID = RE.ID)\n" +
                "  INTO\n" +
                "    :BEGIN_TIMESTAMP,\n" +
                "    :USER_ID;\n" +
                " \n" +
                "  INSERT INTO VW_PT_USAGE_EVENTS (\n" +
                "    EVENT_TYPE_ID,\n" +
                "    USAGE_OWNER_ID,\n" +
                "    ARTICLE_ID,\n" +
                "    PRODUCT_ID,\n" +
                "    TRAINER_ID,\n" +
                "    APPOINTMENT_DATE\n" +
                "  )\n" +
                "  VALUES (\n" +
                "    15,\n" +
                "    NEW.USAGE_OWNER_ID,\n" +
                "    NEW.ARTICLE_ID,\n" +
                "    NEW.PRODUCT_ID,\n" +
                "    :USER_ID,\n" +
                "    :BEGIN_TIMESTAMP\n" +
                "  );\n" +
                "END");
    }

    //    @Test
    public void injectReportTrigger() throws Exception {
        injectRawSQL("CREATE OR ALTER TRIGGER REPORTS_AI0 FOR REPORTS\n" +
                "ACTIVE AFTER INSERT POSITION 0\n" +
                "AS\n" +
                "  DECLARE VARIABLE USAGE_OWNER_ID INTEGER;\n" +
                "  DECLARE VARIABLE ARTICLE_ID INTEGER;\n" +
                "  DECLARE VARIABLE PRODUCT_ID INTEGER;\n" +
                "  DECLARE VARIABLE BEGIN_TIMESTAMP TIMESTAMP;\n" +
                "  DECLARE VARIABLE USER_ID INTEGER;\n" +
                "  DECLARE VARIABLE EVENT_TYPE_ID INTEGER;\n" +
                "BEGIN\n" +
                "  SELECT\n" +
                "    BO.USAGE_OWNER_ID,\n" +
                "    BO.ARTICLE_ID,\n" +
                "    BO.PRODUCT_ID\n" +
                "  FROM BOOKINGS BO\n" +
                "  WHERE (BO.SESSION_ID = NEW.SESSION_ID)\n" +
                "  INTO\n" +
                "    :USAGE_OWNER_ID,\n" +
                "    :ARTICLE_ID,\n" +
                "    :PRODUCT_ID;\n" +
                " \n" +
                "  SELECT\n" +
                "    SE.BEGIN_TIMESTAMP,\n" +
                "    RE.USER_ID\n" +
                "  FROM RESOURCES RE, SESSIONS SE\n" +
                "  WHERE (SE.ID = NEW.SESSION_ID) AND (RE.ID = SE.RESOURCE_ID)\n" +
                "  INTO\n" +
                "    :BEGIN_TIMESTAMP,\n" +
                "    :USER_ID;\n" +
                " \n" +
                "  IF (NEW.STATUS = 2) THEN\n" +
                "    EVENT_TYPE_ID = 21;\n" +
                "  ELSE\n" +
                "    EVENT_TYPE_ID = 17;\n" +
                " \n" +
                "  INSERT INTO VW_PT_USAGE_EVENTS (\n" +
                "    EVENT_TYPE_ID,\n" +
                "    USAGE_OWNER_ID,\n" +
                "    ARTICLE_ID,\n" +
                "    PRODUCT_ID,\n" +
                "    TRAINER_ID,\n" +
                "    APPOINTMENT_DATE,\n" +
                "    SOUND_QUALITY,\n" +
                "    TEMPLATE_USED\n" +
                "  )\n" +
                "  VALUES (\n" +
                "    :EVENT_TYPE_ID,\n" +
                "    :USAGE_OWNER_ID,\n" +
                "    :ARTICLE_ID,\n" +
                "    :PRODUCT_ID,\n" +
                "    :USER_ID,\n" +
                "    :BEGIN_TIMESTAMP,\n" +
                "    NEW.CALLQUALITY,\n" +
                "    LEFT(NEW.TEMPLATE, 120)\n" +
                "  );\n" +
                "END\n" +
                "^\n" +
                " \n" +
                " \n" +
                "/* Trigger: REPORTS_AU0 */\n" +
                "CREATE OR ALTER TRIGGER REPORTS_AU0 FOR REPORTS\n" +
                "ACTIVE AFTER UPDATE POSITION 0\n" +
                "AS\n" +
                "  DECLARE VARIABLE EVENT_ID INTEGER;\n" +
                "  DECLARE VARIABLE EVENT_TYPE_ID INTEGER;\n" +
                "BEGIN\n" +
                "  SELECT\n" +
                "    VPT.ID\n" +
                "  FROM VW_PT_USAGE_EVENTS VPT, BOOKINGS BO, SESSIONS SE\n" +
                "  WHERE (BO.SESSION_ID = NEW.SESSION_ID) AND\n" +
                "    (SE.ID =  NEW.SESSION_ID) AND\n" +
                "    (VPT.EVENT_TYPE_ID in (17,21)) AND\n" +
                "    (VPT.USAGE_OWNER_ID = BO.USAGE_OWNER_ID) AND\n" +
                "    (VPT.ARTICLE_ID = BO.ARTICLE_ID) AND\n" +
                "    (VPT.PRODUCT_ID = BO.PRODUCT_ID) AND\n" +
                "    (VPT.APPOINTMENT_DATE = SE.BEGIN_TIMESTAMP)\n" +
                "  INTO\n" +
                "    :EVENT_ID;\n" +
                " \n" +
                "  IF (NEW.STATUS = 2) THEN\n" +
                "    EVENT_TYPE_ID = 21;\n" +
                "  ELSE\n" +
                "    EVENT_TYPE_ID = 17;\n" +
                " \n" +
                "  UPDATE EVENTS SET\n" +
                "    EVENT_TYPE_ID = :EVENT_TYPE_ID\n" +
                "  WHERE ID = :EVENT_ID;\n" +
                " \n" +
                "  UPDATE PT_USAGE_EVENTS SET\n" +
                "    SOUND_QUALITY = NEW.CALLQUALITY,\n" +
                "    TEMPLATE_USED = LEFT(NEW.TEMPLATE, 120)\n" +
                "  WHERE ID = :EVENT_ID;\n" +
                "END");
    }

    //    @Test
    public void injectBookingCancellationTrigger() throws Exception {
        injectRawSQL("CREATE OR ALTER TRIGGER PT_CANCELLATION FOR BOOKINGS\n" +
                "ACTIVE AFTER UPDATE POSITION 0\n" +
                "AS\n" +
                "  DECLARE VARIABLE BEGIN_TIMESTAMP TIMESTAMP;\n" +
                "  DECLARE VARIABLE USER_ID INTEGER;\n" +
                "BEGIN\n" +
                "  IF ((OLD.CANCEL_DATE IS NULL) AND (NEW.CANCEL_DATE IS NOT NULL)) THEN\n" +
                "  BEGIN\n" +
                "    SELECT\n" +
                "      SE.BEGIN_TIMESTAMP,\n" +
                "      RE.USER_ID\n" +
                "    FROM SESSIONS SE, RESOURCES RE\n" +
                "    WHERE (SE.ID = OLD.SESSION_ID) AND (SE.RESOURCE_ID = RE.ID)\n" +
                "    INTO\n" +
                "      :BEGIN_TIMESTAMP,\n" +
                "      :USER_ID;\n" +
                " \n" +
                "    INSERT INTO VW_PT_USAGE_EVENTS (\n" +
                "      EVENT_TYPE_ID,\n" +
                "      USAGE_OWNER_ID,\n" +
                "      ARTICLE_ID,\n" +
                "      PRODUCT_ID,\n" +
                "      TRAINER_ID,\n" +
                "      APPOINTMENT_DATE\n" +
                "    )\n" +
                "    VALUES (\n" +
                "      16,\n" +
                "      OLD.USAGE_OWNER_ID,\n" +
                "      OLD.ARTICLE_ID,\n" +
                "      OLD.PRODUCT_ID,\n" +
                "      :USER_ID,\n" +
                "      :BEGIN_TIMESTAMP\n" +
                "    );\n" +
                "  END\n" +
                "END");
    }

    @Test
    public void createI18nFacetView() throws Exception {
        final String i18nFacetViewName = "VERP_I18N_FACET";
        // remove the table with the same name as the view that has been created through EclipseLink
//        ec.injectRawSQL("DROP TABLE " + i18nFacetViewName);
        final String appTextpoolMacroCtxId = "1008";

        try {
            injectRawSQL("CREATE OR ALTER VIEW " + i18nFacetViewName + " (" +
                    " ID," + // TEXT_MACRO_ID
                    " TRANSLATION," +
                    " LOCALE," +
                    " MACRO_NAME" +
                    " )" +
                    " AS" +
                    " SELECT" +
                    " m.ID," +
                    " tp.TEXT," +
                    " l.CODE," +
                    " m.NAME" +
                    " FROM TEXTPOOL_MACRO_CONTEXT tmc" +
                    " JOIN TEXTPOOL tp ON (tmc.MACRO_ID = tp.TEXTMACRO_ID AND tp.PUBLISHED = 'Y')" +
                    " JOIN TEXTPOOL_MACROS m ON (tmc.MACRO_ID = m.ID)" +
                    " JOIN LANGUAGES l ON (tp.LANG_ID = l.ID)" +
                    " WHERE tmc.CONTEXT_ID = " + appTextpoolMacroCtxId
            );
        } catch (Exception e) {
            injectRawSQL("DROP TABLE " + i18nFacetViewName);
            createI18nFacetView();
        }
    }

    @Test
    public void populateUsers() throws Exception {
        Integer context = -1008;

        for (String userName : userNames) {
            String mail = userName + "@speexx.com";
            String firstName = userName;
            String lastName = userName;
            createUsers(userName, firstName, lastName, mail, context);
        }
    }

    public void createUsers(String userName, String firstName, String lastName, String mail, Integer context) {
        final EntityManager em = Datastore.getEMF().createEntityManager();
        try {
            final EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                rawSQLQuery("select rdb$set_context('USER_SESSION', 'SESSION_ID', (select session_id from login_plain('Administrator', 'RosenHeim112'))) from rdb$database;");
                rawSQLQuery("execute procedure CREATE_USER('" + userName + "', '" + firstName + "', '" + lastName + "', 'description', null, 'customer', 'department', '" + mail + "', '', '', null, null, -2000, " + context + ");");
                tx.commit();

            } catch (Exception e) {
                System.err.println("e = " + e);
            } finally {
                Datastore.rollbackIfStillActive(tx);
            }
        } finally {
            em.close();
        }
    }
}
