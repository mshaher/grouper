/*
	$Header: /home/hagleyj/i2mi/signet/src/edu/internet2/middleware/signet/util/xml/adapter/LimitImplRefXa.java,v 1.2 2007-12-06 01:18:32 ddonn Exp $

Copyright (c) 2007 Internet2, Stanford University

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
package edu.internet2.middleware.signet.util.xml.adapter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import edu.internet2.middleware.signet.LimitImpl;
import edu.internet2.middleware.signet.Signet;
import edu.internet2.middleware.signet.dbpersist.HibernateDB;
import edu.internet2.middleware.signet.util.xml.binder.LimitImplRefXb;
import edu.internet2.middleware.signet.util.xml.binder.ObjectFactory;

/**
 * LimitImplRefXa<p>
 * Adapter class for Signet XML Binding.
 * Maps a LimitImpl and a LimitImplRefXb
 * @see LimitImpl
 * @see LimitImplRefXb
 */
public class LimitImplRefXa
{
	protected Signet			signet;
	protected LimitImpl			signetLimitImpl;
	protected LimitImplRefXb	xmlLimitImplRef;
	protected Log				log;


	public LimitImplRefXa()
	{
		log = LogFactory.getLog(LimitImplRefXa.class);
	}

	public LimitImplRefXa(Signet signet)
	{
		this();
		this.signet = signet;
	}

	public LimitImplRefXa(LimitImpl signetLimitImpl, Signet signet)
	{
		this(signet);
		this.signetLimitImpl = signetLimitImpl;
		xmlLimitImplRef = new ObjectFactory().createLimitImplRefXb();
		setValues(signetLimitImpl);
	}

	public LimitImplRefXa(LimitImplRefXb xmlLimitImpl, Signet signet)
	{
		this(signet);
		this.xmlLimitImplRef = xmlLimitImpl;
		signetLimitImpl = new LimitImpl();
		setValues(xmlLimitImpl);
	}

	public LimitImpl getSignetLimitImpl()
	{
		return (signetLimitImpl);
	}

	public void setValues(LimitImpl signetLimitImpl)
	{
//	protected Integer			key;
		xmlLimitImplRef.setLimitPK(signetLimitImpl.getKey());

//	protected String			subsystemId;
		xmlLimitImplRef.setSubsystemId(signetLimitImpl.getSubsystem().getId());

//	protected String			id;
		xmlLimitImplRef.setId(signetLimitImpl.getId());

//	protected String			dataType;
//		xmlLimitImplRef.setDataType(signetLimitImpl.getDataType().getName());

//	protected String			choiceSetId;
//		xmlLimitImplRef.setChoiceSetId(signetLimitImpl.getChoiceSet().getId());

//	protected String			name;
//		xmlLimitImplRef.setName(signetLimitImpl.getName());

//	protected String			status;
//		xmlLimitImplRef.setStatus(signetLimitImpl.getStatus().getName());

//	protected int				displayOrder;
//		xmlLimitImplRef.setDisplayOrder(signetLimitImpl.getDisplayOrder());

//	protected Set<LimitValueXb>	limitValues;
//		List<LimitValueXb> xmlLimitValueList = xmlLimitImplRef.getLimitValues();
//		for (Iterator<LimitValue> sigLimitValues = signetLimitImpl.get
	}

	public LimitImplRefXb getXmlLimitImplRef()
	{
		return (xmlLimitImplRef);
	}

	public void setValues(LimitImplRefXb xmlLimitImpl)
	{
		signetLimitImpl = null;

		if (null == signet)
		{
			log.error("Unable to lookup Limit (PK=" +
					xmlLimitImpl.getLimitPK() +
					") because no Signet instance is available");
			return;
		}

		HibernateDB hibr = signet.getPersistentDB();
		if (null != hibr)
		{
			int limit_pk = xmlLimitImpl.getLimitPK().intValue();
			if (0 < limit_pk)
			{
				signetLimitImpl = hibr.getLimit(limit_pk);
				if (null == signetLimitImpl)
					log.warn("No Limit found with primary key = " + limit_pk);
			}
			else
				log.warn("Invalid Limit primary key \"" + limit_pk + "\" specified");
			if (null == signetLimitImpl)
			{
				log.warn("Attempting to find Limit by Subsystem and LimitId...");

				String subsysId = xmlLimitImpl.getSubsystemId();
				String limitId = xmlLimitImpl.getId();
				signetLimitImpl = hibr.getLimit(subsysId, limitId);

				StringBuffer buf = new StringBuffer();
				if (null == signetLimitImpl)
					buf.append("No ");
				buf.append("Limit found for SubsystemId \"" + subsysId + "\" and LimitId \"" + limitId + "\"");
				log.warn(buf.toString());
			}
		}
		else
			log.error("Unable to lookup Limit: no HibernateDB instance is available");
	}

}
