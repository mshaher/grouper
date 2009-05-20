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

import edu.internet2.middleware.grouper.Stem.Scope;
import edu.internet2.middleware.grouper.exception.GrouperRuntimeException;
import edu.internet2.middleware.grouper.exception.InsufficientPrivilegeException;
import edu.internet2.middleware.grouper.exception.RevokePrivilegeException;
import edu.internet2.middleware.grouper.exception.SchemaException;
import edu.internet2.middleware.grouper.exception.StemAddException;
import edu.internet2.middleware.grouper.privs.AccessPrivilege;
import edu.internet2.middleware.grouper.privs.NamingPrivilege;
import edu.internet2.middleware.grouper.privs.Privilege;
import junit.textui.TestRunner;


/**
 * Test {@link Stem}.
 * <p/>
 * @author  blair christensen.
 * @version $Id: Test_api_Stem.java,v 1.12.2.1 2009-04-10 18:44:21 mchyzer Exp $
 * @since   1.2.1
 */
public class Test_api_Stem extends GrouperTest {


  private Group           child_group, top_group;
  private GrouperSession  s;
  private Stem            child, root, top;

  /**
   * 
   */
  public Test_api_Stem() {
    super();
  }

  /**
   * @param name
   */
  public Test_api_Stem(String name) {
    super(name);
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    TestRunner.run(new Test_api_Stem("test_getChildGroups_PrivilegeArrayAndScope_createPrivAndSubScope"));
    //TestRunner.run(Test_api_Stem.class);
  }

  /** size before getting started */
  private int originalRootGroupSubSize = -1;
  
  /** original chld stem size */
  private int originalRootChildStemSize = -1;
  
  /** original */
  private int originalRootChildStemOneSize = -1;
  
  /** original */
  private int originalRootChildStemSubSize = -1;
  
  /** original */
  private int originalRootCreateOne = -1;
  
  /** original */
  private int originalRootCreateSub = -1;
  
  /** original */
  private int originalRootViewOne = -1;
  
  /** original */
  private int originalRootViewSub = -1;
  
  /** original */
  private int originalRootCreateAndViewOne = -1;
  
  /** original */
  private int originalRootCreateAndViewSub = -1;
  
  /**
   * 
   * @see edu.internet2.middleware.grouper.GrouperTest#setUp()
   */
  public void setUp() {
    super.setUp();
    try {
      this.s            = GrouperSession.start( SubjectFinder.findRootSubject() );
      this.root         = StemFinder.findRootStem(this.s);
      
      this.originalRootGroupSubSize = this.root.getChildGroups(Stem.Scope.SUB).size();
      this.originalRootChildStemSize = this.root.getChildStems().size();
      this.originalRootChildStemOneSize = this.root.getChildStems(Stem.Scope.ONE).size();
      this.originalRootChildStemSubSize = this.root.getChildStems(Stem.Scope.SUB).size();
      
      this.originalRootCreateOne =  this.root.getChildStems( 
          new Privilege[]{NamingPrivilege.CREATE}, Stem.Scope.ONE ).size();
      this.originalRootCreateSub =  this.root.getChildStems( 
          new Privilege[]{NamingPrivilege.CREATE}, Stem.Scope.SUB ).size();
      this.originalRootViewOne =  this.root.getChildStems( 
          new Privilege[]{AccessPrivilege.VIEW}, Stem.Scope.ONE ).size();
      this.originalRootViewSub =  this.root.getChildStems( 
          new Privilege[]{AccessPrivilege.VIEW}, Stem.Scope.SUB ).size();
      this.originalRootCreateAndViewOne =  this.root.getChildStems( 
          new Privilege[]{NamingPrivilege.CREATE, AccessPrivilege.VIEW}, Stem.Scope.ONE ).size();
      this.originalRootCreateAndViewSub =  this.root.getChildStems( 
          new Privilege[]{NamingPrivilege.CREATE, AccessPrivilege.VIEW}, Stem.Scope.SUB ).size();
      
      this.top          = this.root.addChildStem("top", "top");
      this.top_group    = this.top.addChildGroup("top group", "top group");
      this.child        = this.top.addChildStem("child", "child");
      this.child_group  = this.child.addChildGroup("child group", "child group");
    }
    catch (Exception e) {
      throw new GrouperRuntimeException( "test setUp() error: " + e.getMessage(), e );
    }
  }

