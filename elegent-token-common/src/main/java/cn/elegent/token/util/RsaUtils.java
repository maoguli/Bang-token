package cn.elegent.token.util;


import cn.elegent.token.exceptions.TokenException;
import cn.elegent.util.file.FileUtil;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RsaUtils
 *
 * @author: wgl
 * @describe: 非对称加密使用的RSA算法生成一对公私钥
 * @date: 2022/12/28 10:10
 */
public class RsaUtils {

    private static final int DEFAULT_KEY_SIZE = 2048;

    private static final String RESOURCE_PATH = "classPath:";

    /**
     * 从文件中读取公钥
     *
     * @param filename 公钥保存路径，相对于classpath
     * @return  PublicKey 公钥对象
     * @throws Exception
     */
    public static PublicKey getPublicKey(String filename) throws Exception {
        byte[] bytes = readFile (filename);
        return getPublicKey (bytes);
    }

    /**
     * 从文件中读取密钥
     *
     * @param filename 私钥保存路径，相对于classpath
     * @return  PrivateKey 私钥对象
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String filename) throws Exception {
        byte[] bytes = readFile (filename);
        return getPrivateKey (bytes);
    }

    /**
     * 获取公钥
     * 公钥的字节形式。
     * @param bytes 公钥的字节形式
     * @return
     * @throws Exception
     */
    private static PublicKey getPublicKey(byte[] bytes) throws Exception {
        bytes = Base64.getDecoder ( ).decode (bytes);
        X509EncodedKeySpec spec = new X509EncodedKeySpec (bytes);
        KeyFactory factory = KeyFactory.getInstance ("RSA");
        return factory.generatePublic (spec);
    }

    /**
     * 获取密钥
     *
     * @param bytes 私钥的字节形式
     * @return
     * @throws Exception
     */
    private static PrivateKey getPrivateKey(byte[] bytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        bytes = Base64.getDecoder ( ).decode (bytes);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec (bytes);
        KeyFactory factory = KeyFactory.getInstance ("RSA");
        return factory.generatePrivate (spec);
    }

    /**
     * 根据密文，生存rsa公钥和私钥,并写入指定文件
     *
     * @param publicKeyFilename  公钥文件路径
     * @param privateKeyFilename 私钥文件路径
     * @param secret             生成密钥的密文
     */
    public static void generateKey(String publicKeyFilename, String privateKeyFilename, String secret, int keySize) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance ("RSA");
        SecureRandom secureRandom = new SecureRandom (secret.getBytes ( ));
        keyPairGenerator.initialize (Math.max (keySize, DEFAULT_KEY_SIZE), secureRandom);
        KeyPair keyPair = keyPairGenerator.genKeyPair ( );
        // 获取公钥并写出
        byte[] publicKeyBytes = keyPair.getPublic ( ).getEncoded ( );
        publicKeyBytes = Base64.getEncoder ( ).encode (publicKeyBytes);
        writeFile (publicKeyFilename, publicKeyBytes);
        // 获取私钥并写出
        byte[] privateKeyBytes = keyPair.getPrivate ( ).getEncoded ( );
        privateKeyBytes = Base64.getEncoder ( ).encode (privateKeyBytes);
        writeFile (privateKeyFilename, privateKeyBytes);
    }

    private static byte[] readFile(String fileName) throws Exception {
        if(fileName.contains(RESOURCE_PATH)){
            String resourcePathFile = fileName.replaceAll(RESOURCE_PATH, "");
            String str = FileUtil.readToStr(resourcePathFile);
            return str.getBytes(StandardCharsets.UTF_8);
        }else{
            Path path = new File(fileName).toPath();
            return Files.readAllBytes(path);
        }
    }

    private static void writeFile(String destPath, byte[] bytes) throws IOException {
        File dest = new File (destPath);
        if (!dest.exists ( )) {
            dest.createNewFile ( );
        }
        Files.write (dest.toPath ( ), bytes);
    }


    //生成私钥，公钥地址
    private static String privateFilePath = "D:\\key\\id_rsa.key";
    private static String publicFilePath = "D:\\key\\id_rsa_pub.key";

    public static void main(String[] args) throws Exception {
            // 生成密钥对
            RsaUtils.generateKey(publicFilePath, privateFilePath, "hello", 2048);

            // 获取私钥
            PrivateKey privateKey = RsaUtils.getPrivateKey(privateFilePath);
            System.out.println("privateKey = " + privateKey);
            // 获取公钥
            PublicKey publicKey = RsaUtils.getPublicKey(publicFilePath);
            System.out.println("publicKey = " + publicKey);
    }
}