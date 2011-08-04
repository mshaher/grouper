/**
 * 
 */
package edu.internet2.middleware.grouper.exception;

import org.apache.commons.lang.StringUtils;

import edu.internet2.middleware.grouper.permissions.limits.PermissionLimitDocumentation;
import edu.internet2.middleware.grouper.util.GrouperUtil;


/**
 * When the validator of a limit fires an invalid value
 * @author mchyzer
 *
 */
@SuppressWarnings("serial")
public class LimitInvalidException extends RuntimeException {

  /**
   * the reason invalid
   */
  private PermissionLimitDocumentation permissionLimitDocumentation;
  
  /**
   * the reason invalid
   * @return the reason invalid
   */
  public PermissionLimitDocumentation getPermissionLimitDocumentation() {
    return this.permissionLimitDocumentation;
  }

  /**
   * @param message
   * @param permissionLimitDocumentation1 the reason invalid
   */
  public LimitInvalidException(String message, PermissionLimitDocumentation permissionLimitDocumentation1 ) {
    
    super(argsToString(message, permissionLimitDocumentation1));
    this.permissionLimitDocumentation = permissionLimitDocumentation1;
    
  }

  /**
   * 
   * @param message
   * @param permissionLimitDocumentation1
   * @return the string
   */
  private static String argsToString(String message, PermissionLimitDocumentation permissionLimitDocumentation1) {
    StringBuilder theMessage = new StringBuilder();
    if (!StringUtils.isBlank(message)) {
      theMessage.append(message).append(", ");
    }
    
    if (permissionLimitDocumentation1 != null) {
      theMessage.append(permissionLimitDocumentation1.getDocumentationKey()).append(", ");
      for (String arg : GrouperUtil.nonNull(permissionLimitDocumentation1.getArgs())) {
        theMessage.append(arg).append(", ");
      }
    }
    return theMessage.toString();
  }

}
