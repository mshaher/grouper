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
 *
 * <p>
 * This document describes the architecture of the Ldappc program.  The RFQ
 * document <a href=../../../../../../javadocSupplement/ldapConnectorRfq_8-15-06.html>
 * Request for Quote - LDAP Connector for Grouper & Signet</a>
 * should be read before reading this document.  After reading this document, 
 * if you desire more detailed design information, go to 
 * {@link edu.internet2.middleware.ldappc.documentation.Design}. 
 * Depending on your interest, you may also wish to go to 
 * {@link edu.internet2.middleware.ldappc.documentation.DeploymentGuide} 
 * or 
 * {@link edu.internet2.middleware.ldappc.documentation.UserManual}. 
 * </p>
 *
 * <h2>Introduction</h2>
 *
 * <p>
 * The purpose of the Ldappc program is to provide a tool and an API 
 * (Application Programming Interface) for provisioning group, membership and privilege 
 * information to an LDAP server.  The data to be provisioned is dependent on the 
 * Grouper and/or Signet databases and their APIs that allow access to the data.
 *
 * The Ldappc program provides a command line interface for running the program
 * as described later.  It is also designed so that some of its capabilities constitute
 * an API that can be used by other programs.
 * </p>
 *
 * <h2>Program Environment</h2>
 *
 * <p> 
 * The Ldappc program depends the use of an LDAP (Lightweight Directory Access Protocol)
 * server.  The server used for development was the OpenLDAP server.  
 * See <a href=http://www.openldap.org/>http://www.openldap.org</a> for additional
 * information about this server.  Many alternative servers exist and should work
 * equally well, although the Ldappc documentation will limit its discussion of
 * setting up the program for the OpenLDAP server.
 * </p>
 * <p> 
 * Depending on institutional needs, one or both of the following databases and associated
 * software is needed to provide the data to be provisioned: Grouper and/or Signet.
 * These are documented at <a href=http://grouper.internet2.edu/>http://grouper.internet2.edu</a>
 * and <a href=http://signet.internet2.edu/>http://signet.internet2.edu</a> respectively.
 * </p>
 *
 * <h2>Top Level Architecture</h2>
 *
 * <p> 
 * The relationship among the various components that Ldappc interacts with and
 * the code itself is well described in the diagram in the RFQ (Request for Quote) document 
 * referred to above.  The following provides some additional details about the provisioning 
 * process. 
 * </p>
 * 
 * <h3> Provisioning Control</h3>
 *
 * <p> 
 * The provisioning of the LDAP directory is controlled using two mechanisms:
 * a program control interface and a program configuration interface.
 * </p>
 * <p> 
 * The program control interface provides simple instructions that determine 
 * the major types of data to be provisioned, instructions concerning the 
 * timestamp of the data to be selected, and the frequency of performing
 * the provisioning.  This interface is implemented in the program as
 * a processor of the command line input arguments.  However, the interface
 * can be used with other implementations by other programs that make use 
 * of the Ldappc API.
 * </p>
 * <p> 
 * Regardless of what is being provisioned, the program control interface
 * requires that a subject be identified, and all provisioning is relative 
 * to the privileges of the subject. 
 * </p>
 * <p> 
 * The program configuration interface provides simple instructions that determine 
 * how the major types of data are selected to be provisioned and where this 
 * provisioned data is to be stored in the directory.  This interface is implemented 
 * in the program as a processor of a configuration file.  However, the interface
 * can be used with other implementations by other programs that make 
 * use of the Ldappc API.
 * </p>
 *
 * <h3> Provisioning in General</h3>
 *
 * <p> 
 * Some sites will only use Signet and not use Grouper and thus will only provision 
 * permissions.  Others will only use Grouper and not Signet.  These may provision 
 * group and membership information or only groups or only memberships.  Users of the
 * tool will only have to provide information to the program configuration relevant
 * to their particular needs.
 * </p>
 *
 * The command line parameters are meant to enable a site to decide to provision 
 * these three types of information independently, if they wish. Their purpose is not 
 * related to the criteria for selecting which sets of groups or permissions are 
 * subject to provisioning.
 *
 * <h3> Provisioning Groups</h3>
 *
 * <p> 
 * The process for provisioning groups begins with selecting the groups to provision.  
 * This is accomplished using the selection criteria provided in the configuration file.
 * For each selected group, using the group's DN (distinguished name), created as defined in the configuration 
 * file, the application determines if an entry already exists for the group in the directory, or 
 * a new entry must be created.  If the entry exists, it is updated with current membership data.
 * If the entry does not exist, the application creates a new entry for the group and stores its
 * membership data. 
 * </p>
 * <p>
 * There are two methods of storing membership data in the group entry. Either one or
 * both maybe used.  The first method stores the list of members in an attribute of the 
 * group entry (e.g., hasMember attribute).  Each member is identified by an attribute 
 * value of the associated Subject.  The Subject's identifying attribute
 * is defined in the configuration file.  
 * </p>
 * <p>
 * The second method stores the DN of the member's LDAP entry in an attribute of the group 
 * entry.  A LDAP query filter allowing the application to lookup each member's associated 
 * LDAP entry is provided in the configuration file.  The DN of this entry is stored in an
 * attribute of the Group entry. 
 * </p>
 *
 * <h3>Provisioning Memberships</h3>  
 *
 * <p> 
 * The process for provisioning memberships is group centric. It begins with selecting a set of
 * groups.  It is the memberships of those groups which are to be to provisioned.  Selecting the 
 * groups is accomplished using the selection criteria provided in the configuration file.  For 
 * each member (i.e., subject) of the selected groups, the application, using an LDAP search filter 
 * defined in the configuration file, attempts to locate the LDAP entry for the member.  If an 
 * entry is not found for the member (i.e., subject), no action is taken.  If an entry is found,
 * then the application lists the groups to which the member belongs in an attribute of the 
 * member's LDAP entry (e.g., isMemberOf).  The attribute used is defined in the configuration 
 * file. The list of groups is a subset of the groups selected, and each is identified by
 * the value of an attribute (e.g., name) defined in the configuration.
 * </p>
 * 
 * <h3>Provisioning Permissions</h3>  
 *
 * <p>
 * The process for provisioning permissions begins with selecting all of the Subjects which
 * have been assigned privileges.  For each subject, the permissions to be provision are
 * selected.  Selecting permissions occurs in two ways as defined in the application
 * configuration.  Either all active permissions are selected, or permissions are selected
 * based on permission characteristics defined in the configuration.  After identifying a Subject
 * and its permissions to be provisioned, the application uses an LDAP query filter defined in 
 * the configuration to lookup the Subject's LDAP entry.  If no entry is found, no action takes 
 * place.  If the entry is found, the Subject's current permissions replace any existing 
 * permissions already stored in the entry.  
 * </p>
 * <p>
 * Permissions are stored in one of two ways.  In the first, an <i>eduPermission</i> entry is
 * created for each permission as a child of the Subject's entry.  In the second, each 
 * permission is stored in an attribute of the Subject's entry as a string.  The format of the permission
 * string and the attribute where the permissions are stored are defined in the configuration 
 * file.
 * </p>
 * 
 * <h3>Provisioning Schema</h3>  
 * 
 * <p> 
 * To accomplish the above provisioning, additional schema are needed beyond
 * those that are standard LDAP schema.  These are included in a file,
 * ldappc.schema and include objectclass definitions for <i>eduPermissions</i>
 * and <i>eduMember</i>. The eduPermissions objectclass is required if the application
 * is to store provisioned permissions as eduPermission entries.  The eduMember objectclass
 * is provided as convience as it includes "hasMember" and "isMemberOf" attributes used to
 * describe membership relationships.  The eduMember objectclass is used for the default 
 * configuration of the application.  This schema file needs to be made available to the 
 * LDAP server being used.  
 * </p>
 *
 * <h2>Changes to the RFQ</h2>  
 * 
 * <p> 
 * The following is list of changes to the original RFQ. 
 * </p> 
 * <p> 
 * All references to "auth2ldap" should be replaced by "ldappc". 
 * </p> 
 * <p> 
 * In section 3.1, replace "-privileges" with "-permissions".
 * </p> 
 * <p> 
 * In section 3.6, replace "Three" with "Two".
 * </p> 
 */

public class Architecture
{
}
