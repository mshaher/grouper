/*
 * Copyright (C) 2006-2007 blair christensen.
 * All Rights Reserved.
 *
 * You may use and distribute under the same terms as Grouper itself.
 */

package edu.internet2.middleware.grouper.app.gsh;
import bsh.CallStack;
import bsh.Interpreter;
import edu.internet2.middleware.grouper.Composite;
import edu.internet2.middleware.grouper.Group;
import edu.internet2.middleware.grouper.GroupFinder;
import edu.internet2.middleware.grouper.GrouperSession;
import edu.internet2.middleware.grouper.exception.GroupNotFoundException;
import edu.internet2.middleware.grouper.exception.InsufficientPrivilegeException;
import edu.internet2.middleware.grouper.exception.MemberDeleteException;

/**
 * Delete a composite member.
 * <p/>
 * @author  blair christensen.
 * @version $Id: delComposite.java,v 1.3 2009-03-15 06:37:23 mchyzer Exp $
 * @since   0.0.1
 */
public class delComposite {

  // PUBLIC CLASS METHODS //

  /**
   * Delete a composite member.
   * <p/>
   * @param   i           BeanShell interpreter.
   * @param   stack       BeanShell call stack.
   * @param   group       Delete {@link Composite} from {@link Group} with this name.
   * @return  True if succeeds.
   * @throws  GrouperShellException
   * @since   0.0.1
   */
  public static boolean invoke(
    Interpreter i, CallStack stack, String group
  ) 
    throws  GrouperShellException
  {
    GrouperShell.setOurCommand(i, true);
    try {
      GrouperSession  s     = GrouperShell.getSession(i);
      Group           g     = GroupFinder.findByName(s, group, true);
      g.deleteCompositeMember();
      return true;
    }
    catch (GroupNotFoundException eGNF)         {
      GrouperShell.error(i, eGNF);
    }
    catch (InsufficientPrivilegeException eIP)  {
      GrouperShell.error(i, eIP);
    }
    catch (MemberDeleteException eMD)           {
      GrouperShell.error(i, eMD);
    }
    return false;
  } // public static boolean invoke(i, stack, group)

} // public class delComposite
