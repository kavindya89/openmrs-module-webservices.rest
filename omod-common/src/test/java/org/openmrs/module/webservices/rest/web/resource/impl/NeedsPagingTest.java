package org.openmrs.module.webservices.rest.web.resource.impl;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.Order;
import org.openmrs.Patient;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.ConversionUtil;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.resource.api.Converter;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * Tests {@link NeedsPaging}.
 */
public class NeedsPagingTest extends BaseModuleWebContextSensitiveTest {
	
	Converter<Order> converter;
	
	MockHttpServletRequest request = new MockHttpServletRequest();
	
	RequestContext context;
	
	Patient patientWithOrders = new Patient();
	
	Order order1 = new Order();
	
	Order order2 = new Order();
	
	List<Order> orders = new ArrayList<Order>();
	
	@Before
	public void setUp() {
		
		converter = ConversionUtil.getConverter(Order.class);
		
		order1.setPatient(patientWithOrders);
		order2.setPatient(patientWithOrders);
		orders.add(order1);
		orders.add(order2);
		
		context = new RequestContext();
		context.setStartIndex(0);
		context.setLimit(1);
		
		context.setRequest(request);
	}
	
	/**
	 * @see BasePageableResult#toSimpleObject(Converter)
	 * @verifies add property totalCount if context contains parameter totalCount which is true
	 */
	@Test
	public void toSimpleObject_shouldAddPropertyTotalCountIfContextContainsParameterTotalCountWhichIsTrue() throws Exception {
		
		request.addParameter("totalCount", "true");
		NeedsPaging<Order> needsPaging = new NeedsPaging<Order>(orders, context);
		
		SimpleObject result = needsPaging.toSimpleObject(converter);
		
		assertTrue(result.containsKey("totalCount"));
		assertThat((Long) result.get("totalCount"), is((long) orders.size()));
		assertThat(needsPaging.getTotalCount(), is((long) orders.size()));
	}
	
	/**
	 * @see BasePageableResult#toSimpleObject(Converter)
	 * @verifies not add property totalCount if context contains parameter totalCount which is false
	 */
	@Test
	public void toSimpleObject_shouldNotAddPropertyTotalCountIfContextContainsParameterTotalCountWhichIsFalse()
	        throws Exception {
		
		request.addParameter("totalCount", "false");
		NeedsPaging<Order> needsPaging = new NeedsPaging<Order>(orders, context);
		
		SimpleObject result = needsPaging.toSimpleObject(converter);
		
		assertFalse(result.containsKey("totalCount"));
	}
	
	/**
	 * @see BasePageableResult#toSimpleObject(Converter)
	 * @verifies not add property totalCount if context does not contains parameter totalCount
	 */
	@Test
	public void toSimpleObject_shouldNotAddPropertyTotalCountIfContextDoesNotContainsParameterTotalCount() throws Exception {
		
		NeedsPaging<Order> needsPaging = new NeedsPaging<Order>(orders, context);
		
		SimpleObject result = needsPaging.toSimpleObject(converter);
		
		assertFalse(result.containsKey("totalCount"));
	}
}
