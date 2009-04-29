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

package edu.internet2.middleware.grouper.privs;

import java.util.Set;

import edu.internet2.middleware.grouper.GrouperSession;
import edu.internet2.middleware.grouper.Stem;
import edu.internet2.middleware.grouper.exception.GrantPrivilegeException;
import edu.internet2.middleware.grouper.exception.InsufficientPrivilegeException;
import edu.internet2.middleware.grouper.exception.RevokePrivilegeException;
import edu.internet2.middleware.grouper.exception.SchemaException;
import edu.internet2.middleware.grouper.hibernate.HqlQuery;
import edu.internet2.middleware.subject.Subject;


/** 
 * Grouper Naming Privilege interface.
 * <p>
 * Unless you are implementing a new implementation of this interface,
 * you should not need to directly use these methods as they are all
 * wrapped by methods in the {@link Stem} class.
 * </p>
 * @author  blair christensen.
 * @version $Id: NamingAdapter.java,v 1.6 2009-04-13 16:53:07 mchyzer Exp $
 */
public interface NamingAdapter {

  // Public Instance Methods

  /**
   * Get all subjects with this privilege on this stem.
   * <pre class="eg">
   * Set stemmers = np.getSubjectsWithPriv(s, ns, NamingPrivilege.STEM);
   * </pre>
   * @param   s     Get privileges within this session context.
   * @param   ns    Get privileges on this stem.
   * @param   priv  Get this privilege.
   * @return  Set of {@link Subject} objects.
   * @throws  SchemaException
   */
  Set getSubjectsWithPriv(GrouperSession s, Stem ns, Privilege priv)
    throws  SchemaException;

  /**
   * Get all stems where this subject has this privilege.
   * <pre class="eg">
   * try {
   *   Set isStemmer = np.getStemsWhereSubjectHasPriv(
   *     s, subj, NamingPrivilege.STEM
   *   );
   * }
   * catch (SchemaException eS) {
   *   // Invalid priv
   * }
   * </pre>
   * @param   s     Get privileges within this session context.
   * @param   subj  Get privileges for this subject.
   * @param   priv  Get this privilege.
   * @return  Set of {@link Stem} objects.
   * @throws  SchemaException
   */
  Set getStemsWhereSubjectHasPriv(
    GrouperSession s, Subject subj, Privilege priv
  ) 
    throws  SchemaException;

  /**
   * Get all privileges held by this subject on this stem.
   * <pre class="eg">
   * Set privs = np.getPrivs(s, ns, subj);
   * </pre>
   * @param   s     Get privileges within this session context.
   * @param   ns    Get privileges on this stem.
   * @param   subj  Get privileges for this subject.
   * @return  Set of {@link NamingPrivilege} objects.
   */
  Set<NamingPrivilege> getPrivs(GrouperSession s, Stem ns, Subject subj);

  /**
   * Grant the privilege to the subject on this stem.
   * <pre class="eg">
   * try {
   *   np.grantPriv(s, ns, subj, NamingPrivilege.STEM);
   * }
   * catch (GrantPrivilegeException e0) {
   *   // Unable to grant the privilege
   * }
   * catch (InsufficientPrivilegeException e1) {
   *   // Not privileged to grant the privilege
   * }
   * </pre>
   * @param   s     Grant privilege in this session context.
   * @param   ns    Grant privilege on this stem.
   * @param   subj  Grant privilege to this subject.
   * @param   priv  Grant this privilege.   
   * @throws  GrantPrivilegeException
   * @throws  InsufficientPrivilegeException
   * @throws  SchemaException
   */
  void grantPriv(GrouperSession s, Stem ns, Subject subj, Privilege priv)
    throws  GrantPrivilegeException, 
            InsufficientPrivilegeException,
            SchemaException
            ;

  /**
   * Check whether the subject has this privilege on this stem.
   * <pre class="eg">
   * try {
   *   np.hasPriv(s, ns, subj, NamingPrivilege.STEM);
   * }
   * catch (SchemaException e) {
   *   // Invalid privilege
   * }
   * </pre>
   * @param   s     Check privilege in this session context.
   * @param   ns    Check privilege on this stem.
   * @param   subj     Check privilege for this subject.
   * @param   priv  Check this privilege.   
   * @return if has priv
   * @throws  SchemaException
   */
  boolean hasPriv(GrouperSession s, Stem ns, Subject subj, Privilege priv)
    throws SchemaException;

