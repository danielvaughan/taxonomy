package com.danielvaughan.taxonomy.client;

import com.danielvaughan.taxonomy.shared.model.Taxon;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaxonomyClientImpl implements TaxonomyClient {

  public static void main(final String[] args) {
    final TaxonomyClient taxonomyClient = new TaxonomyClientImpl("http://127.0.0.1:8080/");
    Taxon taxon = taxonomyClient.getTaxonById("9606");
    System.out.println(taxon.getCommonName());
  }
  
  private final String baseUrl;
  
  private final RestTemplate restTemplate = new RestTemplate();
  
  
  public TaxonomyClientImpl(final String baseUrl)
  {
    this.baseUrl = baseUrl;
    List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
    MappingJacksonHttpMessageConverter messageConverter = new MappingJacksonHttpMessageConverter();
    messageConverters.add(messageConverter);
    restTemplate.setMessageConverters(messageConverters);
  }
  
  @Override
  public Taxon getTaxonById(final String taxId) {
    Map<String, String> vars = new HashMap<String, String>();
    vars.put("taxId", taxId);
    String url = baseUrl + "taxonomy/service/taxon/id/{taxId}";
    TaxonResponse taxonResponse = restTemplate.getForObject(url, TaxonResponse.class, vars);
    return taxonResponse.getTaxon();
  }
  
  @Override
  public List<Taxon> getLineage(final String taxId) {
    Map<String, String> vars = new HashMap<String, String>();
    vars.put("taxId", taxId);
    String url = baseUrl + "taxonomy/service/taxon/lineage/{taxId}";
    LineageResponse lineageResponse = restTemplate.getForObject(url, LineageResponse.class, vars);
    return lineageResponse.getLineage();
  }

  @Override
  public Taxon getTaxonByScientificName(String scientificName) {
    Map<String, String> vars = new HashMap<String, String>();
    vars.put("scientificName", scientificName);
    String url = baseUrl + "taxonomy/service/taxon/sciname/{scientificName}";
    TaxonResponse taxonResponse = restTemplate.getForObject(url, TaxonResponse.class, vars);
    return taxonResponse.getTaxon();
  }
  
  @Override
  public Taxon getTaxonByCommonName(String commonName) {
    Map<String, String> vars = new HashMap<String, String>();
    vars.put("commonName", commonName);
    String url = baseUrl + "taxonomy/service/taxon/comname/{commonName}";
    TaxonResponse taxonResponse = restTemplate.getForObject(url, TaxonResponse.class, vars);
    return taxonResponse.getTaxon();
  }
}
