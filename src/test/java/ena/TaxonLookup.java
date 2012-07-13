package ena;

import java.util.List;

public interface TaxonLookup {

  public TaxonInfo getTaxonInfoFromCommonName(String commonName);
  
  public TaxonInfo getTaxonInfoFromScientificName(String scientificName);

  public TaxonInfo getTaxonInfoFromTaxId(String taxId);

  public boolean isTaxIdValid(String taxId);

  public List<TaxonInfo> suggestScientificName(String partialName, int suggestionsLimit);

  public String suggestTaxId(String partialTaxId);

  public boolean isScientificNameValid(String scientificName);

  public TaxonInfo getTaxonInfoFromSynonym(String synonym);

  public List<TaxonInfo> suggestAll(String query, int limit);

}
