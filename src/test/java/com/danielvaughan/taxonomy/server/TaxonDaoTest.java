package com.danielvaughan.taxonomy.server;

import com.danielvaughan.taxonomy.server.daos.TaxonDao;
import com.danielvaughan.taxonomy.shared.model.Taxon;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/applicationContext.xml"})
public class TaxonDaoTest {

  @Autowired
  private TaxonDao taxonDao;
  
  @Test
  public void testCreateAndRetrieve() {
    final String taxId = "9606";
    final String scientificName = "Homo Sapien";
    final String commonName = "Human";
    final Taxon createdTaxon = new Taxon(taxId, scientificName, commonName);
    taxonDao.addTaxon(createdTaxon);
    Taxon retrievedTaxon = taxonDao.getTaxonByTaxId(taxId);
    assertEquals(createdTaxon.getTaxId(), retrievedTaxon.getTaxId());
    assertEquals(createdTaxon.getScientificName(), retrievedTaxon.getScientificName());
    assertEquals(createdTaxon.getCommonName(), retrievedTaxon.getCommonName());
  }

}
