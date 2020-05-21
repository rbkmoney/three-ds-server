package com.rbkmoney.threeds.server.visa;

import com.rbkmoney.threeds.server.domain.device.DeviceChannel;
import com.rbkmoney.threeds.server.domain.message.MessageCategory;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArs;
import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDSRequestorAuthenticationInd;
import com.rbkmoney.threeds.server.domain.transaction.TransactionStatus;
import com.rbkmoney.threeds.server.domain.unwrapped.Address;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.jwx.HeaderParameterNames;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.X509EncodedKeySpec;
import java.util.Collection;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class APP_PA_VisaIntegrationTest extends VisaIntegrationConfig {

    @Autowired
    private ResourceLoader resourceLoader;

    @Test
    public void name() throws IOException, CertificateException {
        String certStr = "-----BEGIN PKCS7-----\n" +
                "MIIMFAYJKoZIhvcNAQcCoIIMBTCCDAECAQExADALBgkqhkiG9w0BBwGgggvpMIID\n" +
                "mDCCAoCgAwIBAgIUDwB3o83nGKOw5k3cboXx2UelVggwDQYJKoZIhvcNAQELBQAw\n" +
                "fzEmMCQGA1UEAxMdVkJWVEVTVFNVSVRFMl9SU0FfU0lHTl9DQV9DVEUxFTATBgNV\n" +
                "BAsTDFZCVlRFU1RTVUlURTENMAsGA1UEChMEVklTQTEPMA0GA1UEBxMGRGVudmVy\n" +
                "MREwDwYDVQQIEwhDb2xvcmFkbzELMAkGA1UEBhMCVVMwHhcNMTkxMDAzMjMxNzU3\n" +
                "WhcNMjAxMDAyMjMxNzU3WjBnMQswCQYDVQQGEwJVUzELMAkGA1UECAwCVFgxDzAN\n" +
                "BgNVBAcMBkFVU1RJTjENMAsGA1UECgwEVklTQTENMAsGA1UECwwEVlNUUzEcMBoG\n" +
                "A1UEAwwTVkJWVEVTVFNVSVRFX0RTX1JTQTCCASIwDQYJKoZIhvcNAQEBBQADggEP\n" +
                "ADCCAQoCggEBAJRUX7QXJO2Nt/O0ncx702FmF5E8+V02JwMGMvrsQyH3CkRs7pHO\n" +
                "6YgYeeidq1juf2WjSvOghsNFTC1jbsPnSFaAf3obXJlBmKyssor6wH0MZrrFBPP4\n" +
                "5CpiSxMk0Hddi+BoUAHGytr8Yc6kGlWjsfXEeLzPSOfFJ632wWBK/8UaqcIucaqi\n" +
                "r2pFeoh8yaVvAEZ5MkhcIDBEA4kxDN9+WwO7svhem2NKEIpq6uvIfEs/0fmBuFej\n" +
                "Ybtu75nVT9vuA7KZDmVjOaUN/Ptdpnf/ULEhfQgouHdeoLk8X7D3hDBr+9POnL1k\n" +
                "y/AXPyyWzo76B54mK3Vsba/vkmOHSTqMFnsCAwEAAaMkMCIwCwYDVR0PBAQDAgeA\n" +
                "MBMGA1UdJQQMMAoGCCsGAQUFBwMCMA0GCSqGSIb3DQEBCwUAA4IBAQAqBiNWVUIB\n" +
                "Au3UE1YoVu9CuUojQkV2XD/6Z9MZs2xHCbhuqJdLrumxVu2Z+WxLQJUOkHHrBtAW\n" +
                "miEzqADUR/VeJRFi1bybhUSinNA+Mdj1nfHpScKcFOz0VzOTm52VYOQAITh/VOZJ\n" +
                "00k2oSP+35Ftw5mM0RPzrjVAXK1dWrjw+PKASLkxMH6BWVDLQfmzRj8fzhrmwt4z\n" +
                "AQqyvjPBX+7dagtCQie0CqpTmjHrrEwZrcTARwSAjFmaHFVqeEliNIAu96aTrZ9+\n" +
                "AYoGj5UVOtd1njlxQraJ0K/vMloBHCNpQ6UkNJot6i4dNGxJOKtxOLnUGV1kAxxw\n" +
                "eJkJ0OBv8Q6mMIIEFjCCAv6gAwIBAgIQetu1SMxpnENAnnOz1P+PtTANBgkqhkiG\n" +
                "9w0BAQUFADBpMQswCQYDVQQGEwJVUzENMAsGA1UEChMEVmlzYTEvMC0GA1UECxMm\n" +
                "VmlzYSBJbnRlcm5hdGlvbmFsIFNlcnZpY2UgQXNzb2NpYXRpb24xGjAYBgNVBAMT\n" +
                "EVZpc2EgZWNvbW1lcmNlIFFBMB4XDTEzMDExMjAzMzc0OVoXDTMzMDExMjAzMzc0\n" +
                "OVowaTELMAkGA1UEBhMCVVMxDTALBgNVBAoTBFZpc2ExLzAtBgNVBAsTJlZpc2Eg\n" +
                "SW50ZXJuYXRpb25hbCBTZXJ2aWNlIEFzc29jaWF0aW9uMRowGAYDVQQDExFWaXNh\n" +
                "IGVjb21tZXJjZSBRQTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBANkU\n" +
                "flKmpn52tYI3ZtBNPWPY4s4KJn/HVWcFD+SViaOUBWh8GeydubN/oCwGItkfKxvS\n" +
                "iDRRmSfCUIJBq0WlEQ3ucALb/B+IOrRwQgisq3bN0IZLAc0zHIpyWXumaoks7saR\n" +
                "vDsa2wrbz/Et1qkM0j/oH/vOrDdaKH9J95qkCq4TZ7sEcy5nKwfDkpF2fokZt0UK\n" +
                "KFtxVHcSEPSE1zNN1xkNxSNp7vpXyxvLSsC/Xdr8G7TCg2b1yxXWFjrP01uAv6En\n" +
                "JKTnZI4C2UehwxjuTACfSsT/AGvxDhfWPrrEtA6TCcD9rOp9DpwXNiSJGPzkcUUW\n" +
                "fHGUxXg9eYgiTEo924cCAwEAAaOBuTCBtjAPBgNVHRMBAf8EBTADAQH/MHQGA1Ud\n" +
                "IARtMGswaQYFZ4EDAQEwYDAjBggrBgEFBQcCARYXaHR0cDovL3d3dy52aXNhLmNv\n" +
                "bS9wa2kwOQYIKwYBBQUHAgIwLTAYFhFSZXBsYWNlIFRoaXMgVGV4dDADAgEBGhFS\n" +
                "ZXBsYWNlIFRoaXMgVGV4dDAOBgNVHQ8BAf8EBAMCAQYwHQYDVR0OBBYEFBx8Hyad\n" +
                "QAWl4BuB3UDx9zzFNkIzMA0GCSqGSIb3DQEBBQUAA4IBAQCe1G2hPsAA+RoNo0cV\n" +
                "n1+Tec8SYdQBAF7qCnL9Fehau47e36fVGNizOkyIT9AHGor6R1LIahKX+E6mqQTY\n" +
                "sznJS5hXJ8G8c0P0mxjVScb7ykwQ5wBYqXWM2bXKpiLTeYVWf1ArFWqp1y9G6R0A\n" +
                "OanLjmwNRBArgyv6l1QZb7UM3Bf9S5l0urFML06En8B79lle5X32Iv9jfucu6caI\n" +
                "rBLkPSx6Yq+q8ECs48NRSON+/Pqm9Hxw1H3/yz2qLG4zTI7xJVDESZGEXadLwCJX\n" +
                "D6OReX2F/BtUd8q23djXZbVYiIfE9ebr4g3152BlVCHZ2GyPdjhIuLeH21VbT/dy\n" +
                "EHHAMIIELzCCAxegAwIBAgIQIsB7FwBdgljBd50GLaxkAzANBgkqhkiG9w0BAQsF\n" +
                "ADBpMQswCQYDVQQGEwJVUzENMAsGA1UEChMEVmlzYTEvMC0GA1UECxMmVmlzYSBJ\n" +
                "bnRlcm5hdGlvbmFsIFNlcnZpY2UgQXNzb2NpYXRpb24xGjAYBgNVBAMTEVZpc2Eg\n" +
                "ZWNvbW1lcmNlIFFBMB4XDTE3MDQwNTE4MTgxNVoXDTIyMDQwNTE4MTgxNlowfzEm\n" +
                "MCQGA1UEAxMdVkJWVEVTVFNVSVRFMl9SU0FfU0lHTl9DQV9DVEUxFTATBgNVBAsT\n" +
                "DFZCVlRFU1RTVUlURTENMAsGA1UEChMEVklTQTEPMA0GA1UEBxMGRGVudmVyMREw\n" +
                "DwYDVQQIEwhDb2xvcmFkbzELMAkGA1UEBhMCVVMwggEiMA0GCSqGSIb3DQEBAQUA\n" +
                "A4IBDwAwggEKAoIBAQCEOIppdHllRtXnfW59DOcVgodC+GerzxFYG6N/girVHk1b\n" +
                "ETED77jCw5mbbnJyHXO40SynSCHzKaWxdFR9TuRZjf/a4D7SheLFd3Fhzdm9FCdO\n" +
                "dAOVx7+0hHxtqUIGBOX9k8aohHumVyf7x5Mj9L4Fwv0jqBFvD6VJVEq7EpE/VCQx\n" +
                "Ks1pnFKsZ33YdDwTaduSSKytyBC9pwI/2bCSN+meBewWx/qZUj0AK3HKIDuxZ2Nw\n" +
                "PNnAP8YCIRTaTiueHmHBeVgum3JIOxL6K0Eui4PKqWwTgiHblzUrl670vBUGxZeG\n" +
                "2bp+5eQWHVEmSaDvXnL9d8bftsmz2sajnHdqt4XRAgMBAAGjgbwwgbkwEgYDVR0T\n" +
                "AQH/BAgwBgEB/wIBADB0BgNVHSAEbTBrMGkGBWeBAwEBMGAwIwYIKwYBBQUHAgEW\n" +
                "F2h0dHA6Ly93d3cudmlzYS5jb20vcGtpMDkGCCsGAQUFBwICMC0wGBYRUmVwbGFj\n" +
                "ZSBUaGlzIFRleHQwAwIBARoRUmVwbGFjZSBUaGlzIFRleHQwDgYDVR0PAQH/BAQD\n" +
                "AgEGMB0GA1UdDgQWBBQHgy8dWx4MOByoGrI0B36j65/WlDANBgkqhkiG9w0BAQsF\n" +
                "AAOCAQEAysCcn4p0fE1Os6AiRDwoNTzvUelHXWfL2xv0LSJLoCaZLRJI544V5D9e\n" +
                "pQD5Eni14qM96k2hSdQxeyUFhWMs2/IJsRSdZCQxjGCB4Oac0Fq1H7rzny0+6T2T\n" +
                "sUBPNOksN0SUA3arnS3GYcAA2JMBbA7v143YfLZK16JERas5cIiHdEmCm7JQDA7d\n" +
                "8UZ4Os4tkeswFkHJWD0LsNwqi1nxhMO4B+AyUfN8lNVip1iLLfGylFc8Z5CkccG1\n" +
                "fqSgBDl7YNcCq84eh/NP5ZHoC1aLhGrGDA0oJnz2qdMpWFDMp2GU3Mb+cl57y8zB\n" +
                "nryQMKQBz3d4CQbecV6IRfgdOJ43KzEA\n" +
                "-----END PKCS7-----\n";
        // Init bouncycastle
        Security.addProvider(new BouncyCastleProvider());

        // Get certificate
        StringReader sr = new StringReader(certStr);
        PemReader pr = new PemReader(sr);
        PemObject pemObject = pr.readPemObject();
        pr.close();

        final ByteArrayInputStream bais = new ByteArrayInputStream(pemObject.getContent());
        final CertificateFactory certificateFactory = CertificateFactory.getInstance("X509");
        Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(bais);

        assertNotNull(certificates);
        // Get Public Key as RSA in order to get extra attributes
//        RSAPublicKey rsaPublicKey = (RSAPublicKey)cert.getPublicKey();
//
//        System.out.println(printCertInfo(cert, rsaPublicKey));
    }
    //
//    @Test
//    public void name() throws Exception {
//        Certificate cert = null;
//        File file = new File(getClass().getClassLoader().getResource("pbds_rsa").getFile());
//        try (InputStream in = new BufferedInputStream(new FileInputStream(file))) {
//            CertificateFactory certificatefactory = CertificateFactory.getInstance("X.509");
//            cert = certificatefactory.generateCertificate(in);
//        } catch (CertificateException e) {
//            e.printStackTrace();
//        }
//
//        File f = new File("myFile.p7b");
//        byte[] buffer = new byte[(int) f.length()];
//        DataInputStream in = new DataInputStream(new FileInputStream(f));
//        in.readFully(buffer);
//        in.close();
//
//        //Corresponding class of signed_data is CMSSignedData
//        CMSSignedData signature = new CMSSignedData(buffer);
//        Store cs = signature.getCertificates();
//        SignerInformationStore signers = signature.getSignerInfos();
//        Collection c = signers.getSigners();
//        Iterator it = c.iterator();
//
//        //the following array will contain the content of xml document
//        byte[] data = null;
//
//        while (it.hasNext()) {
//            SignerInformation signer = (SignerInformation) it.next();
//            Collection certCollection = cs.getMatches(signer.getSID());
//            Iterator certIt = certCollection.iterator();
//            X509CertificateHolder cert = (X509CertificateHolder) certIt.next();
//
//            CMSProcessable sc = signature.getSignedContent();
//            data = (byte[]) sc.getContent();
//        }
//        String s = readLineByLineJava8("classpath:3ds_server_pki/pbds_rsa");
//        String s = new String(Files.readAllBytes(Paths.get("3ds_server_pki/pbds_rsa")));
//        Assert.assertNotNull(s);
//        System.out.println(s);
//        File file = new File(
//                getClass().getClassLoader().getResource("3ds_server_pki/pbds_rsa").getFile()
//        );
//        FileInputStream fileInputStream = new FileInputStream("3ds_server_pki/mir2.p12");
//        FileInputStream fileInputStream = new FileInputStream("classpath:3ds_server_pki/pbds_rsa");
//        Assert.assertNotNull(file);
//        PublicKey publicKey = get("classpath:3ds_server_pki/pbds_rsa");
//        PublicKey publicKey = get("classpath:3ds_server_pki/pbds_rsa");
//        System.out.println(publicKey.toString());
//    }

    private static String readLineByLineJava8(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return contentBuilder.toString();
    }

    public PublicKey get(String filename) throws Exception {
        byte[] keyBytes = Files.readAllBytes(Path.of(resourceLoader.getResource(filename).getURI()));

        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    @Test
    public void test3DSS_210_201() {
        String acctNumber = "4012000000003085";
        DeviceChannel deviceChannel = DeviceChannel.APP_BASED;
        String acquirerBIN = "400551";
        String messageVersion = "2.1.0";
        MessageCategory messageCategory = MessageCategory.PAYMENT_AUTH;
        String threeDSServerOperatorID = "10075020";
        ThreeDSRequestorAuthenticationInd threeDSRequestorAuthenticationInd = ThreeDSRequestorAuthenticationInd.PAYMENT_TRANSACTION;

        PArq pArq = buildPArq(acctNumber, deviceChannel, acquirerBIN, messageVersion, messageCategory, threeDSServerOperatorID, threeDSRequestorAuthenticationInd);

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.AUTHENTICATION_VERIFICATION_SUCCESSFUL, pArs.getTransStatus());
        assertEquals("05", pArs.getEci());
        assertNotNull(pArs.getAuthenticationValue());
    }

    private PArq buildPArq(String acctNumber, DeviceChannel deviceChannel, String acquirerBIN, String messageVersion, MessageCategory messageCategory, String threeDSServerOperatorID, ThreeDSRequestorAuthenticationInd threeDSRequestorAuthenticationInd) {
        PArq pArq = PArq.builder()
                .acctNumber(acctNumber)
                .acquirerBIN(acquirerBIN)
                .acquirerMerchantID(randomNumeric(10))
                .cardExpiryDate(randomCardExpiryDate())
                .deviceChannel(getEnumWrapper(deviceChannel))
                .deviceRenderOptions(randomDeviceRenderOptions())
                .mcc(randomNumeric(4))
                .merchantCountryCode(randomMerchantCountryCode())
                .merchantName(randomString())
                .messageCategory(getEnumWrapper(messageCategory))
                .purchaseAmount(randomNumeric(5))
                .purchaseCurrency(randomPurchaseCurrency())
                .purchaseDate(randomLocalDateTime())
                .purchaseExponent(randomPurchaseExponent())
                .sdkAppID(randomId())
                .sdkEncData(randomString())
                .sdkEphemPubKey(randomSdkEphemPubKey())
                .sdkMaxTimeout(randomNumericTwoNumbers())
                .sdkReferenceNumber(randomString())
                .sdkTransID(randomId())
                .threeDSRequestorAuthenticationInd(getEnumWrapper(threeDSRequestorAuthenticationInd))
                .threeDSRequestorID(randomNumeric(10))
                .threeDSRequestorName(randomString())
                .threeDSRequestorURL(randomUrl())
                .threeDSServerOperatorID(threeDSServerOperatorID)
                .build();
        pArq.setMessageVersion(messageVersion);
        pArq.setXULTestCaseRunId(randomString());
        pArq.setBillingAddress(new Address());
        pArq.setShippingAddress(new Address());
        fullFilling(pArq);
        return pArq;
    }

    private String asd() {
        String deviceInfoJson = randomDeviceInfoJson();
        JsonWebEncryption jwe = new JsonWebEncryption();
        jwe.setHeader(HeaderParameterNames.CONTENT_TYPE, "JWT");
        jwe.setKey(keyEncrypt.getPublicKey());
        jwe.setPayload(signedJwt);
        jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.RSA_OAEP_256);
        jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_256_GCM);
        String encryptedJwt = jwe.getCompactSerialization();
        System.out.println("Encrypted ::" + encryptedJwt);
    }
}
