/**
 *
 */
package edu.internet2.middleware.grouper.webservicesClient;

import java.lang.reflect.Array;

import org.apache.axis2.client.Options;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.http.HttpTransportProperties;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import edu.internet2.middleware.grouper.webservicesClient.util.GeneratedClientSettings;
import edu.internet2.middleware.grouper.ws.samples.types.WsSampleGenerated;
import edu.internet2.middleware.grouper.ws.samples.types.WsSampleGeneratedType;
import edu.internet2.middleware.grouper.ws.soap.xsd.HasMember;
import edu.internet2.middleware.grouper.ws.soap.xsd.WsGroupLookup;
import edu.internet2.middleware.grouper.ws.soap.xsd.WsHasMemberResult;
import edu.internet2.middleware.grouper.ws.soap.xsd.WsHasMemberResults;
import edu.internet2.middleware.grouper.ws.soap.xsd.WsSubjectLookup;


/**
 * @author mchyzer
 *
 */
public class WsSampleHasMember implements WsSampleGenerated {
    /**
     * @param args
     */
    public static void main(String[] args) {
        hasMember(WsSampleGeneratedType.soap);
    }

    /**
     * @see edu.internet2.middleware.grouper.ws.samples.types.WsSampleGenerated#executeSample(edu.internet2.middleware.grouper.ws.samples.types.WsSampleGeneratedType)
     */
    public void executeSample(WsSampleGeneratedType wsSampleGeneratedType) {
        hasMember(wsSampleGeneratedType);
    }

    /**
     * @param wsSampleGeneratedType can run as soap or xml/http
     */
    public static void hasMember(WsSampleGeneratedType wsSampleGeneratedType) {
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

            HasMember hasMember = HasMember.class.newInstance();

            //version, e.g. v1_3_000
            hasMember.setClientVersion(GeneratedClientSettings.VERSION);

            // set the act as id
            WsSubjectLookup actAsSubject = WsSubjectLookup.class.newInstance();
            actAsSubject.setSubjectId("GrouperSystem");
            hasMember.setActAsSubjectLookup(actAsSubject);

            // check all
            hasMember.setMemberFilter("All");

            WsGroupLookup wsGroupLookup = WsGroupLookup.class.newInstance();
            wsGroupLookup.setGroupName("aStem:aGroup");
            hasMember.setWsGroupLookup(wsGroupLookup);

            // check two subjects from the group
            WsSubjectLookup[] subjectLookups = (WsSubjectLookup[]) Array.newInstance(WsSubjectLookup.class,
                    2);
            subjectLookups[0] = WsSubjectLookup.class.newInstance();
            subjectLookups[0].setSubjectId("GrouperSystem");

            subjectLookups[1] = WsSubjectLookup.class.newInstance();
            subjectLookups[1].setSubjectId("10039438");

            hasMember.setSubjectLookups(subjectLookups);

            WsHasMemberResults wsHasMemberResults = stub.hasMember(hasMember)
                                                        .get_return();

            System.out.println(ToStringBuilder.reflectionToString(
                    wsHasMemberResults));

            WsHasMemberResult[] results = wsHasMemberResults.getResults();

            if (results != null) {
                for (WsHasMemberResult wsHasMemberResult : results) {
                    System.out.println(ToStringBuilder.reflectionToString(
                            wsHasMemberResult));
                }
            }
            if (!StringUtils.equals("T", 
                wsHasMemberResults.getResultMetadata().getSuccess())) {
              throw new RuntimeException("didnt get success! ");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}