/*******************************************************************************
 * Copyright 2012 Internet2
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
/**
 * @author mchyzer
 * $Id: PrivilegeSubjectContainerImpl.java 8245 2012-04-24 13:45:50Z mchyzer $
 */
package edu.internet2.middleware.grouper.membership;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.collections.keyvalue.MultiKey;
import org.apache.commons.lang.StringUtils;

import edu.internet2.middleware.grouper.Field;
import edu.internet2.middleware.grouper.FieldFinder;
import edu.internet2.middleware.grouper.Group;
import edu.internet2.middleware.grouper.GrouperSession;
import edu.internet2.middleware.grouper.Member;
import edu.internet2.middleware.grouper.Membership;
import edu.internet2.middleware.grouper.Stem;
import edu.internet2.middleware.grouper.SubjectFinder;
import edu.internet2.middleware.grouper.exception.GrouperSessionException;
import edu.internet2.middleware.grouper.misc.GrouperSessionHandler;
import edu.internet2.middleware.grouper.subj.SubjectBean;
import edu.internet2.middleware.grouper.subj.SubjectHelper;
import edu.internet2.middleware.grouper.util.GrouperUtil;
import edu.internet2.middleware.subject.Subject;


/**
 * in a list of memberships, this is one subject.  this only works when filtering for one owner
 */
public class MembershipSubjectContainer {

  /** subject */
  private Subject subject;

  /**
   * add effective memberships for inheritance of privileges or 
   * GrouperAll for stem
   * @param membershipSubjectContainers
   */
  public static void considerNamingPrivilegeInheritance(final Set<MembershipSubjectContainer> membershipSubjectContainers,
      final Stem stem) {

    GrouperSession.callbackGrouperSession(GrouperSession.staticGrouperSession(), new GrouperSessionHandler() {
      
      /**
       * 
       */
      @Override
      public Object callback(GrouperSession grouperSession) throws GrouperSessionException {
        boolean grouperAllHasStem = stem.hasStem(SubjectFinder.findAllSubject());
        boolean grouperAllHasCreate = grouperAllHasStem || stem.hasCreate(SubjectFinder.findAllSubject());
        boolean grouperAllHasAttrRead = grouperAllHasStem || grouperAllHasCreate || stem.hasStemAttrRead(SubjectFinder.findAllSubject());
        boolean grouperAllHasAttrUpdate = grouperAllHasStem || grouperAllHasCreate || stem.hasStemAttrUpdate(SubjectFinder.findAllSubject());
        
        Set<String> stemFieldNames = GrouperUtil.toSet(Field.FIELD_NAME_STEMMERS, Field.FIELD_NAME_CREATORS, 
            Field.FIELD_NAME_STEM_ATTR_READERS, Field.FIELD_NAME_STEM_ATTR_UPDATERS);
        
        Subject rootSubject = SubjectFinder.findRootSubject();
        Subject everyEntitySubject = SubjectFinder.findAllSubject();
        
        for (MembershipSubjectContainer membershipSubjectContainer : membershipSubjectContainers) {
          
          Subject subject = membershipSubjectContainer.getSubject();
          
          //if we are on grouper system
          if (SubjectHelper.eq(subject, rootSubject)) {
            
            for (String fieldName : stemFieldNames) {
              //it is also effective, merge that with whatever was there
              membershipSubjectContainer.addMembership(fieldName, MembershipAssignType.EFFECTIVE);
            }
            
          } else {
            //else
            boolean isEveryEntity = SubjectHelper.eq(everyEntitySubject, membershipSubjectContainer.getSubject());
            
            //see what the subject has
            boolean subjectHasStemEffective = membershipSubjectContainer.getMembershipContainers().get(Field.FIELD_NAME_STEMMERS) != null
                && membershipSubjectContainer.getMembershipContainers().get(Field.FIELD_NAME_STEMMERS).getMembershipAssignType().isNonImmediate()
                || (isEveryEntity ? false : grouperAllHasStem);

            boolean subjectHasStem = membershipSubjectContainer.getMembershipContainers().get(Field.FIELD_NAME_STEMMERS) != null 
                || subjectHasStemEffective || grouperAllHasStem;
            
            boolean subjectHasCreateEffective = membershipSubjectContainer.getMembershipContainers().get(Field.FIELD_NAME_CREATORS) != null
                && membershipSubjectContainer.getMembershipContainers().get(Field.FIELD_NAME_CREATORS).getMembershipAssignType().isNonImmediate()
                || subjectHasStem || (isEveryEntity ? false : grouperAllHasCreate);
            
            boolean subjectHasCreate = membershipSubjectContainer.getMembershipContainers().get(Field.FIELD_NAME_CREATORS) != null 
                || subjectHasCreateEffective || grouperAllHasCreate;
            
            boolean subjectHasAttrReadEffective = membershipSubjectContainer.getMembershipContainers().get(Field.FIELD_NAME_STEM_ATTR_READERS) != null
                && membershipSubjectContainer.getMembershipContainers().get(Field.FIELD_NAME_STEM_ATTR_READERS).getMembershipAssignType().isNonImmediate()
                || subjectHasCreate || subjectHasStem || (isEveryEntity ? false : grouperAllHasAttrRead);
            
            boolean subjectHasAttrUpdateEffective = membershipSubjectContainer.getMembershipContainers().get(Field.FIELD_NAME_STEM_ATTR_UPDATERS) != null
                && membershipSubjectContainer.getMembershipContainers().get(Field.FIELD_NAME_STEM_ATTR_UPDATERS).getMembershipAssignType().isNonImmediate()
                || subjectHasCreate || subjectHasStem  || (isEveryEntity ? false : grouperAllHasAttrUpdate);

            //if the subject has an effective stem priv, add it in
            if (subjectHasStemEffective) {
              membershipSubjectContainer.addMembership(Field.FIELD_NAME_STEMMERS, MembershipAssignType.EFFECTIVE);
            }
            if (subjectHasCreateEffective) {
              membershipSubjectContainer.addMembership(Field.FIELD_NAME_CREATORS, MembershipAssignType.EFFECTIVE);
            }
            if (subjectHasAttrReadEffective) {
              membershipSubjectContainer.addMembership(Field.FIELD_NAME_STEM_ATTR_READERS, MembershipAssignType.EFFECTIVE);
            }
            if (subjectHasAttrUpdateEffective) {
              membershipSubjectContainer.addMembership(Field.FIELD_NAME_STEM_ATTR_UPDATERS, MembershipAssignType.EFFECTIVE);
            }
            
          }
          
          
        }
        
        return null;
      }
    });

  }

