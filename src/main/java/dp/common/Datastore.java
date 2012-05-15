/*
 * Copyright 2012 digital publishing AG. All rights reserved.
 */

package dp.common;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Map;

/**
 * @author Alexander Orlov <a.orlov@digitalpublishing.de>
 */
public final class Datastore {
    private static final Cfg cfg = new Cfg();
    private static final String puHandle = cfg.getPuHandle();
    private static final Map<String, Object> puConfig = cfg.getPuConfig();

    private static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory(puHandle, puConfig);

    public static EntityManagerFactory getEMF() {
        return EMF;
    }

    public static void rollbackIfStillActive(final EntityTransaction tx) {
        if (tx.isActive()) {
            tx.rollback();
            System.err.println("[ROLLBACK]");
        }
    }

    public static void addResetFlag() {
        final String dropAndCreateTablesKey = "eclipselink.ddl-generation";
        final String dropAndCreateTablesDeclaration = "drop-and-create-tables";
//        NTH provide this via a system property?
        puConfig.put(dropAndCreateTablesKey, dropAndCreateTablesDeclaration);
    }

    public static void setAppEnv() {
        puConfig.putAll(cfg.getPuConfig());
    }

    private Datastore() {
    }
}

