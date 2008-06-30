/*
  Copyright (C) 2004-2007 University Corporation for Advanced Internet Development, Inc.
  Copyright (C) 2004-2007 The University Of Chicago

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/

package edu.internet2.middleware.grouper;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import edu.internet2.middleware.grouper.hibernate.HibernateSession;
import edu.internet2.middleware.grouper.hooks.GroupTypeTupleHooks;
import edu.internet2.middleware.grouper.hooks.beans.HooksGroupTypeTupleBean;
import edu.internet2.middleware.grouper.hooks.logic.GrouperHookType;
import edu.internet2.middleware.grouper.hooks.logic.GrouperHooksUtils;
import edu.internet2.middleware.grouper.hooks.logic.VetoTypeGrouper;
import edu.internet2.middleware.grouper.util.GrouperUtil;

/**
 * Basic Hibernate <code>Group</code> and <code>GroupType</code> tuple DTO implementation.
 * @author  blair christensen.
 * @version $Id: GroupTypeTuple.java,v 1.3 2008-06-30 04:01:34 mchyzer Exp $
 * @since   @HEAD@
 */
public class GroupTypeTuple extends GrouperAPI {

  //*****  START GENERATED WITH GenerateFieldConstants.java *****//

  /** constant for field name for: dbVersion */
  public static final String FIELD_DB_VERSION = "dbVersion";

  /** constant for field name for: groupUUID */
  public static final String FIELD_GROUP_UUID = "groupUUID";

  /** constant for field name for: id */
  public static final String FIELD_ID = "id";

  /** constant for field name for: typeUUID */
  public static final String FIELD_TYPE_UUID = "typeUUID";

  /**
   * fields which are included in db version
   */
  private static final Set<String> DB_VERSION_FIELDS = GrouperUtil.toSet(
      FIELD_GROUP_UUID, FIELD_ID, FIELD_TYPE_UUID);

  //*****  END GENERATED WITH GenerateFieldConstants.java *****//

  // PRIVATE INSTANCE VARIABLES //
  private String  groupUUID;
  private String  id;
  private String  typeUUID;


  // PUBLIC CLASS METHODS //