  /**
   * consider a new membership
   * @param fieldName
   * @param membershipAssignType
   */
  public void addMembership(String fieldName, MembershipAssignType newMembershipAssignType) {
    MembershipContainer membershipContainer = this.getMembershipContainers().get(fieldName);

    if(membershipContainer != null) {
      //maybe we need to change or set the type
      newMembershipAssignType = MembershipAssignType.convert(membershipContainer.getMembershipAssignType(),newMembershipAssignType);
      membershipContainer.setMembershipAssignType(newMembershipAssignType);
    } else {
      //create a new one where one didnt exist before
      membershipContainer = new MembershipContainer(fieldName, newMembershipAssignType);
      this.getMembershipContainers().put(fieldName, membershipContainer);
    }
  }
  
  /**
   * member
   */
  private Member member;
  
  /**
   * member
   * @return the member
   */
  public Member getMember() {
    return this.member;
  }

  /**
   * member
   * @param member1
   */
  public void setMember(Member member1) {
    this.member = member1;
  }

  /**
   * membership containers for field
   */
  private Map<String, MembershipContainer> membershipContainers;
  
  /**
   * @see edu.internet2.middleware.grouper.privs.PrivilegeSubjectContainer#getPrivilegeContainers()
   */
  public Map<String, MembershipContainer> getMembershipContainers() {
    return this.membershipContainers;
  }

