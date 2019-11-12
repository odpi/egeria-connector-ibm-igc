/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.egeria.connectors.ibm.igc.clientlibrary.model.base;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeName;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.odpi.egeria.connectors.ibm.igc.clientlibrary.model.common.Reference;
import org.odpi.egeria.connectors.ibm.igc.clientlibrary.model.common.ItemList;
import java.util.Date;

/**
 * POJO for the {@code warehouse_mapping} asset type in IGC, displayed as '{@literal Warehouse Mapping}' in the IGC UI.
 * <br><br>
 * (this code has been created based on out-of-the-box IGC metadata types.
 *  If modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.EXISTING_PROPERTY, property="_type", visible=true, defaultImpl=WarehouseMapping.class)
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeName("warehouse_mapping")
public class WarehouseMapping extends Reference {

    @JsonProperty("created_by")
    protected String createdBy;

    @JsonProperty("created_on")
    protected Date createdOn;

    @JsonProperty("description")
    protected String description;

    @JsonProperty("modified_by")
    protected String modifiedBy;

    @JsonProperty("modified_on")
    protected Date modifiedOn;

    @JsonProperty("name")
    protected String name;

    @JsonProperty("rule")
    protected String rule;

    @JsonProperty("sources")
    protected ItemList<InformationAsset> sources;

    @JsonProperty("targets")
    protected ItemList<InformationAsset> targets;

    @JsonProperty("warehouse_mapping_document")
    protected ItemList<WarehouseMappingDocument> warehouseMappingDocument;

    /**
     * Retrieve the {@code created_by} property (displayed as '{@literal Created By}') of the object.
     * @return {@code String}
     */
    @JsonProperty("created_by")
    public String getCreatedBy() { return this.createdBy; }

    /**
     * Set the {@code created_by} property (displayed as {@code Created By}) of the object.
     * @param createdBy the value to set
     */
    @JsonProperty("created_by")
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    /**
     * Retrieve the {@code created_on} property (displayed as '{@literal Created On}') of the object.
     * @return {@code Date}
     */
    @JsonProperty("created_on")
    public Date getCreatedOn() { return this.createdOn; }

    /**
     * Set the {@code created_on} property (displayed as {@code Created On}) of the object.
     * @param createdOn the value to set
     */
    @JsonProperty("created_on")
    public void setCreatedOn(Date createdOn) { this.createdOn = createdOn; }

    /**
     * Retrieve the {@code description} property (displayed as '{@literal Description}') of the object.
     * @return {@code String}
     */
    @JsonProperty("description")
    public String getDescription() { return this.description; }

    /**
     * Set the {@code description} property (displayed as {@code Description}) of the object.
     * @param description the value to set
     */
    @JsonProperty("description")
    public void setDescription(String description) { this.description = description; }

    /**
     * Retrieve the {@code modified_by} property (displayed as '{@literal Modified By}') of the object.
     * @return {@code String}
     */
    @JsonProperty("modified_by")
    public String getModifiedBy() { return this.modifiedBy; }

    /**
     * Set the {@code modified_by} property (displayed as {@code Modified By}) of the object.
     * @param modifiedBy the value to set
     */
    @JsonProperty("modified_by")
    public void setModifiedBy(String modifiedBy) { this.modifiedBy = modifiedBy; }

    /**
     * Retrieve the {@code modified_on} property (displayed as '{@literal Modified On}') of the object.
     * @return {@code Date}
     */
    @JsonProperty("modified_on")
    public Date getModifiedOn() { return this.modifiedOn; }

    /**
     * Set the {@code modified_on} property (displayed as {@code Modified On}) of the object.
     * @param modifiedOn the value to set
     */
    @JsonProperty("modified_on")
    public void setModifiedOn(Date modifiedOn) { this.modifiedOn = modifiedOn; }

    /**
     * Retrieve the {@code name} property (displayed as '{@literal Name}') of the object.
     * @return {@code String}
     */
    @JsonProperty("name")
    public String getTheName() { return this.name; }

    /**
     * Set the {@code name} property (displayed as {@code Name}) of the object.
     * @param name the value to set
     */
    @JsonProperty("name")
    public void setTheName(String name) { this.name = name; }

    /**
     * Retrieve the {@code rule} property (displayed as '{@literal Rule}') of the object.
     * @return {@code String}
     */
    @JsonProperty("rule")
    public String getRule() { return this.rule; }

    /**
     * Set the {@code rule} property (displayed as {@code Rule}) of the object.
     * @param rule the value to set
     */
    @JsonProperty("rule")
    public void setRule(String rule) { this.rule = rule; }

    /**
     * Retrieve the {@code sources} property (displayed as '{@literal Sources}') of the object.
     * @return {@code ItemList<InformationAsset>}
     */
    @JsonProperty("sources")
    public ItemList<InformationAsset> getSources() { return this.sources; }

    /**
     * Set the {@code sources} property (displayed as {@code Sources}) of the object.
     * @param sources the value to set
     */
    @JsonProperty("sources")
    public void setSources(ItemList<InformationAsset> sources) { this.sources = sources; }

    /**
     * Retrieve the {@code targets} property (displayed as '{@literal Targets}') of the object.
     * @return {@code ItemList<InformationAsset>}
     */
    @JsonProperty("targets")
    public ItemList<InformationAsset> getTargets() { return this.targets; }

    /**
     * Set the {@code targets} property (displayed as {@code Targets}) of the object.
     * @param targets the value to set
     */
    @JsonProperty("targets")
    public void setTargets(ItemList<InformationAsset> targets) { this.targets = targets; }

    /**
     * Retrieve the {@code warehouse_mapping_document} property (displayed as '{@literal Warehouse Mapping Document}') of the object.
     * @return {@code ItemList<WarehouseMappingDocument>}
     */
    @JsonProperty("warehouse_mapping_document")
    public ItemList<WarehouseMappingDocument> getWarehouseMappingDocument() { return this.warehouseMappingDocument; }

    /**
     * Set the {@code warehouse_mapping_document} property (displayed as {@code Warehouse Mapping Document}) of the object.
     * @param warehouseMappingDocument the value to set
     */
    @JsonProperty("warehouse_mapping_document")
    public void setWarehouseMappingDocument(ItemList<WarehouseMappingDocument> warehouseMappingDocument) { this.warehouseMappingDocument = warehouseMappingDocument; }

}