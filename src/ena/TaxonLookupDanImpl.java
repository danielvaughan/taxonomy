package ena;

import com.danielvaughan.taxonomy.client.TaxonomyClient;
import com.danielvaughan.taxonomy.client.TaxonomyClientImpl;
import com.danielvaughan.taxonomy.shared.model.Taxon;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TaxonLookupDanImpl implements TaxonLookup {

  private final TaxonomyClient taxonomyClient = new TaxonomyClientImpl("http://127.0.0.1:8080/");

  @Override
  public TaxonInfo getTaxonInfoFromCommonName(final String commonName) {
    if (!StringUtils.isNumeric(commonName)) {
      final Taxon taxon = taxonomyClient.getTaxonByCommonName(commonName);
      return getTaxonInfoFromTaxon(taxon);
    } else {
      throw new IllegalArgumentException("Name should not be a number");
    }
  }

  @Override
  public TaxonInfo getTaxonInfoFromScientificName(final String scientificName) {
    final Taxon taxon = taxonomyClient.getTaxonByScientificName(scientificName);
    return getTaxonInfoFromTaxon(taxon);
  }

  @Override
  public TaxonInfo getTaxonInfoFromSynonym(final String synonym) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public TaxonInfo getTaxonInfoFromTaxId(final String taxId) {
    final Taxon taxon = taxonomyClient.getTaxonByTaxId(taxId);
    return getTaxonInfoFromTaxon(taxon);
  }

  @Override
  public boolean isScientificNameValid(final String scientificName) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isValidTaxId(final String taxId) {
    return taxonomyClient.isValidTaxId(taxId);
  }

  @Override
  public List<TaxonInfo> suggestAll(final String query, final int limit) {
    final List<Taxon> taxons = taxonomyClient.suggest(query, limit);
    final List<TaxonInfo> taxonInfos = new ArrayList<TaxonInfo>();
    for (final Taxon taxon : taxons) {
      taxonInfos.add(getTaxonInfoFromTaxon(taxon));
    }
    return taxonInfos;
  }

  @Override
  public List<TaxonInfo> suggestScientificName(final String partialName, final int suggestionsLimit) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String suggestTaxId(final String partialTaxId) {
    //final List<Taxon> taxons = taxonomyClient.suggestTaxId(partialTaxId);
    return null;
  }

  private TaxonInfo getTaxonInfoFromTaxon(final Taxon taxon) {
    final TaxonInfo taxonInfo = new TaxonInfo(taxon.getTaxId(), taxon.getCommonName(), taxon.getScientificName());
    return taxonInfo;
  }

}
