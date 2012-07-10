package com.danielvaughan.taxonomy.server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/applicationContext.xml"})
public class TaxonomySetupTest {

  @Autowired
  TaxonomySetup taxonomySetup;
  
  @Before
  public void setUp() throws Exception {

  }

  @Test
  public void testRun() {
    taxonomySetup.run();
  }
  
  @After
  public void tearDown() throws Exception {
  }

}