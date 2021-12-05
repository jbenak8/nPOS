package cz.jbenak.npos.pos.system.util;

import java.util.concurrent.ThreadFactory;

/**
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-01-23
 * <p>
 * Třída pro inicializaci vláken pro spouštění procesů jako démonů.
 */
public class DaemonThreadFactory implements ThreadFactory {

    /**
     * Metoda pro výprbu procesu démona, který bude provádět činnost na pozadí.
     *
     * @param r vlastní proces, který má být spuštěn jako démon
     * @return proces démona dané služby
     */
    @Override
    public Thread newThread(Runnable r) {
        Thread demon = new Thread(r);
        demon.setName("UkolovyDemon");
        demon.setDaemon(true);
        return demon;
    }
}
