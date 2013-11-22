package ena;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public abstract class TaxonLookupTest {

  public abstract TaxonLookup getTaxonLookup();
  
  private TaxonInfo taxonInfo9606;
  
  private TaxonInfo taxonInfo0;
  
  private TaxonInfo getTaxonInfo9606()
  {
    if (taxonInfo9606==null)
    {
    taxonInfo9606 = new TaxonInfo("9606", "human", "Homo sapiens");
    }
    return taxonInfo9606;
  }
  
  private TaxonInfo getTaxonInfo0()
  {
    if (taxonInfo0==null)
    {
    taxonInfo0 = new TaxonInfo("0", "", "");
    }
    return taxonInfo0;
  }
  
  
  @Test
  public void TestExtractionOfCommonName() {
    TaxonInfo taxonInfo = getTaxonLookup().getTaxonInfoFromTaxId("7955");
    System.out.println(taxonInfo.toString());
    taxonInfo = getTaxonLookup().getTaxonInfoFromTaxId("9103");
    System.out.println(taxonInfo.toString());
    taxonInfo = getTaxonLookup().getTaxonInfoFromTaxId("9606");
    System.out.println(taxonInfo.toString());
    assertEquals(getTaxonInfo9606(), taxonInfo);
  }
  
  @Test
  public void TestGet0TaxonInfoFromCommonNameForFishFinger() {
    TaxonInfo taxonInfo = getTaxonLookup().getTaxonInfoFromCommonName("fish finger");
    assertEquals(getTaxonInfo0(), taxonInfo);
  }
  @Test
  public void TestGet0TaxonInfoFromCommonNameForHomoSapien() {
    TaxonInfo taxonInfo = getTaxonLookup().getTaxonInfoFromCommonName("Homo Sapien");
    assertEquals(getTaxonInfo0(), taxonInfo);
  }

  @Test
  public void TestGet0TaxonInfoFromCommonNameForHumanScientificName() {
    TaxonInfo taxonInfo = getTaxonLookup().getTaxonInfoFromScientificName("Homo sapiens (Human)");
    assertEquals(getTaxonInfo0(), taxonInfo);
  }
  
  @Test
  public void TestGet0TaxonInfoFromTaxIdIs9999999() {
    TaxonInfo taxonInfo = getTaxonLookup().getTaxonInfoFromTaxId("9999999");
    assertEquals(getTaxonInfo0(), taxonInfo);
  }
  
  @Test
  public void TestGet7655TaxonInfoFromCommonNameForzebrafish() {
    TaxonInfo taxonInfo = getTaxonLookup().getTaxonInfoFromCommonName("zebrafish");
    assertEquals("7955", taxonInfo.getTaxId());
  }
  
  @Test
  public void TestGet7955TaxonInfoFromCommonNameForBrachydaniorerio() {
    TaxonInfo taxonInfo = getTaxonLookup().getTaxonInfoFromSynonym("Brachydanio rerio");
    assertEquals("7955", taxonInfo.getTaxId());
  }
  
  @Test
  public void TestGet7955TaxonInfoFromScientificNameForDaniorerio() {
    TaxonInfo taxonInfo = getTaxonLookup().getTaxonInfoFromScientificName("Danio rerio");
    assertEquals("7955", taxonInfo.getTaxId());
  }
  
  @Test
  public void TestGet7955TaxonInfoFromTaxId7955() {
    TaxonInfo taxonInfo = getTaxonLookup().getTaxonInfoFromTaxId("7955");
    assertEquals("7955", taxonInfo.getTaxId());
  }
  
  @Test
  public void TestGet9103TaxonInfoFromCommonNameForCommonturkey() {
    TaxonInfo taxonInfo = getTaxonLookup().getTaxonInfoFromCommonName("Common turkey");
    assertEquals("9103", taxonInfo.getTaxId());
  }
  
  @Test
  public void TestGet9103TaxonInfoFromCommonNameForturkey() {
    TaxonInfo taxonInfo = getTaxonLookup().getTaxonInfoFromCommonName("turkey");
    assertEquals("9103", taxonInfo.getTaxId());
  }
  
  @Test
  public void TestGet9103TaxonInfoFromCommonNameForTurkey() {
    TaxonInfo taxonInfo = getTaxonLookup().getTaxonInfoFromCommonName("Turkey");
    assertEquals("9103", taxonInfo.getTaxId());
  }

  @Test
  public void TestGet9103TaxonInfoFromTaxIdI9103() {
    TaxonInfo taxonInfo = getTaxonLookup().getTaxonInfoFromTaxId("9103");
    assertEquals("9103", taxonInfo.getTaxId());
  }
  
  @Test
  public void TestGet9606TaxonInfoFromCommonNameForhuman() {
    TaxonInfo taxonInfo = getTaxonLookup().getTaxonInfoFromCommonName("human");
    assertEquals(getTaxonInfo9606(), taxonInfo);
  }
  
  @Test
  public void TestGet9606TaxonInfoFromCommonNameForHuman() {
    TaxonInfo taxonInfo = getTaxonLookup().getTaxonInfoFromCommonName("Human");
    assertEquals(getTaxonInfo9606(), taxonInfo);
  }
  
  @Test
  public void TestGet9606TaxonInfoFromScientificNameForHomosapiens() {
    TaxonInfo taxonInfo = getTaxonLookup().getTaxonInfoFromCommonName("Homo sapiens");
    assertEquals(getTaxonInfo9606(), taxonInfo);
  }
  
  @Test
  public void TestGet9606TaxonInfoFromTaxId9606() {
    TaxonInfo taxonInfo = getTaxonLookup().getTaxonInfoFromTaxId("9606");
    assertEquals(getTaxonInfo9606(), taxonInfo);
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void TestGetTaxonInfoFromCommonNameIsErrorForTaxId() {
    getTaxonLookup().getTaxonInfoFromCommonName("9606");
  }
  
  @Test
  public void TestIsTaxIdValid()
  {
    assertTrue(getTaxonLookup().isValidTaxId("9606"));
    assertTrue(getTaxonLookup().isValidTaxId("9103"));
    assertTrue(getTaxonLookup().isValidTaxId("7955"));
    assertTrue(getTaxonLookup().isValidTaxId("999999"));
    assertFalse(getTaxonLookup().isValidTaxId("99999912345"));
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
  public void TestSuggestAllFor9606()
  {
    int size = 10;
    //Should return 10 results, the first the taxon for 9606 and the rest partial matches
    List<TaxonInfo> taxonInfos = getTaxonLookup().suggestAll("9606", size);
    assertFalse(taxonInfos.isEmpty());
    assertEquals(size, taxonInfos.size());
    assertEquals(getTaxonInfo9606(), taxonInfos.iterator().next());
  }
  
  @Test
  public void TestSuggestAllForHuman()
  {
    int size = 10;
    //Should return 10 results, the first the taxon for 9606 and the rest partial matches
    List<TaxonInfo> taxonInfos = getTaxonLookup().suggestAll("human", size);
    assertFalse(taxonInfos.isEmpty());
    assertEquals(size, taxonInfos.size());
    assertEquals(getTaxonInfo9606(), taxonInfos.iterator().next());
  }
  
  @Test
  public void TestSuggestAllForHomoSapiens()
  {
    int size = 4;
    //Should return 4 results, the first the taxon for 9606 and the rest partial matches
    List<TaxonInfo> taxonInfos = getTaxonLookup().suggestAll("homo sapiens", size);
    assertFalse(taxonInfos.isEmpty());
    assertEquals(4, taxonInfos.size());
    assertEquals(getTaxonInfo9606(), taxonInfos.iterator().next());
  }
}
