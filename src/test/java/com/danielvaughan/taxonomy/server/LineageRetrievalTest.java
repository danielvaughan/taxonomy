package com.danielvaughan.taxonomy.server;

import com.danielvaughan.taxonomy.server.daos.TaxonDao;
import com.danielvaughan.taxonomy.shared.model.Taxon;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/applicationContext.xml"})
public class LineageRetrievalTest {

  @Autowired
  private TaxonDao taxonDao;

  @Test
  public void testLineage() {
    final String taxId = "9606";
    System.out.println("taxId:" + taxId);
    List<Taxon> taxons = taxonDao.getLineageByTaxId(taxId, true);
    for (final Taxon taxon : taxons) {
      System.out.println(taxon.toString());
    }
  }
}
