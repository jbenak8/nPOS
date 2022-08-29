package cz.jbenak.npos.api.shared;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

/**
 * Utilities class for several usages accross the project.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2022-08-29
 */
public class Utils {

    /**
     * Utility method for encryption and decryption mainly configuration properties. But it can be used everywhere.
     *
     * @return String encryptor for encryption and decryption.
     */
    public final StringEncryptor getStringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword("PosC_srv1369a,82");
        config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        return encryptor;
    }
}
