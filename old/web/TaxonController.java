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
public class TaxonController {

  @Autowired
  private TaxonDao taxonDao;

  @RequestMapping(value = "/detail/{taxId}", method = RequestMethod.GET)
  public void getDetailedTaxonByTaxId(@PathVariable final String taxId, final ModelMap modelMap) {
    final DetailedTaxon detailedTaxon = taxonDao.getDetailedTaxonByTaxId(taxId);
    modelMap.addAttribute("detailedTaxon", detailedTaxon);
  }

  @RequestMapping(value = "/fulllineage/{taxId}", method = RequestMethod.GET)
  public void getFullLineageByTaxId(@PathVariable final String taxId, final ModelMap modelMap) {
    final List<Taxon> taxons = taxonDao.getLineageByTaxId(taxId, true);
    modelMap.addAttribute("taxonList", taxons);
  }

  @RequestMapping(value = "/isvalid/taxid/{taxId}")
  public void getIsValidTaxId(@PathVariable final String taxId, final ModelMap modelMap) {
    final Boolean valid = taxonDao.isTaxIdValid(taxId);
    modelMap.addAttribute("valid", valid);
  }

  @RequestMapping(value = "/lineage/{taxId}", method = RequestMethod.GET)
  public void getLineageByTaxId(@PathVariable final String taxId, final ModelMap modelMap) {
    final List<Taxon> taxons = taxonDao.getLineageByTaxId(taxId, false);
    modelMap.addAttribute("taxonList", taxons);
  }

  @RequestMapping(value = "/relationship/{taxIdA}/to/{taxIdB}", method = RequestMethod.GET)
  public void getRelationshipByTaxIds(@PathVariable final String taxIdA,
                                      @PathVariable final String taxIdB,
                                      final ModelMap modelMap) {
    final List<Taxon> taxons = taxonDao.getRelationshipByTaxIds(taxIdA, taxIdB);
    modelMap.addAttribute("relationship", taxons);
  }

  @RequestMapping(value = "/comname/{commonName}", method = RequestMethod.GET)
  public void getTaxonByCommonName(@PathVariable final String commonName, final ModelMap modelMap) {
    final Taxon taxon = taxonDao.getTaxonByCommonName(commonName);
    modelMap.addAttribute("taxon", taxon);
  }

  @RequestMapping(value = "/sciname/{scientificName}", method = RequestMethod.GET)
  public void getTaxonByScientificName(@PathVariable final String scientificName, final ModelMap modelMap) {
    final Taxon taxon = taxonDao.getTaxonByScientificName(scientificName);
    modelMap.addAttribute("taxon", taxon);
  }

  @RequestMapping(value = "/taxid/{taxId}", method = RequestMethod.GET)
  public void getTaxonByTaxId(@PathVariable final String taxId, final ModelMap modelMap) {
    final Taxon taxon = taxonDao.getTaxonByTaxId(taxId);
    modelMap.addAttribute("taxon", taxon);
  }

  @RequestMapping(value = "/search/{query}", method = RequestMethod.GET)
  public void getTaxonSuggestions(@PathVariable final String query, final ModelMap modelMap) {
    final List<Taxon> taxons = taxonDao.searchTaxons(query, 100);
    modelMap.addAttribute("taxonList", taxons);
  }
  
  @RequestMapping(value = "/search/{query}/limit/{limit}", method = RequestMethod.GET)
  public void getTaxonSuggestionsWithLimit(@PathVariable final String query, @PathVariable final int limit, final ModelMap modelMap) {
    final List<Taxon> taxons = taxonDao.searchTaxons(query, limit);
    modelMap.addAttribute("taxonList", taxons);
  }

}