  /**
   * 
   * @see edu.internet2.middleware.grouper.GrouperTest#tearDown()
   */
  public void tearDown() {
    super.tearDown();
  }



  public void test_getChildGroups_fromRoot() {
    assertEquals( 0, this.root.getChildGroups().size() );
  }

  public void test_getChildGroups_fromTop() {
    assertEquals( 1, this.top.getChildGroups().size() );
  }

  public void test_getChildGroups_fromChild() {
    assertEquals( 1, this.child.getChildGroups().size() );
  }

  public void test_getChildGroups_Scope_nullScope() {
    try {
      this.root.getChildGroups(null);
      fail("failed to throw IllegalArgumentException");
    }
    catch (IllegalArgumentException eExpected) {
      assertTrue("threw expected exception", true);
    }
  }



  public void test_getChildGroups_PrivilegeArrayAndScope_nullArray() {
    try {
      this.root.getChildGroups(null, (Scope)null);
      fail("failed to throw IllegalArgumentException");
    }
    catch (IllegalArgumentException eExpected) {
      assertTrue("threw expected exception", true);
    }
  }
  public void test_getChildGroups_PrivilegeArrayAndScope_nullScope() {
    try {
      this.root.getChildGroups( new Privilege[0], null );
      fail("failed to throw IllegalArgumentException");
    }
    catch (IllegalArgumentException eExpected) {
      assertTrue("threw expected exception", true);
    }
  }
  public void test_getChildGroups_PrivilegeArrayAndScope_emptyArray() {
    Set<Group> childGroups = this.top.getChildGroups( new Privilege[0], Stem.Scope.SUB );
    assertEquals( 0, childGroups.size() );
  }
  public void test_getChildGroups_PrivilegeArrayAndScope_createPrivAndOneScope() {
    Privilege[] privs = { NamingPrivilege.CREATE };
    Set<Group> childGroups = this.top.getChildGroups( privs, Stem.Scope.ONE );
    assertEquals( 0, childGroups.size() );
  }
  public void test_getChildGroups_PrivilegeArrayAndScope_createPrivAndSubScope() {
    Privilege[] privs = { NamingPrivilege.CREATE };
    assertEquals( 0, this.top.getChildGroups( privs, Stem.Scope.SUB ).size() );
  }
  public void test_getChildGroups_PrivilegeArrayAndScope_viewPrivAndOneScope() {
    Privilege[] privs = { AccessPrivilege.VIEW };
    assertEquals( 1, this.top.getChildGroups( privs, Stem.Scope.ONE ).size() );
  }
  public void test_getChildGroups_PrivilegeArrayAndScope_viewPrivAndSubScope() {
    Privilege[] privs = { AccessPrivilege.VIEW };
    assertEquals( 2, this.top.getChildGroups( privs, Stem.Scope.SUB ).size() );
  }
  public void test_getChildGroups_PrivilegeArrayAndScope_createAndViewPrivsAndOneScope() {
    Privilege[] privs = { NamingPrivilege.CREATE, AccessPrivilege.VIEW };
    assertEquals( 1, this.top.getChildGroups( privs, Stem.Scope.ONE ).size() );
  }
  public void test_getChildGroups_PrivilegeArrayAndScope_createAndViewPrivsAndSubScope() {
    Privilege[] privs = { NamingPrivilege.CREATE, AccessPrivilege.VIEW };
    assertEquals( 2, this.top.getChildGroups( privs, Stem.Scope.SUB ).size() );
  }



  public void test_getChildGroups_Scope_fromRootScopeONE() {
    assertEquals( 0, this.root.getChildGroups(Stem.Scope.ONE).size() );
  }

