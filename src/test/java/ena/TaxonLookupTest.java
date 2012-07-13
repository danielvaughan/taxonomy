package ena;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public abstract class TaxonLookupTest {

  public abstract TaxonLookup getTaxonLookup();
  
  @Test
  public void TestGetTaxonInfoFromCommonNameIs7655Forzebrafish() {
    TaxonInfo taxonInfo = getTaxonLookup().getTaxonInfoFromCommonName("zebrafish");
    assertEquals("7955", taxonInfo.getTaxId());
  }
  
  @Test
  public void TestGetTaxonInfoFromCommonNameIs7655ForBrachydaniorerio() {
    TaxonInfo taxonInfo = getTaxonLookup().getTaxonInfoFromSynonym("Brachydanio rerio");
    assertEquals("7955", taxonInfo.getTaxId());
  }

  @Test
  public void TestGetTaxonInfoFromCommonNameIs9606Forhuman() {
    TaxonInfo taxonInfo = getTaxonLookup().getTaxonInfoFromCommonName("human");
    assertEquals("9606", taxonInfo.getTaxId());
  }
  
  @Test
  public void TestGetTaxonInfoFromCommonNameIs9606ForHuman() {
    TaxonInfo taxonInfo = getTaxonLookup().getTaxonInfoFromCommonName("Human");
    assertEquals("9606", taxonInfo.getTaxId());
  }
  
  @Test
  public void TestGetTaxonInfoFromCommonNameIs9103Forturkey() {
    TaxonInfo taxonInfo = getTaxonLookup().getTaxonInfoFromCommonName("turkey");
    assertEquals("9103", taxonInfo.getTaxId());
  }
  
  @Test
  public void TestGetTaxonInfoFromCommonNameIs9103ForCommonturkey() {
    TaxonInfo taxonInfo = getTaxonLookup().getTaxonInfoFromCommonName("Common turkey");
    assertEquals("9103", taxonInfo.getTaxId());
  }
  
  @Test
  public void TestGetTaxonInfoFromCommonNameIs9103ForTurkey() {
    TaxonInfo taxonInfo = getTaxonLookup().getTaxonInfoFromCommonName("Turkey");
    assertEquals("9103", taxonInfo.getTaxId());
  }
  
  @Test
  public void TestGetTaxonInfoFromCommonNameIs9606ForHomoSapian() {
    TaxonInfo taxonInfo = getTaxonLookup().getTaxonInfoFromCommonName("Homo Sapien");
    assertEquals("0", taxonInfo.getTaxId());
  }
  
  @Test
  public void TestGetTaxonInfoFromCommonNameIsEmptyForFishFinger() {
    TaxonInfo taxonInfo = getTaxonLookup().getTaxonInfoFromCommonName("fish finger");
    assertEquals("0", taxonInfo.getTaxId());
  }
  
  @Test
  public void TestGetTaxonInfoFromCommonNameIsEmptyForHumanScientificName() {
    TaxonInfo taxonInfo = getTaxonLookup().getTaxonInfoFromScientificName("Homo sapiens (Human)");
    assertEquals("0", taxonInfo.getTaxId());
  }

  @Test(expected=IllegalArgumentException.class)
  public void TestGetTaxonInfoFromCommonNameIsErrorForTaxId() {
    getTaxonLookup().getTaxonInfoFromCommonName("9606");
  }
  
  @Test
  public void TestGetTaxonInfoFromTaxIdIsHumanFor9606() {
    TaxonInfo taxonInfo = getTaxonLookup().getTaxonInfoFromTaxId("9606");
    assertEquals("9606", taxonInfo.getTaxId());
  }
  
  @Test
  public void TestGetTaxonInfoFromTaxIdIs0For99999() {
    TaxonInfo taxonInfo = getTaxonLookup().getTaxonInfoFromTaxId("9999999");
    assertEquals("0", taxonInfo.getTaxId());
  }
  
  @Test
  public void TestGetTaxonInfoFromTaxIdIsTurkeyFor9103() {
    TaxonInfo taxonInfo = getTaxonLookup().getTaxonInfoFromTaxId("9103");
    assertEquals("9103", taxonInfo.getTaxId());
  }
  
  @Test
  public void TestGetTaxonInfoFromTaxIdIsZebrafishFor7955() {
    TaxonInfo taxonInfo = getTaxonLookup().getTaxonInfoFromTaxId("7955");
    assertEquals("7955", taxonInfo.getTaxId());
  }
  
  @Test
  public void TestExtractionOfCommonName() {
    TaxonInfo taxonInfo = getTaxonLookup().getTaxonInfoFromTaxId("7955");
    System.out.println(taxonInfo.toString());
    taxonInfo = getTaxonLookup().getTaxonInfoFromTaxId("9103");
    System.out.println(taxonInfo.toString());
    taxonInfo = getTaxonLookup().getTaxonInfoFromTaxId("9606");
    System.out.println(taxonInfo.toString());
    assertEquals("9606", taxonInfo.getTaxId());
  }
  
  @Test
  public void TestSuggestScientificName()
  {
    assertNotNull(getTaxonLookup().suggestScientificName("h", 10));
    assertNotNull(getTaxonLookup().suggestScientificName("hu", 10));
    assertNotNull(getTaxonLookup().suggestScientificName("hum", 10));
    assertNotNull(getTaxonLookup().suggestScientificName("huma", 10));
    assertNotNull(getTaxonLookup().suggestScientificName("z", 10));
    assertNotNull(getTaxonLookup().suggestScientificName("ze", 10));
    assertNotNull(getTaxonLookup().suggestScientificName("zeb", 10));
    assertNotNull(getTaxonLookup().suggestScientificName("zebra", 10));
    assertNotNull(getTaxonLookup().suggestScientificName("zebraf", 10));
    assertNotNull(getTaxonLookup().suggestScientificName("zebrafi", 10));
    
  }
  
  @Test
  public void TestIsTaxIdValid()
  {
    assertTrue(getTaxonLookup().isTaxIdValid("9606"));
    assertTrue(getTaxonLookup().isTaxIdValid("9103"));
    assertTrue(getTaxonLookup().isTaxIdValid("7955"));
    assertTrue(getTaxonLookup().isTaxIdValid("999999"));
    assertFalse(getTaxonLookup().isTaxIdValid("99999912345"));
  }
}
