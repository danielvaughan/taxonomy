package ena;

import java.io.Serializable;

public class TaxonInfo implements Serializable {

  private String commonName;

  private String scientificName;

  private String taxId;

  public TaxonInfo() {
    this("0", "", "");
  }

  public TaxonInfo(String taxId, String commonName, String scientificName) {
    super();
    this.taxId = taxId;
    this.commonName = commonName;
    this.scientificName = scientificName;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    TaxonInfo other = (TaxonInfo) obj;
    if (commonName == null) {
      if (other.commonName != null)
        return false;
    } else if (!commonName.equals(other.commonName))
      return false;
    if (scientificName == null) {
      if (other.scientificName != null)
        return false;
    } else if (!scientificName.equals(other.scientificName))
      return false;
    if (taxId == null) {
      if (other.taxId != null)
        return false;
    } else if (!taxId.equals(other.taxId))
      return false;
    return true;
  }

  public String getCommonName() {
    return commonName;
  }

  public String getScientificName() {
    return scientificName;
  }

  public String getTaxId() {
    return taxId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((commonName == null) ? 0 : commonName.hashCode());
    result = prime * result + ((scientificName == null) ? 0 : scientificName.hashCode());
    result = prime * result + ((taxId == null) ? 0 : taxId.hashCode());
    return result;
  }

  @Override
  public String toString() {
    return "TaxonInfo [commonName=" + commonName + ", scientificName=" + scientificName + ", taxId=" + taxId + "]";
  }

}