  /**
   * Revoke this privilege from everyone on this stem.
   * <pre class="eg">
   * try {
   *   np.revokePriv(s, ns, NamingPrivilege.STEM);
   * }
   * catch (InsufficientPrivilegeException eIP) {
   *   // Not privileged to revoke the privilege
   * }
   * catch (RevokePrivilegeException eRP) {
   *   // Unable to revoke the privilege
   * }
   * </pre>
   * @param   s     Revoke privilege in this session context.
   * @param   ns    Revoke privilege on this stem.
   * @param   priv  Revoke this privilege.   
   * @throws  InsufficientPrivilegeException
   * @throws  RevokePrivilegeException
   * @throws  SchemaException
   */
  void revokePriv(GrouperSession s, Stem ns, Privilege priv)
    throws  InsufficientPrivilegeException, 
            RevokePrivilegeException,
            SchemaException
            ;

  /**
   * Revoke the privilege from the subject on this stem.
   * <pre class="eg">
   * try {
   *   np.revokePriv(s, ns, subj, NamingPrivilege.STEM);
   * }
   * catch (InsufficientPrivilegeException eIP) {
   *   // Not privileged to grant the privilege
   * }
   * catch (RevokePrivilegeException eRP) {
   *   // Unable to revoke the privilege
   * }
   * </pre>
   * @param   s     Revoke privilege in this session context.
   * @param   ns    Revoke privilege on this stem.
   * @param   subj  Revoke privilege from this subject.
   * @param   priv  Revoke this privilege.   
   * @throws  InsufficientPrivilegeException
   * @throws  RevokePrivilegeException
   * @throws  SchemaException
   */
  void revokePriv(GrouperSession s, Stem ns, Subject subj, Privilege priv)
    throws  InsufficientPrivilegeException, 
            RevokePrivilegeException,
            SchemaException
            ;

  /**
   * Copies privileges for subjects that have the specified privilege on stem1 to stem2.
   * @param s 
   * @param stem1
   * @param stem2
   * @param priv 
   * @throws InsufficientPrivilegeException 
   * @throws GrantPrivilegeException 
   * @throws SchemaException 
   */
  void privilegeCopy(GrouperSession s, Stem stem1, Stem stem2, Privilege priv)
      throws InsufficientPrivilegeException, GrantPrivilegeException, SchemaException;
  
  /**
   * Copies privileges of type priv on any subject for the given Subject subj1 to the given Subject subj2.
   * For instance, if subj1 has STEM privilege to Stem x, this method will result with subj2
   * having STEM privilege to Stem x.
   * @param s 
   * @param subj1
   * @param subj2
   * @param priv 
   * @throws InsufficientPrivilegeException 
   * @throws GrantPrivilegeException 
   * @throws SchemaException 
   */
  void privilegeCopy(GrouperSession s, Subject subj1, Subject subj2, Privilege priv)
      throws InsufficientPrivilegeException, GrantPrivilegeException, SchemaException;

  /**
   * for a stem query, check to make sure the subject can see the records (if filtering HQL, you can do 
   * the postHqlFilterGroups instead if you like).  Note, this joins to tables, so the queries should
   * probably be "distinct"
   * @param grouperSession 
   * @param subject which needs view access to the groups
   * @param hql is the select and part part (hql prefix)
   * @param hqlQuery 
   * @param stemColumn is the name of the stem column to join to
   * @param privInSet find a privilege which is in this set 
   * (e.g. for view, send all access privs).  There are pre-canned sets in AccessAdapter
   * @return if the query was changed
   */
  public boolean hqlFilterStemsWhereClause(GrouperSession grouperSession, 
      Subject subject, HqlQuery hqlQuery, StringBuilder hql, 
      String stemColumn, Set<Privilege> privInSet);

  /**
   * after HQL is run, filter stems.  If you are filtering in HQL, then dont filter here
   * @param grouperSession 
   * @param stems
   * @param subject which needs view access to the groups
   * @param privInSet find a privilege which is in this set 
   * (e.g. for view, send all access privs).  There are pre-canned sets in NamingPrivilege
   * @return the set of filtered groups
   */
  public Set<Stem> postHqlFilterStems(GrouperSession grouperSession, 
      Set<Stem> stems, Subject subject, Set<Privilege> privInSet);

}
