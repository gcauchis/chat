package com.gc.common.service.core.crypto.impl;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.crypto.Cipher;

import javolution.util.FastList;

import org.apache.commons.lang.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gc.common.crypto.dal.service.IDekDalService;
import com.gc.common.crypto.dal.service.IKekDalService;
import com.gc.common.dal.exception.DTOAlreadyExistsException;
import com.gc.common.dal.exception.DTONullPropertyException;
import com.gc.common.dto.external.UIPublicKey;
import com.gc.common.exception.ServiceException;
import com.gc.common.exception.ServiceException.EServiceExceptionType;
import com.gc.common.model.Dek;
import com.gc.common.model.Kek;
import com.gc.common.model.ref.EDekAlgo;
import com.gc.common.model.ref.EDekStatus;
import com.gc.common.model.ref.EKekStatus;
import com.gc.common.service.core.crypto.ICryptoService;
import com.gc.common.service.core.crypto.IKeystoreService;

/**
 * CryptoService.
 */
public class CryptoService implements ICryptoService {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CryptoService.class);

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    // PBEWITHSHA256AND128BITAES-CBC-BC
    /** The Constant KEY_ALGORITHM. */
    private static final String KEY_ALGORITHM = "PBEWITHSHA256AND256BITAES-CBC-BC";

    /** The Constant AES_ALGORITHM. */
    private static final String AES_ALGORITHM = "PBEWITHSHA256AND256BITAES-CBC-BC";

    /** The Constant SHA_ALGORITHM. */
    private static final String SHA_ALGORITHM = "PBEWITHSHA256AND256BITAES-CBC-BC";

    /** The Constant RSA_ALGORITHM. */
    private static final String RSA_ALGORITHM = "RSA/None/PKCS1Padding";

    /** The kek dal service. */
    private IKekDalService kekDalService;

    /** The keystore service. */
    private IKeystoreService keystoreService;

    /** The dek dal service. */
    private IDekDalService dekDalService;

    /** The decrypted deks map. */
    private Map < EDekAlgo, String > decryptedDeksMap;

    /** The decrypted data map. */
    private Map < String, String > decryptedDataMap = new ConcurrentHashMap < String, String >();

    /** The encrypted data map. */
    private Map < String, String > encryptedDataMap = new ConcurrentHashMap < String, String >();

    /*
     * (non-Javadoc)
     * @see com.gc.common.service.core.crypto.ICryptoService#encryptData(java.lang.String, com.gc.common.model.ref.EDekAlgo)
     */
    public String encryptData(String data, EDekAlgo algo) {
        return encryptData(data, this.chooseDekForEncryption(algo), this.chooseAlgoForEncryption(algo));
    }

    /*
     * (non-Javadoc)
     * @see com.gc.common.service.core.crypto.ICryptoService#decryptData(java.lang.String, com.gc.common.model.ref.EDekAlgo)
     */
    public String decryptData(String data, EDekAlgo algo) {
        return decryptData(data, this.chooseDekForEncryption(algo), this.chooseAlgoForEncryption(algo));
    }

    /*
     * (non-Javadoc)
     * @see com.gc.common.service.core.crypto.ICryptoService#decryptDataAsymmetric(java.lang.String)
     */
    public String decryptDataAsymmetric(String encryptedData) {
        return decrypteDataAsymmetricHex(encryptedData);
    }

    /*
     * (non-Javadoc)
     * @see com.gc.common.service.core.crypto.ICryptoService#encryptDataAsymmetric(java.lang.String)
     */
    public String encryptDataAsymmetric(String data) {
        return encryptDataAsymmetricHex(data);
    }

    /*
     * (non-Javadoc)
     * @see com.gc.common.service.core.crypto.ICryptoService#loadKey()
     */
    public void loadKey() throws ServiceException, NoSuchAlgorithmException, InvalidKeySpecException {
        getLog().info("load keys");
        // on recupere la kek active
        DetachedCriteria criteria = DetachedCriteria.forClass(Kek.class);
        criteria = criteria.add(Restrictions.eq("status", EKekStatus.ACTIVE));
        List < Kek > kekList = kekDalService.searchByCriteria(criteria);
        if (kekList != null && kekList.size() != 0) {
            Kek activeKek = kekList.get(0);

            // on recupere la liste des dek actives
            Set < Dek > dekList = activeKek.getDeks();
            if (dekList.size() != 0) {
                List < Dek > activeDekList = new FastList < Dek >();
                Iterator < Dek > dekIterator = dekList.iterator();
                while (dekIterator.hasNext()) {
                    Dek tmpDek = dekIterator.next();
                    if (tmpDek.getStatus().equals(EDekStatus.ACTIVE)) {
                        activeDekList.add(tmpDek);
                    }
                }

                // on decrypte la kek avec la cle privee du certificat
                RSAPrivateKey pk;
                try {
                    pk = (RSAPrivateKey) keystoreService.getPrivateKey();
                } catch (Exception e) {
                    throw new ServiceException(ServiceException.NO_METHOD_MESSAGE, e, EServiceExceptionType.CRYPTO_ERROR);
                }

                String decryptedKek = this.decryptData(activeKek.getData(), pk.getPrivateExponent().toString(), KEY_ALGORITHM);

                // pour chaque dek active :
                // * on decrypt la dek avec la kek
                // * on stocke la dek (mapkey == dek.algo)
                Iterator < Dek > activeDekIterator = activeDekList.iterator();
                decryptedDeksMap = new HashMap < EDekAlgo, String >();

                while (activeDekIterator.hasNext()) {
                    Dek tmpDek = activeDekIterator.next();
                    String decryptedDek = this.decryptData(tmpDek.getData(), decryptedKek, KEY_ALGORITHM);
                    decryptedDeksMap.put(tmpDek.getAlgo(), decryptedDek);
                }

                decryptedDataMap = new ConcurrentHashMap < String, String >();
                encryptedDataMap = new ConcurrentHashMap < String, String >();
            }
        } else {
            throw new ServiceException("Please init kek and dek !!!", new Exception(), EServiceExceptionType.CRYPTO_ERROR);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.gc.common.service.core.crypto.ICryptoService#initKey()
     */
    public void initKey() throws ServiceException, DTONullPropertyException, DTOAlreadyExistsException, NoSuchAlgorithmException, InvalidKeySpecException {
        getLog().info("initKey");
        createNewKeysWithStatus(EKekStatus.ACTIVE, EDekStatus.ACTIVE);
    }

    /**
     * Encrypt data.
     * 
     * @param data the data
     * @param key the key
     * @param algo the algo
     * @return the string
     */
    private String encryptData(String data, String key, String algo) {
        if (data == null || data.equals("")) {
            return null;
        }

        String encryptedDataKey = data + key + algo;
        if (encryptedDataMap.containsKey(encryptedDataKey)) {
            return encryptedDataMap.get(encryptedDataKey);
        }
        Security.addProvider(new BouncyCastleProvider());
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setProviderName("BC");
        encryptor.setProvider(new BouncyCastleProvider());
        encryptor.setPassword(key);
        encryptor.setAlgorithm(algo);
        String encryptedData = encryptor.encrypt(data);
        encryptedDataMap.put(encryptedDataKey, encryptedData);
        String decryptedDataKey = encryptedData + key + algo;
        if (!decryptedDataMap.containsKey(decryptedDataKey)) {
            decryptedDataMap.put(decryptedDataKey, data);
        }
        return encryptedData;
    }

    /**
     * Decrypt data.
     * 
     * @param data the data
     * @param key the key
     * @param algo the algo
     * @return the string
     */
    private String decryptData(String data, String key, String algo) {
        if (data == null || data.equals("")) {
            return null;
        }
        String decryptedDataKey = data + key + algo;
        if (decryptedDataMap.containsKey(decryptedDataKey)) {
            return decryptedDataMap.get(decryptedDataKey);
        }
        Security.addProvider(new BouncyCastleProvider());
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setProviderName("BC");
        encryptor.setProvider(new BouncyCastleProvider());
        encryptor.setPassword(key);
        encryptor.setAlgorithm(algo);
        String decryptedData = encryptor.decrypt(data);
        decryptedDataMap.put(decryptedDataKey, decryptedData);
        String encryptedDataKey = decryptedData + key + algo;
        if (!encryptedDataMap.containsKey(encryptedDataKey)) {
            encryptedDataMap.put(encryptedDataKey, data);
        }
        return decryptedData;
    }

    /**
     * Choose dek for encryption.
     * 
     * @param algo the algo
     * @return the string
     */
    private String chooseDekForEncryption(EDekAlgo algo) {
        try {
            if (decryptedDeksMap == null) {
                this.loadKey();
            }
        } catch (ServiceException e) {
            getLog().warn("Fail to closed Dek For Encryption.", e);
        } catch (NoSuchAlgorithmException e) {
            getLog().warn("Fail to closed Dek For Encryption.", e);
        } catch (InvalidKeySpecException e) {
            getLog().warn("Fail to closed Dek For Encryption.", e);
        }
        String dek = this.decryptedDeksMap.get(algo);
        return dek;
    }

    /**
     * Choose algo for encryption.
     * 
     * @param algo the algo
     * @return the string
     */
    private String chooseAlgoForEncryption(EDekAlgo algo) {
        if (algo.equals(EDekAlgo.IRREVERSIBLE)) {
            return SHA_ALGORITHM;
        } else if (algo.equals(EDekAlgo.REVERSIBLE)) {
            return AES_ALGORITHM;
        } else {
            return "";
        }
    }

    /**
     * Ecnrypt a String data into an Hexadecimal encoded String containing the crypted data (symmetric cipher).
     * 
     * @param data String data to encrypt
     * @return an Hexadecimal encoded String containing a crypted data
     */
    private String encryptDataAsymmetricHex(String data) {
        return encryptDataAsymmetricHex(data, this.getPublicKey());
    }

    /**
     * Encrypt data asymmetric hex.
     * 
     * @param data the data
     * @param publicKey the public key
     * @param provider the provider
     * @return the string
     */
    public static String encryptDataAsymmetricHex(String data, RSAPublicKey publicKey) {
        if (StringUtils.isEmpty(data)) {
            return null;
        }
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(RSA_ALGORITHM, "BC");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return getHexString(encryptedBytes);
        } catch (ArrayIndexOutOfBoundsException e) {
            LOGGER.info("ArrayIndexOutOfBoundsException: failed to encrypt value, maybe the value is already encrypted");
            return data;
        } catch (Exception e) {
            LOGGER.warn("Fail to encrypt Data Asymmetric.", e);
            return null;
        }
    }

    /**
     * Decrypt an Hexadecimal encoded String containing a crypted data (symmetric cipher).
     * 
     * @param encryptedData Hexadecimal encoded String containing a crypted data
     * @return the data
     */
    private String decrypteDataAsymmetricHex(String encryptedData) {
        if (StringUtils.isEmpty(encryptedData)) {
            return null;
        }
        try {
            byte[] encryptedHexData = hexStringToByteArray(encryptedData);
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM, "BC");
            cipher.init(Cipher.DECRYPT_MODE, this.getPrivateKey());
            byte[] plainText = cipher.doFinal(encryptedHexData);
            return new String(plainText);
        } catch (Exception e) {
            // getLog().warn("Fail to decrypte Data Asymmetric.", e);
            return encryptedData;
        }
    }

    /**
     * Convert an Hexadecimal encoded String into a decoded byte array.
     * 
     * @param s an Hexadecimal encoded String
     * @return a decoded byte array
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * Convert a byte array into an hexadecimal encoded string.
     * 
     * @param b a byte array
     * @return hexadecimal encoded string
     * @throws Exception the exception
     */
    public static String getHexString(byte[] b) throws Exception {
        StringBuffer result = new StringBuffer("");
        for (int i = 0; i < b.length; i++) {
            result.append(Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }

    /**
     * Get the private key contained in the keystore.
     * 
     * @return the private key contained in the keystore
     */
    private PrivateKey getPrivateKey() {
        try {
            return keystoreService.getPrivateKey();
        } catch (Exception e) {
            getLog().warn("Fail to get Private Key.", e);
            return null;
        }
    }

    /**
     * Get the public key contained in the keystore for UI usage.
     * 
     * @return the rsa public key contained in the keystore for UI usage
     */
    public UIPublicKey getUIPublicKey() {
        RSAPublicKey pk = this.getPublicKey();
        if (pk == null)
            return null;
        String modulus = pk.getModulus().toString(16);
        int publicExponent = pk.getPublicExponent().intValue();
        return new UIPublicKey(modulus, publicExponent);
    }

    /**
     * Get the public key contained in the keystore for UI usage.
     * 
     * @return the public key contained in the keystore for UI usage
     */
    private RSAPublicKey getPublicKey() {
        try {
            return keystoreService.getPublicKey();
        } catch (Exception e) {
            getLog().warn("Fail to get Public Key.", e);
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * @see com.gc.common.service.ILoggable#getLog()
     */
    public Logger getLog() {
        return LOGGER;
    }

    /**
     * Gets the kek dal service.
     * 
     * @return the kek dal service
     */
    public IKekDalService getKekDalService() {
        return kekDalService;
    }

    /**
     * Sets the kek dal service.
     * 
     * @param kekDalService the new kek dal service
     */
    public void setKekDalService(IKekDalService kekDalService) {
        this.kekDalService = kekDalService;
    }

    /**
     * Gets the keystore service.
     * 
     * @return the keystore service
     */
    public IKeystoreService getKeystoreService() {
        return keystoreService;
    }

    /**
     * Sets the keystore service.
     * 
     * @param keystoreService the new keystore service
     */
    public void setKeystoreService(IKeystoreService keystoreService) {
        this.keystoreService = keystoreService;
    }

    /**
     * Get an unmodifiable view of decryptedDeksMap.
     * 
     * @return the decryptedDeksMap
     */
    public Map < EDekAlgo, String > getDecryptedDeksMap() {
        return Collections.unmodifiableMap(decryptedDeksMap);
    }

    /*
     * (non-Javadoc)
     * @see com.gc.common.service.core.crypto.ICryptoService#initialized()
     */
    @Override
    public Boolean initialized() {
        return dekDalService.searchFromCriteria(DetachedCriteria.forClass(Dek.class).add(Property.forName("status").eq(EDekStatus.ACTIVE))) != null;
    }

    /**
     * Sets the dek dal service.
     * 
     * @param dekDalService the new dek dal service
     */
    public void setDekDalService(IDekDalService dekDalService) {
        this.dekDalService = dekDalService;
    }

    /*
     * (non-Javadoc)
     * @see com.gc.common.service.core.crypto.ICryptoService#archiveActiveKeysAndActiveCreated()
     */
    @Override
    public void archiveActiveKeysAndActiveCreated() throws ServiceException {
        getLog().info("Archive active Keys and active created.");
        kekDalService.archiveActiveAndActiveCreated();
        dekDalService.archiveActiveAndActiveCreated();

    }

    /*
     * (non-Javadoc)
     * @see com.gc.common.service.core.crypto.ICryptoService#createNewKeys()
     */
    @Override
    public void createNewKeys() throws ServiceException {
        getLog().info("createNewKeys");
        createNewKeysWithStatus(EKekStatus.CREATED, EDekStatus.CREATED);
    }

    /**
     * Creates the new keys with given status status.
     * 
     * @param kekStatus the kek status
     * @param dekStatus the dek status
     * @throws ServiceException the service exception
     * @throws DTONullPropertyException the dTO null property exception
     * @throws DTOAlreadyExistsException the dTO already exists exception
     */
    private void createNewKeysWithStatus(EKekStatus kekStatus, EDekStatus dekStatus) throws ServiceException {
        getLog().info("Create keys with status  [kekStatus = {}, dekStatus = {}]", kekStatus, dekStatus);
        // on genere une kek en random (long), on crypte la kek avec la PrivateKey
        final Date now = new Date();
        Kek kek = new Kek();
        kek.setStatus(kekStatus);
        switch (kekStatus) {
            case ARCHIVED:
            case ACTIVE:
                kek.setActivationDate(now);
            case CREATED:
            default:
                kek.setCreationDate(now);
        }
        Random r = new Random();
        String decryptedKek = "" + r.nextLong();
        RSAPrivateKey pk;
        try {
            pk = (RSAPrivateKey) keystoreService.getPrivateKey();
        } catch (Exception e) {
            throw new ServiceException(ServiceException.NO_METHOD_MESSAGE, e, EServiceExceptionType.CRYPTO_ERROR);
        }

        String encryptedKek = encryptData(decryptedKek, pk.getPrivateExponent().toString(), KEY_ALGORITHM);
        kek.setData(encryptedKek);

        // on genere des dek en random (court), on crypte les dek avec la kek
        // DEK REVERSIBLE
        Dek dekRev = new Dek();
        dekRev.setKek(kek);
        dekRev.setStatus(dekStatus);
        switch (dekStatus) {
            case ARCHIVED:
                dekRev.setInactivationDate(now);
            case ACTIVE:
                dekRev.setActivationDate(now);
            case CREATED:
            default:
                dekRev.setCreationDate(now);
        }
        String decryptedDekRev = "" + r.nextInt();
        String encryptedDek_rev = encryptData(decryptedDekRev, decryptedKek, KEY_ALGORITHM);
        dekRev.setData(encryptedDek_rev);
        dekRev.setAlgo(EDekAlgo.REVERSIBLE);

        // DEK IRREVERSIBLE
        Dek dekIrrev = new Dek();
        dekIrrev.setKek(kek);
        dekIrrev.setStatus(dekStatus);
        switch (dekStatus) {
            case ARCHIVED:
                dekIrrev.setInactivationDate(now);
            case ACTIVE:
                dekIrrev.setActivationDate(now);
            case CREATED:
            default:
                dekIrrev.setCreationDate(now);
        }
        String decryptedDekIrrev = "" + r.nextInt();
        String encryptedDek_irrev = encryptData(decryptedDekIrrev, decryptedKek, KEY_ALGORITHM);
        dekIrrev.setData(encryptedDek_irrev);
        dekIrrev.setAlgo(EDekAlgo.IRREVERSIBLE);

        // on ajoute les dek crypte a la kek crypte dans le Set
        kek.getDeks().add(dekRev);
        kek.getDeks().add(dekIrrev);

        // on sauvegarde la kek (hibernate ce charge de la cascade)
        if (kek != null) {
            try {
                kekDalService.create(kek);
            } catch (DTONullPropertyException e) {
                throw new ServiceException(ServiceException.NO_METHOD_MESSAGE, e, EServiceExceptionType.DATABASE_ERROR);
            } catch (DTOAlreadyExistsException e) {
                throw new ServiceException(ServiceException.NO_METHOD_MESSAGE, e, EServiceExceptionType.DATABASE_ERROR);
            }
        }
    }

}