  /**
   * @see edu.internet2.middleware.grouper.privs.PrivilegeSubjectContainer#getSubject()
   */
  public Subject getSubject() {
    return this.subject;
  }

  
  /**
   * @param subject1 the subject to set
   */
  public void setSubject(Subject subject1) {
    this.subject = subject1;
  }

  
  /**
   * @param privilegeContainers1 the privilegeContainers to set
   */
  public void setMembershipContainers(Map<String, MembershipContainer> privilegeContainers1) {
    this.membershipContainers = privilegeContainers1;
  }

  /**
   * @see Object#toString()
   */
  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    if (this.subject == null) {
      result.append("Subject: null");
    } else {
      result.append(GrouperUtil.subjectToString(this.subject));
    }
    result.append(": ");
    if (GrouperUtil.length(this.membershipContainers) == 0) {
      result.append(" no memberships");
    } else {
      
      Set<String> fieldNameSet = this.membershipContainers.keySet();
      int index = 0;
      for (String fieldName: fieldNameSet) {
        result.append(this.membershipContainers.get(fieldName));
        if (index < fieldNameSet.size()-1) {
          result.append(", ");
        }
        index++;
      }
    }
    return result.toString();
  }
  
  /**
   * add effective memberships for inheritance of privileges or 
   * GrouperAll for group
   * @param membershipSubjectContainers
   */
  public static void considerAccessPrivilegeInheritance(final Set<MembershipSubjectContainer> membershipSubjectContainers,
      final Group group) {
  
    GrouperSession.callbackGrouperSession(GrouperSession.staticGrouperSession(), new GrouperSessionHandler() {
      
      /**
       * 
       */
      @Override
      public Object callback(GrouperSession grouperSession) throws GrouperSessionException {
        boolean grouperAllHasAdmin = group.hasAdmin(SubjectFinder.findAllSubject());
        boolean grouperAllHasUpdate = grouperAllHasAdmin || group.hasUpdate(SubjectFinder.findAllSubject());
        boolean grouperAllHasRead = grouperAllHasAdmin || group.hasRead(SubjectFinder.findAllSubject());
        boolean grouperAllHasOptin = grouperAllHasAdmin || group.hasOptin(SubjectFinder.findAllSubject());
        boolean grouperAllHasOptout = grouperAllHasAdmin || group.hasOptout(SubjectFinder.findAllSubject());
        boolean grouperAllHasAttrRead = grouperAllHasAdmin || group.hasGroupAttrRead(SubjectFinder.findAllSubject());
        boolean grouperAllHasAttrUpdate = grouperAllHasAdmin || group.hasGroupAttrUpdate(SubjectFinder.findAllSubject());
        boolean grouperAllHasView = grouperAllHasAdmin || grouperAllHasUpdate || grouperAllHasRead 
            || grouperAllHasOptin || grouperAllHasOptout || grouperAllHasAttrRead || grouperAllHasAttrUpdate
            || group.hasView(SubjectFinder.findAllSubject());
        
        Set<String> groupFieldNames = GrouperUtil.toSet(Field.FIELD_NAME_ADMINS, Field.FIELD_NAME_UPDATERS, 
            Field.FIELD_NAME_GROUP_ATTR_READERS, Field.FIELD_NAME_GROUP_ATTR_UPDATERS,
            Field.FIELD_NAME_READERS, Field.FIELD_NAME_OPTINS, Field.FIELD_NAME_OPTOUTS, Field.FIELD_NAME_VIEWERS);
        
        Subject rootSubject = SubjectFinder.findRootSubject();
        Subject everyEntitySubject = SubjectFinder.findAllSubject();
        
        for (MembershipSubjectContainer membershipSubjectContainer : membershipSubjectContainers) {
          
          Subject subject = membershipSubjectContainer.getSubject();
          
          //if we are on grouper system
          if (SubjectHelper.eq(subject, rootSubject)) {
            
            for (String fieldName : groupFieldNames) {
              //it is also effective, merge that with whatever was there
              membershipSubjectContainer.addMembership(fieldName, MembershipAssignType.EFFECTIVE);
            }
            
          } else {
            //else
            boolean isEveryEntity = SubjectHelper.eq(everyEntitySubject, membershipSubjectContainer.getSubject());
            
            //see what the subject has
            boolean subjectHasAdminEffective = membershipSubjectContainer.getMembershipContainers().get(Field.FIELD_NAME_ADMINS) != null
                && membershipSubjectContainer.getMembershipContainers().get(Field.FIELD_NAME_ADMINS).getMembershipAssignType().isNonImmediate()
                || (isEveryEntity ? false : grouperAllHasAdmin);
  
            boolean subjectHasAdmin = membershipSubjectContainer.getMembershipContainers().get(Field.FIELD_NAME_ADMINS) != null 
                || subjectHasAdminEffective || grouperAllHasAdmin;
            
            boolean subjectHasUpdateEffective = membershipSubjectContainer.getMembershipContainers().get(Field.FIELD_NAME_UPDATERS) != null
                && membershipSubjectContainer.getMembershipContainers().get(Field.FIELD_NAME_UPDATERS).getMembershipAssignType().isNonImmediate()
                || subjectHasAdmin || (isEveryEntity ? false : grouperAllHasUpdate);
            
            boolean subjectHasUpdate = membershipSubjectContainer.getMembershipContainers().get(Field.FIELD_NAME_UPDATERS) != null 
                || subjectHasUpdateEffective || grouperAllHasUpdate;
            
            boolean subjectHasReadEffective = membershipSubjectContainer.getMembershipContainers().get(Field.FIELD_NAME_READERS) != null
                && membershipSubjectContainer.getMembershipContainers().get(Field.FIELD_NAME_READERS).getMembershipAssignType().isNonImmediate()
                || subjectHasAdmin || (isEveryEntity ? false : grouperAllHasRead);
            
            boolean subjectHasRead = membershipSubjectContainer.getMembershipContainers().get(Field.FIELD_NAME_READERS) != null 
                || subjectHasReadEffective || grouperAllHasRead;
            
            boolean subjectHasOptinEffective = membershipSubjectContainer.getMembershipContainers().get(Field.FIELD_NAME_OPTINS) != null
                && membershipSubjectContainer.getMembershipContainers().get(Field.FIELD_NAME_OPTINS).getMembershipAssignType().isNonImmediate()
                || subjectHasAdmin || (isEveryEntity ? false : grouperAllHasOptin);
            
            boolean subjectHasOptin = membershipSubjectContainer.getMembershipContainers().get(Field.FIELD_NAME_OPTINS) != null 
                || subjectHasOptinEffective || grouperAllHasOptin;
            
            boolean subjectHasOptoutEffective = membershipSubjectContainer.getMembershipContainers().get(Field.FIELD_NAME_OPTOUTS) != null
                && membershipSubjectContainer.getMembershipContainers().get(Field.FIELD_NAME_OPTOUTS).getMembershipAssignType().isNonImmediate()
                || subjectHasAdmin || (isEveryEntity ? false : grouperAllHasOptout);
            
            boolean subjectHasOptout = membershipSubjectContainer.getMembershipContainers().get(Field.FIELD_NAME_OPTOUTS) != null 
                || subjectHasReadEffective || grouperAllHasOptout;

            boolean subjectHasAttrReadEffective = membershipSubjectContainer.getMembershipContainers().get(Field.FIELD_NAME_GROUP_ATTR_READERS) != null
                && membershipSubjectContainer.getMembershipContainers().get(Field.FIELD_NAME_GROUP_ATTR_READERS).getMembershipAssignType().isNonImmediate()
                || subjectHasAdmin || (isEveryEntity ? false : grouperAllHasAttrRead);

            boolean subjectHasAttrRead = membershipSubjectContainer.getMembershipContainers().get(Field.FIELD_NAME_GROUP_ATTR_READERS) != null 
                || subjectHasAttrReadEffective || grouperAllHasAttrRead;

            boolean subjectHasAttrUpdateEffective = membershipSubjectContainer.getMembershipContainers().get(Field.FIELD_NAME_GROUP_ATTR_UPDATERS) != null
                && membershipSubjectContainer.getMembershipContainers().get(Field.FIELD_NAME_GROUP_ATTR_UPDATERS).getMembershipAssignType().isNonImmediate()
                || subjectHasAdmin  || (isEveryEntity ? false : grouperAllHasAttrUpdate);

            boolean subjectHasAttrUpdate = membershipSubjectContainer.getMembershipContainers().get(Field.FIELD_NAME_GROUP_ATTR_UPDATERS) != null 
                || subjectHasAttrUpdateEffective || grouperAllHasAttrUpdate;
            
            boolean subjectHasViewEffective = membershipSubjectContainer.getMembershipContainers().get(Field.FIELD_NAME_VIEWERS) != null
                && membershipSubjectContainer.getMembershipContainers().get(Field.FIELD_NAME_VIEWERS).getMembershipAssignType().isNonImmediate()
                || subjectHasAdmin || subjectHasUpdate || subjectHasRead || subjectHasOptout || subjectHasOptin
                || subjectHasAttrUpdate || subjectHasAttrRead || (isEveryEntity ? false : grouperAllHasView);


            //if the subject has an effective stem priv, add it in
            if (subjectHasAdminEffective) {
              membershipSubjectContainer.addMembership(Field.FIELD_NAME_ADMINS, MembershipAssignType.EFFECTIVE);
            }
            if (subjectHasUpdateEffective) {
              membershipSubjectContainer.addMembership(Field.FIELD_NAME_UPDATERS, MembershipAssignType.EFFECTIVE);
            }
            if (subjectHasReadEffective) {
              membershipSubjectContainer.addMembership(Field.FIELD_NAME_READERS, MembershipAssignType.EFFECTIVE);
            }
            if (subjectHasViewEffective) {
              membershipSubjectContainer.addMembership(Field.FIELD_NAME_VIEWERS, MembershipAssignType.EFFECTIVE);
            }
            if (subjectHasOptinEffective) {
              membershipSubjectContainer.addMembership(Field.FIELD_NAME_OPTINS, MembershipAssignType.EFFECTIVE);
            }
            if (subjectHasOptoutEffective) {
              membershipSubjectContainer.addMembership(Field.FIELD_NAME_OPTOUTS, MembershipAssignType.EFFECTIVE);
            }
            if (subjectHasAttrReadEffective) {
              membershipSubjectContainer.addMembership(Field.FIELD_NAME_GROUP_ATTR_READERS, MembershipAssignType.EFFECTIVE);
            }
            if (subjectHasAttrUpdateEffective) {
              membershipSubjectContainer.addMembership(Field.FIELD_NAME_GROUP_ATTR_UPDATERS, MembershipAssignType.EFFECTIVE);
            }
            
          }
          
          
        }
        
        return null;
      }
    });
  
  }

  /**
   * convert memberships into membership subject containers
   * @param membershipResults
   * @return the containers per user
   */
  public static Set<MembershipSubjectContainer> convertFromMembershipsOwnersMembers(Set<Object[]> memberships) {
    
    //can only do for one owner...
    String ownerId = null;
    
    //lets put it all back together...
    Set<MembershipSubjectContainer> results = new LinkedHashSet<MembershipSubjectContainer>();

    if (GrouperUtil.length(memberships) > 0) {

      //lets get all the subjects by member id
      Map<String, Subject> memberIdToSubject = new HashMap<String, Subject>();
      
      {
        Map<String, SubjectBean> memberIdToSubjectBean = new HashMap<String, SubjectBean>();
        Set<SubjectBean> subjectBeans = new HashSet<SubjectBean>();
        for (Object[] membershipResult : memberships) {
          Member member = (Member)membershipResult[2];
          SubjectBean subjectBean = new SubjectBean(member.getSubjectId(), member.getSubjectSourceId());
          memberIdToSubjectBean.put(member.getUuid(), subjectBean);
          subjectBeans.add(subjectBean);
        }
        Map<SubjectBean, Subject> subjectBeanToSubject = SubjectFinder.findBySubjectBeans(subjectBeans);
    
        for (String memberId : memberIdToSubjectBean.keySet()) {
          SubjectBean subjectBean = memberIdToSubjectBean.get(memberId);
          Subject subject = subjectBeanToSubject.get(subjectBean);
          memberIdToSubject.put(memberId, subject);
        }
      }
      
      //memberId to membership subject container
      Map<String, MembershipSubjectContainer> memberIdToResultsMap = new HashMap<String, MembershipSubjectContainer>();

      //key is member id, source and subject
      Map<String, List<Object[]>> memberIdToMembershipsMap = new HashMap<String, List<Object[]>>();
      
      //this multikey is sourceid, subjectid, ownerid, fieldid, 
      Map<MultiKey, MembershipAssignType> membershipAssignTypeMap = new HashMap<MultiKey, MembershipAssignType>();
      
      //lets get all the members first, and keep the answer
      for (Object[] objectArray: memberships) {
        
        Member member = (Member)objectArray[2];
        
        MembershipSubjectContainer membershipSubjectContainer = memberIdToResultsMap.get(member.getUuid());
        if (membershipSubjectContainer == null) {
          membershipSubjectContainer = new MembershipSubjectContainer();
          membershipSubjectContainer.setSubject(memberIdToSubject.get(member.getUuid()));
          membershipSubjectContainer.setMember(member);
          memberIdToResultsMap.put(member.getUuid(), membershipSubjectContainer);
          results.add(membershipSubjectContainer);
        }            

        Membership membership = (Membership)objectArray[0];

        List<Object[]> membershipList = memberIdToMembershipsMap.get(member.getUuid());
        
        if (membershipList == null) {

          membershipList = new ArrayList<Object[]>();
          
          memberIdToMembershipsMap.put(member.getUuid(), membershipList);

        }

        membershipList.add(objectArray);
        
        String theOwnerId = membership.getOwnerId();
        
        if (StringUtils.isBlank(ownerId)) {
          ownerId = theOwnerId;
        } else {
          if (!StringUtils.equals(ownerId, theOwnerId)) {
            throw new RuntimeException("Cant make a membership subject container for different sources: " 
                + ownerId + ", " + theOwnerId);
          }
        }
        
        MultiKey membershipAssignTypeKey = new MultiKey(member.getSubjectSourceId(), member.getSubjectId(), 
            theOwnerId, membership.getFieldId());
        
        MembershipAssignType membershipAssignType = membershipAssignTypeMap.get(membershipAssignTypeKey);
        membershipAssignType = MembershipAssignType.convertMembership(membershipAssignType, membership);
        membershipAssignTypeMap.put(membershipAssignTypeKey, membershipAssignType);
        
      }
      
      for (MembershipSubjectContainer membershipSubjectContainer : results) {
        
        membershipSubjectContainer.setMembershipContainers(new TreeMap<String, MembershipContainer>());
        
        Member containerMember = membershipSubjectContainer.getMember();
        
        //lets get the memberships
        List<Object[]> membershipList = memberIdToMembershipsMap.get(containerMember.getUuid());
        
        if (membershipList != null) {
          for (Object[] objectArray : membershipList) {
            
            Membership membership = (Membership)objectArray[0];
            
            Member member = (Member)objectArray[2];
            Field field = FieldFinder.findById(membership.getFieldId(), true);
            
            //multiple memberships could have the same result, just skip if already set
            if (membershipSubjectContainer.getMembershipContainers().get(field.getName()) == null) {
              MembershipContainer membershipContainer = new MembershipContainer();
              membershipContainer.setFieldName(field.getName());
              
              //if the subject, field, groupId match, then correlate the assign type...
              
              MultiKey membershipAssignTypeKey = new MultiKey(member.getSubjectSourceId(), member.getSubjectId(), ownerId, membership.getFieldId());
  
              MembershipAssignType membershipAssignType = membershipAssignTypeMap.get(membershipAssignTypeKey);
              if (membershipAssignType == null) {
                throw new RuntimeException("Why is result not there???");
              }
              membershipContainer.setMembershipAssignType(membershipAssignType);
              membershipSubjectContainer.getMembershipContainers().put(field.getName(), membershipContainer);
              
            }
          }
        }
      }
    }
    return results;
  }
  
}