  public void test_getChildGroups_Scope_fromRootScopeSUB() {
    assertEquals( this.originalRootGroupSubSize + 2, this.root.getChildGroups(Stem.Scope.SUB).size() );
  }

  public void test_getChildGroups_Scope_fromTopScopeONE() {
    assertEquals( 1, this.top.getChildGroups(Stem.Scope.ONE).size() );
  }

  public void test_getChildGroups_Scope_fromTopScopeSUB() {
    assertEquals( 2, this.top.getChildGroups(Stem.Scope.SUB).size() );
  }

  public void test_getChildGroups_Scope_fromChildScopeONE() {
    assertEquals( 1, this.child.getChildGroups(Stem.Scope.ONE).size() );
  }

  public void test_getChildGroups_Scope_fromChildScopeSUB() {
    assertEquals( 1, this.child.getChildGroups(Stem.Scope.SUB).size() );
  }



  public void test_getChildStems_fromRoot() {
    assertEquals( this.originalRootChildStemSize + 1, this.root.getChildStems().size() );
  }

  public void test_getChildStems_fromTop() {
    assertEquals( 1, this.top.getChildStems().size() );
  }

  public void test_getChildStems_fromChild() {
    assertEquals( 0, this.child.getChildStems().size() );
  }



  public void test_getChildStems_Scope_nullScope() {
    try {
      this.root.getChildStems(null);
      fail("failed to throw IllegalArgumentException");
    }
    catch (IllegalArgumentException eExpected) {
      assertTrue("threw expected exception", true);
    }
  }

  public void test_getChildStems_Scope_fromRootScopeONE() {
    assertEquals( this.originalRootChildStemOneSize + 1, this.root.getChildStems(Stem.Scope.ONE).size() );
  }

  public void test_getChildStems_Scope_fromRootScopeSUB() {
    assertEquals( this.originalRootChildStemSubSize + 2, this.root.getChildStems(Stem.Scope.SUB).size() );
  }

  public void test_getChildStems_Scope_fromTopScopeONE() {
    assertEquals( 1, this.top.getChildStems(Stem.Scope.ONE).size() );
  }

  public void test_getChildStems_Scope_fromTopScopeSUB() {
    assertEquals( 1, this.top.getChildStems(Stem.Scope.SUB).size() );
  }

  public void test_getChildStems_Scope_fromChildScopeONE() {
    assertEquals( 0, this.child.getChildStems(Stem.Scope.ONE).size() );
  }

  public void test_getChildStems_Scope_fromChildScopeSUB() {
    assertEquals( 0, this.child.getChildStems(Stem.Scope.SUB).size() );
  }



