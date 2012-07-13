package com.danielvaughan.taxonomy.server;

import com.danielvaughan.taxonomy.server.daos.TaxonDao;
import com.danielvaughan.taxonomy.server.util.ApplicationContextLoader;
import com.danielvaughan.taxonomy.shared.model.DetailedTaxon;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

@Component
public class TaxonomySetup {

  private static final int BATCH_SIZE = 10000;
  private static final int PROGRESS_SIZE = 1000;

  public static void main(final String[] args) {
    final TaxonomySetup taxonomySetup = new TaxonomySetup();
    final ApplicationContextLoader loader = new ApplicationContextLoader();
    loader.load(taxonomySetup, "META-INF/applicationContext.xml");
    taxonomySetup.run();
    taxonomySetup.configureParents();
  }

  private final String filePath;

  private final Log log = LogFactory.getLog(TaxonomySetup.class);

  @Autowired
  private TaxonDao taxonDao;

  public TaxonomySetup() {
    this.filePath = "/Users/dvaughan/Downloads/taxonomy.xml";

    // "/Users/dvaughan/webin_dev/taxonomy/src/main/resources/data/mini.xml";
    // "/Users/dvaughan/Downloads/taxonomy.xml";
  }

  public TaxonomySetup(final String filePath) {
    this.filePath = filePath;
  }

  public void configureParents() {
    taxonDao.assignParents();
  }

  @Transactional
  public void run() {
    staxParse();
  }

  protected DetailedTaxon loadXmlTaxon(final Element eleTaxon) {
    try {
      final String taxId = eleTaxon.getAttributeValue("taxId");
      final String scientificName = eleTaxon.getAttributeValue("scientificName");
      final String commonName = eleTaxon.getAttributeValue("commonName");
      final DetailedTaxon detailedTaxon = new DetailedTaxon(taxId, scientificName, commonName);
      return detailedTaxon;
    } catch (final Exception e) {
      log.error("Exception parsing taxon element", e);
      return null;
    }
  }

  @SuppressWarnings("unused")
  private void JDOMParse() {
    try {
      log.info("Loading xml data: " + filePath);
      final SAXBuilder parser = new SAXBuilder();
      final InputStream in = getClass().getClassLoader().getResourceAsStream(filePath);
      final org.jdom.Document jdomDocument = parser.build(in);
      final Element eleRoot = jdomDocument.getRootElement();
      @SuppressWarnings("unchecked")
      final List<Element> taxonElements = eleRoot.getChildren("taxon");
      for (final Element eleTaxon : taxonElements) {
        final DetailedTaxon detailedTaxon = loadXmlTaxon(eleTaxon);
        taxonDao.addTaxon(detailedTaxon);
      }
    } catch (final IOException e) {
      log.error("File error", e);

    } catch (final JDOMException e) {
      log.error("JDOM error", e);
    }
  }

  private void staxParse() {
    XMLInputFactory2 xmlif = null;
    try {
      xmlif = (XMLInputFactory2) XMLInputFactory.newInstance();
      xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
      xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
      xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
      xmlif.configureForSpeed();
    } catch (final Exception ex) {
      ex.printStackTrace();
    }
    System.out.println("Starting to parse " + filePath);
    System.out.println("");
    final long starttime = System.currentTimeMillis();
    int elementCount = 0;
    int depth = -1;
    try {
      final XMLStreamReader2 xmlr =
          (XMLStreamReader2) xmlif.createXMLStreamReader(filePath, new FileInputStream(filePath));
      int eventType = xmlr.getEventType();
      String curElement = "";
      final List<DetailedTaxon> taxonBatch = new ArrayList<DetailedTaxon>();
      while (xmlr.hasNext()) {
        eventType = xmlr.next();
        switch (eventType) {
          case XMLEvent.START_ELEMENT:
            depth++;
            curElement = xmlr.getName().toString();
            if (curElement.equals("taxon") && depth == 1) {
              final String taxId = xmlr.getAttributeValue("", "taxId");
              final String scientificName = xmlr.getAttributeValue("", "scientificName");
              final String hidden = xmlr.getAttributeValue("", "hidden");
              final String rank = xmlr.getAttributeValue("", "rank");
              final String pln = xmlr.getAttributeValue("", "PLN");
              final String geneticCode = xmlr.getAttributeValue("", "geneticCode");
              final String mitochondrialGeneticCode = xmlr.getAttributeValue("", "mitochondrialGeneticCode");
              final String parentTaxId = xmlr.getAttributeValue("", "parentTaxId");
              final String commonName = xmlr.getAttributeValue("", "commonName");
              final DetailedTaxon detailedTaxon = new DetailedTaxon(taxId, scientificName, commonName);
              detailedTaxon.setParentTaxId(parentTaxId);
              detailedTaxon.setHidden(hidden);
              detailedTaxon.setRank(rank);
              detailedTaxon.setPln(pln);
              detailedTaxon.setGeneticCode(geneticCode);
              detailedTaxon.setMitochondrialGeneticCode(mitochondrialGeneticCode);
              taxonBatch.add(detailedTaxon);
              if (taxonBatch.size() == BATCH_SIZE) {
                System.out.println("Adding batch");
                taxonDao.batchAddTaxons(taxonBatch);
                taxonBatch.clear();
              }
              if (elementCount % PROGRESS_SIZE == 0) {
                System.out.print(".");
              }
            }
            /*
             * if (curElement.equals("synonym")) { final String type = xmlr.getAttributeValue("", "type"); final String
             * name = xmlr.getAttributeValue("", "name"); final Synonym synonym = new Synonym(currentTaxId, type, name);
             * taxonDao.addSynonym(synonym); }
             */
            break;
          case XMLEvent.END_ELEMENT:
            depth--;
            if (curElement.equals("taxon")) {
              elementCount++;
            }
            break;
          case XMLEvent.END_DOCUMENT:
            System.out.println("Total of " + elementCount + " occurrences");
        }
      }
      // Add partial batch
      taxonDao.batchAddTaxons(taxonBatch);
      taxonBatch.clear();
    } catch (final XMLStreamException ex) {
      System.out.println(ex.getMessage());
      if (ex.getNestedException() != null) {
        ex.getNestedException().printStackTrace();
      }
    } catch (final Exception ex) {
      ex.printStackTrace();
    }
    System.out.println(" completed in " + (System.currentTimeMillis() - starttime) + " ms");
  }
}
