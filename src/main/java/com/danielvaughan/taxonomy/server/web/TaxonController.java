package com.danielvaughan.taxonomy.server.web;

import com.danielvaughan.taxonomy.server.daos.TaxonDao;
import com.danielvaughan.taxonomy.shared.model.DetailedTaxon;
import com.danielvaughan.taxonomy.shared.model.Taxon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping(value = "/taxon/**")
public class TaxonController
{
  
  @Autowired
  private TaxonDao taxonDao;

  @RequestMapping(value = "/id/{taxId}", method = RequestMethod.GET)
  public void getTaxonByTaxId(@PathVariable String taxId, ModelMap modelMap)
  {
    Taxon taxon = taxonDao.getTaxonByTaxId(taxId);
    modelMap.addAttribute("taxon", taxon);
  }

  @RequestMapping(value = "/comname/{commonName}", method = RequestMethod.GET)
  public void getTaxonByCommonName(@PathVariable String commonName, ModelMap modelMap)
  {
    Taxon taxon = taxonDao.getTaxonByCommonName(commonName);
    modelMap.addAttribute("taxon", taxon);
  }
  
  @RequestMapping(value = "/sciname/{scientificName}", method = RequestMethod.GET)
  public void getTaxonByScientificName(@PathVariable String scientificName, ModelMap modelMap)
  {
    Taxon taxon = taxonDao.getTaxonByScientificName(scientificName);
    modelMap.addAttribute("taxon", taxon);
  }
  
  @RequestMapping(value = "/detail/{taxId}", method = RequestMethod.GET)
  public void getDetailedTaxonByTaxId(@PathVariable String taxId, ModelMap modelMap)
  {
    DetailedTaxon detailedTaxon = taxonDao.getDetailedTaxonByTaxId(taxId);
    modelMap.addAttribute("detailedTaxon", detailedTaxon);
  }
  
  @RequestMapping(value = "/relationship/{taxIdA}/to/{taxIdB}", method = RequestMethod.GET)
  public void getRelationshipByTaxIds(@PathVariable String taxIdA, @PathVariable String taxIdB, ModelMap modelMap)
  {
    List<Taxon> taxons = taxonDao.getRelationshipByTaxIds(taxIdA, taxIdB);
    modelMap.addAttribute("relationship", taxons);
  }
  
  @RequestMapping(value = "/lineage/{taxId}", method = RequestMethod.GET)
  public void getLineageByTaxId(@PathVariable String taxId, ModelMap modelMap)
  {
    List<Taxon> taxons = taxonDao.getLineageByTaxId(taxId, false);
    modelMap.addAttribute("lineage", taxons);
  }
  
  @RequestMapping(value = "/fulllineage/{taxId}", method = RequestMethod.GET)
  public void getFullLineageByTaxId(@PathVariable String taxId, ModelMap modelMap)
  {
    List<Taxon> taxons = taxonDao.getLineageByTaxId(taxId, true);
    modelMap.addAttribute("lineage", taxons);
  }
  
  @RequestMapping(value ="/{query}", method = RequestMethod.GET)
  public void getTaxonSuggestions(@PathVariable String query, ModelMap modelMap)
  {
    List<Taxon> taxons = taxonDao.searchTaxons(query, 100);
    modelMap.addAttribute("taxons", taxons);
  }
  
}