  public void test_getChildStems_PrivilegeArrayAndScope_nullArray() {
    try {
      this.root.getChildStems(null, null);
      fail("failed to throw IllegalArgumentException");
    }
    catch (IllegalArgumentException eExpected) {
      assertTrue("threw expected exception", true);
    }
  }
  public void test_getChildStems_PrivilegeArrayAndScope_nullScope() {
    try {
      this.root.getChildStems( new Privilege[0], null );
      fail("failed to throw IllegalArgumentException");
    }
    catch (IllegalArgumentException eExpected) {
      assertTrue("threw expected exception", true);
    }
  }
  public void test_getChildStems_PrivilegeArrayAndScope_emptyArray() {
    assertEquals( 0, this.root.getChildStems( new Privilege[0], Stem.Scope.SUB ).size() );
  }
  public void test_getChildStems_PrivilegeArrayAndScope_createPrivAndOneScope() {
    Privilege[] privs = { NamingPrivilege.CREATE };
    assertEquals( this.originalRootCreateOne + 1, this.root.getChildStems( privs, Stem.Scope.ONE ).size() );
  }
  public void test_getChildStems_PrivilegeArrayAndScope_createPrivAndSubScope() {
    Privilege[] privs = { NamingPrivilege.CREATE };
    assertEquals( this.originalRootCreateSub + 2, this.root.getChildStems( privs, Stem.Scope.SUB ).size() );
  }
  public void test_getChildStems_PrivilegeArrayAndScope_viewPrivAndOneScope() {
    Privilege[] privs = { AccessPrivilege.VIEW };
    assertEquals( this.originalRootViewOne + 1, this.root.getChildStems( privs, Stem.Scope.ONE ).size() );
  }
  public void test_getChildStems_PrivilegeArrayAndScope_viewPrivAndSubScope() {
    Privilege[] privs = { AccessPrivilege.VIEW };
    assertEquals( this.originalRootViewSub + 2, this.root.getChildStems( privs, Stem.Scope.SUB ).size() );
  }
  public void test_getChildStems_PrivilegeArrayAndScope_createAndViewPrivsAndOneScope() {
    Privilege[] privs = { NamingPrivilege.CREATE, AccessPrivilege.VIEW };
    assertEquals( this.originalRootCreateAndViewOne + 1, this.root.getChildStems( privs, Stem.Scope.ONE ).size() );
  }
  public void test_getChildStems_PrivilegeArrayAndScope_createAndViewPrivsAndSubScope() {
    Privilege[] privs = { NamingPrivilege.CREATE, AccessPrivilege.VIEW };
    assertEquals( this.originalRootCreateAndViewSub + 2, this.root.getChildStems( privs, Stem.Scope.SUB ).size() );
  }
  /**
   * @since   1.2.1
   */
  public void test_getChildStems_PrivilegeArrayAndScope_OneScopeDoNotReturnThisStem() {
    Privilege[] privs = { AccessPrivilege.VIEW };
    assertEquals( 1, this.top.getChildStems( privs, Stem.Scope.ONE ).size() );
  }



  public void test_isChildGroup_nullChild() {
    try {
      this.root.isChildGroup(null);
      fail("failed to throw IllegalArgumentException");
    }
    catch (IllegalArgumentException eExpected) {
      assertTrue("threw expected exception", true);
    } 
  }

  public void test_isChildGroup_rootAsPotentialParent() {
    assertTrue( this.root.isChildGroup( this.child_group ) );
  }

  public void test_isChildGroup_immediateChild() {
    assertTrue( this.child.isChildGroup( this.child_group ) );
  }

  public void test_isChildGroup_notChild() {
    assertFalse( this.child.isChildGroup( this.top_group ) );
  }


  public void test_isChildStem_nullChild() {
    try {
      this.root.isChildStem(null);
      fail("failed to throw IllegalArgumentException");
    }
    catch (IllegalArgumentException eExpected) {
      assertTrue("threw expected exception", true);
    }
  }

  public void test_isChildStem_rootAsPotentialParent() {
    assertTrue( this.root.isChildStem( this.child ) );
  }

  public void test_isChildStem_rootAsChild() {
    assertFalse( this.child.isChildStem( this.root ) );
  }

  public void test_isChildStem_selfAsChild() {
    assertFalse( this.child.isChildStem( this.child ) );
  }

  public void test_isChildStem_isChild() {
    assertTrue( this.top.isChildStem( this.child ) );
  }

  public void test_isChildStem_notChild() 
    throws  InsufficientPrivilegeException,
            StemAddException
  {
    Stem otherTop = this.root.addChildStem("other top", "other top");
    assertFalse( otherTop.isChildStem( this.child ) );
  }



  public void test_isRootStem_root() {
    assertTrue( this.root.isRootStem() );
  }

  public void test_isRootStem_notRootStem() {
    assertFalse( this.top.isRootStem() );
  }



  /**
   * @since   1.2.1
   */
  public void test_revokePriv_Priv_accessPrivilege() 
    throws  InsufficientPrivilegeException,
            RevokePrivilegeException
  {
    try {
      this.root.revokePriv(AccessPrivilege.ADMIN);
      fail("failed to throw expected SchemaException");
    }
    catch (SchemaException eExpected) {
      assertTrue(true);
    }
  }

}
