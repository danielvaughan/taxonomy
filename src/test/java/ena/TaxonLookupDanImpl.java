package ena;

import com.danielvaughan.taxonomy.client.TaxonomyClient;
import com.danielvaughan.taxonomy.client.TaxonomyClientImpl;
import com.danielvaughan.taxonomy.shared.model.Taxon;

import java.util.List;

public class TaxonLookupDanImpl implements TaxonLookup {

  private final TaxonomyClient taxonomyClient = new TaxonomyClientImpl("http://127.0.0.1:8080/");
  
  @Override
  public TaxonInfo getTaxonInfoFromScientificName(String scientificName) {
    Taxon taxon = taxonomyClient.getTaxonByScientificName(scientificName);
    return getTaxonInfoFromTaxon(taxon);
  }
  
  @Override
  public TaxonInfo getTaxonInfoFromCommonName(String commonName) {
    Taxon taxon = taxonomyClient.getTaxonByCommonName(commonName);
    return getTaxonInfoFromTaxon(taxon);
  }

  private TaxonInfo getTaxonInfoFromTaxon(final Taxon taxon)
  {
    TaxonInfo taxonInfo = new TaxonInfo(taxon.getTaxId(), taxon.getCommonName(), taxon.getScientificName());
    return taxonInfo;
  }
  
  @Override
  public TaxonInfo getTaxonInfoFromTaxId(String taxId) {
    Taxon taxon = taxonomyClient.getTaxonById(taxId);
    return getTaxonInfoFromTaxon(taxon);
  }

  @Override
  public boolean isTaxIdValid(String taxId) {
    
    return false;
  }

  @Override
  public List<TaxonInfo> suggestScientificName(String partialName, int suggestionsLimit) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String suggestTaxId(String partialTaxId) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isScientificNameValid(String scientificName) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public TaxonInfo getTaxonInfoFromSynonym(String synonym) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<TaxonInfo> suggestAll(String query, int limit) {
    // TODO Auto-generated method stub
    return null;
  }

}
