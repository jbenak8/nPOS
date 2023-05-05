package cz.jbenak.npos.communicator.configuration;

import cz.jbenak.npos.api.shared.Utils;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Jasypt encryptor used for connections.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2022-08-10
 */

@Configuration
public class JasyptConfig {

    @Bean(name = "propsEncryptorBean")
    public StringEncryptor stringEncryptor() {
        Utils apiUtilities = new Utils();
        return apiUtilities.getStringEncryptor();
    }
}