  /**
   * @since   @HEAD@
   */
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof GroupTypeTuple)) {
      return false;
    }
    GroupTypeTuple that = (GroupTypeTuple) other;
    return new EqualsBuilder()
      .append( this.getGroupUuid(), that.getGroupUuid() )
      .append( this.getTypeUuid(),  that.getTypeUuid()  )
      .isEquals();
  }
  
  /**
   * @since   @HEAD@
   */
  public int hashCode() {
    return new HashCodeBuilder()
      .append( this.getGroupUuid() )
      .append( this.getTypeUuid()  )
      .toHashCode();
  }
  
  /**
   * @since   @HEAD@
   */
  public String toString() {
    return new ToStringBuilder(this)
      .append( "groupUuid", this.getGroupUuid() )
      .append( "typeUuid",  this.getTypeUuid()  )
      .toString();
  }


  // PROTECTED CLASS METHODS //

  public String getGroupUuid() {
    return this.groupUUID;
  }
  public String getId() {
    return this.id;
  }
  public String getTypeUuid() {
    return this.typeUUID;
  }


  // SETTERS //

  public GroupTypeTuple setGroupUuid(String groupUUID) {
    this.groupUUID = groupUUID;
    return this;
  }
  public GroupTypeTuple setId(String id) {
    this.id = id;
    return this;
  }
  public GroupTypeTuple setTypeUuid(String typeUUID) {
    this.typeUUID = typeUUID;
    return this;
  }

  /**
   * @see edu.internet2.middleware.grouper.GrouperAPI#onPostDelete(edu.internet2.middleware.grouper.hibernate.HibernateSession)
   */
  @Override
  public void onPostDelete(HibernateSession hibernateSession) {
    super.onPostDelete(hibernateSession);
    GrouperHooksUtils.callHooksIfRegistered(this, GrouperHookType.GROUP_TYPE_TUPLE, 
        GroupTypeTupleHooks.METHOD_GROUP_TYPE_TUPLE_POST_DELETE, HooksGroupTypeTupleBean.class, 
        this, GroupTypeTuple.class, VetoTypeGrouper.GROUP_TYPE_TUPLE_POST_DELETE, false, true);
  }

  /**
   * @see edu.internet2.middleware.grouper.GrouperAPI#onPostSave(edu.internet2.middleware.grouper.hibernate.HibernateSession)
   */
  @Override
  public void onPostSave(HibernateSession hibernateSession) {
    super.onPostSave(hibernateSession);
    GrouperHooksUtils.callHooksIfRegistered(this, GrouperHookType.GROUP_TYPE_TUPLE, 
        GroupTypeTupleHooks.METHOD_GROUP_TYPE_TUPLE_POST_INSERT, HooksGroupTypeTupleBean.class, 
        this, GroupTypeTuple.class, VetoTypeGrouper.GROUP_TYPE_TUPLE_POST_INSERT, true, false);
  }

  /**
   * @see edu.internet2.middleware.grouper.GrouperAPI#onPostUpdate(edu.internet2.middleware.grouper.hibernate.HibernateSession)
   */
  @Override
  public void onPostUpdate(HibernateSession hibernateSession) {
    super.onPostUpdate(hibernateSession);
    GrouperHooksUtils.callHooksIfRegistered(this, GrouperHookType.GROUP_TYPE_TUPLE, 
        GroupTypeTupleHooks.METHOD_GROUP_TYPE_TUPLE_POST_UPDATE, HooksGroupTypeTupleBean.class, 
        this, GroupTypeTuple.class, VetoTypeGrouper.GROUP_TYPE_TUPLE_POST_UPDATE, true, false);
  }

  /**
   * @see edu.internet2.middleware.grouper.GrouperAPI#onPreDelete(edu.internet2.middleware.grouper.hibernate.HibernateSession)
   */
  @Override
  public void onPreDelete(HibernateSession hibernateSession) {
    super.onPreDelete(hibernateSession);
    
    GrouperHooksUtils.callHooksIfRegistered(this, GrouperHookType.GROUP_TYPE_TUPLE, 
        GroupTypeTupleHooks.METHOD_GROUP_TYPE_TUPLE_PRE_DELETE, HooksGroupTypeTupleBean.class, 
        this, GroupTypeTuple.class, VetoTypeGrouper.GROUP_TYPE_TUPLE_PRE_DELETE, false, false);
  
  }

  /**
   * 
   * @see edu.internet2.middleware.grouper.GrouperAPI#onPreSave(edu.internet2.middleware.grouper.hibernate.HibernateSession)
   */
  @Override
  public void onPreSave(HibernateSession hibernateSession) {
    super.onPreSave(hibernateSession);
    
    
    GrouperHooksUtils.callHooksIfRegistered(this, GrouperHookType.GROUP_TYPE_TUPLE, 
        GroupTypeTupleHooks.METHOD_GROUP_TYPE_TUPLE_PRE_INSERT, HooksGroupTypeTupleBean.class, 
        this, GroupTypeTuple.class, VetoTypeGrouper.GROUP_TYPE_TUPLE_PRE_INSERT, false, false);
  
  }

  /**
   * @see edu.internet2.middleware.grouper.GrouperAPI#onPreUpdate(edu.internet2.middleware.grouper.hibernate.HibernateSession)
   */
  @Override
  public void onPreUpdate(HibernateSession hibernateSession) {
    super.onPreUpdate(hibernateSession);
    
    GrouperHooksUtils.callHooksIfRegistered(this, GrouperHookType.GROUP_TYPE_TUPLE, 
        GroupTypeTupleHooks.METHOD_GROUP_TYPE_TUPLE_PRE_UPDATE, HooksGroupTypeTupleBean.class, 
        this, GroupTypeTuple.class, VetoTypeGrouper.GROUP_TYPE_TUPLE_PRE_UPDATE, false, false);
  }

  /**
   * save the state when retrieving from DB
   * @return the dbVersion
   */
  @Override
  public GroupTypeTuple dbVersion() {
    return (GroupTypeTuple)this.dbVersion;
  }

  /**
   * note, these are massaged so that name, extension, etc look like normal fields.
   * access with fieldValue()
   * @see edu.internet2.middleware.grouper.GrouperAPI#dbVersionDifferentFields()
   */
  @Override
  public Set<String> dbVersionDifferentFields() {
    if (this.dbVersion == null) {
      throw new RuntimeException("State was never stored from db");
    }
    //easier to unit test if everything is ordered
    Set<String> result = GrouperUtil.compareObjectFields(this, this.dbVersion,
        DB_VERSION_FIELDS, null);
    return result;
  }

  /**
   * take a snapshot of the data since this is what is in the db
   */
  @Override
  public void dbVersionReset() {
    //lets get the state from the db so we know what has changed
    this.dbVersion = new GroupTypeTuple();
    
    GrouperUtil.copyObjectFields(this, this.dbVersion, DB_VERSION_FIELDS);
  }

} 

