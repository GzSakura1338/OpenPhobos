package org.phobos.core.crypto;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class SslUtils {
   public static X509TrustManager getX509TrustManager() throws Exception, CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException {
      CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");

      Certificate certificate;
      try (InputStream inputStream = SslUtils.class.getResourceAsStream("/assets/phobos/certificate.crt")) {
         if (inputStream == null) {
            throw new IllegalArgumentException("Certificate not found");
         }

         certificate = certificateFactory.generateCertificate(inputStream);
      }

      KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
      keyStore.load(null, null);
      keyStore.setCertificateEntry("ca", certificate);
      TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
      trustManagerFactory.init(keyStore);

      for (TrustManager trustManager : trustManagerFactory.getTrustManagers()) {
         if (trustManager instanceof X509TrustManager x509TrustManager) {
            return x509TrustManager;
         }
      }

      throw new IllegalStateException("Unable to find X509");
   }

   public static SSLContext getSSLContext() throws Exception, CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
      X509TrustManager x509TrustManager = getX509TrustManager();
      SSLContext sSLContext = SSLContext.getInstance("TLS");
      sSLContext.init(null, new TrustManager[]{x509TrustManager}, null);
      return sSLContext;
   }
}
