/**
 *
 */
package edu.internet2.middleware.grouper.webservicesClient;

import org.apache.axis2.client.Options;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.http.HttpTransportProperties;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import edu.internet2.middleware.grouper.webservicesClient.util.GeneratedClientSettings;
import edu.internet2.middleware.grouper.ws.samples.types.WsSampleGenerated;
import edu.internet2.middleware.grouper.ws.samples.types.WsSampleGeneratedType;
import edu.internet2.middleware.grouper.ws.soap.xsd.StemDelete;
import edu.internet2.middleware.grouper.ws.soap.xsd.WsStemDeleteResult;
import edu.internet2.middleware.grouper.ws.soap.xsd.WsStemDeleteResults;
import edu.internet2.middleware.grouper.ws.soap.xsd.WsStemLookup;
import edu.internet2.middleware.grouper.ws.soap.xsd.WsSubjectLookup;


/**
 *
 * @author mchyzer
 *
 */
public class WsSampleStemDelete implements WsSampleGenerated {
    /**
     * @param args
     */
    public static void main(String[] args) {
        stemDelete(WsSampleGeneratedType.soap);
    }

    /**
     * @see edu.internet2.middleware.grouper.ws.samples.types.WsSampleGenerated#executeSample(edu.internet2.middleware.grouper.ws.samples.types.WsSampleGeneratedType)
     */
    public void executeSample(WsSampleGeneratedType wsSampleGeneratedType) {
        stemDelete(wsSampleGeneratedType);
    }

    /**
     * @param wsSampleGeneratedType can run as soap or xml/http
     */
    public static void stemDelete(WsSampleGeneratedType wsSampleGeneratedType) {
        try {
            //URL, e.g. http://localhost:8091/grouper-ws/services/GrouperService
            GrouperServiceStub stub = new GrouperServiceStub(GeneratedClientSettings.URL);
            Options options = stub._getServiceClient().getOptions();
            HttpTransportProperties.Authenticator auth = new HttpTransportProperties.Authenticator();
            auth.setUsername(GeneratedClientSettings.USER);
            auth.setPassword(GeneratedClientSettings.PASS);
            auth.setPreemptiveAuthentication(true);

            options.setProperty(HTTPConstants.AUTHENTICATE, auth);
            options.setProperty(HTTPConstants.SO_TIMEOUT, new Integer(3600000));
            options.setProperty(HTTPConstants.CONNECTION_TIMEOUT,
                new Integer(3600000));

            StemDelete stemDelete = StemDelete.class.newInstance();

            //version, e.g. v1_3_000
            stemDelete.setClientVersion(GeneratedClientSettings.VERSION);

            // set the act as id
            WsSubjectLookup actAsSubject = WsSubjectLookup.class.newInstance();
            actAsSubject.setSubjectId("GrouperSystem");
            stemDelete.setActAsSubjectLookup(actAsSubject);

            WsStemLookup wsStemLookup = WsStemLookup.class.newInstance();
            wsStemLookup.setStemName("aStem:stemNotExist");
            stemDelete.setWsStemLookups(new WsStemLookup[] { wsStemLookup });

            WsStemDeleteResults wsStemDeleteResults = stub.stemDelete(stemDelete)
                                                          .get_return();

            System.out.println(ToStringBuilder.reflectionToString(
                    wsStemDeleteResults));

            WsStemDeleteResult[] wsStemDeleteResultArray = wsStemDeleteResults.getResults();

            if (wsStemDeleteResultArray != null) {
                for (WsStemDeleteResult wsStemDeleteResult : wsStemDeleteResultArray) {
                    System.out.println(ToStringBuilder.reflectionToString(
                            wsStemDeleteResult));
                }
            }
            if (!StringUtils.equals("T", 
                wsStemDeleteResults.getResultMetadata().getSuccess())) {
              throw new RuntimeException("didnt get success! ");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}