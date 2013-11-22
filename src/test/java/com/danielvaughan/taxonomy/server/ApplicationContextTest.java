package com.danielvaughan.taxonomy.server;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.danielvaughan.taxonomy.server.daos.TaxonDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/META-INF/applicationContext.xml"})
public class ApplicationContextTest {

  @Autowired
  TaxonDao taxonDao;
  
  @Test
  public void test() {
    try {
      assertNotNull(taxonDao);
    } catch (Exception e) {
      fail();
    }
  }

}
