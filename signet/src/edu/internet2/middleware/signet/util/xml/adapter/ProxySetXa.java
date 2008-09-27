/*
	$Header: /home/hagleyj/i2mi/signet/src/edu/internet2/middleware/signet/util/xml/adapter/ProxySetXa.java,v 1.2 2008-09-27 01:02:09 ddonn Exp $

Copyright (c) 2008 Internet2, Stanford University

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

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import edu.internet2.middleware.signet.ProxyImpl;
import edu.internet2.middleware.signet.Signet;
import edu.internet2.middleware.signet.util.xml.binder.ObjectFactory;
import edu.internet2.middleware.signet.util.xml.binder.ProxyImplXb;
import edu.internet2.middleware.signet.util.xml.binder.ProxySetXb;

/**
 * ProxySetXa<p>
 * Adapter class for Signet XML Binding.
 * Maps a Collection&lt;ProxyImpl&gt; and an ProxySetXb.
 * @see ProxyImpl
 * @see ProxySetXb
 * 
 */
public class ProxySetXa
{
	protected Signet				signet;
	protected Collection<ProxyImpl>	signetProxies;
	protected ProxySetXb			xbProxies;
	/** logging */
	private static Log				log = LogFactory.getLog(ProxySetXa.class);


	/**
	 * Constructor
	 */
	protected ProxySetXa()
	{
	}

	/**
	 * Constructor
	 * @param signetProxies
	 * @param signet
	 */
	public ProxySetXa(Collection<ProxyImpl> signetProxies, Signet signet)
	{
		this();
		this.signet = signet;
		setValues(signetProxies);
//		setSignetAssignments(signetAssignments);
	}

	/**
	 * Constructor
	 * @param xbProxies
	 * @param signet
	 */
	public ProxySetXa(ProxySetXb xbProxies, Signet signet)
	{
		this();
		this.signet = signet;
		setValues(xbProxies);
//		setXmlAssignments(xbAssignments);
	}


	/**
	 * Get the set of proxies from this adapter
	 * @return the set of proxies
	 */
	public Collection<ProxyImpl> getSignetProxies()
	{
		return (signetProxies);
	}

	/**
	 * Replace the set of proxies
	 * @param signetProxies
	 */
	public void setValues(Collection<ProxyImpl> signetProxies)
	{
		this.signetProxies = signetProxies;

		ObjectFactory of = new ObjectFactory();
		xbProxies = of.createProxySetXb();

		List<ProxyImplXb> list = xbProxies.getProxy();
		for (ProxyImpl proxy : signetProxies)
		{
//log.info("ProxySetXa.setValues(Collection<ProxyImpl>): signetProxy = " + proxy.toString());
			ProxyImplXa xaProxy = new ProxyImplXa(proxy, signet);
			list.add(xaProxy.getXmlProxy());
		}
	}

	/**
	 * Get the set of XML proxies
	 * @return The set of XML proxies
	 */
	public ProxySetXb getXmlProxies()
	{
		return (xbProxies);
	}

	/**
	 * Replace the set of proxies for this adapter
	 * @param xbProxies
	 */
	public void setValues(ProxySetXb xbProxies)
	{
		this.xbProxies = xbProxies;
		signetProxies = new HashSet<ProxyImpl>();
		for (Iterator<ProxyImplXb> xmlProxies = xbProxies.getProxy().iterator();
				xmlProxies.hasNext(); )
		{
			ProxyImplXb proxy = xmlProxies.next();
//log.info("ProxySetXa.setValues(ProxySetXb): xmlProxy = " + proxy.toString());
			ProxyImplXa adapter = new ProxyImplXa(proxy, signet);
			signetProxies.add(adapter.getSignetProxy());
		}
	}

}
