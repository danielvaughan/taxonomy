package com.danielvaughan.taxonomy.server;

import com.danielvaughan.taxonomy.server.daos.TaxonDao;
import com.danielvaughan.taxonomy.shared.model.Taxon;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/applicationContext.xml"})
public class TaxonRetrievalTest {

  @Autowired
  private TaxonDao taxonDao;
  
  @Test
  public void testRetrieveByTaxId() {
    List<String> taxIds = Arrays.asList("9606", "7675", "9000");
    for (final String taxId : taxIds) {
      Taxon retrievedTaxon = taxonDao.getTaxonByTaxId(taxId);
      System.out.println(retrievedTaxon.toString());
    }
  }

  @Test
  public void testRetrieveByPartialText() {
    List<String> searchTerms = Arrays.asList("960", "Hum", "Hom");
    for (final String searchTerm : searchTerms) {
      List<Taxon> taxons = taxonDao.searchTaxons(searchTerm, 10);
      System.out.println("-- Search term: " + searchTerm);
      for (final Taxon taxon : taxons) {
        System.out.println(taxon.toString());
      }
    }
  }
}
