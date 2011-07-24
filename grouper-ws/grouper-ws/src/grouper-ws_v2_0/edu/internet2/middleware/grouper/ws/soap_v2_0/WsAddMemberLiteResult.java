/**
 * 
 */
package edu.internet2.middleware.grouper.ws.soap_v2_0;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.internet2.middleware.grouper.misc.GrouperVersion;
import edu.internet2.middleware.grouper.ws.WsResultCode;

/**
 * Result of one subject being added to a group.  The number of
 * subjects will equal the number of subjects sent in to the method
 * 
 * @author mchyzer
 */
public class WsAddMemberLiteResult {

  /**
   * empty
   */
  public WsAddMemberLiteResult() {
    //empty
  }

  /** logger */
  @SuppressWarnings("unused")
  private static final Log LOG = LogFactory.getLog(WsAddMemberLiteResult.class);


  /**
    * metadata about the result
    */
  private WsResultMeta resultMetadata = new WsResultMeta();

  /**
   * attributes of subjects returned, in same order as the data
   */
  private String[] subjectAttributeNames;

  /**
   * group assigned to
   */
  private WsGroup wsGroupAssigned;

  /**
   * subject that was added 
   */
  private WsSubject wsSubject;

  /**
   * result code of a request
   */
  public static enum WsAddMemberLiteResultCode implements WsResultCode {

    /** cant find group (rest http status code 404) (success: F) */
    GROUP_NOT_FOUND(404),

    /** added member (rest http status code 201) (success: T) */
    SUCCESS(201),

    /** found the subject (rest http status code 500) (success: F) */
    EXCEPTION(500),

    /** problem deleting existing members (rest http status code 500) (success: F) */
    PROBLEM_DELETING_MEMBERS(500),

    /** invalid query (e.g. if everything blank) (rest http status code 400) (success: F) */
    INVALID_QUERY(400),

    /** if one request, and that is a duplicate (rest http status code 409) (success: F) */
    SUBJECT_DUPLICATE(409),

    /** if one request, and that is a subject not found (rest http status code 404) (success: F) */
    SUBJECT_NOT_FOUND(404),

    /** if one request, and that is a insufficient privileges (rest http status code 403) (success: F) */
    INSUFFICIENT_PRIVILEGES(403),

    /** something in one assignment wasnt successful (rest http status code 500) (success: F) */
    PROBLEM_WITH_ASSIGNMENT(500),

    /** success but it was already a member */
    SUCCESS_ALREADY_EXISTED(200);

    /** get the name label for a certain version of client 
     * @param clientVersion 
     * @return name */
    public String nameForVersion(GrouperVersion clientVersion) {
      return this.name();
    }

    /**
     * if this is a successful result
     * 
     * @return true if success
     */
    public boolean isSuccess() {
      return this.name().startsWith("SUCCESS");
    }

    /** http status code for rest/lite e.g. 200 */
    private int httpStatusCode;

    /**
     * status code for rest/lite e.g. 200
     * @param statusCode
     */
    private WsAddMemberLiteResultCode(int statusCode) {
      this.httpStatusCode = statusCode;
    }

    /**
     * @see edu.internet2.middleware.grouper.ws.WsResultCode#getHttpStatusCode()
     */
    public int getHttpStatusCode() {
      return this.httpStatusCode;
    }
  }

  /**
   * @return the resultMetadata
   */
  public WsResultMeta getResultMetadata() {
    return this.resultMetadata;
  }

  /**
   * attributes of subjects returned, in same order as the data
   * @return the attributeNames
   */
  public String[] getSubjectAttributeNames() {
    return this.subjectAttributeNames;
  }

  /**
   * group assigned to
   * @return the wsGroupLookup
   */
  public WsGroup getWsGroupAssigned() {
    return this.wsGroupAssigned;
  }

  /**
   * attributes of subjects returned, in same order as the data
   * @param attributeNamesa the attributeNames to set
   */
  public void setSubjectAttributeNames(String[] attributeNamesa) {
    this.subjectAttributeNames = attributeNamesa;
  }

  /**
   * group assigned to
   * @param theWsGroupLookupAssigned the wsGroupLookup to set
   */
  public void setWsGroupAssigned(WsGroup theWsGroupLookupAssigned) {
    this.wsGroupAssigned = theWsGroupLookupAssigned;
  }

  /**
   * subject that was added
   * @return the subjectId
   */
  public WsSubject getWsSubject() {
    return this.wsSubject;
  }

  /**
   * subject that was added
   * @param wsSubject1 the wsSubject1 to set
   */
  public void setWsSubject(WsSubject wsSubject1) {
    this.wsSubject = wsSubject1;
  }

  /**
   * metadata about the result
   */
  private WsResponseMeta responseMetadata = new WsResponseMeta();

  /**
   * @see edu.internet2.middleware.grouper.ws.rest.WsResponseBean#getResponseMetadata()
   * @return the response metadata
   */
  public WsResponseMeta getResponseMetadata() {
    return this.responseMetadata;
  }

  
  /**
   * @param resultMetadata1 the resultMetadata to set
   */
  public void setResultMetadata(WsResultMeta resultMetadata1) {
    this.resultMetadata = resultMetadata1;
  }

  
  /**
   * @param responseMetadata1 the responseMetadata to set
   */
  public void setResponseMetadata(WsResponseMeta responseMetadata1) {
    this.responseMetadata = responseMetadata1;
  }

}
