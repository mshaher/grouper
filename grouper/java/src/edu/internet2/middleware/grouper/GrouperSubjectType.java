/* 
 * Copyright (C) 2004 TODO
 * All Rights Reserved. 
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted only as authorized by the Academic
 * Free License version 2.1.
 *
 * A copy of this license is available in the file LICENSE in the
 * top-level directory of the distribution or, alternatively, at
 * <http://www.opensource.org/licenses/afl-2.1.php>
 */

package edu.internet2.middleware.grouper;

/** 
 * Class representing a type of {@link GrouperGroup}.
 *
 * @author  blair christensen.
 * @version $Id: GrouperSubjectType.java,v 1.2 2004-08-24 17:37:57 blair Exp $
 */
public class GrouperMemberType {

  private String memberType;
  private String displayName;

  /**
   * Create a {@link GrouperMemberType} object.
   */
  public GrouperMemberType() {
    memberType  = null;
    displayName = null;
  }

  public String toString() {
    return "" + this.getMemberType() + ":" + this.getDisplayName();
  }

  /*
   * Below for Hibernate
   */

  protected String getMemberType() {
    return this.memberType;
  }

  // XXX Do we really want to allow write access? 
  private void setMemberType(String memberType) {
    this.memberType = memberType;
  }

  protected String getDisplayName() {
    return this.displayName;
  }

  // XXX Do we really want to allow write access? 
  private void setDisplayName(String displayName) {
    this.displayName = displayName;
  }
}

