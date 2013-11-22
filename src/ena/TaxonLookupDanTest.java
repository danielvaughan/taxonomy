package ena;


public class TaxonLookupDanTest extends TaxonLookupTest {

  private final TaxonLookup taxonLookup = new TaxonLookupDanImpl();
  
  @Override
  public TaxonLookup getTaxonLookup() {
    return taxonLookup;
  } 
}
