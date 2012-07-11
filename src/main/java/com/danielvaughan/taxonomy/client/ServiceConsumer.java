package com.danielvaughan.taxonomy.client;

import com.danielvaughan.taxonomy.shared.model.Taxon;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceConsumer {

  public static void main(final String[] args) {
    final ServiceConsumer serviceConsumer = new ServiceConsumer();
    Taxon taxon = serviceConsumer.getTaxon("9606");
    System.out.println(taxon.getCommonName());
  }
  
  private static final String BASE_URL = "http://127.0.0.1:8080/";
  
  public Taxon getTaxon(final String taxId) {
    RestTemplate restTemplate = new RestTemplate();
    List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
    MappingJacksonHttpMessageConverter messageConverter = new MappingJacksonHttpMessageConverter();
    messageConverters.add(messageConverter);
    restTemplate.setMessageConverters(messageConverters);

    Map<String, String> vars = new HashMap<String, String>();
    vars.put("taxId", taxId);
    String url = BASE_URL + "taxonomy/service/taxon/id/{taxId}";
    TaxonResponse taxonResponse = restTemplate.getForObject(url, TaxonResponse.class, vars);
    return taxonResponse.getTaxon();
  }
}
