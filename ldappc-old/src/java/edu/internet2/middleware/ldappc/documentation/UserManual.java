/*
  Copyright 2006-2007 The University Of Chicago
  Copyright 2006-2007 University Corporation for Advanced Internet Development, Inc.
  Copyright 2006-2007 EDUCAUSE
 
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

package edu.internet2.middleware.ldappc.documentation;

/**
 * <h1> Users Manual </h1>
 * <p>
 * This document describes how to use the Ldappc program. This document is not
 * yet complete. The discussion that is provided is limited to some of the
 * program documentation and does not currently include discussion of the API.
 * </p>
 * <p>
 * The user instructs the program on what actions to take using two separate
 * mechanisms, command line input and a configuration file. The command line
 * input determines the categories of data to be provisioned to the LDAP
 * database. The configuration file determines the details of how the data is to
 * be provisioned. The sections below describe these two mechanisms.
 * </p>
 * 
 * <h2>Command Line Input</h2>
 * 
 * <p>
 * The command line parameters are meant to enable a site to decide to provision
 * groups, memberships, or permissions independently. The command line is not
 * used as a criteria for selecting which sets of groups, memberships, or
 * permissions are subject to provisioning. The later is the role of the
 * configuration file and its processor.
 * </p>
 * <p>
 * The Program can be started from the command line with the following
 * parameters. Replace the names listed under the Value column with actual
 * values. Enter the keys exactly as shown.
 * </p>
 * 
 * <pre align="left">
 *  Key             Value          Description
 *  --------------  -------------  -------------------------------------------------------------------------
 *  no arguments                   Display the following list of available arguments to standard output. 
 *  -subject        subjectId      The SubjectId used to establish Grouper API and Signet API sessions.
 *                                 If any arguments are used, then &quot;-subject subjectId&quot; is required.
 *  -groups                        (Optional) When present, group information will be provisioned.
 *  -memberships                   (Optional) When present, membership information will be provisioned.
 *  -permissions                   (Optional) When present, privilege information will be provisioned.
 *  -lastModifyTime lastModifyTime (Optional) DateTime representation to select only objects changed since then.
 *                                 Allowed format: yyyy-MM-dd_hh:mm:ss or yyyy-MM-dd_hh:mm or 
 *                                 yyyy-MM-dd_hh or yyyy-MM-dd.  Missing hours, minutes, or seconds
 *                                 are replaced with zeros and the appropriate delimiters.
 *  -interval       interval       (Optional) Number of seconds between polling intervals.
 *  -configManager  configMgr      (Optional) Location of substitute configuration file, other than default.
 * </pre>
 * 
 * <p>
 * When the "-groups" argument is present, Grouper group information will be
 * provisioned into LDAP group entries per declarations in the configuration
 * file.
 * </p>
 * <p>
 * When the "-memberships" argument is present, Grouper group membership
 * information will be provisioned into LDAP entries corresponding to members
 * per declarations in the configuration file.
 * </p>
 * <p>
 * When the "-permissions" argument is present, Signet permission information
 * will be provisioned into LDAP entries corresponding to privilegees per
 * declarations in the configuration file.
 * </p>
 * <p>
 * If the "-interval" parameter is present, the Program will continue to operate
 * until killed; otherwise, it performs one provisioning cycle and terminates.
 * </p>
 * 
 * <h2>The Configuration File</h2>
 * 
 * <p>
 * Most of the details concerning the configuration file are contained in a
 * configuration file template discussed below. This section describes some
 * important concepts in how the configuration file gets used and describes some
 * details not discussed in the template.
 * </p>
 * 
 * <h3>Configuration Overview</h3>
 * 
 * <p>
 * The configuration file is an XML document organized by configuration elements
 * necessary for provisioning Grouper data, elements necessary for provisioning
 * Signet data, and elements common to both. An overview of the configuration
 * file is provided below. The reader should view the configuration file
 * template for specific details. The configuration file is designed such that
 * users only need to defined those elements specific to their situation. For
 * example, in a environment which utilizes only Grouper and not Signet, the
 * Signet configuration elements can be removed (or commented out) from the
 * configuration file.
 * </p>
 * <h3>Common Configuration Elements</h3>
 * <p>
 * There are two configuration elements common to both Signet and Grouper. They
 * are the <code>ldap</code> and <code>source-subject-identifiers</code>
 * elements. The <code>ldap</code> element defines the values necessary to
 * interact with the LDAP directory where provisioned data will reside.
 * Currently this is limited to the parameters necessary to connect with the
 * directory. The parameters are based on JNDI, and their values may vary
 * according to the directory server being used.
 * </p>
 * <p>
 * The <code>source-subject-identifiers</code> element identifies for each
 * Subject source how the provisioner locates the directory entry for a given
 * Subject. Within both Grouper and Signet, Subjects can originate from multiple
 * sources. When provisioning data for a Subject (either memberships or
 * permissions) it is necessary to locate the Subject's directory entry in order
 * to either store data (e.g., when provisioning permissions) or obtain the
 * Subject's DN (e.g., when tracking memberships by DN during group
 * provisioning). This is accomplished by defining for each source the Subject
 * attribute whose value uniquely identifies the Subject within the directory,
 * and an LDAP query filter that when combined with the attribute value locates
 * the Subject's directory entry.
 * </p>
 * <h3>Signet Configuration Elements</h3>
 * <p>
 * The <code>signet</code> element contains the configuration elements needed
 * for provisioning Signet permissions. There is one required element,
 * <code>permissions-listing</code>. This element defines how provisioned
 * permissions are stored within the directory. There are two supported storage
 * methods. One method stores each of the Subject's permissions as a child
 * <i>eduPermission</i> entry of the Subject's directory entry. This method
 * requires the <i>eduPermission</i> schema to be available to the directory
 * server. The other method stores the permissions in a string format within an
 * attribute of the Subject's directory entry. The attribute to be used on the
 * Subject's directory entry and part of the string format are defined here.
 * </p>
 * <p>
 * There are two methods for identifying the permissions to be provisioned.
 * Either all active permissions can be provisioned, or permissions can be
 * further limited by permission characteristics. The optional
 * <code>permissions-queries</code> element defines the characteristics to use
 * for selecting permissions to be provisioned. Two characteristics, functions
 * and subsystems, are supported. The <code>subsystem-queries</code> element
 * defines the subsystems to limit permissions. A permission must be associated
 * with at least one of the subsystems for it to be selected for provisioning.
 * The <code>function-queries</code> element identifies the functions to limit
 * permissions. A permission must be associated with at least one of the
 * functions for it to be selected for provisioning. If both the
 * <code>subsystem-queries</code> and <code>function-queries</code> elements
 * are defined, a permission only needs to satisfy one of those to be selected
 * for provisioning.
 * </p>
 * <h3>Grouper Configuration Elements</h3>
 * <p>
 * The <code>grouper</code> element contains the configuration elements for
 * Grouper. Whether provisioning memberships or groups, the provisioner takes a
 * group centric approach for selecting the provisioned data. In particular, the
 * provisioner selects a set of groups. This set of groups either is the
 * collection of groups to provision, or the membership of these selected groups
 * defines the memberships to be provisioned. The provisioner selects groups
 * based on subordinate stem or attribute value matching queries. The
 * <code>group-queries</code> element contains the definitions of the queries
 * to use when selecting the groups that define the provisioning data.
 * </p>
 * <p>
 * Either groups, memberships or both may be provisioned when provisioning
 * Grouper data. The <code>grouper</code> element splits the configuration
 * items for groups and memberships into the elements <code>groups</code> and
 * <code>memberships</code> respectively. The user need only define the
 * element or elements necessary for the data being provisioned. Each is
 * described further below.
 * </p>
 * <p>
 * Provisioning groups requires the provisioner to create new or maintain
 * existing Group directory entries. The <code>groups</code> element defines
 * the needed information for this. This element identifies the root entry in
 * the directory under which all group entries are maintained. It must be noted
 * that the provisioner owns all entries under this root element. In particular,
 * it can add, modify and delete any entries under the root element. The
 * provisioner creates new entries based on the objectclass defined here. The
 * Group entries can be create either in a "flat" or a "bushy" structure. If the
 * structure is flat, then all Group entries are children of the root entry and
 * the RDN of the Group entry is the value of either the "id" or "name"
 * attribute of the Group. If the structure is bushy, then a heirarchy of
 * organizationalUnit entries is created under the root entry for each element
 * of the Group's stem, the Group entry is created under the parent stem's OU
 * entry, and the RDN is set to the Group's extension. It should be noted that
 * the attribute name of the RDN of the Group may not be <i>ou</i>. This
 * prevents naming collisions between stem entries and the group entry when the
 * "bushy" structure is used.
 * </p>
 * <p>
 * The <code>groups</code> element defines two methods of storing membership
 * information in a Group's directory entry. They can be used simultaneously.
 * The first is to maintain an attribute with the list of members identified by
 * an attribute value from the associated Subject found via the Grouper API. The
 * <code>group-members-name-list</code> element defines the Group entry
 * attribute where this list is maintained. It has a child element,
 * <code>source-subject-name-mapping</code>, that defines, by Subject source,
 * the Subject attribute whose value is the indentifier stored in the list
 * (e.g., loginId attribute). If the <code>group-members-name-list</code>
 * element is not defined, then this membership list is not maintained.
 * </p>
 * <p>
 * The second storage method is to store the DN of the associated directory
 * entry for each member. The directory entry for each member is identified
 * using the information from the <code>source-subject-identifiers</code>
 * element (see above). The existence of the <code>group-members-dn-list</code>
 * element identifies that the DN list is to be maintained and defines the
 * attribute to hold the DN list. If an entry cannot be found for a member, no
 * DN is included in the membership list for that member.
 * </p>
 * <p>
 * The <code>groups</code> element also provides a means of provisioning
 * attribute values of the Group to the Group's directory entry. This is
 * accomplished using the optional <code>group-attribute-mapping</code>
 * element. This element maps a selected set of Group attributes to the
 * corresponding directory entry attributes. The value from the Group attribute
 * is provisioned to the directory entry attribute.
 * </p>
 * <p>
 * The <code>memberships</code> element contains the configuration items
 * related to provisioning memberships. It currently has only one child element,
 * <code>member-groups-list</code>. The <code>member-groups-list</code>
 * element defines how the list of Groups to which a member belongs is
 * maintained. This list is maintained on the member's associated directory
 * entry. The <code>member-groups-list</code> element defines the attribute on
 * the entry where the list is stored. It also defines the Group attribute whose
 * value is used to identify the Group within the list.
 * </p>
 * 
 * <h3>Attributes</h3>
 * 
 * <p>
 * The provisioning process expects the attribute name used to
 * query for the attribute values is the name returned by the Ldap server.
 * Depending on the attribute name defined in the configuration file this may not be the case.  
 * For example, there are many Ldap servers (e.g., OpenLDAP) that support synonyms for
 * attribute names. If a synonym is used, the server may return the canonical name 
 * rather than the synonym. Unexpected errors may occur in
 * the provisioning process in this situation. This same issue arises if the
 * Ldap server being used supports <i>attribute subclassing</i> or <i>attribute
 * language preferences</i>. Deployers will need to select attribute names 
 * appropriately for their environment.
 * </p>
 * <p>
 * The provisioning process assumes that attribute values returned from the Ldap server
 * via Java JNDI are strings.  It is possible that some attribute values are not returned as strings.
 * This is dependent on the directory server and the JNDI service provider. For example,
 * Sun's LDAP provider uses byte arrays to represent attribute values with nonstring 
 * attribute syntaxes, and strings are used to represent values of all other syntaxes. In the
 * case of Sun's LDAP provider the number of attribute values represented as byte arrays is 
 * minimal and shouldn't present a problem to the deployer (see the
 * <a href="http://java.sun.com/products/jndi/tutorial/ldap/misc/attrs.html">Java JNDI Tutorial</a>
 * for more details).  Deployers will need to select attributes appropriate for their environment.
 * </p>
 * 
 * <h3>Configuration Side Effects</h3>
 * 
 * <p>
 * It is important to understand the possible side effects of modifying the
 * configuration file after the initial provisioning. When provisioning groups
 * or memberships, the selection of groups completely determines the data being
 * provisioned. Modifying the selection parameters (e.g., modifying the
 * <code>group-queries</code> element) can result in a group or membership no
 * longer being included in the directory even though the group or membership
 * still exists within Grouper. The same issue exists when provisioning
 * permissions. Modifying the <code>permissions-queries</code> element can
 * significantly alter the permissions to be provisioned.
 * </p>
 * <p>
 * Many of the configuration elements allow a directory entry attribute to be
 * defined where a list is to be maintained. When an attribute where a list is
 * being maintained is altered, the provisioner no longer has any reference to
 * the original attribute. It will be the responsibility of the user to maintain
 * the original attribute.
 * </p>
 * <p>
 * The provisioning process assumes ownership of all attributes and entries that
 * are defined in the configuration file. It adds and deletes values from these
 * attributes and entries based completely on the data selected from within
 * Grouper or Signet. For example, any existing data in an attribute prior to
 * the provisioning process is disregarded by the provisioning process. There is
 * one exception to this. When provisioning permissions as strings, the
 * provisioning process only assumes ownership of the attribute values that
 * start with the prefix defined in the <code>permissions-listing</code>
 * element in the configuration file
 * </p>
 * <p>
 * The membership and permissions provisioning processes remove any previously
 * provisioned memberships or permissions stored on directory entries that are
 * no longer valid. This can occur for example if a Group is deleted from
 * Grouper, a privileged subject no longer exists in Signet, or the
 * <code>group-queries</code> or <code>permissions-queries</code> elements
 * are altered. This process considers any entry found using the filters defined
 * in the <code>source-subject-identifiers</code> element with the <i>{0}</i>
 * parameter replaced by <i>*</i> and having the appropriate attribute
 * populated as an entry that previously had data provisioned. These entries are
 * updated be consistent with the provisioned memberships or permissions.
 * </p>
 * 
 * <h3>The Configuration File Template</h3>
 * 
 * <p>
 * A template for creating the configuration file that controls how the data
 * categories selected on the command line us provided in the conf directory as
 * "ldappcTemplate.xml". The user should first copy this file into the config
 * directory and name it ldappc.xml. Then modify it according to the
 * instructions therein.
 * </p>
 * 
 * <h2>Ldappc Processing</h2>
 * 
 * <h3>Value Comparisons</h3>
 * 
 * <p>
 * The provisioning process needs to compare attribute values in order to determine
 * the data to be stored in an attribute, and to determine how to updated an attribute
 * efficiently.  The provisioning process uses a case insensitive string comparison of
 * values to determine equality. This can possibly effect the performance of the
 * provisioning process or the accuracy of the data stored in the directory.  Each
 * situation is described below. 
 * </p>
 * <p>
 * The provisioning process attempts to minimize the number of updates made
 * to the directory.  To do this requires comparing the current set of data stored
 * in an attribute with the set of data being provisioned to that attribute.
 * The number and type of modifications identified for the attribute are based on 
 * examining the differences in the two data sets.  The comparison of the two 
 * data sets is performed by the provisioning process using a case insensitive 
 * comparison of the value strings.  This is performed in the Java application, and
 * does not utilize any Ldap compare functionality provided by the directory.
 * As such, if the directory should modify provisioned data in any way when stored (e.g., 
 * store it in a standard format, remove leading or trailing spaces), the
 * provisioning process may perform unnecessary updates on the attribute as it 
 * may view equivalent values as different.  This situation can also occur if the 
 * directory supports a more robust comparison operation on the attribute. Although
 * unneeded updates are performed to the directory, the data will remain accurate.
 * </p>
 * <p>
 * The case insensitive string value comparison used by the provisioning process
 * may cause provisioned data to be inaccurate in the following situations.  If 
 * the data being provisioned is case sensitive, values that should be included in
 * the provisioned data set may not be.  If equality of the data being provisioned 
 * cannot be determined via string comparison, the data stored in the directory may 
 * not be accurate. For example, if a date value is being stored in a Group attribute
 * within Grouper, the two values "12 May 1990" and "May 12 1990" will not be considered 
 * equal by the provisioning process.  Both values will be included in the provisioned data
 * set.
 * </p>
 * <p>
 * There is one exception to this discussion.  When the group members DN list is being 
 * maintained on the provisioned Group entries, these values are always compared as
 * parsed DN strings.  This ensures that DNs will always compare correctly.  For example,
 * the two DNs <i>first=fred+last=flintstone,ou=bedrock</i> and <i>last=flintstone+first=fred,ou=bedrock</i>
 * will compare equal.
 * </p>
 */

public class UserManual
{
}
