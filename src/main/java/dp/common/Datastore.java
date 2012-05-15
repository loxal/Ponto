/*
 * Copyright 2012 Alexander Orlov <alexander.orlov@loxal.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dp.common;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            Logger.getLogger("Datastore").log(Level.SEVERE, "[ROLLBACK]");
        }
    }

    public static void setAppEnv() {
        puConfig.putAll(cfg.getPuConfig());
    }

    private Datastore() {
    }
}

